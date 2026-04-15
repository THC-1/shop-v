<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTree, ElButton, ElInput, ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput as ElInput2 } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { listCategories, createCategory, updateCategory, deleteCategory } from '@/api/category'
import type { CategoryTreeVO } from '@/api/types'

const loading = ref(false)
const categories = ref<CategoryTreeVO[]>([])
const expandedKeys = ref<number[]>([])
const selectedNode = ref<CategoryTreeVO | null>(null)

const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref()
const saving = ref(false)
const isEdit = ref(false)

const formData = ref({
  name: '',
  parentId: undefined as number | undefined,
  sort: 0,
  icon: ''
})

const rules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const loadCategories = async () => {
  loading.value = true
  try {
    const res = await listCategories()
    categories.value = res.data
    expandedKeys.value = res.data.map(c => c.id)
  } finally {
    loading.value = false
  }
}

const handleNodeClick = (data: CategoryTreeVO) => {
  selectedNode.value = data
}

const openAddDialog = (parent?: CategoryTreeVO) => {
  dialogTitle.value = parent ? `新增子分类（${parent.name}）` : '新增顶级分类'
  isEdit.value = false
  formData.value = { name: '', parentId: parent?.id, sort: 0, icon: '' }
  dialogVisible.value = true
}

const openEditDialog = (node: CategoryTreeVO) => {
  dialogTitle.value = '编辑分类'
  isEdit.value = true
  formData.value = { name: node.name, parentId: node.parentId || undefined, sort: node.sort, icon: node.icon || '' }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  saving.value = true
  try {
    if (isEdit.value && selectedNode.value) {
      await updateCategory(selectedNode.value.id, formData.value)
    } else {
      await createCategory(formData.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadCategories()
  } catch {}
}

const handleDelete = async (node: CategoryTreeVO) => {
  try {
    await ElMessageBox.confirm(
      node.children?.length ? '该分类下存在子分类，无法删除' : '确定删除该分类吗？',
      '提示'
    )
    await deleteCategory(node.id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch {}
}

onMounted(() => {
  loadCategories()
})
</script>

<template>
  <div class="category-view">
    <div class="page-header">
      <h2 class="page-title">分类管理</h2>
      <div class="actions">
        <el-button type="primary" :icon="Plus" @click="openAddDialog()">新增顶级分类</el-button>
        <el-button :icon="Refresh" @click="loadCategories">刷新</el-button>
      </div>
    </div>
    
    <el-card>
      <el-tree
        :data="categories"
        :props="{ label: 'name', children: 'children' }"
        node-key="id"
        :default-expanded-keys="expandedKeys"
        v-loading="loading"
        @node-click="handleNodeClick"
        class="category-tree"
      >
        <template #default="{ node, data }">
          <span class="tree-node">
            <span class="node-name">{{ node.label }}</span>
            <span class="node-actions">
              <el-button type="primary" link size="small" @click.stop="openAddDialog(data)">添加子分类</el-button>
              <el-button type="primary" link size="small" @click.stop="openEditDialog(data)">编辑</el-button>
              <el-button type="danger" link size="small" @click.stop="handleDelete(data)">删除</el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input2 v-model="formData.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input2 v-model.number="formData.sort" type="number" placeholder="数值越小越靠前" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input2 v-model="formData.icon" placeholder="图标URL（可选）" />
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
.category-view {
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

.category-tree {
  background: transparent;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding-right: 20px;
}

.node-name {
  font-size: 14px;
}

.node-actions {
  display: none;
}

.tree-node:hover .node-actions {
  display: flex;
  gap: 8px;
}
</style>