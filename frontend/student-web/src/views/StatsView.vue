<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const userId = ref(user.value.id || 1)
const weekData = ref<any>({ trend: [], totalHours: 0 })

function buildLine(): string {
  const w = 700, h = 150
  const data = weekData.value.trend.map((d: any) => d.hours)
  if (!data.length) return ''
  const max = Math.max(...data, 1)
  const stepX = w / Math.max(data.length - 1, 1)
  return data.map((v: number, i: number) => {
    const x = i * stepX
    const y = h - 20 - (v / max) * (h - 50)
    return `${x},${y}`
  }).join(' ')
}

async function loadData() {
  const res: any = await request.get('/study/week', { params: { userId: userId.value } })
  weekData.value = res || { trend: [], totalHours: 0 }
}

onMounted(() => loadData())
</script>

<template>
  <div class="stats-page">
    <header class="nav">
      <div class="logo">
        <div class="logo-mark">公</div>
        <span>考公打卡</span>
      </div>
      <nav class="menu">
        <span @click="$router.push('/')">首页</span>
        <span @click="$router.push('/plan')">规划</span>
        <span @click="$router.push('/checkin')">打卡</span>
        <span class="active">数据</span>
      </nav>
      <div class="avatar">{{ (user.nickname || 'U').charAt(0).toUpperCase() }}</div>
    </header>

    <div class="content">
      <h2>📊 本周学习统计</h2>
      <div class="stat-cards">
        <div class="stat-card">
          <div class="num">{{ weekData.totalHours }}</div>
          <div class="label">本周总时长 (h)</div>
        </div>
        <div class="stat-card">
          <div class="num">{{ weekData.trend.length }}</div>
          <div class="label">打卡天数</div>
        </div>
      </div>

      <div class="chart-card">
        <h3>每日学习时长趋势</h3>
        <svg v-if="buildLine()" class="trend" viewBox="0 0 700 150" preserveAspectRatio="none">
          <defs>
            <linearGradient id="area" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0" stop-color="#557A9A" stop-opacity=".22"/>
              <stop offset="1" stop-color="#557A9A" stop-opacity="0"/>
            </linearGradient>
          </defs>
          <path :d="`M${buildLine().replaceAll(' ', ' L')} L700,150 L0,150 Z`" fill="url(#area)"/>
          <polyline :points="buildLine()" fill="none" stroke="#557A9A" stroke-width="2.5"/>
        </svg>
        <div v-else class="empty">本周还没有学习记录</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stats-page { max-width: 900px; margin: 0 auto; padding: 0 24px 60px; }
.nav { display: flex; align-items: center; padding: 20px 0; }
.logo { display: flex; align-items: center; gap: 10px; font-family: serif; font-weight: 700; font-size: 19px; color: #4A3826; }
.logo-mark { width: 36px; height: 36px; border-radius: 50%; background: #557A9A; color: #FFF8E7; display: flex; align-items: center; justify-content: center; font-weight: 700; }
.menu { display: flex; gap: 36px; margin-left: 60px; font-size: 14px; color: #8B7355; cursor: pointer; }
.menu .active { color: #4A3826; font-weight: 600; position: relative; }
.menu .active::after { content: ""; position: absolute; left: 50%; transform: translateX(-50%); bottom: -8px; width: 20px; height: 2px; border-radius: 2px; background: #C97B4A; }
.avatar { margin-left: auto; width: 38px; height: 38px; border-radius: 50%; background: #fff; border: 1.5px solid #E8DCC0; display: flex; align-items: center; justify-content: center; font-weight: 600; color: #557A9A; }

.content { margin-top: 24px; }
h2 { font-family: serif; font-size: 22px; margin-bottom: 20px; color: #4A3826; }
.stat-cards { display: flex; gap: 16px; margin-bottom: 20px; }
.stat-card {
  flex: 1; background: #FFFDF6; border-radius: 16px; padding: 24px;
  border: 1px solid rgba(234,223,194,.6); text-align: center;
}
.stat-card .num { font-family: serif; font-size: 36px; font-weight: 900; color: #557A9A; }
.stat-card .label { color: #8B7355; font-size: 13px; margin-top: 4px; }

.chart-card {
  background: #FFFDF6; border-radius: 16px; padding: 24px;
  border: 1px solid rgba(234,223,194,.6);
}
.chart-card h3 { font-size: 15px; color: #4A3826; margin-bottom: 16px; }
.trend { width: 100%; height: 160px; }
.empty { text-align: center; color: #8B7355; padding: 40px; }
</style>
