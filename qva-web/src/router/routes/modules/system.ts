import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const LIST: AppRouteRecordRaw = {
  path: '/system',
  name: 'system',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.system',
    requiresAuth: true,
    icon: 'icon-settings',
    order: 2,
  },
  children: [
    {
      path: 'users', // The midline path complies with SEO specifications
      name: 'Users',
      component: () => import('@/views/system/users/index.vue'),
      meta: {
        locale: 'menu.system.users',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'card',
      name: 'Card',
      component: () => import('@/views/list/card/index.vue'),
      meta: {
        locale: 'menu.list.cardList',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'order',
      name: 'Order',
      component: () => import('@/views/order/order.vue'),
      meta: {
        locale: '订单管理',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'refund',
      name: 'Refund',
      component: () => import('@/views/order/refund.vue'),
      meta: {
        locale: '退款订单',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

export default LIST;
