import request from '@/utils/request'
import type { PageDTO, PageVO, RoleVO, PermissionVO } from './types'

export const listRoles = (params: PageDTO & { name?: string; status?: string }) => {
  return request.get<any, { data: PageVO<RoleVO> }>('/roles', { params })
}

export const getRole = (id: number) => {
  return request.get<any, { data: RoleVO }>(`/roles/${id}`)
}

export const createRole = (data: { name: string; code: string; description?: string; sort?: number; permissionIds?: number[] }) => {
  return request.post<any, { data: { id: number } }>('/roles', data)
}

export const updateRole = (id: number, data: { name?: string; description?: string; sort?: number; status?: string; permissionIds?: number[] }) => {
  return request.put<any, any>(`/roles/${id}`, data)
}

export const deleteRole = (id: number) => {
  return request.delete<any, any>(`/roles/${id}`)
}

export const listPermissions = () => {
  return request.get<any, { data: PermissionVO[] }>('/permissions')
}

export const assignPermissions = (roleId: number, permissionIds: number[]) => {
  return request.post<any, any>(`/roles/${roleId}/permissions`, { permissionIds })
}