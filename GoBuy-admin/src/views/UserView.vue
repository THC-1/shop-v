<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElInput, ElSelect, ElOption, ElPagination, ElMessage, ElDialog, ElTag } from 'element-plus'
import { Search, Refresh, View } from '@element-plus/icons-vue'
import { listUsers, getUser } from '@/api/user'
import type { UserVO } from '@/api/types'

const loading = ref(false)
const users = ref<UserVO[]>([])
const total = ref(0)

const queryParams = ref({ page: 1, size: 10, username: '', status: '' })

const detailVisible = ref(false)
const detailData = ref<UserVO | null>(null)

const getStatusType = (status: string) => status?.toUpperCase() === 'ACTIVE' ? 'success' : 'danger'
const getStatusText = (status: string) => status?.toUpperCase() === 'ACTIVE' ? '正常' : '已封禁'

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await listUsers(queryParams.value)
    users.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { queryParams.value.page = 1; loadUsers() }
const handleReset = () => { queryParams.value = { page: 1, size: 10, username: '', status: '' }; loadUsers() }
const handlePageChange = (page: number) => { queryParams.value.page = page; loadUsers() }

const handleViewDetail = async (row: UserVO) => {
  const res = await getUser(row.id)
  detailData.value = res.data
  detailVisible.value = true
}

onMounted(() => { loadUsers() })
</script>

<template>
  <div class="user-view">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
    </div>
    
    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="已封禁" value="DISABLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card>
      <el-table :data="users" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
        <el-table-column prop="orderCount" label="订单数" width="100" align="center" />
        <el-table-column prop="totalSpent" label="消费总额" width="120" align="right">
          <template #default="{ row }">¥{{ row.totalSpent.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link :icon="View" @click="handleViewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.page" :page-size="queryParams.size" :total="total" layout="total, prev, pager, next" @current-change="handlePageChange" />
      </div>
    </el-card>
    
    <el-dialog v-model="detailVisible" title="用户详情" width="600px">
      <el-descriptions v-if="detailData" :column="2" border>
        <el-descriptions-item label="ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detailData.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailData.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detailData.status)">{{ getStatusText(detailData.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订单数">{{ detailData.orderCount }}</el-descriptions-item>
        <el-descriptions-item label="消费总额">¥{{ detailData.totalSpent.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="注册时间" :span="2">{{ detailData.createdAt }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>