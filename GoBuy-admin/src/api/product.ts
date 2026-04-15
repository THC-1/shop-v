import request from '@/utils/request'
import type { PageDTO, PageVO, ProductVO, ProductCreateDTO } from './types'

export const listProducts = (params: PageDTO & { name?: string; categoryId?: number; brandId?: number; status?: string }) => {
  return request.get<any, { data: PageVO<ProductVO> }>('/products', { params })
}

export const getProduct = (id: number) => {
  return request.get<any, { data: ProductVO }>(`/products/${id}`)
}

export const createProduct = (data: ProductCreateDTO) => {
  return request.post<any, { data: { id: number } }>('/products', data)
}

export const updateProduct = (id: number, data: ProductCreateDTO) => {
  return request.put<any, any>(`/products/${id}`, data)
}

export const deleteProduct = (id: number) => {
  return request.delete<any, any>(`/products/${id}`)
}

export const updateProductStatus = (id: number, status: string) => {
  return request.patch<any, any>(`/products/${id}/status`, { status })
}

export const batchUpdateStatus = (productIds: number[], status: string) => {
  return request.post<any, any>('/products/batch/status', { productIds, status })
}