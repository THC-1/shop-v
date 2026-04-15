<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDatePicker, ElSelect, ElOption, ElPagination } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'

const loading = ref(false)
const logs = ref<any[]>([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  size: 10,
  module: '',
  startDate: '',
  endDate: ''
})

const moduleOptions = [
  { value: 'PRODUCT', label: '商品' },
  { value: 'CATEGORY', label: '分类' },
  { value: 'BRAND', label: '品牌' },
  { value: 'ORDER', label: '订单' },
  { value: 'USER', label: '用户' },
  { value: 'ROLE', label: '角色' },
  { value: 'ADMIN', label: '管理员' }
]

const loadLogs = async () => {
  loading.value = true
  try {
    logs.value = [
      { id: 1, adminUsername: 'admin', module: 'PRODUCT', action: '更新商品', targetType: 'Product', targetId: 1, detail: '{"name": "iPhone 15"}', ip: '127.0.0.1', createdAt: '2026-04-14 10:00:00' },
      { id: 2, adminUsername: 'admin', module: 'ORDER', action: '订单发货', targetType: 'Order', targetId: 1, detail: '{"expressNo": "SF123"}', ip: '127.0.0.1', createdAt: '2026-04-14 11:00:00' }
    ]
    total.value = logs.value.length
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.value = { page: 1, size: 10, module: '', startDate: '', endDate: '' }
  loadLogs()
}

const handlePageChange = (page: number) => {
  queryParams.value.page = page
  loadLogs()
}

onMounted(() => {
  loadLogs()
})
</script>

<template>
  <div class="log-view">
    <div class="page-header">
      <h2 class="page-title">操作日志</h2>
      <el-button :icon="Refresh" @click="loadLogs">刷新</el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="操作模块">
          <el-select v-model="queryParams.module" placeholder="请选择模块" clearable style="width: 140px">
            <el-option v-for="opt in moduleOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryParams.startDate"
            type="date"
            placeholder="开始日期"
            value-format="YYYY-MM-DD"
            style="width: 130px"
          />
          <span style="margin: 0 8px">至</span>
          <el-date-picker
            v-model="queryParams.endDate"
            type="date"
            placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="logs" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="adminUsername" label="管理员" width="120" />
        <el-table-column prop="module" label="模块" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.module }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="action" label="操作" width="120" />
        <el-table-column prop="targetType" label="操作对象" width="100" />
        <el-table-column prop="targetId" label="对象ID" width="100" />
        <el-table-column prop="detail" label="详情" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="130" />
        <el-table-column prop="createdAt" label="操作时间" width="160" />
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.page"
          :page-size="queryParams.size"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.log-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
