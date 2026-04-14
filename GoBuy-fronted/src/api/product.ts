import request from './request'

export function listProducts() {
  return request.get('/products')
}

export function getProduct(id: number) {
  return request.get(`/products/${id}`)
}

export function searchProducts(params: {
  keyword?: string
  categoryId?: number
  brandId?: number
  minPrice?: number
  maxPrice?: number
  sortField?: string
  sortOrder?: string
  pageNum?: number
  pageSize?: number
}) {
  return request.get('/products/search', { params })
}

export function getCategoryProducts(categoryId: number, pageNum = 1, pageSize = 10) {
  return request.get(`/products/category/${categoryId}`, { params: { pageNum, pageSize } })
}

export function getProductStock(id: number) {
  return request.get(`/products/${id}/stock`)
}
