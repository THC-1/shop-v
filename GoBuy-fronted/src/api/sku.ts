import request from './request'

export interface Sku {
  id: number
  productId: number
  name: string
  specValues: string
  price: number
  stock: number
  image: string
  createdAt: string
  updatedAt: string
}

export function getSkusByProductId(productId: number): Promise<Sku[]> {
  return request.get(`/skus/product/${productId}`) as Promise<Sku[]>
}