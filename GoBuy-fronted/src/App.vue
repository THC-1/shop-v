<script setup lang="ts">
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { onMounted } from 'vue'

const userStore = useUserStore()
const cartStore = useCartStore()

onMounted(async () => {
  if (userStore.isLoggedIn()) {
    await Promise.all([
      userStore.fetchUserInfo(),
      cartStore.fetchCartItems(),
    ])
  }
})
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <AppHeader />
    <main class="flex-1">
      <router-view />
    </main>
    <AppFooter />
  </div>
</template>
