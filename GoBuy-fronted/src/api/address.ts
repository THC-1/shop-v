import request from './request'

export function listMyAddresses() {
  return request.get('/addresses/my')
}

export function getDefaultAddress() {
  return request.get('/addresses/default')
}

export function setDefaultAddress(id: number) {
  return request.put(`/addresses/${id}/default`)
}

export function createAddress(data: {
  receiverName: string
  phone: string
  province: string
  city: string
  district: string
  detailAddress: string
  isDefault?: number
}) {
  return request.post('/addresses', data)
}

export function updateAddress(id: number, data: {
  receiverName?: string
  phone?: string
  province?: string
  city?: string
  district?: string
  detailAddress?: string
  isDefault?: number
}) {
  return request.put(`/addresses/${id}`, data)
}

export function deleteAddress(id: number) {
  return request.delete(`/addresses/${id}`)
}
