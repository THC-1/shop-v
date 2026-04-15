import request from '@/utils/request'
import type { PageDTO, PageVO, BrandVO } from './types'

export const listBrands = (params: PageDTO & { name?: string }) => {
  return request.get<any, { data: PageVO<BrandVO> }>('/brands', { params })
}

export const getBrand = (id: number) => {
  return request.get<any, { data: BrandVO }>(`/brands/${id}`)
}

export const createBrand = (data: { name: string; logo?: string; description?: string }) => {
  return request.post<any, { data: { id: number } }>('/brands', data)
}

export const updateBrand = (id: number, data: { name?: string; logo?: string; description?: string }) => {
  return request.put<any, any>(`/brands/${id}`, data)
}

export const deleteBrand = (id: number) => {
  return request.delete<any, any>(`/brands/${id}`)
}