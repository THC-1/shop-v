<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElInput, ElSelect, ElOption, ElPagination, ElMessage, ElMessageBox, ElDrawer, ElForm, ElFormItem, ElInput as ElInput2, ElSelect as ElSelect2, ElOption as ElOption2 } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete, Upload, Download } from '@element-plus/icons-vue'
import { listProducts, createProduct, updateProduct, deleteProduct, updateProductStatus, batchUpdateStatus } from '@/api/product'
import { listCategories } from '@/api/category'
import { listBrands } from '@/api/brand'
import type { ProductVO, CategoryTreeVO, BrandVO, ProductCreateDTO } from '@/api/types'

const loading = ref(false)
const products = ref<ProductVO[]>([])
const categories = ref<CategoryTreeVO[]>([])
const brands = ref<BrandVO[]>([])
const total = ref(0)

const queryParams = ref({
  page: 1,
  size: 10,
  name: '',
  categoryId: undefined as number | undefined,
  brandId: undefined as number | undefined,
  status: ''
})

const selectedIds = ref<number[]>([])
const drawerVisible = ref(false)
const drawerTitle = ref('新增商品')
const formRef = ref()
const saving = ref(false)

const formData = ref<ProductCreateDTO>({
  name: '',
  categoryId: 0,
  brandId: undefined,
  price: 0,
  originalPrice: undefined,
  stock: 0,
  description: '',
  images: []
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

const flatCategories = ref<{ id: number; name: string; level: number }[]>([])

const flattenCategories = (cats: CategoryTreeVO[], level = 1) => {
  cats.forEach(cat => {
    flatCategories.value.push({ id: cat.id, name: cat.name, level })
    if (cat.children?.length) {
      flattenCategories(cat.children, level + 1)
    }
  })
}

const loadProducts = async () => {
  loading.value = true
  try {
    const res = await listProducts(queryParams.value)
    products.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  const res = await listCategories()
  categories.value = res.data
  flatCategories.value = []
  flattenCategories(res.data)
}

const loadBrands = async () => {
  const res = await listBrands({ page: 1, size: 100 })
  brands.value = res.data.records
}

const handleSearch = () => {
  queryParams.value.page = 1
  loadProducts()
}

const handleReset = () => {
  queryParams.value = { page: 1, size: 10, name: '', categoryId: undefined, brandId: undefined, status: '' }
  loadProducts()
}

const handlePageChange = (page: number) => {
  queryParams.value.page = page
  loadProducts()
}

const handleSelectionChange = (selection: ProductVO[]) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleStatusChange = async (id: number, status: string) => {
  try {
    await updateProductStatus(id, status)
    ElMessage.success('状态更新成功')
    loadProducts()
  } catch {}
}

const handleBatchStatus = async (status: string) => {
  if (!selectedIds.value.length) {
    ElMessage.warning('请先选择商品')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要将选中商品设为${status === 'ON_SALE' ? '上架' : '下架'}吗？`)
    await batchUpdateStatus(selectedIds.value, status)
    ElMessage.success('批量更新成功')
    loadProducts()
  } catch {}
}

const openAddDrawer = () => {
  drawerTitle.value = '新增商品'
  formData.value = { name: '', categoryId: 0, brandId: undefined, price: 0, originalPrice: undefined, stock: 0, description: '', images: [] }
  drawerVisible.value = true
}

const openEditDrawer = (row: ProductVO) => {
  drawerTitle.value = '编辑商品'
  formData.value = {
    name: row.name,
    categoryId: row.categoryId,
    brandId: row.brandId,
    price: row.price,
    originalPrice: row.originalPrice,
    stock: row.stock,
    description: row.description,
    images: row.images
  }
  drawerVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    if (formData.value.id) {
      await updateProduct(formData.value.id as any, formData.value)
    } else {
      await createProduct(formData.value)
    }
    ElMessage.success('保存成功')
    drawerVisible.value = false
    loadProducts()
  } catch {}
}

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？删除后可在回收站恢复', '提示')
    await deleteProduct(id)
    ElMessage.success('删除成功')
    loadProducts()
  } catch {}
}

onMounted(() => {
  loadProducts()
  loadCategories()
  loadBrands()
})
</script>

<template>
  <div class="product-list">
    <div class="page-header">
      <h2 class="page-title">商品管理</h2>
      <div class="actions">
        <el-button type="primary" :icon="Plus" @click="openAddDrawer">新增商品</el-button>
      </div>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.name" placeholder="请输入商品名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 150px">
            <el-option v-for="cat in flatCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="品牌">
          <el-select v-model="queryParams.brandId" placeholder="请选择品牌" clearable style="width: 150px">
            <el-option v-for="brand in brands" :key="brand.id" :label="brand.name" :value="brand.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="上架" value="ON_SALE" />
            <el-option label="下架" value="OFF_SALE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="batch-actions">
      <el-button size="small" @click="handleBatchStatus('ON_SALE')">批量上架</el-button>
      <el-button size="small" @click="handleBatchStatus('OFF_SALE')">批量下架</el-button>
    </el-card>

    <el-card>
      <el-table :data="products" v-loading="loading" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="商品信息" min-width="200">
          <template #default="{ row }">
            <div class="product-info">
              <img v-if="row.images?.length" :src="row.images[0]" class="product-image" />
              <div v-else class="product-image placeholder">暂无</div>
              <span class="product-name">{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="brandName" label="品牌" width="100" />
        <el-table-column prop="price" label="价格" width="100" align="right">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" align="right" />
        <el-table-column prop="salesCount" label="销量" width="80" align="right" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ON_SALE' ? 'success' : 'info'" size="small">
              {{ row.status === 'ON_SALE' ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="openEditDrawer(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
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

    <el-drawer v-model="drawerVisible" :title="drawerTitle" size="600px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input2 v-model="formData.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select2 v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option2 v-for="cat in flatCategories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select2>
        </el-form-item>
        <el-form-item label="品牌">
          <el-select2 v-model="formData.brandId" placeholder="请选择品牌" clearable style="width: 100%">
            <el-option2 v-for="brand in brands" :key="brand.id" :label="brand.name" :value="brand.id" />
          </el-select2>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input2 v-model.number="formData.price" type="number" placeholder="请输入价格">
            <template #prepend>¥</template>
          </el-input2>
        </el-form-item>
        <el-form-item label="原价">
          <el-input2 v-model.number="formData.originalPrice" type="number" placeholder="请输入原价">
            <template #prepend>¥</template>
          </el-input2>
        </el-form-item>
        <el-form-item label="库存">
          <el-input2 v-model.number="formData.stock" type="number" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input2 v-model="formData.description" type="textarea" :rows="4" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input2 v-model="formData.images" type="textarea" :rows="2" placeholder="请输入图片URL，多个用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="drawerVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<style scoped>
.product-list {
  padding: 0;
}

.page-header {
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

.filter-card {
  margin-bottom: 12px;
}

.batch-actions {
  margin-bottom: 12px;
  padding: 12px;
}

.product-info {
  display: flex;
  align-items: center;
}

.product-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 10px;
}

.product-image.placeholder {
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 12px;
}

.product-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
