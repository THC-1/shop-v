import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as apiLogin, register as apiRegister, getMe, updateMe, logout as apiLogout } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<any>(null)

  const isLoggedIn = () => !!token.value

  async function login(username: string, password: string) {
    const data: any = await apiLogin({ username, password })
    token.value = data
    localStorage.setItem('token', data)
  }

  async function register(username: string, password: string, email?: string, phone?: string) {
    await apiRegister({ username, password, email, phone })
  }

  async function fetchUserInfo() {
    if (!token.value) return
    userInfo.value = await getMe()
  }

  async function updateUserProfile(data: { nickname?: string; phone?: string; email?: string; avatar?: string }) {
    await updateMe(data)
    await fetchUserInfo()
  }

  async function logout() {
    try {
      await apiLogout()
    } finally {
      token.value = null
      userInfo.value = null
      localStorage.removeItem('token')
    }
  }

  return { token, userInfo, isLoggedIn, login, register, fetchUserInfo, updateUserProfile, logout }
})
