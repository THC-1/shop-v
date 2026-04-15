<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMenu, ElMenuItem, ElIcon, ElSubMenu, ElAvatar, ElDropdown, ElDropdownMenu, ElDropdownItem, ElBreadcrumb, ElBreadcrumbItem } from 'element-plus'
import { 
  HomeFilled, Goods, Grid, Shop, List, User, Key, Document, Operation 
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapse = ref(false)

const adminInfo = ref({
  username: localStorage.getItem('admin-username') || 'Admin',
  nickname: localStorage.getItem('admin-nickname') || '管理员'
})

const activeMenu = computed(() => route.path)

const menuItems = [
  { path: '/', title: '首页看板', icon: HomeFilled },
  { path: '/products', title: '商品管理', icon: Goods },
  { path: '/categories', title: '分类管理', icon: Grid },
  { path: '/brands', title: '品牌管理', icon: Shop },
  { path: '/orders', title: '订单管理', icon: List },
  { path: '/users', title: '用户管理', icon: User },
  { path: '/roles', title: '角色管理', icon: Key },
  { path: '/admins', title: '管理员', icon: Operation },
  { path: '/logs', title: '操作日志', icon: Document },
]

const handleMenuSelect = (path: string) => {
  router.push(path)
}

const handleLogout = () => {
  localStorage.removeItem('admin-token')
  localStorage.removeItem('admin-username')
  localStorage.removeItem('admin-nickname')
  router.push('/login')
}

const getBreadcrumb = () => {
  const matched = route.matched.filter(item => item.meta?.title)
  return matched.map(item => ({ path: item.path, title: item.meta?.title }))
}
</script>

<template>
  <div class="main-layout">
    <aside class="sidebar" :class="{ 'collapsed': isCollapse }">
      <div class="logo-area">
        <h1 class="logo-text">GoBuy</h1>
        <span class="logo-sub">运营后台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
        background-color="#1a1a2e"
        text-color="#b8b8b8"
        active-text-color="#ff5000"
        @select="handleMenuSelect"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </aside>
    
    <div class="main-content">
      <header class="topbar">
        <div class="topbar-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-for="crumb in getBreadcrumb()" :key="crumb.path" :to="crumb.path">
              {{ crumb.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="topbar-right">
          <el-dropdown @command="handleLogout">
            <span class="user-info">
              <el-avatar :size="32" style="background-color: #ff5000;">
                {{ adminInfo.nickname?.charAt(0) }}
              </el-avatar>
              <span class="username">{{ adminInfo.nickname }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <main class="content-area">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 220px;
  background-color: var(--color-sidebar-bg);
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
}

.sidebar.collapsed {
  width: 64px;
}

.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.logo-text {
  color: #ff5000;
  font-size: 24px;
  font-weight: bold;
  margin-right: 8px;
}

.logo-sub {
  color: #b8b8b8;
  font-size: 14px;
}

.sidebar-menu {
  border-right: none;
  flex: 1;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 220px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  height: 60px;
  background: white;
  border-bottom: 1px solid #e4e4e4;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.topbar-left {
  display: flex;
  align-items: center;
}

.topbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.username {
  margin: 0 8px;
  color: #333;
}

.content-area {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background-color: var(--color-content-bg);
}
</style>