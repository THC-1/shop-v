export interface LoginDTO {
  username: string
  password: string
}

export interface LoginVO {
  token: string
  adminId: number
  username: string
  nickname: string
  roles: string[]
}

export interface PageDTO {
  page: number
  size: number
}

export interface PageVO<T> {
  total: number
  records: T[]
  size: number
  current: number
  pages: number
}

export interface ProductVO {
  id: number
  name: string
  categoryId: number
  categoryName: string
  brandId: number
  brandName: string
  price: number
  originalPrice: number
  stock: number
  salesCount: number
  status: 'ON_SALE' | 'OFF_SALE'
  images: string[]
  description: string
  createdAt: string
}

export interface ProductCreateDTO {
  name: string
  categoryId: number
  brandId?: number
  price: number
  originalPrice?: number
  stock?: number
  description?: string
  images?: string[]
}

export interface CategoryTreeVO {
  id: number
  name: string
  parentId: number | null
  sort: number
  icon?: string
  children: CategoryTreeVO[]
}

export interface BrandVO {
  id: number
  name: string
  logo?: string
  description?: string
  productCount: number
  createdAt: string
}

export interface OrderVO {
  id: number
  orderNo: string
  userId: number
  username: string
  receiverName: string
  phone: string
  province: string
  city: string
  district: string
  detailAddress: string
  totalAmount: number
  status: string
  expressCompany?: string
  expressNo?: string
  note?: string
  createdAt: string
  items: OrderItemVO[]
}

export interface OrderItemVO {
  id: number
  productId: number
  productName: string
  skuId: number
  skuName: string
  price: number
  quantity: number
  subtotal: number
}

export interface UserVO {
  id: number
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  status: 'ACTIVE' | 'DISABLED'
  orderCount: number
  totalSpent: number
  createdAt: string
}

export interface RoleVO {
  id: number
  name: string
  code: string
  description?: string
  isSystem: boolean
  status: string
  userCount: number
  permissions: PermissionVO[]
  createdAt: string
}

export interface PermissionVO {
  id: number
  name: string
  code: string
  type: 'MENU' | 'BUTTON'
  parentId: number | null
}

export interface AdminVO {
  id: number
  username: string
  nickname?: string
  email?: string
  phone?: string
  status: string
  roles: string[]
  lastLoginAt?: string
  createdAt: string
}

export interface SalesSummaryVO {
  totalOrders: number
  totalAmount: number
  totalUsers: number
  avgOrderAmount: number
}

export interface SalesTrendVO {
  date: string
  orders: number
  amount: number
}

export interface TopProductVO {
  rank: number
  productId: number
  productName: string
  brandName: string
  salesCount: number
  salesAmount: number
}

export interface UvPvVO {
  totalUv: number
  totalPv: number
  avgPvPerUv: number
  dailyList: { date: string; uv: number; pv: number }[]
}