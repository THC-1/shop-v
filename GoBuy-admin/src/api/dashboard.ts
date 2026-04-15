import request from '@/utils/request'
import type { SalesSummaryVO, SalesTrendVO, TopProductVO, UvPvVO } from './types'

export const getSalesSummary = (params: { startDate: string; endDate: string }) => {
  return request.get<any, { data: SalesSummaryVO }>('/dashboard/sales-summary', { params })
}

export const getSalesTrend = (params: { startDate: string; endDate: string; type?: string }) => {
  return request.get<any, { data: SalesTrendVO[] }>('/dashboard/sales-trend', { params })
}

export const getTopProducts = (params: { startDate?: string; endDate?: string; limit?: number }) => {
  return request.get<any, { data: TopProductVO[] }>('/dashboard/top-products', { params })
}

export const getUvPv = (params: { startDate: string; endDate: string }) => {
  return request.get<any, { data: UvPvVO }>('/dashboard/uv-pv', { params })
}