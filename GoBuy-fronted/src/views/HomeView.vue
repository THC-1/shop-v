<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Carousel from '@/components/Carousel.vue'
import CategorySidebar from '@/components/CategorySidebar.vue'
import ProductCard from '@/components/ProductCard.vue'
import { listScenarios } from '@/api/scenario'
import { listProducts } from '@/api/product'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const scenarios = ref<any[]>([])
const products = ref<any[]>([])

const categories = ref([
  { id: 1, name: '女装 / 男装 / 内衣', children: [{ id: 11, name: '连衣裙' }, { id: 12, name: 'T恤' }, { id: 13, name: '衬衫' }, { id: 14, name: '外套' }] },
  { id: 2, name: '鞋靴 / 箱包 / 配件', children: [{ id: 21, name: '运动鞋' }, { id: 22, name: '手提包' }, { id: 23, name: '双肩包' }] },
  { id: 3, name: '童装玩具 / 孕产 / 用品', children: [{ id: 31, name: '童装' }, { id: 32, name: '玩具' }] },
  { id: 4, name: '家电 / 数码 / 手机', children: [{ id: 41, name: '手机' }, { id: 42, name: '笔记本' }, { id: 43, name: '耳机' }] },
  { id: 5, name: '美妆 / 洗护 / 保健品', children: [{ id: 51, name: '面膜' }, { id: 52, name: '口红' }] },
  { id: 6, name: '珠宝 / 眼镜 / 手表', children: [{ id: 61, name: '项链' }, { id: 62, name: '手表' }] },
  { id: 7, name: '运动 / 户外 / 乐器', children: [{ id: 71, name: '跑步鞋' }, { id: 72, name: '瑜伽垫' }] },
  { id: 8, name: '游戏 / 动漫 / 影视', children: [{ id: 81, name: '游戏机' }, { id: 82, name: '手办' }] },
  { id: 9, name: '美食 / 生鲜 / 零食', children: [{ id: 91, name: '零食' }, { id: 92, name: '水果' }] },
  { id: 10, name: '家具 / 家饰 / 家纺', children: [{ id: 101, name: '沙发' }, { id: 102, name: '床品' }] },
])

