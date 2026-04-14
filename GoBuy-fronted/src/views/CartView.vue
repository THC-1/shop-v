<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const cartStore = useCartStore()

onMounted(() => {
  cartStore.fetchCartItems()
})

function goCheckout() {
  if (cartStore.selectedItems.length === 0) {
    alert('请先选择商品')
    return
  }
  router.push('/checkout')
}
</script>

<template>
  <div class="max-w-[1190px] mx-auto py-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">我的购物车</h1>

    <div v-if="cartStore.loading" class="text-center py-20 text-gray-400">
      <i class="fa-solid fa-spinner fa-spin text-3xl"></i>
      <p class="mt-4">加载中...</p>
    </div>

    <template v-else>
      <div v-if="cartStore.items.length" class="bg-white rounded-lg shadow-sm overflow-hidden">
        <div class="grid grid-cols-[40px_1fr_120px_180px_120px_80px] gap-4 items-center px-6 py-3 bg-gray-50 text-sm text-gray-500 border-b">
          <div class="flex justify-center">
            <input
              type="checkbox"
              :checked="cartStore.selectedItems.length === cartStore.items.length && cartStore.items.length > 0"
              class="w-4 h-4 text-taobao rounded border-gray-300 focus:ring-taobao cursor-pointer"
              @change="cartStore.items.forEach((item: any) => cartStore.toggleSelect(item.id))"
            />
          </div>
          <span>商品信息</span>
          <span class="text-center">单价</span>
          <span class="text-center">数量</span>
          <span class="text-center">小计</span>
          <span class="text-center">操作</span>
        </div>

        <div
          v-for="item in cartStore.items"
          :key="item.id"
          class="grid grid-cols-[40px_1fr_120px_180px_120px_80px] gap-4 items-center px-6 py-4 border-b hover:bg-gray-50 transition-colors"
        >
          <div class="flex justify-center">
            <input
              type="checkbox"
              :checked="item.selected"
              class="w-4 h-4 text-taobao rounded border-gray-300 focus:ring-taobao cursor-pointer"
              @change="cartStore.toggleSelect(item.id)"
            />
          </div>
          <div class="flex items-center gap-3">
            <router-link :to="`/products/${item.productId}`" class="w-20 h-20 rounded overflow-hidden bg-gray-100 shrink-0">
              <img v-if="item.productImage" :src="item.productImage" :alt="item.productName" class="w-full h-full object-cover" />
              <div v-else class="w-full h-full flex items-center justify-center text-gray-300">
                <i class="fa-solid fa-image"></i>
              </div>
            </router-link>
            <div class="min-w-0">
              <router-link :to="`/products/${item.productId}`" class="text-sm text-gray-800 hover:text-taobao line-clamp-2">{{ item.productName }}</router-link>
              <p v-if="item.skuInfo" class="text-xs text-gray-400 mt-1">{{ item.skuInfo }}</p>
            </div>
          </div>
          <div class="text-center text-taobao font-bold">¥{{ item.price }}</div>
          <div class="flex items-center justify-center">
            <div class="flex items-center border border-gray-300 rounded">
              <button class="w-8 h-8 flex items-center justify-center hover:bg-gray-100 text-sm" @click="cartStore.updateQuantity(item.id, item.quantity - 1)">-</button>
              <span class="w-10 h-8 flex items-center justify-center border-x border-gray-300 text-sm">{{ item.quantity }}</span>
              <button class="w-8 h-8 flex items-center justify-center hover:bg-gray-100 text-sm" @click="cartStore.updateQuantity(item.id, item.quantity + 1)">+</button>
            </div>
          </div>
          <div class="text-center text-taobao font-bold">¥{{ item.totalPrice }}</div>
          <div class="text-center">
            <button class="text-gray-400 hover:text-red-500 transition-colors" @click="cartStore.removeItem(item.id)">
              <i class="fa-solid fa-trash-can"></i>
            </button>
          </div>
        </div>

        <div class="flex items-center justify-between px-6 py-4 bg-gray-50">
          <div class="flex items-center gap-4">
            <button
              class="text-sm text-gray-500 hover:text-red-500 transition-colors"
              @click="cartStore.clearAll()"
            >
              清空购物车
            </button>
          </div>
          <div class="flex items-center gap-6">
            <span class="text-sm text-gray-500">
              已选 <span class="text-taobao font-bold">{{ cartStore.selectedItems.length }}</span> 件商品
            </span>
            <span class="text-sm text-gray-500">
              合计：<span class="text-taobao text-2xl font-bold">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
            </span>
            <button
              class="bg-taobao hover:bg-taobao-dark text-white font-bold px-10 py-3 rounded-full text-lg transition-colors disabled:opacity-50"
              :disabled="cartStore.selectedItems.length === 0"
              @click="goCheckout"
            >
              结算
            </button>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-20 text-gray-400">
        <i class="fa-solid fa-cart-shopping text-6xl mb-4"></i>
        <p class="text-lg">购物车是空的</p>
        <router-link to="/" class="text-taobao hover:underline mt-4 inline-block">去逛逛</router-link>
      </div>
    </template>
  </div>
</template>
