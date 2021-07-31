import {DEPT} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export async function list(param) {
  return request(DEPT, METHOD.GET, param)
}

export async function add(param) {
  return request(DEPT, METHOD.POST, param)
}

export async function update(param) {
  return request(DEPT, METHOD.PUT, param)
}

export async function remove(id) {
  return request(DEPT + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(DEPT + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export default {
  list, add, update, remove, removeBatch
}
