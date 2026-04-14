import request from './request'

export function addCartItem(data: { productId: number; skuId: number; quantity: number }) {
  return request.post('/carts/items', data)
}

export function listCartItems() {
  return request.get('/carts/items')
}

export function updateCartItemQuantity(id: number, quantity: number) {
  return request.put(`/carts/items/${id}`, { quantity })
}

export function deleteCartItem(id: number) {
  return request.delete(`/carts/items/${id}`)
}

export function toggleCartItemSelected(id: number) {
  return request.put(`/carts/items/${id}/select`)
}

export function clearCart() {
  return request.delete('/carts/items')
}

export function removeCartItems(cartItemIds: number[]) {
  return request.delete('/carts/items/batch', { data: cartItemIds })
}
