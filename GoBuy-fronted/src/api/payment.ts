import request from './request'

export function getPaymentByOrderId(orderId: number) {
  return request.get(`/payments/order/${orderId}`)
}

export function listMyPayments() {
  return request.get('/payments/my')
}

export function handlePaymentCallback(paymentId: number, status: string) {
  return request.post(`/payments/${paymentId}/callback`, null, { params: { status } })
}

export function refundPayment(paymentId: number) {
  return request.put(`/payments/${paymentId}/refund`)
}
