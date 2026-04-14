<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  total: number
  pageNum: number
  pageSize: number
}>()

const emit = defineEmits<{
  'update:pageNum': [page: number]
}>()

const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

const pages = computed(() => {
  const result: number[] = []
  const start = Math.max(1, props.pageNum - 2)
  const end = Math.min(totalPages.value, props.pageNum + 2)
  for (let i = start; i <= end; i++) {
    result.push(i)
  }
  return result
})

function goTo(page: number) {
  if (page >= 1 && page <= totalPages.value && page !== props.pageNum) {
    emit('update:pageNum', page)
  }
}
</script>

<template>
  <div v-if="totalPages > 1" class="flex items-center justify-center space-x-1 mt-6">
    <button
      class="px-3 py-1.5 rounded text-sm border border-gray-300 hover:border-taobao hover:text-taobao disabled:opacity-50 disabled:cursor-not-allowed"
      :disabled="pageNum <= 1"
      @click="goTo(pageNum - 1)"
    >
      上一页
    </button>
    <button
      v-for="page in pages"
      :key="page"
      class="px-3 py-1.5 rounded text-sm border"
      :class="page === pageNum ? 'bg-taobao text-white border-taobao' : 'border-gray-300 hover:border-taobao hover:text-taobao'"
      @click="goTo(page)"
    >
      {{ page }}
    </button>
    <button
      class="px-3 py-1.5 rounded text-sm border border-gray-300 hover:border-taobao hover:text-taobao disabled:opacity-50 disabled:cursor-not-allowed"
      :disabled="pageNum >= totalPages"
      @click="goTo(pageNum + 1)"
    >
      下一页
    </button>
  </div>
</template>
