import axios from 'axios'

const DOMAIN_HEADER = 'X-DOMAIN'
const QR_CODE_HEADER = 'X-QR-CODE'

const instance = axios.create({
  baseURL: '/api'
})

const groupName = localStorage.getItem('groupName')
const qrCode = localStorage.getItem('qrCode')
if (groupName) {
  instance.defaults.headers.common[DOMAIN_HEADER] = groupName
}
if (qrCode) {
  instance.defaults.headers.common[QR_CODE_HEADER] = qrCode
}

export function setHeader(groupName, qrCode) {
  localStorage.setItem('groupName', groupName)
  localStorage.setItem('qrCode', qrCode)

  instance.defaults.headers.common[DOMAIN_HEADER] = groupName
  instance.defaults.headers.common[QR_CODE_HEADER] = qrCode
}

export default instance