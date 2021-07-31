import {BUSINESS_LOG} from '@/services/api'
import {request, METHOD} from '@/utils/request'

export async function list(param) {
  return request(BUSINESS_LOG, METHOD.GET, param)
}

export default {
  list
}
