<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const userId = ref(user.value.id || 1)
const streak = ref(0)
const todayDuration = ref(0)
const note = ref('')
const records = ref<any[]>([])

async function loadData() {
  const s: any = await request.get('/checkin/streak', { params: { userId: userId.value } })
  streak.value = s || 0
}

async function doCheckin() {
  if (!todayDuration.value || todayDuration.value <= 0) {
    ElMessage.warning('请输入学习时长'); return
  }
  await request.post('/checkin/do', null, {
    params: { userId: userId.value, duration: todayDuration.value, note: note.value }
  })
  ElMessage.success('打卡成功！')
  todayDuration.value = 0
  note.value = ''
  loadData()
}

onMounted(() => loadData())
</script>

<template>
  <div class="checkin-page">
    <header class="nav">
      <div class="logo">
        <div class="logo-mark">公</div>
        <span>考公打卡</span>
      </div>
      <nav class="menu">
        <span @click="$router.push('/')">首页</span>
        <span @click="$router.push('/plan')">规划</span>
        <span class="active">打卡</span>
        <span @click="$router.push('/stats')">数据</span>
      </nav>
      <div class="avatar">{{ (user.nickname || 'U').charAt(0).toUpperCase() }}</div>
    </header>

    <div class="content">
      <div class="streak-card">
        <div class="flame">🔥</div>
        <div class="streak-num">{{ streak }}</div>
        <div class="streak-label">天连续打卡</div>
      </div>

      <div class="form-card">
        <h2>今日打卡</h2>
        <el-input-number v-model="todayDuration" :min="15" :step="15" placeholder="学习时长（分钟）" />
        <el-input v-model="note" type="textarea" :rows="3" placeholder="今日学习笔记（可选）" />
        <el-button type="primary" size="large" round @click="doCheckin">完成今日打卡</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.checkin-page { max-width: 600px; margin: 0 auto; padding: 0 24px 60px; }
.nav { display: flex; align-items: center; padding: 20px 0; }
.logo { display: flex; align-items: center; gap: 10px; font-family: serif; font-weight: 700; font-size: 19px; color: #4A3826; }
.logo-mark { width: 36px; height: 36px; border-radius: 50%; background: #557A9A; color: #FFF8E7; display: flex; align-items: center; justify-content: center; font-weight: 700; }
.menu { display: flex; gap: 36px; margin-left: 60px; font-size: 14px; color: #8B7355; cursor: pointer; }
.menu .active { color: #4A3826; font-weight: 600; position: relative; }
.menu .active::after { content: ""; position: absolute; left: 50%; transform: translateX(-50%); bottom: -8px; width: 20px; height: 2px; border-radius: 2px; background: #C97B4A; }
.avatar { margin-left: auto; width: 38px; height: 38px; border-radius: 50%; background: #fff; border: 1.5px solid #E8DCC0; display: flex; align-items: center; justify-content: center; font-weight: 600; color: #557A9A; }

.content { margin-top: 32px; }
.streak-card {
  background: linear-gradient(135deg, #C97B4A, #E8A87C);
  border-radius: 24px; padding: 40px; text-align: center;
  color: #FFF8E7; margin-bottom: 24px;
}
.flame { font-size: 48px; }
.streak-num { font-family: serif; font-size: 64px; font-weight: 900; line-height: 1; margin: 8px 0; }
.streak-label { font-size: 14px; opacity: .9; }

.form-card {
  background: #FFFDF6; border-radius: 24px; padding: 32px;
  border: 1px solid rgba(234,223,194,.6);
}
.form-card h2 { font-family: serif; font-size: 20px; margin-bottom: 20px; color: #4A3826; }
.form-card :deep(.el-input-number) { width: 100%; margin-bottom: 16px; }
.form-card :deep(.el-input-number .el-input__wrapper) { border-radius: 20px; }
.form-card :deep(.el-textarea__inner) { border-radius: 16px; margin-bottom: 20px; }
.form-card .el-button { width: 100%; height: 44px; }
</style>
