<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isLogin = ref(true)
const username = ref('')
const password = ref('')
const email = ref('')
const phone = ref('')
const submitting = ref(false)
const errorMsg = ref('')

function switchMode() {
  isLogin.value = !isLogin.value
  errorMsg.value = ''
}

async function handleSubmit() {
  errorMsg.value = ''
  if (!username.value.trim() || !password.value.trim()) {
    errorMsg.value = '请输入用户名和密码'
    return
  }

  submitting.value = true
  try {
    if (isLogin.value) {
      await userStore.login(username.value, password.value)
      const redirect = (route.query.redirect as string) || '/'
      router.push(redirect)
    } else {
      await userStore.register(username.value, password.value, email.value || undefined, phone.value || undefined)
      isLogin.value = true
      errorMsg.value = ''
      alert('注册成功，请登录')
    }
  } catch {
    errorMsg.value = isLogin.value ? '登录失败，请检查用户名和密码' : '注册失败，请稍后重试'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="min-h-[600px] flex items-center justify-center py-12">
    <div class="w-[420px] bg-white rounded-2xl shadow-lg overflow-hidden">
      <div class="bg-gradient-to-r from-orange-400 to-taobao p-8 text-center">
        <h1 class="text-white text-3xl font-bold italic">GoBuy</h1>
        <p class="text-orange-100 text-sm mt-2">{{ isLogin ? '欢迎回来' : '加入我们' }}</p>
      </div>

      <div class="p-8">
        <div class="flex mb-6 border-b border-gray-200">
          <button
            class="flex-1 pb-3 text-center font-bold transition-colors border-b-2 -mb-px"
            :class="isLogin ? 'text-taobao border-taobao' : 'text-gray-400 border-transparent hover:text-taobao'"
            @click="isLogin = true"
          >
            登录
          </button>
          <button
            class="flex-1 pb-3 text-center font-bold transition-colors border-b-2 -mb-px"
            :class="!isLogin ? 'text-taobao border-taobao' : 'text-gray-400 border-transparent hover:text-taobao'"
            @click="isLogin = false"
          >
            注册
          </button>
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <input
              v-model="username"
              type="text"
              placeholder="用户名"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-sm outline-none focus:border-taobao transition-colors"
            />
          </div>
          <div>
            <input
              v-model="password"
              type="password"
              placeholder="密码"
              class="w-full border border-gray-300 rounded-lg px-4 py-3 text-sm outline-none focus:border-taobao transition-colors"
            />
          </div>
          <template v-if="!isLogin">
            <div>
              <input
                v-model="email"
                type="email"
                placeholder="邮箱（选填）"
                class="w-full border border-gray-300 rounded-lg px-4 py-3 text-sm outline-none focus:border-taobao transition-colors"
              />
            </div>
            <div>
              <input
                v-model="phone"
                type="tel"
                placeholder="手机号（选填）"
                class="w-full border border-gray-300 rounded-lg px-4 py-3 text-sm outline-none focus:border-taobao transition-colors"
              />
            </div>
          </template>

          <p v-if="errorMsg" class="text-red-500 text-sm">{{ errorMsg }}</p>

          <button
            type="submit"
            class="w-full bg-taobao hover:bg-taobao-dark text-white font-bold py-3 rounded-full text-lg transition-colors disabled:opacity-50"
            :disabled="submitting"
          >
            {{ submitting ? '处理中...' : (isLogin ? '登录' : '注册') }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>
