<script setup lang="ts">
defineProps<{
  categories: { id: number; name: string; children?: { id: number; name: string }[] }[]
}>()
</script>

<template>
  <div class="w-[240px] bg-white border border-gray-200 py-3 flex flex-col relative z-20 shadow-sm shrink-0">
    <div
      v-for="cat in categories"
      :key="cat.id"
      class="category-item px-5 py-2 hover:bg-red-50 hover:text-taobao cursor-pointer text-sm flex items-center justify-between group"
    >
      <router-link :to="`/products?categoryId=${cat.id}`" class="font-bold">{{ cat.name }}</router-link>
      <i class="fa-solid fa-angle-right text-xs text-gray-400"></i>
      <div
        v-if="cat.children?.length"
        class="hidden group-hover:block absolute left-full top-0 w-[500px] h-full bg-white border border-gray-200 shadow-lg p-6 z-30"
      >
        <h3 class="font-bold text-lg mb-4 text-gray-800">{{ cat.name }}</h3>
        <div class="grid grid-cols-4 gap-4 text-sm">
          <router-link
            v-for="child in cat.children"
            :key="child.id"
            :to="`/products?categoryId=${child.id}`"
            class="text-gray-600 hover:text-taobao"
          >
            {{ child.name }}
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>
