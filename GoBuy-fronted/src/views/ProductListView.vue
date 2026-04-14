<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import Pagination from '@/components/Pagination.vue'
import { searchProducts, getCategoryProducts } from '@/api/product'

const route = useRoute()
const router = useRouter()

const products = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)
const loading = ref(false)
const keyword = ref('')
const sortField = ref('')
const sortOrder = ref('')

async function fetchProducts() {
  loading.value = true
  try {
    const categoryId = route.query.categoryId ? Number(route.query.categoryId) : undefined
    if (categoryId) {
      const data: any = await getCategoryProducts(categoryId, pageNum.value, pageSize.value)
      products.value = data?.records || []
      total.value = data?.total || 0
    } else {
      const data: any = await searchProducts({
        keyword: keyword.value || undefined,
        categoryId,
        sortField: sortField.value || undefined,
        sortOrder: sortOrder.value || undefined,
        pageNum: pageNum.value,
        pageSize: pageSize.value,
      })
      products.value = data?.records || []
      total.value = data?.total || 0
    }
  } catch {
    products.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pageNum.value = 1
  router.replace({ query: { ...route.query, keyword: keyword.value || undefined } })
}

function setSort(field: string, order: string) {
  sortField.value = field
  sortOrder.value = order
  pageNum.value = 1
  fetchProducts()
}

watch(() => route.query, () => {
  keyword.value = (route.query.keyword as string) || ''
  pageNum.value = 1
  fetchProducts()
}, { immediate: false })

onMounted(() => {
  keyword.value = (route.query.keyword as string) || ''
  fetchProducts()
})
</script>

<template>
  <div class="max-w-[1190px] mx-auto py-6">
    <div class="bg-white rounded-lg shadow-sm p-4 mb-6">
      <div class="flex items-center gap-4">
        <div class="flex-1 border-2 border-taobao rounded-full overflow-hidden flex items-center pl-4 h-10">
          <i class="fa-solid fa-magnifying-glass text-gray-400"></i>
          <input
            v-model="keyword"
            type="text"
            placeholder="搜索商品"
            class="w-full h-full px-3 outline-none text-sm text-gray-700"
            @keyup.enter="handleSearch"
          />
        </div>
        <button class="bg-taobao hover:bg-taobao-dark text-white font-bold px-8 h-10 rounded-full transition-colors" @click="handleSearch">
          搜索
        </button>
      </div>
      <div class="flex items-center gap-3 mt-3 text-sm">
        <span class="text-gray-500">排序：</span>
        <button
          class="px-3 py-1 rounded border transition-colors"
          :class="sortField === 'price' && sortOrder === 'asc' ? 'bg-taobao text-white border-taobao' : 'border-gray-300 hover:border-taobao hover:text-taobao'"
          @click="setSort('price', 'asc')"
        >
          价格升序
        </button>
        <button
          class="px-3 py-1 rounded border transition-colors"
          :class="sortField === 'price' && sortOrder === 'desc' ? 'bg-taobao text-white border-taobao' : 'border-gray-300 hover:border-taobao hover:text-taobao'"
          @click="setSort('price', 'desc')"
        >
          价格降序
        </button>
        <button
          class="px-3 py-1 rounded border transition-colors"
          :class="sortField === 'createdAt' && sortOrder === 'desc' ? 'bg-taobao text-white border-taobao' : 'border-gray-300 hover:border-taobao hover:text-taobao'"
          @click="setSort('createdAt', 'desc')"
        >
          最新上架
        </button>
      </div>
    </div>

    <div v-if="loading" class="text-center py-20 text-gray-400">
      <i class="fa-solid fa-spinner fa-spin text-3xl"></i>
      <p class="mt-4">搜索中...</p>
    </div>

    <template v-else>
      <div class="text-sm text-gray-500 mb-4">
        共找到 <span class="text-taobao font-bold">{{ total }}</span> 件商品
      </div>

      <div v-if="products.length" class="grid grid-cols-5 gap-4">
        <ProductCard v-for="product in products" :key="product.id" :product="product" />
      </div>

      <div v-else class="text-center py-20 text-gray-400">
        <i class="fa-solid fa-search text-5xl mb-4"></i>
        <p class="text-lg">未找到相关商品</p>
        <p class="text-sm mt-2">换个关键词试试？</p>
      </div>

      <Pagination
        v-if="total > 0"
        :total="total"
        v-model:pageNum="pageNum"
        :pageSize="pageSize"
        @update:pageNum="fetchProducts"
      />
    </template>
  </div>
</template>
