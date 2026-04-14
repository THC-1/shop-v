<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const searchKeyword = ref('')

function handleSearch() {
  const keyword = searchKeyword.value.trim()
  if (keyword) {
    router.push({ path: '/products', query: { keyword } })
  }
}

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<template>
  <div class="bg-gray-100 border-b border-gray-200 text-xs">
    <div class="max-w-[1190px] mx-auto flex justify-between items-center h-9">
      <div class="flex items-center space-x-4">
        <span class="text-gray-500">中国大陆</span>
        <template v-if="userStore.isLoggedIn()">
          <span class="text-gray-600">Hi, {{ userStore.userInfo?.nickname || userStore.userInfo?.username || '用户' }}</span>
          <a href="#" class="text-taobao hover:underline" @click.prevent="handleLogout">退出</a>
        </template>
        <template v-else>
          <router-link to="/login" class="text-taobao hover:underline">亲，请登录</router-link>
          <router-link to="/login" class="hover:text-taobao hover:underline">免费注册</router-link>
        </template>
      </div>
      <div class="flex items-center space-x-4">
        <router-link to="/user" class="hover:text-taobao hover:underline">我的淘宝</router-link>
        <router-link to="/cart" class="hover:text-taobao hover:underline flex items-center">
          <i class="fa-solid fa-cart-shopping text-taobao mr-1"></i>购物车
          <span v-if="cartStore.totalCount > 0" class="text-taobao font-bold ml-0.5">{{ cartStore.totalCount }}</span>
        </router-link>
        <router-link to="/orders" class="hover:text-taobao hover:underline">我的订单</router-link>
      </div>
    </div>
  </div>

  <div class="bg-white py-5 shadow-sm">
    <div class="max-w-[1190px] mx-auto flex items-center">
      <router-link to="/" class="text-taobao text-4xl font-bold italic flex items-baseline cursor-pointer select-none">
        GoBuy
        <span class="text-sm font-normal not-italic text-white bg-taobao px-1.5 py-0.5 rounded ml-2 shadow-sm">购</span>
      </router-link>

      <div class="flex-1 max-w-3xl ml-10">
        <div class="flex">
          <div class="flex-1 border-2 border-taobao rounded-l-full overflow-hidden flex items-center pl-4 bg-white h-10">
            <i class="fa-solid fa-magnifying-glass text-gray-400"></i>
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜索商品"
              class="w-full h-full px-3 outline-none text-sm text-gray-700"
              @keyup.enter="handleSearch"
            />
          </div>
          <button
            class="bg-taobao hover:bg-taobao-dark text-white font-bold text-lg px-8 h-10 rounded-r-full transition-colors"
            @click="handleSearch"
          >
            搜索
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
