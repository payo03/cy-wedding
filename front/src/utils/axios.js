import axios from 'axios'

const DOMAIN_HEADER = 'X-DOMAIN'
const QR_CODE_HEADER = 'X-QR-CODE'

const instance = axios.create({
  baseURL: '/api'
})

const domain = localStorage.getItem('domain')
const qrCode = localStorage.getItem('qrcode')
if (domain) {
  instance.defaults.headers.common[DOMAIN_HEADER] = domain
}
if (qrCode) {
  instance.defaults.headers.common[QR_CODE_HEADER] = qrCode
}

export function setHeader(domain, qrCode) {
  localStorage.setItem('domain', domain)
  localStorage.setItem('qrcode', qrCode)

  instance.defaults.headers.common[DOMAIN_HEADER] = domain
  instance.defaults.headers.common[QR_CODE_HEADER] = qrCode
}

export default instance