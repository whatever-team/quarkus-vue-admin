import {OSS} from '@/services/api'

export async function upload(type) {
  return OSS + '/upload/' + type
}

export async function preview(path) {
  return OSS + '/' + path
}

export default {
  upload, preview
}
