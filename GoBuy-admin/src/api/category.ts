import request from '@/utils/request'
import type { CategoryTreeVO } from './types'

export const listCategories = () => {
  return request.get<any, { data: CategoryTreeVO[] }>('/categories')
}

export const getCategory = (id: number) => {
  return request.get<any, { data: CategoryTreeVO }>(`/categories/${id}`)
}

export const createCategory = (data: { name: string; parentId?: number; sort?: number; icon?: string }) => {
  return request.post<any, { data: { id: number } }>('/categories', data)
}

export const updateCategory = (id: number, data: { name?: string; parentId?: number; sort?: number; icon?: string; status?: string }) => {
  return request.put<any, any>(`/categories/${id}`, data)
}

export const deleteCategory = (id: number) => {
  return request.delete<any, any>(`/categories/${id}`)
}