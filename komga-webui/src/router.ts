import Vue from 'vue'
import Router from 'vue-router'
import store from './store'

Vue.use(Router)

const lStore = store

const adminGuard = (to: any, from: any, next: any) => {
  if (!lStore.getters.meAdmin) next({ name: 'home' })
  else next()
}

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: { name: 'dashboard' },
      component: () => import(/* webpackChunkName: "home" */ './views/Home.vue'),
      children: [
        {
          path: '/libraries/add',
          name: 'addlibrary',
          beforeEnter: adminGuard,
          component: () => import(/* webpackChunkName: "addlibrary" */ './components/LibraryAddDialog.vue')
        },
        {
          path: '/welcome',
          name: 'welcome',
          component: () => import(/* webpackChunkName: "welcome" */ './components/Welcome.vue')
        },
        {
          path: '/dashboard',
          name: 'dashboard',
          component: () => import(/* webpackChunkName: "dashboard" */ './components/Dashboard.vue')
        },
        {
          path: '/settings',
          name: 'settings',
          redirect: { name: 'settings-users' },
          component: () => import(/* webpackChunkName: "settings" */ './views/Settings.vue'),
          children: [
            {
              path: '/settings/users',
              name: 'settings-users',
              beforeEnter: adminGuard,
              component: () => import(/* webpackChunkName: "settings-users" */ './components/SettingsUsers.vue'),
              children: [
                {
                  path: '/settings/users/add',
                  name: 'settings-users-add',
                  component: () => import(/* webpackChunkName: "settings-user" */ './components/UserAddDialog.vue')
                }
              ]
            }
          ]
        },
        {
          path: '/account',
          name: 'account',
          component: () => import(/* webpackChunkName: "account" */ './components/AccountSettings.vue')
        },
        {
          path: '/libraries/:libraryId/:index?',
          name: 'browse-libraries',
          component: () => import(/* webpackChunkName: "browse-libraries" */ './components/BrowseLibraries.vue'),
          props: (route) => ({ libraryId: Number(route.params.libraryId) })
        },
        {
          path: '/series/:seriesId/:index?',
          name: 'browse-series',
          component: () => import(/* webpackChunkName: "browse-series" */ './components/BrowseSeries.vue'),
          props: (route) => ({ seriesId: Number(route.params.seriesId) })
        },
        {
          path: '/book/:bookId',
          name: 'browse-book',
          component: () => import(/* webpackChunkName: "browse-book" */ './components/BrowseBook.vue'),
          props: (route) => ({ bookId: Number(route.params.bookId) })
        }
      ]
    },
    {
      path: '/startup',
      name: 'startup',
      component: () => import(/* webpackChunkName: "startup" */ './views/Startup.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import(/* webpackChunkName: "login" */ './views/Login.vue')
    },
    {
      path: '/book/:bookId/read',
      name: 'read-book',
      component: () => import(/* webpackChunkName: "read-book" */ './views/BookReader.vue'),
      props: (route) => ({ bookId: Number(route.params.bookId) })
    },
    {
      path: '*',
      name: 'notfound',
      component: () => import(/* webpackChunkName: "notfound" */ './views/PageNotFound.vue')
    }
  ],
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      if (to.name !== from.name) {
        return { x: 0, y: 0 }
      }
    }
  }
})

router.beforeEach((to, from, next) => {
  if (to.name !== 'startup' && to.name !== 'login' && !lStore.getters.authenticated) next({ name: 'startup' })
  else next()
})

export default router
