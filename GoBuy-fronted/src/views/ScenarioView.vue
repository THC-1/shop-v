<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import { getScenario, getScenarioProducts } from '@/api/scenario'
import { addCartItem } from '@/api/cart'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const cartStore = useCartStore()

const scenario = ref<any>(null)
const products = ref<any[]>([])
const checklist = ref<Map<number, boolean>>(new Map())
const loading = ref(true)

const checkedCount = computed(() => {
  let count = 0
  checklist.value.forEach((v) => { if (v) count++ })
  return count
})

const progressPercent = computed(() => {
  if (checklist.value.size === 0) return 0
  return Math.round((checkedCount.value / checklist.value.size) * 100)
})

function toggleCheck(productId: number) {
  checklist.value.set(productId, !checklist.value.get(productId))
}

async function batchAddToCart() {
  const selectedIds: number[] = []
  checklist.value.forEach((checked, productId) => {
    if (checked) selectedIds.push(productId)
  })
  if (selectedIds.length === 0) return

  for (const productId of selectedIds) {
    const product = products.value.find((p: any) => p.id === productId)
    if (product) {
      try {
        await addCartItem({ productId, skuId: product.id, quantity: 1 })
      } catch {
        // continue adding others
      }
    }
  }
  await cartStore.fetchCartItems()
  alert('已加入购物车！')
}

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const [scenarioData, productsData] = await Promise.all([
      getScenario(id),
      getScenarioProducts(id),
    ])
    scenario.value = scenarioData
    products.value = productsData || []
    products.value.forEach((p: any) => {
      checklist.value.set(p.id, false)
    })
  } catch {
    // handle error
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div v-if="loading" class="max-w-[1190px] mx-auto py-20 text-center text-gray-400">
    <i class="fa-solid fa-spinner fa-spin text-3xl"></i>
    <p class="mt-4">加载中...</p>
  </div>

  <template v-else-if="scenario">
    <div class="bg-white py-5 shadow-sm">
      <div class="max-w-[1190px] mx-auto flex items-center">
        <router-link to="/" class="text-taobao text-4xl font-bold italic flex items-baseline cursor-pointer select-none">
          GoBuy
          <span class="text-sm font-normal not-italic text-white bg-taobao px-1.5 py-0.5 rounded ml-2 shadow-sm">场景购</span>
        </router-link>
        <div class="ml-6 text-sm text-gray-500 flex items-center space-x-2">
          <router-link to="/" class="hover:text-taobao">首页</router-link>
          <i class="fa-solid fa-angle-right text-gray-400 text-xs"></i>
          <span class="text-gray-800 font-bold">{{ scenario.name }}</span>
        </div>
      </div>
    </div>

    <div class="max-w-[1190px] mx-auto mt-4 relative h-[320px] rounded-2xl overflow-hidden shadow-lg group">
      <img v-if="scenario.coverUrl" :src="scenario.coverUrl" :alt="scenario.name" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" />
      <div v-else class="w-full h-full bg-gradient-to-r from-orange-500 to-red-600"></div>
      <div class="absolute inset-0 bg-gradient-to-r from-black/80 via-black/40 to-transparent flex flex-col justify-center p-12">
        <h1 class="text-white text-5xl font-bold mb-4 tracking-wide drop-shadow-md">{{ scenario.name }}</h1>
        <p class="text-gray-200 text-base mb-8 max-w-xl leading-relaxed drop-shadow-sm">{{ scenario.description || '探索这个场景，发现精选好物' }}</p>
        <div class="flex space-x-4">
          <button
            class="bg-taobao hover:bg-taobao-dark text-white px-8 py-3 rounded-full font-bold text-lg flex items-center shadow-lg shadow-taobao/30 transition-all hover:-translate-y-1"
            @click="batchAddToCart"
          >
            <i class="fa-solid fa-magic mr-2"></i> 一键加入清单
          </button>
        </div>
      </div>
    </div>

    <div class="max-w-[1190px] mx-auto mt-6 flex gap-6 items-start pb-16">
      <div class="flex-1 min-w-0">
        <div v-if="products.length" class="mb-10">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-2xl font-bold text-gray-800 flex items-center">
              <span class="w-1.5 h-6 bg-taobao rounded-full mr-3"></span>场景推荐商品
              <span class="text-sm font-normal text-gray-400 ml-4">为你精心挑选</span>
            </h2>
          </div>
          <div class="grid grid-cols-4 gap-4">
            <ProductCard v-for="product in products" :key="product.id" :product="product" />
          </div>
        </div>

        <div v-else class="text-center py-20 text-gray-400">
          <i class="fa-solid fa-box-open text-5xl mb-4"></i>
          <p class="text-lg">该场景暂无推荐商品</p>
        </div>
      </div>

      <div class="w-[286px]">
        <div class="bg-white rounded-xl shadow-lg border border-orange-100 overflow-hidden sticky top-4">
          <div class="bg-gradient-to-r from-orange-400 to-taobao text-white p-4">
            <h3 class="font-bold text-lg flex items-center">
              <i class="fa-solid fa-list-check mr-2"></i> {{ scenario.name }}清单
            </h3>
            <p class="text-xs text-orange-100 mt-1">勾选商品直接加购</p>
            <div class="mt-3">
              <div class="flex justify-between text-xs mb-1">
                <span>准备进度</span>
                <span class="font-bold">{{ checkedCount }}/{{ checklist.size }}</span>
              </div>
              <div class="w-full bg-black/20 rounded-full h-1.5">
                <div class="bg-white h-1.5 rounded-full transition-all duration-500" :style="{ width: progressPercent + '%' }"></div>
              </div>
            </div>
          </div>

          <div class="p-2 max-h-[450px] overflow-y-auto">
            <label
              v-for="product in products"
              :key="product.id"
              class="flex items-center p-2 hover:bg-orange-50 rounded cursor-pointer group transition-colors"
            >
              <input
                type="checkbox"
                :checked="checklist.get(product.id)"
                class="w-4 h-4 text-taobao border-gray-300 rounded focus:ring-taobao mr-3 cursor-pointer"
                @change="toggleCheck(product.id)"
              />
              <span class="flex-1 text-sm text-gray-700 group-hover:text-taobao truncate">{{ product.name }}</span>
            </label>
          </div>

          <div class="p-4 border-t border-gray-100 bg-gray-50 flex flex-col gap-2">
            <button
              class="w-full bg-taobao hover:bg-taobao-dark text-white font-bold py-2.5 rounded-full text-sm transition-colors shadow-md"
              @click="batchAddToCart"
            >
              一键加购选中项 ({{ checkedCount }})
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>

  <div v-else class="max-w-[1190px] mx-auto py-20 text-center text-gray-400">
    <i class="fa-solid fa-circle-exclamation text-5xl mb-4"></i>
    <p class="text-lg">场景不存在</p>
    <router-link to="/" class="text-taobao hover:underline mt-4 inline-block">返回首页</router-link>
  </div>
</template>
