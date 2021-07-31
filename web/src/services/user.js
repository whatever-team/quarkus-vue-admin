import {USER, MENU} from '@/services/api'
import {request, METHOD, removeAuthorization} from '@/utils/request'

/**
 * 登录服务
 * @param name 账户名
 * @param password 账户密码
 * @param code 验证码
 * @param t 验证码时间戳
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function login(name, password, code, t) {
  return request(USER + '/login', METHOD.POST, {
    name: name,
    password: password,
    code,
    t
  })
}

export async function getRoutesConfig() {
  return request(MENU + '/routes', METHOD.GET)
}

/**
 * 退出登录
 */
export function logout() {
  localStorage.removeItem(process.env.VUE_APP_ROUTES_KEY)
  localStorage.removeItem(process.env.VUE_APP_PERMISSIONS_KEY)
  localStorage.removeItem(process.env.VUE_APP_USERS_KEY)
  removeAuthorization()
}

export async function list(param) {
  return request(USER, METHOD.GET, param)
}

export async function listTree(param) {
  return request(USER + '/tree', METHOD.GET, param)
}

export async function add(param) {
  return request(USER, METHOD.POST, param)
}

export async function update(param) {
  return request(USER, METHOD.PUT, param)
}

export async function updatePassword(param) {
  return request(USER + '/password', METHOD.PUT, param)
}

export async function remove(id) {
  return request(USER + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(USER + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export default {
  login,
  logout,
  getRoutesConfig,
  list, listTree, add, update, updatePassword, remove, removeBatch
}
