// 视图组件
const view = {
  tabs: () => import('@/layouts/tabs'),
  blank: () => import('@/layouts/BlankView'),
  page: () => import('@/layouts/PageView')
}

// 路由组件注册
const routerMap = {
  login: {
    authority: '*',
    path: '/login',
    component: () => import('@/pages/login')
  },
  root: {
    path: '/',
    name: '首页',
    redirect: '/login',
    component: view.tabs
  },
  accountCenter: {
    path: '/account/center',
    name: '个人中心',
    component: () => import('@/pages/account/center')
  },
  dashboard: {
    name: 'Dashboard',
    component: view.blank
  },
  workplace: {
    name: '工作台',
    component: () => import('@/pages/dashboard/workplace')
  },
  analysis: {
    name: '分析页',
    component: () => import('@/pages/dashboard/analysis')
  },
  form: {
    name: '表单页',
    icon: 'form',
    component: view.page
  },
  basicForm: {
    path: 'basic',
    name: '基础表单',
    component: () => import('@/pages/form/basic')
  },
  stepForm: {
    path: 'step',
    name: '分步表单',
    component: () => import('@/pages/form/step')
  },
  advanceForm: {
    path: 'advance',
    name: '高级表单',
    component: () => import('@/pages/form/advance')
  },
  list: {
    name: '列表页',
    icon: 'table',
    component: view.page
  },
  queryList: {
    path: 'query',
    name: '查询表格',
    component: () => import('@/pages/list/QueryList')
  },
  primaryList: {
    path: 'primary',
    name: '标准列表',
    component: () => import('@/pages/list/StandardList')
  },
  cardList: {
    path: 'card',
    name: '卡片列表',
    component: () => import('@/pages/list/CardList')
  },
  searchList: {
    path: 'search',
    name: '搜索列表',
    component: () => import('@/pages/list/search/SearchLayout')
  },
  article: {
    name: '文章',
    component: () => import('@/pages/list/search/ArticleList')
  },
  application: {
    name: '应用',
    component: () => import('@/pages/list/search/ApplicationList')
  },
  project: {
    name: '项目',
    component: () => import('@/pages/list/search/ProjectList')
  },
  details: {
    name: '详情页',
    icon: 'profile',
    component: view.blank
  },
  basicDetails: {
    path: 'basic',
    name: '基础详情页',
    component: () => import('@/pages/detail/BasicDetail')
  },
  advanceDetails: {
    path: 'advance',
    name: '高级详情页',
    component: () => import('@/pages/detail/AdvancedDetail')
  },
  result: {
    name: '结果页',
    icon: 'check-circle-o',
    component: view.page
  },
  success: {
    name: '成功',
    component: () => import('@/pages/result/Success')
  },
  error: {
    name: '失败',
    component: () => import('@/pages/result/Error')
  },
  exception: {
    name: '异常页',
    icon: 'warning',
    component: view.blank
  },
  exp403: {
    authority: '*',
    name: 'exp403',
    path: '403',
    component: () => import('@/pages/exception/403')
  },
  exp404: {
    name: 'exp404',
    path: '404',
    component: () => import('@/pages/exception/404')
  },
  exp500: {
    name: 'exp500',
    path: '500',
    component: () => import('@/pages/exception/500')
  },
  components: {
    name: '小组件',
    icon: 'appstore-o',
    component: view.page
  },
  taskCard: {
    name: '任务卡片',
    component: () => import('@/pages/components/TaskCard')
  },
  palette: {
    name: '颜色复选框',
    component: () => import('@/pages/components/Palette')
  },
  advanceTable: {
    name: '高级表格',
    component: () => import('@/pages/components/table')
  },
  multipart: {
    component: () => import('@/pages/components/Multipart')
  },
  system: {
    name: '系统管理',
    icon: 'setting',
    component: view.blank
  },
  systemMenu: {
    component: () => import('@/pages/system/menu/List')
  },
  systemRole: {
    component: () => import('@/pages/system/role/List')
  },
  systemUser: {
    component: () => import('@/pages/system/user/List')
  },
  systemDept: {
    component: () => import('@/pages/system/dept/List')
  },
  systemDict: {
    component: () => import('@/pages/system/dict/Index')
  },
  systemVar: {
    component: () => import('@/pages/system/var/List')
  },
  systemMessage: {
    path: '/system/message',
    component: () => import('@/pages/system/message/List')
  },
  monitor: {
    name: '监控中心',
    icon: 'monitor',
    component: view.blank
  },
  businessLog: {
    component: () => import('@/pages/monitor/BusinessLog')
  },
  swaggerUI: {
    component: view.page
  },
  devUI: {
    component: view.page
  },
  quartz: {
    component: () => import('@/pages/exception/dev')
  }
}
export default routerMap

