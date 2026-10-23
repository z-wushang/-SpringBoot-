<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const userId = ref(user.value.id || 1)

const goals = ref<any[]>([])
const tasks = ref<any[]>([])
const newGoal = ref({ targetName: '', position: '', examDate: '' })
const newTask = ref({ subject: '', taskContent: '', planDuration: 60 })

async function loadGoals() {
  const res: any = await request.get('/goal/current', { params: { userId: userId.value } })
  if (res?.goal) goals.value = [res.goal]
}

async function loadTasks() {
  const res: any = await request.get('/task/today', { params: { userId: userId.value } })
  tasks.value = res || []
}

async function addGoal() {
  if (!newGoal.value.targetName || !newGoal.value.examDate) {
    ElMessage.warning('请填写目标名称和考试日期'); return
  }
  await request.post('/goal/add', { ...newGoal.value, userId: userId.value })
  ElMessage.success('目标已添加')
  newGoal.value = { targetName: '', position: '', examDate: '' }
  loadGoals()
}

async function addTask() {
  if (!newTask.value.taskContent) { ElMessage.warning('请填写任务内容'); return }
  await request.post('/task/add', { ...newTask.value, userId: userId.value })
  ElMessage.success('任务已添加')
  newTask.value = { subject: '', taskContent: '', planDuration: 60 }
  loadTasks()
}

onMounted(() => { loadGoals(); loadTasks() })
</script>

<template>
  <div class="plan-page">
    <header class="nav">
      <div class="logo">
        <div class="logo-mark">公</div>
        <span>考公打卡</span>
      </div>
      <nav class="menu">
        <span @click="$router.push('/')">首页</span>
        <span class="active">规划</span>
        <span @click="$router.push('/checkin')">打卡</span>
        <span @click="$router.push('/stats')">数据</span>
      </nav>
      <div class="avatar">{{ (user.nickname || 'U').charAt(0).toUpperCase() }}</div>
    </header>

    <div class="content">
      <div class="section">
        <h2>🎯 备考目标</h2>
        <div v-for="g in goals" :key="g.id" class="goal-card">
          <div class="goal-name">{{ g.targetName }}</div>
          <div class="goal-meta">{{ g.position }} · {{ g.examDate }} 考试</div>
        </div>
        <div class="add-form">
          <el-input v-model="newGoal.targetName" placeholder="目标名称，如：福建省考" />
          <el-input v-model="newGoal.position" placeholder="报考岗位" />
          <el-date-picker v-model="newGoal.examDate" type="date" placeholder="考试日期" />
          <el-button type="primary" round @click="addGoal">添加目标</el-button>
        </div>
      </div>

      <div class="section">
        <h2>📋 今日任务</h2>
        <div v-for="t in tasks" :key="t.id" class="task-row" :class="{ done: t.status === 1 }">
          <span class="subject-tag">{{ t.subject }}</span>
          <span class="content">{{ t.taskContent }}</span>
          <span class="duration">{{ t.planDuration }}min</span>
        </div>
        <div class="add-form">
          <el-input v-model="newTask.subject" placeholder="科目，如：行测" />
          <el-input v-model="newTask.taskContent" placeholder="任务内容" />
          <el-input-number v-model="newTask.planDuration" :min="15" :step="15" />
          <el-button type="primary" round @click="addTask">添加任务</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.plan-page { max-width: 1100px; margin: 0 auto; padding: 0 24px 60px; }
.nav { display: flex; align-items: center; padding: 20px 0; }
.logo { display: flex; align-items: center; gap: 10px; font-family: serif; font-weight: 700; font-size: 19px; color: #4A3826; }
.logo-mark { width: 36px; height: 36px; border-radius: 50%; background: #557A9A; color: #FFF8E7; display: flex; align-items: center; justify-content: center; font-weight: 700; }
.menu { display: flex; gap: 36px; margin-left: 60px; font-size: 14px; color: #8B7355; cursor: pointer; }
.menu .active { color: #4A3826; font-weight: 600; position: relative; }
.menu .active::after { content: ""; position: absolute; left: 50%; transform: translateX(-50%); bottom: -8px; width: 20px; height: 2px; border-radius: 2px; background: #C97B4A; }
.avatar { margin-left: auto; width: 38px; height: 38px; border-radius: 50%; background: #fff; border: 1.5px solid #E8DCC0; display: flex; align-items: center; justify-content: center; font-weight: 600; color: #557A9A; }

.content { margin-top: 24px; }
.section { margin-bottom: 40px; }
.section h2 { font-family: serif; font-size: 22px; margin-bottom: 16px; color: #4A3826; }
.goal-card {
  background: #FFFDF6; border-radius: 16px; padding: 20px 24px;
  border: 1px solid rgba(234,223,194,.6); margin-bottom: 12px;
}
.goal-name { font-size: 18px; font-weight: 600; color: #557A9A; }
.goal-meta { color: #8B7355; font-size: 13px; margin-top: 4px; }

.task-row {
  display: flex; align-items: center; gap: 12px;
  background: #FFFDF6; border-radius: 12px; padding: 14px 18px;
  border: 1px solid rgba(234,223,194,.4); margin-bottom: 8px; font-size: 14px;
}
.task-row.done { opacity: .5; text-decoration: line-through; }
.subject-tag {
  background: #E3ECF3; color: #557A9A; padding: 3px 10px;
  border-radius: 12px; font-size: 12px;
}
.task-row .content { flex: 1; }
.task-row .duration { color: #8B7355; font-size: 12px; }

.add-form {
  display: flex; gap: 10px; margin-top: 16px; align-items: center;
  flex-wrap: wrap;
}
.add-form :deep(.el-input__wrapper) { border-radius: 20px; }
</style>
