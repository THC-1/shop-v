import request from '@/utils/request'
import type { PageDTO, PageVO, UserVO } from './types'

export const listUsers = (params: PageDTO & { username?: string; status?: string }) => {
  return request.get<any, { data: PageVO<UserVO> }>('/users', { params })
}

export const getUser = (id: number) => {
  return request.get<any, { data: UserVO }>(`/users/${id}`)
}

export const updateUserStatus = (id: number, status: string) => {
  return request.patch<any, any>(`/users/${id}/status`, { status })
}

export const assignUserRoles = (id: number, roleIds: number[]) => {
  return request.post<any, any>(`/users/${id}/roles`, { roleIds })
}