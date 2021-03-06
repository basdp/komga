package org.gotson.komga.interfaces.web.rest

import com.github.klinq.jpaspec.`in`
import com.github.klinq.jpaspec.likeLower
import mu.KotlinLogging
import org.gotson.komga.domain.model.Media
import org.gotson.komga.domain.model.Series
import org.gotson.komga.domain.persistence.BookRepository
import org.gotson.komga.domain.persistence.LibraryRepository
import org.gotson.komga.domain.persistence.SeriesRepository
import org.gotson.komga.domain.service.AsyncOrchestrator
import org.gotson.komga.infrastructure.security.KomgaPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.RejectedExecutionException

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("api/v1/series", produces = [MediaType.APPLICATION_JSON_VALUE])
class SeriesController(
  private val seriesRepository: SeriesRepository,
  private val libraryRepository: LibraryRepository,
  private val bookRepository: BookRepository,
  private val bookController: BookController,
  private val asyncOrchestrator: AsyncOrchestrator
) {

  @GetMapping
  fun getAllSeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      @RequestParam(name = "search", required = false) searchTerm: String?,
      @RequestParam(name = "library_id", required = false) libraryIds: List<Long>?,
      page: Pageable
  ): Page<SeriesDto> {
    val pageRequest = PageRequest.of(
        page.pageNumber,
        page.pageSize,
        if (page.sort.isSorted) page.sort
        else Sort.by(Sort.Order.asc("name").ignoreCase())
    )

    return mutableListOf<Specification<Series>>().let { specs ->
      when {
        !principal.user.sharedAllLibraries && !libraryIds.isNullOrEmpty() -> {
          val authorizedLibraryIDs = libraryIds.intersect(principal.user.sharedLibraries.map { it.id })
          if (authorizedLibraryIDs.isEmpty()) return@let Page.empty<Series>(pageRequest)
          else specs.add(Series::library.`in`(libraryRepository.findAllById(authorizedLibraryIDs)))
        }

        !principal.user.sharedAllLibraries -> specs.add(Series::library.`in`(principal.user.sharedLibraries))

        !libraryIds.isNullOrEmpty() -> {
          specs.add(Series::library.`in`(libraryRepository.findAllById(libraryIds)))
        }
      }

      if (!searchTerm.isNullOrEmpty()) {
        specs.add(Series::name.likeLower("%$searchTerm%"))
      }

      if (specs.isNotEmpty()) {
        seriesRepository.findAll(specs.reduce { acc, spec -> acc.and(spec)!! }, pageRequest)
      } else {
        seriesRepository.findAll(pageRequest)
      }
    }.map { it.toDto(includeUrl = principal.user.isAdmin()) }
  }

  // all updated series, whether newly added or updated
  @GetMapping("/latest")
  fun getLatestSeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      page: Pageable
  ): Page<SeriesDto> {
    val pageRequest = PageRequest.of(
        page.pageNumber,
        page.pageSize,
        Sort.by(Sort.Direction.DESC, "lastModifiedDate")
    )

    return if (principal.user.sharedAllLibraries) {
      seriesRepository.findAll(pageRequest)
    } else {
      seriesRepository.findByLibraryIn(principal.user.sharedLibraries, pageRequest)
    }.map { it.toDto(includeUrl = principal.user.isAdmin()) }
  }

  // new series only, doesn't contain existing updated series
  @GetMapping("/new")
  fun getNewSeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      page: Pageable
  ): Page<SeriesDto> {
    val pageRequest = PageRequest.of(
        page.pageNumber,
        page.pageSize,
        Sort.by(Sort.Direction.DESC, "createdDate")
    )

    return if (principal.user.sharedAllLibraries) {
      seriesRepository.findAll(pageRequest)
    } else {
      seriesRepository.findByLibraryIn(principal.user.sharedLibraries, pageRequest)
    }.map { it.toDto(includeUrl = principal.user.isAdmin()) }
  }

  // updated series only, doesn't contain new series
  @GetMapping("/updated")
  fun getUpdatedSeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      page: Pageable
  ): Page<SeriesDto> {
    val pageRequest = PageRequest.of(
        page.pageNumber,
        page.pageSize,
        Sort.by(Sort.Direction.DESC, "lastModifiedDate")
    )

    return if (principal.user.sharedAllLibraries) {
      seriesRepository.findRecentlyUpdated(pageRequest)
    } else {
      seriesRepository.findRecentlyUpdatedByLibraryIn(principal.user.sharedLibraries, pageRequest)
    }.map { it.toDto(includeUrl = principal.user.isAdmin()) }
  }

  @GetMapping("{seriesId}")
  fun getOneSeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      @PathVariable(name = "seriesId") id: Long
  ): SeriesDto =
      seriesRepository.findByIdOrNull(id)?.let {
        if (!principal.user.canAccessSeries(it)) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        it.toDto(includeUrl = principal.user.isAdmin())
      } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @GetMapping(value = ["{seriesId}/thumbnail"], produces = [MediaType.IMAGE_JPEG_VALUE])
  fun getSeriesThumbnail(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      request: WebRequest,
      @PathVariable(name = "seriesId") id: Long
  ): ResponseEntity<ByteArray> =
      seriesRepository.findByIdOrNull(id)?.let { series ->
        if (!principal.user.canAccessSeries(series)) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

        series.books.minBy { it.number }?.let { firstBook ->
          bookController.getBookThumbnail(principal, request, firstBook.id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
      } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @GetMapping("{seriesId}/books")
  fun getAllBooksBySeries(
      @AuthenticationPrincipal principal: KomgaPrincipal,
      @PathVariable(name = "seriesId") id: Long,
      @RequestParam(name = "media_status", required = false) mediaStatus: List<Media.Status>?,
      page: Pageable
  ): Page<BookDto> {
    seriesRepository.findByIdOrNull(id)?.let {
      if (!principal.user.canAccessSeries(it)) throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    val pageRequest = PageRequest.of(
        page.pageNumber,
      page.pageSize,
      if (page.sort.isSorted) page.sort
      else Sort.by(Sort.Order.asc("number"))
    )

    return (if (!mediaStatus.isNullOrEmpty())
      bookRepository.findAllByMediaStatusInAndSeriesId(mediaStatus, id, pageRequest)
    else
      bookRepository.findAllBySeriesId(id, pageRequest)).map { it.toDto(includeFullUrl = principal.user.isAdmin()) }
  }

  @PostMapping("{seriesId}/analyze")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ResponseStatus(HttpStatus.ACCEPTED)
  fun analyze(@PathVariable seriesId: Long) {
    seriesRepository.findByIdOrNull(seriesId)?.let { series ->
      try {
        asyncOrchestrator.reAnalyzeBooks(series.books)
      } catch (e: RejectedExecutionException) {
        throw ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Another book analysis task is already running")
      }
    } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
  }
}
