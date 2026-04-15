import request from '@/utils/request'
import type { PageDTO, PageVO, OrderVO } from './types'

export const listOrders = (params: PageDTO & { orderNo?: string; status?: string; userId?: number; startDate?: string; endDate?: string }) => {
  return request.get<any, { data: PageVO<OrderVO> }>('/orders', { params })
}

export const getOrder = (id: number) => {
  return request.get<any, { data: OrderVO }>(`/orders/${id}`)
}

export const shipOrder = (id: number, data: { expressCompany: string; expressNo: string }) => {
  return request.patch<any, any>(`/orders/${id}/ship`, data)
}

export const batchShip = (data: { orderIds: number[]; expressCompany: string; expressNo: string }) => {
  return request.post<any, any>('/orders/batch/ship', data)
}