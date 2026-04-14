<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  slides: { image: string; title?: string; link?: string }[]
  autoPlay?: boolean
  interval?: number
}>()

const currentIndex = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

function next() {
  currentIndex.value = (currentIndex.value + 1) % props.slides.length
}

function prev() {
  currentIndex.value = (currentIndex.value - 1 + props.slides.length) % props.slides.length
}

function goTo(index: number) {
  currentIndex.value = index
}

function startAutoPlay() {
  if (props.autoPlay !== false && props.slides.length > 1) {
    timer = setInterval(next, props.interval || 3500)
  }
}

function stopAutoPlay() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

onMounted(startAutoPlay)
onUnmounted(stopAutoPlay)
</script>

<template>
  <div
    class="relative overflow-hidden group"
    @mouseenter="stopAutoPlay"
    @mouseleave="startAutoPlay"
  >
    <div
      class="flex transition-transform duration-500 ease-in-out h-full"
      :style="{ transform: `translateX(-${currentIndex * 100}%)` }"
    >
      <div
        v-for="(slide, index) in slides"
        :key="index"
        class="min-w-full h-full relative"
      >
        <img :src="slide.image" :alt="slide.title || ''" class="w-full h-full object-cover" />
        <div v-if="slide.title" class="absolute inset-0 bg-gradient-to-r from-black/50 to-transparent flex items-center p-12">
          <h2 class="text-white text-4xl font-bold">{{ slide.title }}</h2>
        </div>
      </div>
    </div>

    <button
      v-if="slides.length > 1"
      class="absolute left-2 top-1/2 -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white w-8 h-12 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity"
      @click="prev"
    >
      <i class="fa-solid fa-angle-left"></i>
    </button>
    <button
      v-if="slides.length > 1"
      class="absolute right-2 top-1/2 -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white w-8 h-12 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity"
      @click="next"
    >
      <i class="fa-solid fa-angle-right"></i>
    </button>

    <div
      v-if="slides.length > 1"
      class="absolute bottom-3 left-1/2 -translate-x-1/2 flex space-x-2 bg-white/30 px-3 py-1 rounded-full backdrop-blur-sm"
    >
      <div
        v-for="(_, index) in slides"
        :key="index"
        class="w-2.5 h-2.5 rounded-full cursor-pointer transition-colors"
        :class="index === currentIndex ? 'bg-taobao' : 'bg-white'"
        @click="goTo(index)"
      />
    </div>
  </div>
</template>
