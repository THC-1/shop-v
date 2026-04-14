<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProduct } from '@/api/product'
import { getSkusByProductId, type Sku } from '@/api/sku'
import { addCartItem } from '@/api/cart'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import ProductSpecSelector from '@/components/ProductSpecSelector.vue'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const product = ref<any>(null)
const skus = ref<Sku[]>([])
const quantity = ref(1)
const currentImageIndex = ref(0)
const loading = ref(true)
const addingToCart = ref(false)
const selectedSpecs = ref<Record<string, string>>({})
const selectedSku = ref<Sku | null>(null)

const images = ref<string[]>([])

const parsedAttributes = computed<Record<string, string[]>>(() => {
  if (!product.value?.attributes) return {}
  try {
    return JSON.parse(product.value.attributes)
  } catch {
    return {}
  }
})

const displayPrice = computed(() => {
  if (selectedSku.value) {
    return selectedSku.value.price
  }
  return product.value?.price || 0
})

const displayStock = computed(() => {
  if (selectedSku.value) {
    return selectedSku.value.stock
  }
  return product.value?.stock || 0
})

const currentSkuImage = computed(() => {
  if (selectedSku.value?.image) {
    return selectedSku.value.image
  }
  if (images.value.length > 0) {
    return images.value[currentImageIndex.value]
  }
  return ''
})

function selectImage(index: number) {
  currentImageIndex.value = index
}

function increaseQuantity() {
  quantity.value++
}

function decreaseQuantity() {
  if (quantity.value > 1) quantity.value--
}

function handleSpecsUpdate(specs: Record<string, string>) {
  selectedSpecs.value = specs
}

function handleSkuSelected(sku: Sku | null) {
  selectedSku.value = sku
}

async function handleAddToCart() {
  if (!userStore.isLoggedIn()) {
    router.push('/login')
    return
  }
  if (!selectedSku.value) {
    alert('请选择完整的商品规格')
    return
  }
  addingToCart.value = true
  try {
    await addCartItem({ productId: product.value.id, skuId: selectedSku.value.id, quantity: quantity.value })
    await cartStore.fetchCartItems()
    alert('已加入购物车！')
  } catch {
    // error handled by interceptor
  } finally {
    addingToCart.value = false
  }
}

