<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { listMyAddresses, getDefaultAddress } from '@/api/address'
import { createOrder } from '@/api/order'

const router = useRouter()
const cartStore = useCartStore()

const addresses = ref<any[]>([])
const selectedAddressId = ref<number | null>(null)
const note = ref('')
const submitting = ref(false)

const selectedAddress = computed(() => addresses.value.find((a: any) => a.id === selectedAddressId.value))

const orderItems = computed(() =>
  cartStore.selectedItems.map((item: any) => ({
    productId: item.productId,
    skuId: item.skuId,
    productName: item.productName,
    quantity: item.quantity,
    price: item.price,
  }))
)

const totalAmount = computed(() => cartStore.totalPrice)

async function handleSubmit() {
  if (!selectedAddressId.value) {
    alert('请选择收货地址')
    return
  }
  if (orderItems.value.length === 0) {
    alert('请返回购物车选择商品')
    return
  }
  submitting.value = true
  try {
    const result: any = await createOrder({
      addressId: selectedAddressId.value,
      items: orderItems.value,
      note: note.value || undefined,
    })
    alert('订单创建成功！')
    await cartStore.removeCheckedItems()
    router.push('/orders')
  } catch {
    // handled by interceptor
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  try {
    const [addressList, defaultAddr] = await Promise.all([
      listMyAddresses(),
      getDefaultAddress().catch(() => null),
    ])
    addresses.value = addressList || []
    if (defaultAddr) {
      selectedAddressId.value = defaultAddr.id
    } else if (addresses.value.length) {
      const defaultOne = addresses.value.find((a: any) => a.isDefault === 1)
      selectedAddressId.value = defaultOne?.id || addresses.value[0].id
    }
  } catch {
    // handled
  }
})
</script>

<template>
  <div class="max-w-[1190px] mx-auto py-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">确认订单</h1>

    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h2 class="text-lg font-bold text-gray-800 mb-4">收货地址</h2>
      <div v-if="addresses.length" class="space-y-3">
        <label
          v-for="addr in addresses"
          :key="addr.id"
          class="flex items-center gap-3 p-3 rounded-lg border-2 cursor-pointer transition-colors"
          :class="selectedAddressId === addr.id ? 'border-taobao bg-taobao-bg' : 'border-gray-200 hover:border-taobao'"
        >
          <input type="radio" :value="addr.id" v-model="selectedAddressId" class="text-taobao focus:ring-taobao" />
          <span class="font-bold text-gray-800">{{ addr.receiverName }}</span>
          <span class="text-gray-600">{{ addr.phone }}</span>
          <span class="text-gray-500">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</span>
          <span v-if="addr.isDefault === 1" class="text-xs text-taobao border border-taobao px-1 rounded">默认</span>
        </label>
      </div>
      <div v-else class="text-gray-400 text-sm">
        暂无收货地址，请先
        <router-link to="/user" class="text-taobao hover:underline">添加地址</router-link>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h2 class="text-lg font-bold text-gray-800 mb-4">商品清单</h2>
      <div
        v-for="item in cartStore.selectedItems"
        :key="item.id"
        class="flex items-center gap-4 py-3 border-b last:border-b-0"
      >
        <div class="w-16 h-16 rounded overflow-hidden bg-gray-100 shrink-0">
          <img v-if="item.productImage" :src="item.productImage" :alt="item.productName" class="w-full h-full object-cover" />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-300">
            <i class="fa-solid fa-image"></i>
          </div>
        </div>
        <div class="flex-1 min-w-0">
          <p class="text-sm text-gray-800 truncate">{{ item.productName }}</p>
          <p v-if="item.skuInfo" class="text-xs text-gray-400">{{ item.skuInfo }}</p>
        </div>
        <div class="text-sm text-gray-500">¥{{ item.price }} × {{ item.quantity }}</div>
        <div class="text-taobao font-bold">¥{{ item.totalPrice }}</div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <h2 class="text-lg font-bold text-gray-800 mb-4">订单备注</h2>
      <textarea
        v-model="note"
        placeholder="选填，对本次交易的说明"
        class="w-full border border-gray-300 rounded-lg p-3 text-sm outline-none focus:border-taobao resize-none h-20"
      ></textarea>
    </div>

    <div class="bg-white rounded-lg shadow-sm p-6">
      <div class="flex items-center justify-between">
        <div class="text-sm text-gray-500">
          共 <span class="text-taobao font-bold">{{ cartStore.selectedItems.length }}</span> 件商品，
          合计：<span class="text-taobao text-3xl font-bold">¥{{ totalAmount.toFixed(2) }}</span>
        </div>
        <button
          class="bg-taobao hover:bg-taobao-dark text-white font-bold px-12 py-3 rounded-full text-lg transition-colors disabled:opacity-50"
          :disabled="submitting || !selectedAddressId || cartStore.selectedItems.length === 0"
          @click="handleSubmit"
        >
          {{ submitting ? '提交中...' : '提交订单' }}
        </button>
      </div>
    </div>
  </div>
</template>
