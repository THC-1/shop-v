<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElSelect, ElOption, ElPagination, ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput as ElInput2, ElTag } from 'element-plus'
import { Search, Refresh, View, Position } from '@element-plus/icons-vue'
import { listOrders, getOrder, shipOrder, batchShip } from '@/api/order'
import type { OrderVO, OrderItemVO } from '@/api/types'

const loading = ref(false)
const orders = ref<OrderVO[]>([])
const total = ref(0)

const queryParams = ref({ page: 1, size: 10, orderNo: '', status: '' })

const detailVisible = ref(false)
const detailData = ref<OrderVO | null>(null)
const shipVisible = ref(false)
const selectedOrders = ref<OrderVO[]>([])
const shipFormRef = ref()

const shipForm = ref({ expressCompany: '', expressNo: '' })
const shipSaving = ref(false)

const statusMap: Record<string, { label: string; type: string }> = {
  PENDING_PAYMENT: { label: '待付款', type: 'warning' },
  PAID: { label: '已付款', type: 'success' },
  SHIPPED: { label: '已发货', type: 'primary' },
  DELIVERED: { label: '已送达', type: 'info' },
  COMPLETED: { label: '已完成', type: 'success' },
  CANCELLED: { label: '已取消', type: 'danger' }
}

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await listOrders(queryParams.value)
    orders.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { queryParams.value.page = 1; loadOrders() }
const handleReset = () => { queryParams.value = { page: 1, size: 10, orderNo: '', status: '' }; loadOrders() }
const handlePageChange = (page: number) => { queryParams.value.page = page; loadOrders() }

const handleViewDetail = async (row: OrderVO) => {
  const res = await getOrder(row.id)
  detailData.value = res.data
  detailVisible.value = true
}

const handleSelectionChange = (selection: OrderVO[]) => {
  selectedOrders.value = selection
}

const openShipDialog = () => {
  const paidOrders = selectedOrders.value.filter(o => o.status === 'PAID')
  if (!paidOrders.length) {
    ElMessage.warning('请选择已付款的订单')
    return
  }
  shipForm.value = { expressCompany: '', expressNo: '' }
  shipVisible.value = true
}

const handleShip = async () => {
  if (!shipForm.value.expressCompany || !shipForm.value.expressNo) {
    ElMessage.warning('请填写物流信息')
    return
  }
  shipSaving.value = true
  try {
    const orderIds = selectedOrders.value.filter(o => o.status === 'PAID').map(o => o.id)
    if (orderIds.length === 1) {
      await shipOrder(orderIds[0], shipForm.value)
    } else {
      await batchShip({ orderIds, ...shipForm.value })
    }
    ElMessage.success('发货成功')
    shipVisible.value = false
    loadOrders()
  } catch {}
}

onMounted(() => { loadOrders() })
</script>

<template>
  <div class="order-view">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
      <el-button type="primary" :icon="Position" :disabled="!selectedOrders.length" @click="openShipDialog">批量发货</el-button>
    </div>
    
    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="订单号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option v-for="(val, key) in statusMap" :key="key" :label="val.label" :value="key" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card>
      <el-table :data="orders" v-loading="loading" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="username" label="用户" width="100" />
        <el-table-column prop="receiverName" label="收货人" width="100" />
        <el-table-column prop="totalAmount" label="金额" width="100" align="right">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type" size="small">{{ statusMap[row.status]?.label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="View" @click="handleViewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.page" :page-size="queryParams.size" :total="total" layout="total, prev, pager, next" @current-change="handlePageChange" />
      </div>
    </el-card>
    
    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <div v-if="detailData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ detailData.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ detailData.username }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ detailData.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ detailData.province }} {{ detailData.city }} {{ detailData.district }} {{ detailData.detailAddress }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ detailData.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="订单状态"><el-tag :type="statusMap[detailData.status]?.type">{{ statusMap[detailData.status]?.label }}</el-tag></el-descriptions-item>
          <el-descriptions-item v-if="detailData.expressCompany" label="物流公司">{{ detailData.expressCompany }}</el-descriptions-item>
          <el-descriptions-item v-if="detailData.expressNo" label="运单号">{{ detailData.expressNo }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>商品列表</el-divider>
        <el-table :data="detailData.items" size="small">
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="skuName" label="规格" />
          <el-table-column prop="price" label="单价" width="100" align="right" />
          <el-table-column prop="quantity" label="数量" width="80" align="center" />
          <el-table-column prop="subtotal" label="小计" width="100" align="right" />
        </el-table>
      </div>
    </el-dialog>
    
    <el-dialog v-model="shipVisible" title="订单发货" width="500px">
      <el-form ref="shipFormRef" :model="shipForm" label-width="100px">
        <el-form-item label="已选订单">{{ selectedOrders.filter(o => o.status === 'PAID').length }} 个</el-form-item>
        <el-form-item label="物流公司" required>
          <el-input2 v-model="shipForm.expressCompany" placeholder="请输入物流公司名称" />
        </el-form-item>
        <el-form-item label="运单号" required>
          <el-input2 v-model="shipForm.expressNo" placeholder="请输入运单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipVisible = false">取消</el-button>
        <el-button type="primary" :loading="shipSaving" @click="handleShip">确认发货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.order-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>