<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElInput, ElPagination, ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput as ElInput2, ElAvatar } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { listBrands, createBrand, updateBrand, deleteBrand } from '@/api/brand'
import type { BrandVO } from '@/api/types'

const loading = ref(false)
const brands = ref<BrandVO[]>([])
const total = ref(0)

const queryParams = ref({ page: 1, size: 10, name: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增品牌')
const formRef = ref()
const saving = ref(false)
const isEdit = ref(false)

const formData = ref({ name: '', logo: '', description: '' })

const rules = { name: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }] }

const loadBrands = async () => {
  loading.value = true
  try {
    const res = await listBrands(queryParams.value)
    brands.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { queryParams.value.page = 1; loadBrands() }
const handleReset = () => { queryParams.value = { page: 1, size: 10, name: '' }; loadBrands() }
const handlePageChange = (page: number) => { queryParams.value.page = page; loadBrands() }

const openAddDialog = () => {
  dialogTitle.value = '新增品牌'
  isEdit.value = false
  formData.value = { name: '', logo: '', description: '' }
  dialogVisible.value = true
}

const openEditDialog = (row: BrandVO) => {
  dialogTitle.value = '编辑品牌'
  isEdit.value = true
  formData.value = { name: row.name, logo: row.logo || '', description: row.description || '' }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      const brand = brands.value.find(b => b.name === formData.value.name || b.description === formData.value.description)
      if (brand) await updateBrand(brand.id, formData.value)
    } else {
      await createBrand(formData.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadBrands()
  } catch {}
}

const handleDelete = async (row: BrandVO) => {
  try {
    await ElMessageBox.confirm('确定删除该品牌吗？', '提示')
    await deleteBrand(row.id)
    ElMessage.success('删除成功')
    loadBrands()
  } catch {}
}

onMounted(() => { loadBrands() })
</script>

<template>
  <div class="brand-view">
    <div class="page-header">
      <h2 class="page-title">品牌管理</h2>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">新增品牌</el-button>
    </div>
    
    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="品牌名称">
          <el-input v-model="queryParams.name" placeholder="请输入品牌名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card>
      <el-table :data="brands" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="品牌Logo" width="100">
          <template #default="{ row }">
            <el-avatar v-if="row.logo" :src="row.logo" :size="40" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="品牌名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="productCount" label="商品数" width="100" align="center" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.page" :page-size="queryParams.size" :total="total" layout="total, prev, pager, next" @current-change="handlePageChange" />
      </div>
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="品牌名称" prop="name">
          <el-input2 v-model="formData.name" placeholder="请输入品牌名称" />
        </el-form-item>
        <el-form-item label="品牌Logo">
          <el-input2 v-model="formData.logo" placeholder="Logo URL" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input2 v-model="formData.description" type="textarea" :rows="3" placeholder="请输入品牌描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.brand-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>