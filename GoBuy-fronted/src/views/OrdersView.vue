<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { listMyOrders, confirmOrder, cancelOrder, deleteOrder } from '@/api/order'
import { mockPay } from '@/api/payment'

const orders = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const currentStatus = ref<number | undefined>(undefined)
const loading = ref(false)

const statusTabs = [
  { label: '全部', value: undefined },
  { label: '待付款', value: 0 },
  { label: '已付款', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已送达', value: 3 },
  { label: '已完成', value: 4 },
  { label: '已取消', value: 5 },
]

const statusMap: Record<string, string> = {
  '0': '待付款',
  '1': '已付款',
  '2': '已发货',
  '3': '已送达',
  '4': '已完成',
  '5': '已取消',
  PENDING_PAYMENT: '待付款',
  PAID: '已付款',
  SHIPPED: '已发货',
  DELIVERED: '已送达',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}

async function fetchOrders() {
  loading.value = true
  try {
    const data: any = await listMyOrders({
      status: currentStatus.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value,
    })
    orders.value = data?.records || []
    total.value = data?.total || 0
  } catch {
    orders.value = []
  } finally {
    loading.value = false
  }
}

function switchTab(status: number | undefined) {
  currentStatus.value = status
  pageNum.value = 1
  fetchOrders()
}

async function handlePay(id: number) {
  if (!confirm('确认支付该订单？')) return
  try {
    await mockPay(id)
    fetchOrders()
  } catch { /* handled */ }
}

async function handleConfirm(id: number) {
  if (!confirm('确认收货？')) return
  try {
    await confirmOrder(id)
    fetchOrders()
  } catch { /* handled */ }
}

async function handleCancel(id: number) {
  if (!confirm('确认取消订单？')) return
  try {
    await cancelOrder(id)
    fetchOrders()
  } catch { /* handled */ }
}

async function handleDelete(id: number) {
  if (!confirm('确认删除订单？')) return
  try {
    await deleteOrder(id)
    fetchOrders()
  } catch { /* handled */ }
}

onMounted(fetchOrders)
</script>

<template>
  <div class="max-w-[1190px] mx-auto py-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">我的订单</h1>

    <div class="flex border-b border-gray-200 mb-6">
      <button
        v-for="tab in statusTabs"
        :key="String(tab.value)"
        class="px-6 py-3 text-sm font-bold transition-colors border-b-2 -mb-px"
        :class="currentStatus === tab.value ? 'text-taobao border-taobao' : 'text-gray-500 border-transparent hover:text-taobao'"
        @click="switchTab(tab.value)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div v-if="loading" class="text-center py-20 text-gray-400">
      <i class="fa-solid fa-spinner fa-spin text-3xl"></i>
      <p class="mt-4">加载中...</p>
    </div>

    <template v-else>
      <div v-if="orders.length" class="space-y-4">
        <div v-for="order in orders" :key="order.id" class="bg-white rounded-lg shadow-sm overflow-hidden">
          <div class="flex items-center justify-between px-6 py-3 bg-gray-50 border-b text-sm">
            <div class="flex items-center gap-4 text-gray-500">
              <span>订单号：{{ order.orderNo }}</span>
              <span>{{ order.createdAt }}</span>
            </div>
            <span class="font-bold" :class="order.status === '4' || order.status === 'COMPLETED' ? 'text-green-600' : 'text-taobao'">
              {{ statusMap[order.status] || order.status }}
            </span>
          </div>
          <div class="px-6 py-4">
            <div class="text-sm text-gray-500 mb-2">
              订单金额：<span class="text-taobao text-lg font-bold">¥{{ order.totalAmount }}</span>
            </div>
            <div v-if="order.note" class="text-xs text-gray-400">备注：{{ order.note }}</div>
          </div>
          <div class="flex items-center justify-end gap-2 px-6 py-3 border-t bg-gray-50">
            <button
              v-if="order.status === '0' || order.status === 'PENDING_PAYMENT'"
              class="px-4 py-1.5 text-sm bg-taobao text-white rounded-full hover:bg-red-500 transition-colors"
              @click="handlePay(order.id)"
            >
              立即支付
            </button>
            <button
              v-if="order.status === '2' || order.status === 'SHIPPED' || order.status === '3' || order.status === 'DELIVERED'"
              class="px-4 py-1.5 text-sm border border-taobao text-taobao rounded-full hover:bg-taobao hover:text-white transition-colors"
              @click="handleConfirm(order.id)"
            >
              确认收货
            </button>
            <button
              v-if="order.status === '0' || order.status === 'PENDING_PAYMENT'"
              class="px-4 py-1.5 text-sm border border-gray-300 text-gray-500 rounded-full hover:border-red-500 hover:text-red-500 transition-colors"
              @click="handleCancel(order.id)"
            >
              取消订单
            </button>
            <button
              v-if="order.status === '5' || order.status === 'CANCELLED' || order.status === '4' || order.status === 'COMPLETED'"
              class="px-4 py-1.5 text-sm border border-gray-300 text-gray-400 rounded-full hover:border-red-500 hover:text-red-500 transition-colors"
              @click="handleDelete(order.id)"
            >
              删除订单
            </button>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-20 text-gray-400">
        <i class="fa-solid fa-receipt text-5xl mb-4"></i>
        <p class="text-lg">暂无订单</p>
        <router-link to="/" class="text-taobao hover:underline mt-4 inline-block">去逛逛</router-link>
      </div>
    </template>
  </div>
</template>
