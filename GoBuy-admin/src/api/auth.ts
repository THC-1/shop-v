import request from '@/utils/request'
import type { LoginDTO, LoginVO } from './types'

export const login = (data: LoginDTO) => {
  return request.post<any, { data: LoginVO }>('/sessions', data)
}

export const logout = () => {
  return request.delete<any, any>('/sessions')
}