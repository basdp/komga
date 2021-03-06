<template>
  <div id="searchbox">
    <v-text-field v-model="search"
                  solo
                  hide-details
                  clearable
                  prepend-inner-icon="mdi-magnify"
                  label="Search"
                  :loading="loading"
                  @click:clear="clear"
                  @keydown.esc="clear"
    />
    <v-menu nudge-bottom="57"
            nudge-right="52"
            attach="#searchbox"
            v-model="showResults"
            :max-height="$vuetify.breakpoint.height * .9"
            :min-width="$vuetify.breakpoint.mdAndUp ? $vuetify.breakpoint.width * .4 : $vuetify.breakpoint.width * .8"
    >
      <v-list>
        <v-list-item v-if="series.length === 0 && books.length === 0">No results</v-list-item>

        <template v-if="series.length !== 0">
          <v-subheader>SERIES</v-subheader>
          <v-list-item v-for="item in series"
                       :key="item.id"
                       link
                       :to="{name: 'browse-series', params: {seriesId: item.id}}"
          >
            <v-img :src="baseURL + '/api/v1/series/' + item.id + '/thumbnail'"
                   height="50"
                   max-width="35"
                   class="ma-1 mr-3"
            />
            <v-list-item-content>
              <v-list-item-title v-text="item.name"/>
            </v-list-item-content>
          </v-list-item>
        </template>

        <template v-if="books.length !== 0">
          <v-subheader>BOOKS</v-subheader>
          <v-list-item v-for="item in books"
                       :key="item.id"
                       link
                       :to="{name: 'browse-book', params: {bookId: item.id}}"
          >
            <v-img :src="baseURL + '/api/v1/books/' + item.id + '/thumbnail'"
                   height="50"
                   max-width="35"
                   class="ma-1 mr-3"
            />
            <v-list-item-content>
              <v-list-item-title v-text="item.name"/>
            </v-list-item-content>
          </v-list-item>
        </template>

      </v-list>
    </v-menu>
  </div>
</template>

<script lang="ts">
import { debounce } from 'lodash'
import Vue from 'vue'

export default Vue.extend({
  name: 'SearchBox',
  data: function () {
    return {
      baseURL: process.env.VUE_APP_KOMGA_API_URL ? process.env.VUE_APP_KOMGA_API_URL : window.location.origin,
      search: null,
      showResults: false,
      loading: false,
      series: [] as SeriesDto[],
      books: [] as BookDto[],
      pageSize: 10
    }
  },
  watch: {
    search (val) {
      this.searchItems(val)
    },
    showResults (val) {
      !val && this.clear()
    }
  },
  methods: {
    searchItems: debounce(async function (this: any, query: string) {
      if (query) {
        this.loading = true
        this.series = (await this.$komgaSeries.getSeries(undefined, { size: this.pageSize }, query)).content
        this.books = (await this.$komgaBooks.getBooks(undefined, { size: this.pageSize }, query)).content
        this.showResults = true
        this.loading = false
      } else {
        this.clear()
      }
    }, 500),
    clear () {
      this.search = null
      this.showResults = false
      this.series = []
      this.books = []
    }
  }
})
</script>

<style scoped>

</style>