const bannerSlides = [
  { image: 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80', title: '秋季新品大赏' },
  { image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80', title: '数码潮品节 满减优惠' },
  { image: 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80', title: '品质生活 甄选好物' },
]

onMounted(async () => {
  try {
    const [scenarioData, productData] = await Promise.all([
      listScenarios(6),
      listProducts(),
    ])
    scenarios.value = scenarioData || []
    products.value = productData || []
  } catch {
    // silently handle - page shows empty sections
  }
})
</script>

<template>
  <div class="max-w-[1190px] mx-auto mt-3">
    <div class="flex text-base font-bold text-gray-800 space-x-8 px-4 pb-2 border-b-2 border-taobao">
      <router-link to="/" class="text-taobao">主题市场</router-link>
      <router-link to="/products" class="hover:text-taobao">全部商品</router-link>
      <router-link to="/scenarios/1" class="hover:text-taobao">场景购</router-link>
    </div>
  </div>

  <div class="max-w-[1190px] mx-auto mt-2 flex h-[500px] gap-3">
    <CategorySidebar :categories="categories" />

    <div class="flex-1 flex flex-col gap-3 min-w-0">
      <div class="h-[280px] w-full bg-gray-200 relative overflow-hidden z-10">
        <Carousel :slides="bannerSlides" />
      </div>
      <div class="flex-1 flex gap-3">
        <div class="flex-1 bg-white cursor-pointer group overflow-hidden relative shadow-sm">
          <img src="https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" alt="运动潮鞋" />
          <div class="absolute top-2 left-2 bg-black/70 text-white text-xs px-2 py-1 rounded">运动潮鞋</div>
        </div>
        <div class="flex-1 bg-white cursor-pointer group overflow-hidden relative shadow-sm">
          <img src="https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" alt="智能手表" />
          <div class="absolute top-2 left-2 bg-black/70 text-white text-xs px-2 py-1 rounded">智能手表</div>
        </div>
      </div>
    </div>

    <div class="w-[290px] flex flex-col gap-3">
      <div class="bg-white p-4 flex flex-col items-center shadow-sm relative h-[160px] overflow-hidden">
        <div class="w-full h-16 bg-orange-100 absolute top-0 left-0"></div>
        <div class="w-14 h-14 rounded-full bg-gray-200 border-2 border-white shadow-md z-10 overflow-hidden mt-2">
          <img v-if="userStore.userInfo?.avatar" :src="userStore.userInfo.avatar" alt="头像" class="w-full h-full object-cover" />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
            <i class="fa-solid fa-user text-xl"></i>
          </div>
        </div>
        <p class="mt-2 text-sm z-10">
          <template v-if="userStore.isLoggedIn()">Hi, {{ userStore.userInfo?.nickname || '用户' }}</template>
          <template v-else>Hi! 你好</template>
        </p>
        <div class="flex space-x-2 mt-3 w-full justify-center z-10">
          <router-link v-if="!userStore.isLoggedIn()" to="/login" class="bg-taobao text-white text-xs px-4 py-1.5 rounded-full font-bold hover:bg-taobao-dark">登录</router-link>
          <router-link v-if="!userStore.isLoggedIn()" to="/login" class="bg-orange-100 text-taobao text-xs px-4 py-1.5 rounded-full font-bold hover:bg-orange-200">注册</router-link>
          <router-link v-if="userStore.isLoggedIn()" to="/user" class="bg-taobao text-white text-xs px-4 py-1.5 rounded-full font-bold hover:bg-taobao-dark">个人中心</router-link>
          <router-link v-if="userStore.isLoggedIn()" to="/orders" class="bg-orange-100 text-taobao text-xs px-4 py-1.5 rounded-full font-bold hover:bg-orange-200">我的订单</router-link>
        </div>
      </div>

      <div class="bg-white p-4 shadow-sm flex-1">
        <div class="flex justify-between items-center mb-3">
          <h3 class="font-bold text-sm text-gray-800">GoBuy 头条</h3>
        </div>
        <ul class="space-y-2 text-xs">
          <li class="flex items-center hover:text-taobao cursor-pointer"><span class="bg-red-100 text-red-500 px-1 rounded mr-2">热门</span> 数码新品首发，限时优惠</li>
          <li class="flex items-center hover:text-taobao cursor-pointer"><span class="bg-blue-100 text-blue-500 px-1 rounded mr-2">穿搭</span> 初秋微凉，风衣搭配指南</li>
          <li class="flex items-center hover:text-taobao cursor-pointer"><span class="bg-orange-100 text-orange-500 px-1 rounded mr-2">场景</span> 场景购：一站式购齐</li>
        </ul>
      </div>

      <div class="bg-white p-2 shadow-sm grid grid-cols-4 gap-y-4 gap-x-2 text-center text-xs pb-4">
        <a href="#" class="flex flex-col items-center hover:text-taobao group">
          <div class="w-8 h-8 rounded-full bg-orange-100 text-orange-500 flex items-center justify-center mb-1 group-hover:bg-taobao group-hover:text-white transition-colors"><i class="fa-solid fa-bolt"></i></div>
          充话费
        </a>
        <a href="#" class="flex flex-col items-center hover:text-taobao group">
          <div class="w-8 h-8 rounded-full bg-blue-100 text-blue-500 flex items-center justify-center mb-1 group-hover:bg-blue-500 group-hover:text-white transition-colors"><i class="fa-solid fa-plane"></i></div>
          旅行
        </a>
        <a href="#" class="flex flex-col items-center hover:text-taobao group">
          <div class="w-8 h-8 rounded-full bg-green-100 text-green-500 flex items-center justify-center mb-1 group-hover:bg-green-500 group-hover:text-white transition-colors"><i class="fa-solid fa-gamepad"></i></div>
          游戏
        </a>
        <a href="#" class="flex flex-col items-center hover:text-taobao group">
          <div class="w-8 h-8 rounded-full bg-purple-100 text-purple-500 flex items-center justify-center mb-1 group-hover:bg-purple-500 group-hover:text-white transition-colors"><i class="fa-solid fa-film"></i></div>
          电影
        </a>
      </div>
    </div>
  </div>

  <div v-if="scenarios.length" class="max-w-[1190px] mx-auto mt-8">
    <h2 class="text-2xl font-bold text-gray-800 mb-4 flex items-center">
      <i class="fa-solid fa-wand-magic-sparkles text-taobao mr-2"></i> 场景推荐
      <span class="text-sm font-normal text-gray-400 ml-4 border-l border-gray-300 pl-4">AI 为你精选场景</span>
    </h2>
    <div class="grid grid-cols-3 gap-4">
      <router-link
        v-for="scenario in scenarios"
        :key="scenario.id"
        :to="`/scenarios/${scenario.id}`"
        class="bg-white rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition-all group cursor-pointer"
      >
        <div class="h-[180px] overflow-hidden relative">
          <img v-if="scenario.coverUrl" :src="scenario.coverUrl" :alt="scenario.name" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
          <div v-else class="w-full h-full bg-gradient-to-br from-orange-400 to-taobao flex items-center justify-center">
            <i class="fa-solid fa-mountain-sun text-white text-4xl"></i>
          </div>
          <div class="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent"></div>
          <div class="absolute bottom-3 left-4 right-4">
            <h3 class="text-white font-bold text-lg">{{ scenario.name }}</h3>
          </div>
        </div>
        <div class="p-3">
          <p class="text-sm text-gray-500 line-clamp-2">{{ scenario.description || '探索这个场景，发现精选好物' }}</p>
        </div>
      </router-link>
    </div>
  </div>

  <div class="max-w-[1190px] mx-auto mt-8 mb-16">
    <h2 class="text-2xl font-bold text-gray-800 mb-4 flex items-center">
      <i class="fa-solid fa-heart text-taobao mr-2"></i> 猜你喜欢
      <span class="text-sm font-normal text-gray-400 ml-4 border-l border-gray-300 pl-4">为你推荐</span>
    </h2>
    <div class="grid grid-cols-5 gap-4">
      <ProductCard v-for="product in products" :key="product.id" :product="product" />
    </div>
  </div>
</template>
