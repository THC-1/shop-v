import axios from 'axios'
import router from '@/router'

const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000,
})

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

request.interceptors.response.use(
  (response) => {
    const result = response.data
    if (result.code !== 200) {
      alert(result.message || '请求失败')
      return Promise.reject(new Error(result.message))
    }
    return result.data
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    alert(error.response?.data?.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
