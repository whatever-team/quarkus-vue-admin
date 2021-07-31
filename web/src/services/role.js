import {ROLE} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export async function list(param) {
  return request(ROLE, METHOD.GET, param)
}

export async function add(param) {
  return request(ROLE, METHOD.POST, param)
}

export async function update(param) {
  return request(ROLE, METHOD.PUT, param)
}

export async function remove(id) {
  return request(ROLE + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(ROLE + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export default {
  list, add, update, remove, removeBatch
}
