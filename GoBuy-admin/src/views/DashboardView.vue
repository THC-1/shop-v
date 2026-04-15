<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElRow, ElCol, ElCard, ElTable, ElTableColumn, ElDatePicker, ElSelect, ElOption } from 'element-plus'
import * as echarts from 'echarts'
import { getSalesSummary, getSalesTrend, getTopProducts, getUvPv } from '@/api/dashboard'
import type { SalesSummaryVO, SalesTrendVO, TopProductVO, UvPvVO } from '@/api/types'

const loading = ref(false)
const salesSummary = ref<SalesSummaryVO | null>(null)
const salesTrend = ref<SalesTrendVO[]>([])
const topProducts = ref<TopProductVO[]>([])
const uvPv = ref<UvPvVO | null>(null)

let trendChart: echarts.ECharts | null = null
let uvPvChart: echarts.ECharts | null = null

const dateRange = ref<[Date, Date]>([
  new Date(Date.now() - 7 * 24 * 60 * 60 * 1000),
  new Date()
])

const trendType = ref('day')

const initTrendChart = () => {
  const chartDom = document.getElementById('trend-chart')
  if (!chartDom) return
  trendChart = echarts.init(chartDom)

  const option: echarts.EChartsOption = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['订单数', '销售额'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: salesTrend.value.map(item => item.date),
      boundaryGap: false
    },
    yAxis: [
      { type: 'value', name: '订单数', position: 'left' },
      { type: 'value', name: '销售额', position: 'right', formatter: (value: number) => `¥${(value / 10000).toFixed(1)}万` }
    ],
    series: [
      { name: '订单数', type: 'line', smooth: true, data: salesTrend.value.map(item => item.orders), itemStyle: { color: '#ff5000' } },
      { name: '销售额', type: 'line', smooth: true, yAxisIndex: 1, data: salesTrend.value.map(item => item.amount), itemStyle: { color: '#409eff' } }
    ]
  }
  trendChart.setOption(option)
}

const initUvPvChart = () => {
  const chartDom = document.getElementById('uvpv-chart')
  if (!chartDom) return
  uvPvChart = echarts.init(chartDom)

  if (!uvPv.value?.dailyList?.length) return

  const option: echarts.EChartsOption = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['UV', 'PV'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: uvPv.value.dailyList.map(item => item.date), boundaryGap: false },
    yAxis: { type: 'value' },
    series: [
      { name: 'UV', type: 'bar', data: uvPv.value.dailyList.map(item => item.uv), itemStyle: { color: '#67c23a' } },
      { name: 'PV', type: 'bar', data: uvPv.value.dailyList.map(item => item.pv), itemStyle: { color: '#409eff' } }
    ]
  }
  uvPvChart.setOption(option)
}

const loadData = async () => {
  loading.value = true
  try {
    const [startDate, endDate] = dateRange.value
    const start = startDate.toISOString().split('T')[0]
    const end = endDate.toISOString().split('T')[0]

    const [summary, trend, products, uvpvData] = await Promise.all([
      getSalesSummary({ startDate: start, endDate: end }),
      getSalesTrend({ startDate: start, endDate: end, type: trendType.value }),
      getTopProducts({ startDate: start, endDate: end, limit: 10 }),
      getUvPv({ startDate: start, endDate: end })
    ])

    salesSummary.value = summary.data
    salesTrend.value = trend.data
    topProducts.value = products.data
    uvPv.value = uvpvData.data

    setTimeout(() => {
      initTrendChart()
      initUvPvChart()
    }, 100)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h2 class="page-title">数据看板</h2>
      <div class="dashboard-toolbar">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          @change="loadData"
        />
        <el-select v-model="trendType" placeholder="趋势类型" style="width: 120px" @change="loadData">
          <el-option label="按天" value="day" />
          <el-option label="按周" value="week" />
          <el-option label="按月" value="month" />
        </el-select>
      </div>
    </div>

    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orders"><el-icon><ShoppingCart /></el-icon></div>
          <div class="stat-content">
            <p class="stat-label">总订单数</p>
            <p class="stat-value">{{ salesSummary?.totalOrders || 0 }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon amount"><el-icon><Money /></el-icon></div>
          <div class="stat-content">
            <p class="stat-label">总销售额</p>
            <p class="stat-value">¥{{ ((salesSummary?.totalAmount || 0) / 10000).toFixed(2) }}万</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon users"><el-icon><User /></el-icon></div>
          <div class="stat-content">
            <p class="stat-label">总用户数</p>
            <p class="stat-value">{{ salesSummary?.totalUsers || 0 }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon avg"><el-icon><TrendCharts /></el-icon></div>
          <div class="stat-content">
            <p class="stat-label">平均客单价</p>
            <p class="stat-value">¥{{ salesSummary?.avgOrderAmount?.toFixed(2) || 0 }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="chart-row">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>销售趋势</span>
          </template>
          <div id="trend-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>UV/PV 统计</span>
          </template>
          <div class="uvpv-summary">
            <div class="uvpv-item">
              <span class="uvpv-label">总 UV</span>
              <span class="uvpv-value">{{ uvPv?.totalUv || 0 }}</span>
            </div>
            <div class="uvpv-item">
              <span class="uvpv-label">总 PV</span>
              <span class="uvpv-value">{{ uvPv?.totalPv || 0 }}</span>
            </div>
            <div class="uvpv-item">
              <span class="uvpv-label">平均 PV/UV</span>
              <span class="uvpv-value">{{ uvPv?.avgPvPerUv?.toFixed(2) || 0 }}</span>
            </div>
          </div>
          <div id="uvpv-chart" style="height: 200px; margin-top: 16px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="top-products">
      <template #header>
        <span>热销商品排行 TOP 10</span>
      </template>
      <el-table :data="topProducts" stripe style="width: 100%">
        <el-table-column prop="rank" label="排名" width="80" align="center">
          <template #default="{ row }">
            <span :class="['rank-badge', { 'top-3': row.rank <= 3 }]">{{ row.rank }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" min-width="200" />
        <el-table-column prop="brandName" label="品牌" width="120" />
        <el-table-column prop="salesCount" label="销量" width="120" align="right" />
        <el-table-column prop="salesAmount" label="销售额" width="140" align="right">
          <template #default="{ row }">
            ¥{{ row.salesAmount.toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 0;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.dashboard-toolbar {
  display: flex;
  gap: 12px;
}

.stat-cards {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 8px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 28px;
  color: white;
}

.stat-icon.orders { background: linear-gradient(135deg, #ff5000, #ff8a5c); }
.stat-icon.amount { background: linear-gradient(135deg, #409eff, #67c23a); }
.stat-icon.users { background: linear-gradient(135deg, #e6a23c, #f56c6c); }
.stat-icon.avg { background: linear-gradient(135deg, #909399, #c0c4cc); }

.stat-content {
  flex: 1;
}

.stat-label {
  color: #666;
  font-size: 14px;
  margin: 0 0 4px 0;
}

.stat-value {
  color: #333;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.chart-row {
  margin-bottom: 20px;
}

.uvpv-summary {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
}

.uvpv-item {
  text-align: center;
}

.uvpv-label {
  display: block;
  color: #666;
  font-size: 12px;
  margin-bottom: 4px;
}

.uvpv-value {
  display: block;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.top-products {
  margin-bottom: 20px;
}

.rank-badge {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  border-radius: 4px;
  background: #e4e4e4;
  color: #666;
  font-weight: 600;
  font-size: 12px;
}

.rank-badge.top-3 {
  background: linear-gradient(135deg, #ff5000, #ff8a5c);
  color: white;
}
</style>
