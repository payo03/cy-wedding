import axios from 'axios'

const instance = axios.create({
  baseURL: '/api'
})

const token = localStorage.getItem('qrcode')
if (token) {
  instance.defaults.headers.common['X-QR-CODE'] = token
}

export function setQrcodeToken(token) {
  localStorage.setItem('qrcode', token)
  instance.defaults.headers.common['X-QR-CODE'] = token
}

export default instance