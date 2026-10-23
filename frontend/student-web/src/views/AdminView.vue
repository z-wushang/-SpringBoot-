<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const users = ref<any[]>([])
const stats = ref<any>({})

async function loadData() {
  const list: any = await request.get('/admin/users')
  users.value = list || []
  const s: any = await request.get('/admin/stats')
  stats.value = s || {}
}

onMounted(() => loadData())
</script>

<template>
  <div class="admin-page">
    <aside class="sidebar">
      <div class="logo">管理后台</div>
      <div class="menu-item active">仪表盘</div>
      <div class="menu-item">用户管理</div>
      <div class="menu-item">内容审核</div>
      <div class="menu-item">系统设置</div>
    </aside>

    <main class="main">
      <h1>仪表盘</h1>
      <div class="stat-cards">
        <div class="card"><div class="n">{{ stats.userCount }}</div><div class="l">总用户数</div></div>
        <div class="card"><div class="n">{{ stats.taskCount }}</div><div class="l">任务总数</div></div>
        <div class="card"><div class="n">{{ stats.studyRecordCount }}</div><div class="l">学习记录</div></div>
      </div>

      <h2>最近用户</h2>
      <table class="user-table">
        <thead><tr><th>ID</th><th>用户名</th><th>邮箱</th><th>角色</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="u in users" :key="u.id">
            <td>{{ u.id }}</td>
            <td>{{ u.username }}</td>
            <td>{{ u.email }}</td>
            <td>{{ u.role === 0 ? '管理员' : '普通用户' }}</td>
            <td><el-tag size="small" :type="u.status === 1 ? 'success' : 'danger'">{{ u.status === 1 ? '正常' : '禁用' }}</el-tag></td>
          </tr>
        </tbody>
      </table>
    </main>
  </div>
</template>

<style scoped>
.admin-page { display: flex; min-height: 100vh; }
.sidebar {
  width: 220px; background: #3A5A7A; color: #FFF8E7; padding: 24px 0;
}
.logo { font-family: serif; font-size: 18px; font-weight: 700; padding: 0 24px 20px; border-bottom: 1px solid rgba(255,255,255,.1); }
.menu-item { padding: 12px 24px; cursor: pointer; font-size: 14px; }
.menu-item.active { background: rgba(255,255,255,.15); border-left: 3px solid #C97B4A; }

.main { flex: 1; padding: 24px 32px; background: #F8F1DE; }
h1 { font-family: serif; font-size: 22px; color: #4A3826; margin-bottom: 20px; }
h2 { font-family: serif; font-size: 16px; color: #4A3826; margin: 24px 0 12px; }

.stat-cards { display: flex; gap: 16px; }
.card {
  background: #FFFDF6; border-radius: 12px; padding: 20px 28px;
  border: 1px solid rgba(234,223,194,.6); min-width: 140px;
}
.card .n { font-family: serif; font-size: 32px; font-weight: 900; color: #3A5A7A; }
.card .l { color: #8B7355; font-size: 13px; margin-top: 4px; }

.user-table { width: 100%; background: #FFFDF6; border-radius: 12px; overflow: hidden; border: 1px solid rgba(234,223,194,.6); }
.user-table th, .user-table td { padding: 12px 16px; text-align: left; font-size: 14px; }
.user-table th { background: #F3E8C8; color: #4A3826; font-weight: 600; }
.user-table td { border-top: 1px solid #EFE6D0; }
</style>
