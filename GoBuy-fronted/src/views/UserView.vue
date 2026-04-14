<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { listMyAddresses, createAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'

const userStore = useUserStore()

const editing = ref(false)
const editForm = ref({ nickname: '', phone: '', email: '', avatar: '' })
const saving = ref(false)

const addresses = ref<any[]>([])
const showAddressForm = ref(false)
const addressForm = ref({ receiverName: '', phone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 })
const editingAddressId = ref<number | null>(null)
const addressLoading = ref(false)

function startEdit() {
  if (userStore.userInfo) {
    editForm.value = {
      nickname: userStore.userInfo.nickname || '',
      phone: userStore.userInfo.phone || '',
      email: userStore.userInfo.email || '',
      avatar: userStore.userInfo.avatar || '',
    }
  }
  editing.value = true
}

async function saveProfile() {
  saving.value = true
  try {
    await userStore.updateUserProfile(editForm.value)
    editing.value = false
  } catch { /* handled */ }
  finally {
    saving.value = false
  }
}

async function fetchAddresses() {
  addressLoading.value = true
  try {
    addresses.value = await listMyAddresses() || []
  } catch {
    addresses.value = []
  } finally {
    addressLoading.value = false
  }
}

function openAddressForm(addr?: any) {
  if (addr) {
    editingAddressId.value = addr.id
    addressForm.value = {
      receiverName: addr.receiverName,
      phone: addr.phone,
      province: addr.province,
      city: addr.city,
      district: addr.district,
      detailAddress: addr.detailAddress,
      isDefault: addr.isDefault,
    }
  } else {
    editingAddressId.value = null
    addressForm.value = { receiverName: '', phone: '', province: '', city: '', district: '', detailAddress: '', isDefault: 0 }
  }
  showAddressForm.value = true
}

async function saveAddress() {
  try {
    if (editingAddressId.value) {
      await updateAddress(editingAddressId.value, addressForm.value)
    } else {
      await createAddress(addressForm.value)
    }
    showAddressForm.value = false
    fetchAddresses()
  } catch { /* handled */ }
}

async function removeAddress(id: number) {
  if (!confirm('确认删除该地址？')) return
  try {
    await deleteAddress(id)
    fetchAddresses()
  } catch { /* handled */ }
}

async function setDefault(id: number) {
  try {
    await setDefaultAddress(id)
    fetchAddresses()
  } catch { /* handled */ }
}

onMounted(() => {
  userStore.fetchUserInfo()
  fetchAddresses()
})
</script>

<template>
  <div class="max-w-[1190px] mx-auto py-6">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">个人中心</h1>

    <div class="bg-white rounded-lg shadow-sm p-6 mb-6">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold text-gray-800">基本信息</h2>
        <button v-if="!editing" class="text-taobao hover:underline text-sm" @click="startEdit">编辑</button>
      </div>

      <div v-if="editing" class="space-y-4">
        <div class="flex items-center gap-4">
          <label class="text-sm text-gray-500 w-20">昵称</label>
          <input v-model="editForm.nickname" class="flex-1 border border-gray-300 rounded-lg px-4 py-2 text-sm outline-none focus:border-taobao" />
        </div>
        <div class="flex items-center gap-4">
          <label class="text-sm text-gray-500 w-20">手机号</label>
          <input v-model="editForm.phone" class="flex-1 border border-gray-300 rounded-lg px-4 py-2 text-sm outline-none focus:border-taobao" />
        </div>
        <div class="flex items-center gap-4">
          <label class="text-sm text-gray-500 w-20">邮箱</label>
          <input v-model="editForm.email" class="flex-1 border border-gray-300 rounded-lg px-4 py-2 text-sm outline-none focus:border-taobao" />
        </div>
        <div class="flex items-center gap-4">
          <button class="bg-taobao hover:bg-taobao-dark text-white font-bold px-6 py-2 rounded-full text-sm transition-colors disabled:opacity-50" :disabled="saving" @click="saveProfile">
            {{ saving ? '保存中...' : '保存' }}
          </button>
          <button class="text-gray-500 hover:text-gray-800 text-sm" @click="editing = false">取消</button>
        </div>
      </div>

      <div v-else class="space-y-3">
        <div class="flex items-center gap-4">
          <div class="w-16 h-16 rounded-full bg-gray-200 overflow-hidden shrink-0">
            <img v-if="userStore.userInfo?.avatar" :src="userStore.userInfo.avatar" alt="头像" class="w-full h-full object-cover" />
            <div v-else class="w-full h-full flex items-center justify-center text-gray-400">
              <i class="fa-solid fa-user text-2xl"></i>
            </div>
          </div>
          <div>
            <p class="text-lg font-bold text-gray-800">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '用户' }}</p>
            <p class="text-sm text-gray-500">{{ userStore.userInfo?.email || '未设置邮箱' }}</p>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-4 text-sm">
          <div><span class="text-gray-500">用户名：</span>{{ userStore.userInfo?.username }}</div>
          <div><span class="text-gray-500">手机号：</span>{{ userStore.userInfo?.phone || '未设置' }}</div>
          <div><span class="text-gray-500">状态：</span>{{ userStore.userInfo?.status === 'active' ? '正常' : userStore.userInfo?.status }}</div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-sm p-6">
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold text-gray-800">收货地址</h2>
        <button class="text-taobao hover:underline text-sm" @click="openAddressForm()">新增地址</button>
      </div>

      <div v-if="showAddressForm" class="border border-taobao rounded-lg p-4 mb-4 bg-taobao-bg">
        <h3 class="font-bold text-gray-800 mb-3">{{ editingAddressId ? '编辑地址' : '新增地址' }}</h3>
        <div class="grid grid-cols-2 gap-3">
          <input v-model="addressForm.receiverName" placeholder="收货人姓名" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
          <input v-model="addressForm.phone" placeholder="手机号" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
          <input v-model="addressForm.province" placeholder="省份" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
          <input v-model="addressForm.city" placeholder="城市" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
          <input v-model="addressForm.district" placeholder="区县" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
          <input v-model="addressForm.detailAddress" placeholder="详细地址" class="border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:border-taobao" />
        </div>
        <div class="flex items-center gap-2 mt-3">
          <input type="checkbox" v-model="addressForm.isDefault" :true-value="1" :false-value="0" class="w-4 h-4 text-taobao rounded border-gray-300 focus:ring-taobao" />
          <span class="text-sm text-gray-600">设为默认地址</span>
        </div>
        <div class="flex gap-2 mt-3">
          <button class="bg-taobao hover:bg-taobao-dark text-white font-bold px-6 py-2 rounded-full text-sm transition-colors" @click="saveAddress">保存</button>
          <button class="text-gray-500 hover:text-gray-800 text-sm" @click="showAddressForm = false">取消</button>
        </div>
      </div>

      <div v-if="addressLoading" class="text-center py-8 text-gray-400">
        <i class="fa-solid fa-spinner fa-spin"></i>
      </div>

      <div v-else-if="addresses.length" class="space-y-3">
        <div
          v-for="addr in addresses"
          :key="addr.id"
          class="flex items-center justify-between p-4 rounded-lg border transition-colors"
          :class="addr.isDefault === 1 ? 'border-taobao bg-taobao-bg' : 'border-gray-200 hover:border-taobao'"
        >
          <div>
            <span class="font-bold text-gray-800 mr-3">{{ addr.receiverName }}</span>
            <span class="text-gray-600 mr-3">{{ addr.phone }}</span>
            <span class="text-gray-500">{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detailAddress }}</span>
            <span v-if="addr.isDefault === 1" class="text-xs text-taobao border border-taobao px-1 rounded ml-2">默认</span>
          </div>
          <div class="flex items-center gap-3 text-sm">
            <button v-if="addr.isDefault !== 1" class="text-gray-400 hover:text-taobao" @click="setDefault(addr.id)">设为默认</button>
            <button class="text-gray-400 hover:text-taobao" @click="openAddressForm(addr)">编辑</button>
            <button class="text-gray-400 hover:text-red-500" @click="removeAddress(addr.id)">删除</button>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-8 text-gray-400 text-sm">
        暂无收货地址
      </div>
    </div>
  </div>
</template>
