import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'main-view',
    component: () => import('../views/mainView.vue'),
    children: [
      {
        path: 'welcome', // 默认访问 '/' 时不会渲染这个，除非在 mainView.vue 中有额外的逻辑
        component: () => import('../views/main/welcome.vue')
      },
      {
        path: 'about',
        component: () => import('../views/main/about.vue')
      },
      {
        path: 'business/',
        children: [{
          path: 'daily-train',
          component: () => import('../views/main/business/daily-train.vue'),
        }]
      },
      {
        path: 'base/',
        children: [{
          path: 'station',
          component: () => import('../views/main/base/station.vue'),
        }, {
          path: 'train',
          component: () => import('../views/main/base/train.vue'),
        }, {
          path: 'train-station',
          component: () => import('../views/main/base/train-station.vue'),
        }, {
          path: 'train-carriage',
          component: () => import('../views/main/base/train-carriage.vue'),
        }, {
          path: 'train-seat',
          component: () => import('../views/main/base/train-seat.vue'),
        }]
      },
      {
        path: 'batch/',
        children: [{
          path: 'job',
          component: () => import('../views/main/batch/job.vue')
        }]
      }

      // 如果你想要 welcome 作为默认子路由，你需要在 mainView.vue 中添加额外的逻辑
    ]
  },
  // 如果你确实需要重定向，并且想要替换 '/' 路由的行为，可以添加以下重定向规则
  // {
  //   path: '/',
  //   redirect: '/welcome'
  // }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router