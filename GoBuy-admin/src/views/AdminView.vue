<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElInput, ElPagination, ElMessage, ElMessageBox, ElDialog, ElForm, ElFormItem, ElInput as ElInput2, ElSelect, ElOption as ElOption2, ElTag } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { listAdmins, createAdmin, updateAdmin, deleteAdmin } from '@/api/admin'
import { listRoles } from '@/api/role'
import type { AdminVO, RoleVO } from '@/api/types'

const loading = ref(false)
const admins = ref<AdminVO[]>([])
const roles = ref<RoleVO[]>([])
const total = ref(0)
const queryParams = ref({ page: 1, size: 10, username: '', status: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增管理员')
const formRef = ref()
const saving = ref(false)
const isEdit = ref(false)

const formData = ref({ username: '', password: '', nickname: '', email: '', phone: '', roleIds: [] as number[] })

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const getStatusType = (status: string) => status?.toUpperCase() === 'ACTIVE' ? 'success' : 'danger'
const getStatusText = (status: string) => status?.toUpperCase() === 'ACTIVE' ? '正常' : '禁用'

const loadAdmins = async () => {
  loading.value = true
  try {
    const res = await listAdmins(queryParams.value)
    admins.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const loadRoles = async () => {
  const res = await listRoles({ page: 1, size: 100 })
  roles.value = res.data.records
}

const handleSearch = () => { queryParams.value.page = 1; loadAdmins() }
const handleReset = () => { queryParams.value = { page: 1, size: 10, username: '', status: '' }; loadAdmins() }
const handlePageChange = (page: number) => { queryParams.value.page = page; loadAdmins() }

const openAddDialog = () => {
  dialogTitle.value = '新增管理员'
  isEdit.value = false
  formData.value = { username: '', password: '', nickname: '', email: '', phone: '', roleIds: [] }
  dialogVisible.value = true
}

const openEditDialog = (row: AdminVO) => {
  dialogTitle.value = '编辑管理员'
  isEdit.value = true
  formData.value = { username: row.username, password: '', nickname: row.nickname || '', email: row.email || '', phone: row.phone || '', roleIds: [] }
  dialogVisible.value = true
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      const admin = admins.value.find(a => a.username === formData.value.username)
      if (admin) await updateAdmin(admin.id, formData.value)
    } else {
      await createAdmin(formData.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadAdmins()
  } catch {}
}

const handleDelete = async (row: AdminVO) => {
  try {
    await ElMessageBox.confirm('确定删除该管理员吗？', '提示')
    await deleteAdmin(row.id)
    ElMessage.success('删除成功')
    loadAdmins()
  } catch {}
}

onMounted(() => {
  loadAdmins()
  loadRoles()
})
</script>

<template>
  <div class="admin-view">
    <div class="page-header">
      <h2 class="page-title">管理员</h2>
      <el-button type="primary" :icon="Plus" @click="openAddDialog">新增管理员</el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="禁用" value="DISABLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="admins" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="roles" label="角色" width="150">
          <template #default="{ row }">
            <el-tag v-for="role in row.roles" :key="role" size="small" style="margin-right: 4px">{{ role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginAt" label="最后登录" width="160" />
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
        <el-form-item label="用户名" prop="username">
          <el-input2 v-model="formData.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" :prop="isEdit ? '' : 'password'">
          <el-input2 v-model="formData.password" type="password" show-password :placeholder="isEdit ? '留空则不修改密码' : '请输入密码'" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input2 v-model="formData.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input2 v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input2 v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="formData.roleIds" multiple placeholder="请选择角色" style="width: 100%">
            <el-option2 v-for="role in roles" :key="role.id" :label="role.name" :value="role.id" />
          </el-select>
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
.admin-view { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: #333; margin: 0; }
.filter-card { margin-bottom: 12px; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
