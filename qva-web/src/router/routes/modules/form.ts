import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const FORM: AppRouteRecordRaw = {
  path: '/mock',
  name: 'mock',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: '模拟',
    icon: 'icon-settings',
    requiresAuth: true,
    order: 3,
  },
  children: [
    {
      path: 'create-order',
      name: 'CreateOrder',
      component: () => import('@/views/mock/create-order.vue'),
      meta: {
        locale: '模拟下单',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'step',
      name: 'Step',
      component: () => import('@/views/form/step/index.vue'),
      meta: {
        locale: 'menu.form.step',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
    {
      path: 'group',
      name: 'Group',
      component: () => import('@/views/form/group/index.vue'),
      meta: {
        locale: 'menu.form.group',
        requiresAuth: true,
        roles: ['admin'],
      },
    },
  ],
};

export default FORM;
