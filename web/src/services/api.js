//跨域代理前缀
const API_PROXY_PREFIX='/api'
const BASE_URL = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_API_BASE_URL : API_PROXY_PREFIX
// const BASE_URL = process.env.VUE_APP_API_BASE_URL
module.exports = {
  BASE_URL: BASE_URL,
  OSS: `${BASE_URL}/oss`,
  USER: `${BASE_URL}/sys-user`,
  MENU: `${BASE_URL}/sys-menu`,
  DEPT: `${BASE_URL}/sys-dept`,
  ROLE: `${BASE_URL}/sys-role`,
  DICT: `${BASE_URL}/sys-dict`,
  DICT_ITEM: `${BASE_URL}/sys-dict-item`,
  BUSINESS_LOG: `${BASE_URL}/sys-business-log`,
  GOODS: `${BASE_URL}/goods`,
  GOODS_COLUMNS: `${BASE_URL}/columns`,
}