async function buyNow() {
  if (!userStore.isLoggedIn()) {
    router.push('/login')
    return
  }
  if (!selectedSku.value) {
    alert('请选择完整的商品规格')
    return
  }
  await handleAddToCart()
  router.push('/cart')
}

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const [productData, skuData] = await Promise.all([
      getProduct(id),
      getSkusByProductId(id)
    ])
    product.value = productData
    skus.value = skuData || []
    if (product.value?.images) {
      images.value = product.value.images.split(',').map((s: string) => s.trim()).filter(Boolean)
    }
    if (parsedAttributes.value) {
      const initialSpecs: Record<string, string> = {}
      for (const key of Object.keys(parsedAttributes.value)) {
        const options = parsedAttributes.value[key]
        if (options && options.length > 0) {
          initialSpecs[key] = ''
        }
      }
      selectedSpecs.value = initialSpecs
    }
  } catch {
    // handled
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

  <template v-else-if="product">
    <div class="max-w-[1190px] mx-auto py-6">
      <div class="text-sm text-gray-500 mb-4">
        <router-link to="/" class="hover:text-taobao">首页</router-link>
        <span class="mx-2">></span>
        <router-link to="/products" class="hover:text-taobao">全部商品</router-link>
        <span class="mx-2">></span>
        <span class="text-gray-800">{{ product.name }}</span>
      </div>

      <div class="flex gap-8">
        <div class="w-[480px] shrink-0">
          <div class="w-full h-[480px] bg-gray-100 rounded-lg overflow-hidden mb-3">
            <img
              v-if="currentSkuImage"
              :src="currentSkuImage"
              :alt="product.name"
              class="w-full h-full object-cover"
            />
            <div v-else class="w-full h-full flex items-center justify-center text-gray-300">
              <i class="fa-solid fa-image text-6xl"></i>
            </div>
          </div>
          <div v-if="images.length > 1" class="flex gap-2">
            <div
              v-for="(img, index) in images"
              :key="index"
              class="w-16 h-16 rounded border-2 cursor-pointer overflow-hidden transition-colors"
              :class="currentImageIndex === index ? 'border-taobao' : 'border-gray-200 hover:border-taobao'"
              @click="selectImage(index)"
            >
              <img :src="img" :alt="`${product.name}-${index}`" class="w-full h-full object-cover" />
            </div>
          </div>
        </div>

        <div class="flex-1">
          <h1 class="text-2xl font-bold text-gray-800 mb-4">{{ product.name }}</h1>

          <div class="bg-taobao-bg rounded-lg p-4 mb-6">
            <div class="flex items-baseline gap-2">
              <span class="text-taobao text-sm">¥</span>
              <span class="text-taobao text-4xl font-bold">{{ displayPrice }}</span>
              <span v-if="product.originalPrice && product.originalPrice > product.price" class="text-gray-400 line-through text-sm ml-4">
                ¥{{ product.originalPrice }}
              </span>
            </div>
          </div>

          <div class="space-y-4 mb-6">
            <div class="flex items-center text-sm">
              <span class="text-gray-500 w-20">库存</span>
              <span class="text-gray-800">{{ displayStock > 0 ? `${displayStock} 件` : '已售罄' }}</span>
            </div>
            <div v-if="parsedAttributes && Object.keys(parsedAttributes).length > 0" class="text-sm">
              <span class="text-gray-500 w-20 inline-block mb-2">选择规格</span>
              <ProductSpecSelector
                :attributes="parsedAttributes"
                :skus="skus"
                v-model="selectedSpecs"
                @sku-selected="handleSkuSelected"
              />
            </div>
          </div>

          <div class="flex items-center gap-4 mb-8">
            <span class="text-gray-500 text-sm w-20">数量</span>
            <div class="flex items-center border border-gray-300 rounded">
              <button class="w-10 h-10 flex items-center justify-center hover:bg-gray-100" @click="decreaseQuantity">-</button>
              <span class="w-14 h-10 flex items-center justify-center border-x border-gray-300">{{ quantity }}</span>
              <button class="w-10 h-10 flex items-center justify-center hover:bg-gray-100" @click="increaseQuantity">+</button>
            </div>
          </div>

          <div class="flex gap-4">
            <button
              class="flex-1 bg-taobao hover:bg-taobao-dark text-white font-bold py-3 rounded-full text-lg transition-colors disabled:opacity-50"
              :disabled="displayStock <= 0 || addingToCart"
              @click="handleAddToCart"
            >
              {{ addingToCart ? '加入中...' : '加入购物车' }}
            </button>
            <button
              class="flex-1 bg-taobao-dark hover:bg-red-700 text-white font-bold py-3 rounded-full text-lg transition-colors disabled:opacity-50"
              :disabled="displayStock <= 0"
              @click="buyNow"
            >
              立即购买
            </button>
          </div>
        </div>
      </div>

      <div v-if="product.description" class="mt-10">
        <h2 class="text-xl font-bold text-gray-800 mb-4 pb-2 border-b-2 border-taobao">商品详情</h2>
        <div class="bg-white rounded-lg p-6 text-gray-700 leading-relaxed whitespace-pre-wrap">{{ product.description }}</div>
      </div>
    </div>
  </template>

  <div v-else class="max-w-[1190px] mx-auto py-20 text-center text-gray-400">
    <i class="fa-solid fa-circle-exclamation text-5xl mb-4"></i>
    <p class="text-lg">商品不存在</p>
    <router-link to="/" class="text-taobao hover:underline mt-4 inline-block">返回首页</router-link>
  </div>
</template>