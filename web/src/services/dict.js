import {DICT, DICT_ITEM} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export async function list(param) {
  return request(DICT, METHOD.GET, param)
}

export async function add(param) {
  return request(DICT, METHOD.POST, param)
}

export async function update(param) {
  return request(DICT, METHOD.PUT, param)
}

export async function remove(id) {
  return request(DICT + `/${id}`, METHOD.DELETE)
}

export async function removeBatch(ids) {
  return request(DICT + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

/**
 * 根据字典编码
 * @param typeCode 字典编码
 * @param itemCode 值
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function one(dictCode, itemCode) {
  return request(DICT + `/one/${dictCode}/${itemCode}`, METHOD.GET)
}

/**
 * 根据字典编码
 * @param typeCode
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function listByTypeCode(typeCode) {
  return request(DICT + `/list/${typeCode}`, METHOD.GET)
}

/* 字典值 */

export async function listItem(param) {
  return request(DICT_ITEM, METHOD.GET, param)
}

export async function addItem(param) {
  return request(DICT_ITEM, METHOD.POST, param)
}

export async function updateItem(param) {
  return request(DICT_ITEM, METHOD.PUT, param)
}

export async function removeItem(id) {
  return request(DICT_ITEM + `/${id}`, METHOD.DELETE)
}

export async function removeBatchItem(ids) {
  return request(DICT_ITEM + '/batch', METHOD.DELETE, {
    ids: ids
  })
}

export default {
  list, add, update, remove, removeBatch,
  one, listByTypeCode,
  listItem, addItem, updateItem, removeItem, removeBatchItem,
}
