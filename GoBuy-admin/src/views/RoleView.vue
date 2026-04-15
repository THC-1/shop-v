<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElPagination, ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput as ElInput2, ElTree, ElCheckbox, ElCheckboxGroup } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { listRoles, createRole, updateRole, deleteRole, listPermissions, assignPermissions } from '@/api/role'
import type { RoleVO, PermissionVO } from '@/api/types'

const loading = ref(false)
const roles = ref<RoleVO[]>([])
const total = ref(0)
const queryParams = ref({ page: 1, size: 10, name: '' })

const dialogVisible = ref(false)
const permDialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const formRef = ref()
const saving = ref(false)
const isEdit = ref(false)

const formData = ref({ name: '', code: '', description: '', sort: 0 })
const rules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色代码', trigger: 'blur' }]
}

const allPermissions = ref<PermissionVO[]>([])
const selectedRoleId = ref<number | null>(null)
const selectedPermIds = ref<number[]>([])

const loadRoles = async () => {
  loading.value = true
  try {
    const res = await listRoles(queryParams.value)
    roles.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const loadPermissions = async () => {
  const res = await listPermissions()
  allPermissions.value = res.data
}

const handleSearch = () => { queryParams.value.page = 1; loadRoles() }
const handleReset = () => { queryParams.value = { page: 1, size: 10, name: '' }; loadRoles() }
const handlePageChange = (page: number) => { queryParams.value.page = page; loadRoles() }

const openAddDialog = () => {
  dialogTitle.value = '新增角色'
  isEdit.value = false
  formData.value = { name: '', code: '', description: '', sort: 0 }
  dialogVisible.value = true
}

const openEditDialog = (row: RoleVO) => {
  dialogTitle.value = '编辑角色'
  isEdit.value = true
  formData.value = { name: row.name, code: row.code, description: row.description || '', sort: 0 }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      const role = roles.value.find(r => r.name === formData.value.name || r.code === formData.value.code)
      if (role) await updateRole(role.id, formData.value)
    } else {
      await createRole(formData.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadRoles()
  } catch {}
}

const handleDelete = async (row: RoleVO) => {
  try {
    await ElMessageBox.confirm(row.isSystem ? '系统内置角色无法删除' : '确定删除该角色吗？', '提示')
    await deleteRole(row.id)
    ElMessage.success('删除成功')
    loadRoles()
  } catch {}
}

const openPermDialog = (row: RoleVO) => {
  selectedRoleId.value = row.id
  selectedPermIds.value = row.permissions?.map(p => p.id) || []
  permDialogVisible.value = true
}

const handleAssignPerm = async () => {
  if (!selectedRoleId.value) return
  saving.value = true
  try {
    await assignPermissions(selectedRoleId.value, selectedPermIds.value)
    ElMessage.success('权限分配成功')
    permDialogVisible.value = false
    loadRoles()
  } catch {}
}

onMounted(() => {
  loadRoles()
  loadPermissions()
})
</script>

<template>
  <div class="role-view">
    <div class="page-header">
      <h2 class="page-title">角色管理</h2>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">新增角色</el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="角色名称">
          <el-input v-model="queryParams.name" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="roles" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色代码" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="userCount" label="关联人数" width="100" align="center" />
        <el-table-column prop="isSystem" label="系统角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isSystem ? 'danger' : ''" size="small">{{ row.isSystem ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="openEditDialog(row)">编辑</el-button>
            <el-button type="success" link @click="openPermDialog(row)">分配权限</el-button>
            <el-button type="danger" link :icon="Delete" :disabled="row.isSystem" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.page" :page-size="queryParams.size" :total="total" layout="total, prev, pager, next" @current-change="handlePageChange" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input2 v-model="formData.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色代码" prop="code">
          <el-input2 v-model="formData.code" placeholder="请输入角色代码" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input2 v-model="formData.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="permDialogVisible" title="分配权限" width="500px">
      <div class="perm-tree">
        <el-tree
          :data="allPermissions"
          :props="{ label: 'name', children: 'children' }"
          node-key="id"
          show-checkbox
          :default-checked-keys="selectedPermIds"
          @check-change="(data: any, checked: boolean) => { if(checked) { if(!selectedPermIds.includes(data.id)) selectedPermIds.push(data.id) } else { selectedPermIds = selectedPermIds.filter(id => id !== data.id) } }"
        />
      </div>
      <template #footer>
        <el-button @click="permDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleAssignPerm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.role-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
.perm-tree { max-height: 400px; overflow-y: auto; }
</style>
