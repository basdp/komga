# [0.11.0](https://github.com/gotson/komga/compare/v0.10.1...v0.11.0) (2020-01-06)


### Bug Fixes

* **web reader:** remove webp as it's not supported in Safari ([6770107](https://github.com/gotson/komga/commit/6770107dc8532c9bd62e2dbcadc0124df151a385))
* add support for jpeg2000/jbig2 formats ([227975a](https://github.com/gotson/komga/commit/227975a79eecd7882e6f73c5e49910bc86eeba18)), closes [#50](https://github.com/gotson/komga/issues/50)


### Features

* handle archives without images ([70a2da5](https://github.com/gotson/komga/commit/70a2da532160a10ae56fda017478f2b0ecd92ef1)), closes [#56](https://github.com/gotson/komga/issues/56)
* **web reader:** rtl option ([98efa9b](https://github.com/gotson/komga/commit/98efa9b44894eccd3e38c6f2bc8b777fb206732f)), closes [#53](https://github.com/gotson/komga/issues/53)
* display version in UI ([4085f1f](https://github.com/gotson/komga/commit/4085f1fdaac973f549d11a9c2d62cc5f45abd747)), closes [#42](https://github.com/gotson/komga/issues/42)
* rescan library ([30208a2](https://github.com/gotson/komga/commit/30208a234044fad8d68902cfba8e601255665cac)), closes [#38](https://github.com/gotson/komga/issues/38)
* **api:** reAnalyze library ([fa65e94](https://github.com/gotson/komga/commit/fa65e94ae6397fb61e04ded733db10bc997d07de))
* **api:** reAnalyze series ([e80451f](https://github.com/gotson/komga/commit/e80451ffcc79c8bbadcbd452acd4644ff38773ff))
* **rest api:** ability to re-analyze a book ([8e81356](https://github.com/gotson/komga/commit/8e81356908e42d1b7b8edb935a1eaf133cff7035)), closes [#51](https://github.com/gotson/komga/issues/51)
* **rest api:** don't return hidden files for directory listings ([a478d90](https://github.com/gotson/komga/commit/a478d90a596749b5e7752a2a934ef5e6cf244f20))
* **webui:** add menu option to analyze book from browsing view ([64f542d](https://github.com/gotson/komga/commit/64f542d42fb18010fa4d602aaa1c2f5bace5e1e9)), closes [#51](https://github.com/gotson/komga/issues/51)
* **webui:** reAnalyze library ([b599b72](https://github.com/gotson/komga/commit/b599b72c48d6687d6679cce6b12407abe8cf7d86)), closes [#51](https://github.com/gotson/komga/issues/51)
* **webui:** reAnalyze series ([b997561](https://github.com/gotson/komga/commit/b9975618a30694c8bbd791e22794d1427231d837))

## [0.10.1](https://github.com/gotson/komga/compare/v0.10.0...v0.10.1) (2020-01-01)


### Bug Fixes

* **webui:** remove CDN usage for icons and fonts ([c88a27c](https://github.com/gotson/komga/commit/c88a27c10abdfecd5d9476ca74f382418922a546)), closes [#45](https://github.com/gotson/komga/issues/45)
* **webui:** show all books when browsing series ([85ca99d](https://github.com/gotson/komga/commit/85ca99d49aaeabed40b578dfff3e1d7f4c2e6bff))
* **zip extractor:** better handling of exotic charsets ([0254d7d](https://github.com/gotson/komga/commit/0254d7d8671a3743bfedbcd42472dbe974a76c98)), closes [#41](https://github.com/gotson/komga/issues/41)

<a name="v0.10.0"></a>
# [v0.10.0](https://github.com/gotson/komga/releases/tag/v0.10.0) - 31 Dec 2019

### Features

- **webui:** added **Web Reader** (#28)
- **webui:** display all books instead of only books in ready state
- **webui:** add 'Date updated' sort criteria for Series
- **webui:** add 'File size' sort criteria for Books
- **api:** handle the HTTP cache properly for dynamic resources: thumbnails and pages (#27)
- **api:** hide full path to non-admin users (for libraries, series and books)
- **scanner:** add configuration key to force the last modified time of directories (#37)

### Fixes

- **webui:** rework dashboard sliders to be more touch-friendly
- **webui:** better display on mobile and small screens
- remove regeneration of missing thumbnails at startup (don't remember why I added it in the first place ¯\_(ツ)_/¯ )

[Changes][v0.10.0]


<a name="v0.9.1"></a>
# [v0.9.1](https://github.com/gotson/komga/releases/tag/v0.9.1) - 18 Dec 2019

This release is focused on performance enhancements.

### Changes

- Hibernate lazy loading was not working because of Kotlin closed by default classes
- add Hibernate caches: second level, collections, query
- enhance code path to reduce number of database queries
- fix a bug where Series thumbnail would be retrieved by the first book in the collection, instead of the first by number
- remove (unused) bi-directional OneToOne relationship between Book and BookMetadata to reduce database fetch

[Changes][v0.9.1]


<a name="v0.9.0"></a>
# [v0.9.0](https://github.com/gotson/komga/releases/tag/v0.9.0) - 12 Dec 2019

### Features

- Web Interface enhancements (closes #18)
  - browse Libraries, Series, and Books
  - dashboard with recently added Series and Books
  - search Series and Books
  - remove browser pop-up for basic auth, use a dedicated login screen instead
  - http cache for static assets

### Changes

- retrieve real ip in audit logs (for example if behind a reverse proxy)
- remove Humio metrics
- libraries are sorted by name in the API by default

### Deprecation

- endpoints of the form `/series/{seriesId}/books/{bookId}/**`, use `/books/{bookId}/**` instead

### Known issues

- UI is extremely slow when browsing libraries/series with many items (75+) on Chrome Android

[Changes][v0.9.0]


<a name="v0.8.1"></a>
# [v0.8.1](https://github.com/gotson/komga/releases/tag/v0.8.1) - 05 Nov 2019

### Changes

- add metrics exporter for InfluxDB (disabled by default)

[Changes][v0.8.1]


<a name="v0.8.0"></a>
# [v0.8.0](https://github.com/gotson/komga/releases/tag/v0.8.0) - 30 Oct 2019

### Changes

- change docker base image to adoptopenjdk 11
- retrieve file size of books when scanning. Add file size in REST API and OPDS.

### Bug fixes

- Swagger is not showing Kotlin nullable types as optional (fixes #15)
- compare file modification time at millisecond. With JDK 9+ on Windows, time precision of the underlying clock would go over 6 digits, which is the precision of the timestamps in database, which would lead to loss of precision on saved timestamps, and failing comparisons of modification times at every library scan
- return only books in ready state via OPDS, else it would throw an error 500 because metadata is not ready
- prevent circular loop in the error resolver, which would complain in logs

[Changes][v0.8.0]


<a name="v0.7.1"></a>
# [v0.7.1](https://github.com/gotson/komga/releases/tag/v0.7.1) - 25 Oct 2019

### Bug fixes

- could not delete a library if it was specifically shared with any user

### Changes

- add build and git info in `/actuator/info` endpoint
- add `humio` metrics exporter (disabled, need to be enabled by configuration)

[Changes][v0.7.1]


<a name="v0.7.0"></a>
# [v0.7.0](https://github.com/gotson/komga/releases/tag/v0.7.0) - 22 Oct 2019

### :warning: Breaking changes

- `admin` and `user` users are deprecated and replaced by the User Management feature

### Features

- User management:
  - Automatic creation of an admin account at startup if no user exist in database, outputting the login and a random password in the logs
  - Ability to add/remove user accounts
  - Ability to manage roles: Admin/User
  - Ability to manage access to shared libraries per user
  - Ability for a user to change his/her password

### Changes

- OPDS entry links to file now include the full filename. This helps some (badly implemented) OPDS clients to correctly see the files.
- logs are written to disk, and available via the `/actuator/logfile` endpoint

### Bug fixes

- fix OPDS link for page streaming where an incorrect url escape in the `zero_based` query parameter would generate an error for the first page, shift all pages, and prevent the last page to be streamed

[Changes][v0.7.0]


<a name="v0.6.1"></a>
# [v0.6.1](https://github.com/gotson/komga/releases/tag/v0.6.1) - 12 Oct 2019

**Fixes**:

- Thumbnail media type was incorrect for OPDS feed
- `/series` endpoint library filter can accept a list instead of a single value
- `/filesystem` endpoint now sorts directories with case insensitive

[Changes][v0.6.1]


<a name="v0.6.0"></a>
# [v0.6.0](https://github.com/gotson/komga/releases/tag/v0.6.0) - 11 Oct 2019

**Changes**:

- change thumbnail format from PNG to JPEG to reduce size (I observed reduction by 10 of the database size)
- regenerate missing thumbnails on startup
- force one time regeneration of all thumbnails in this release to change the format of existing thumbnails

[Changes][v0.6.0]


<a name="v0.5.0"></a>
# [v0.5.0](https://github.com/gotson/komga/releases/tag/v0.5.0) - 10 Oct 2019

**Features**:

- Support for multiple libraries
- First version of the web interface, which supports addition/deletion of libraries
- OPDS feed supports browsing by library
- REST API `/series` endpoint can be filtered by `library_id`
- Ability to exclude directories from disk scan, to be configured via `komga.libraries-scan-directory-exclusions` configuration key

**Deprecations**:

- The configuration property `komga.root-folder` is deprecated. Use the web interface to add libraries instead.
- The configuration property `komga.root-folder-scan-cron` is deprecated. It is replaced by `komga.libraries-scan-cron`.

[Changes][v0.5.0]


<a name="v0.4.1"></a>
# [v0.4.1](https://github.com/gotson/komga/releases/tag/v0.4.1) - 30 Sep 2019

properly release open files after scan (fixes #9)
properly release open files after accessing PDF files (fixes #10)

[Changes][v0.4.1]


<a name="v0.4.0"></a>
# [v0.4.0](https://github.com/gotson/komga/releases/tag/v0.4.0) - 23 Sep 2019

support for OPDS feed with OpenSearch and Page Streaming Extension (https://vaemendis.net/opds-pse/)

[Changes][v0.4.0]


<a name="v0.3.4"></a>
# [v0.3.4](https://github.com/gotson/komga/releases/tag/v0.3.4) - 10 Sep 2019

the fix in v0.3.2 also increased the scan time, rolling back the change, and adding a one-time rescan to handle potential pdf files that were forgotten in mixed-content directories
subsequent addition of new supported file formats should force a rescan to ensure there are no missing files

[Changes][v0.3.4]


<a name="v0.3.3"></a>
# [v0.3.3](https://github.com/gotson/komga/releases/tag/v0.3.3) - 07 Sep 2019



[Changes][v0.3.3]


<a name="v0.3.2"></a>
# [v0.3.2](https://github.com/gotson/komga/releases/tag/v0.3.2) - 07 Sep 2019

fixed a side effect that could occur in mixed-content directories containing pdf files that were scanned before the support of pdf was introduced, where pdf files in those directories would never be rescanned

[Changes][v0.3.2]


<a name="v0.3.1"></a>
# [v0.3.1](https://github.com/gotson/komga/releases/tag/v0.3.1) - 07 Sep 2019



[Changes][v0.3.1]


<a name="v0.3.0"></a>
# [v0.3.0](https://github.com/gotson/komga/releases/tag/v0.3.0) - 06 Sep 2019

Support for WEBP image format to generate thumbnails
Better PDF handling

[Changes][v0.3.0]


<a name="v0.2.1"></a>
# [v0.2.1](https://github.com/gotson/komga/releases/tag/v0.2.1) - 04 Sep 2019

On the fly conversion of pages

[Changes][v0.2.1]


<a name="v0.2.0"></a>
# [v0.2.0](https://github.com/gotson/komga/releases/tag/v0.2.0) - 30 Aug 2019

PDF support

[Changes][v0.2.0]


<a name="v0.1.1"></a>
# [v0.1.1](https://github.com/gotson/komga/releases/tag/v0.1.1) - 28 Aug 2019



[Changes][v0.1.1]


<a name="v0.1.0"></a>
# [v0.1.0](https://github.com/gotson/komga/releases/tag/v0.1.0) - 27 Aug 2019

First release, support for `cbr` and `cbz` archives

[Changes][v0.1.0]


[v0.10.0]: https://github.com/gotson/komga/compare/v0.9.1...v0.10.0
[v0.9.1]: https://github.com/gotson/komga/compare/v0.9.0...v0.9.1
[v0.9.0]: https://github.com/gotson/komga/compare/v0.8.1...v0.9.0
[v0.8.1]: https://github.com/gotson/komga/compare/v0.8.0...v0.8.1
[v0.8.0]: https://github.com/gotson/komga/compare/v0.7.1...v0.8.0
[v0.7.1]: https://github.com/gotson/komga/compare/v0.7.0...v0.7.1
[v0.7.0]: https://github.com/gotson/komga/compare/v0.6.1...v0.7.0
[v0.6.1]: https://github.com/gotson/komga/compare/v0.6.0...v0.6.1
[v0.6.0]: https://github.com/gotson/komga/compare/v0.5.0...v0.6.0
[v0.5.0]: https://github.com/gotson/komga/compare/v0.4.1...v0.5.0
[v0.4.1]: https://github.com/gotson/komga/compare/v0.4.0...v0.4.1
[v0.4.0]: https://github.com/gotson/komga/compare/v0.3.4...v0.4.0
[v0.3.4]: https://github.com/gotson/komga/compare/v0.3.3...v0.3.4
[v0.3.3]: https://github.com/gotson/komga/compare/v0.3.2...v0.3.3
[v0.3.2]: https://github.com/gotson/komga/compare/v0.3.1...v0.3.2
[v0.3.1]: https://github.com/gotson/komga/compare/v0.3.0...v0.3.1
[v0.3.0]: https://github.com/gotson/komga/compare/v0.2.1...v0.3.0
[v0.2.1]: https://github.com/gotson/komga/compare/v0.2.0...v0.2.1
[v0.2.0]: https://github.com/gotson/komga/compare/v0.1.1...v0.2.0
[v0.1.1]: https://github.com/gotson/komga/compare/v0.1.0...v0.1.1
[v0.1.0]: https://github.com/gotson/komga/tree/v0.1.0

 <!-- Generated by changelog-from-release -->
