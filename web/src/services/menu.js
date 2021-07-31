import {MENU} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export async function list(param) {
  return request(MENU, METHOD.GET, param)
}

export async function listTree(param) {
  return request(MENU + '/tree', METHOD.GET, param)
}

export async function add(param) {
  return request(MENU, METHOD.POST, param)
}

export async function update(param) {
  return request(MENU, METHOD.PUT, param)
}

export async function remove(id) {
  return request(MENU + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(MENU + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export default {
  list, listTree, add, update, remove, removeBatch
}
