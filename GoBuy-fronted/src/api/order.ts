import request from './request'

export function createOrder(data: {
  addressId: number
  items: { productId: number; skuId: number; productName?: string; quantity: number; price: number }[]
  note?: string
}) {
  return request.post('/orders', data)
}

export function listMyOrders(params?: { status?: number; pageNum?: number; pageSize?: number }) {
  return request.get('/orders/my', { params })
}

export function getOrderDetail(id: number) {
  return request.get(`/orders/${id}/detail`)
}

export function confirmOrder(id: number) {
  return request.put(`/orders/${id}/confirm`)
}

export function refundOrder(id: number) {
  return request.put(`/orders/${id}/refund`)
}

export function cancelOrder(id: number) {
  return request.put(`/orders/${id}/cancel`)
}

export function deleteOrder(id: number) {
  return request.delete(`/orders/${id}`)
}
