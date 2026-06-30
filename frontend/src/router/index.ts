import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue'),
    },
    {
      path: '/lobby',
      name: 'Lobby',
      component: () => import('@/views/Lobby.vue'),
    },
    {
      path: '/game/:roomId',
      name: 'Game',
      component: () => import('@/views/Game.vue'),
    },
  ],
})

export default router
