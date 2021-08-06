import {request, METHOD} from '@/utils/request'
import {BASE_URL} from "@/services/api";

const URL = BASE_URL + "/sys-var"

export async function list(param) {
  return request(URL, METHOD.GET, param)
}

export async function add(param) {
  return request(URL, METHOD.POST, param)
}

export async function update(param) {
  return request(URL, METHOD.PUT, param)
}

export async function remove(id) {
  return request(URL + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(URL + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export async function exportExcel(param) {
  return request(URL + '/export', METHOD.POST, param, {
    responseType: 'blob'
  })
}

export const importExcel = URL + '/import'

export default {
  list, add, update, remove, removeBatch, exportExcel, importExcel
}
