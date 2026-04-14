<script setup lang="ts">
defineProps<{
  product: {
    id: number
    name: string
    price: number
    originalPrice?: number
    images?: string
    stock?: number
  }
}>()
</script>

<template>
  <router-link
    :to="`/products/${product.id}`"
    class="product-card bg-white border border-transparent rounded-lg overflow-hidden cursor-pointer block"
  >
    <div class="h-[230px] bg-gray-100 relative overflow-hidden">
      <img
        v-if="product.images"
        :src="product.images.split(',')[0]"
        :alt="product.name"
        class="w-full h-full object-cover hover:scale-105 transition-transform duration-500"
      />
      <div v-else class="w-full h-full flex items-center justify-center text-gray-300">
        <i class="fa-solid fa-image text-4xl"></i>
      </div>
    </div>
    <div class="p-3">
      <h3 class="text-sm text-gray-800 line-clamp-2 h-10 hover:text-taobao mb-2">{{ product.name }}</h3>
      <div class="flex items-end justify-between">
        <div class="text-taobao font-bold">
          <span class="text-xs">¥</span><span class="text-xl">{{ product.price }}</span>
        </div>
        <div v-if="product.originalPrice && product.originalPrice > product.price" class="text-xs text-gray-400 line-through">
          ¥{{ product.originalPrice }}
        </div>
      </div>
    </div>
  </router-link>
</template>
