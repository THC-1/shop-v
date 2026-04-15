import request from '@/utils/request'
import type { PageDTO, PageVO, AdminVO } from './types'

export const listAdmins = (params: PageDTO & { username?: string; status?: string }) => {
  return request.get<any, { data: PageVO<AdminVO> }>('/admins', { params })
}

export const getAdmin = (id: number) => {
  return request.get<any, { data: AdminVO }>(`/admins/${id}`)
}

export const createAdmin = (data: { username: string; password: string; nickname?: string; email?: string; phone?: string; roleIds: number[] }) => {
  return request.post<any, { data: { id: number } }>('/admins', data)
}

export const updateAdmin = (id: number, data: { nickname?: string; email?: string; phone?: string; status?: string; roleIds?: number[] }) => {
  return request.put<any, any>(`/admins/${id}`, data)
}

export const deleteAdmin = (id: number) => {
  return request.delete<any, any>(`/admins/${id}`)
}