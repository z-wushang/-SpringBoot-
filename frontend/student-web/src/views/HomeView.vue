<script setup lang="ts">
import { ref, computed } from 'vue'

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const nickname = computed(() => user.value.nickname || '同学')
const avatarText = computed(() => (user.value.nickname || 'U').charAt(0).toUpperCase())

const todayTasks = [
  { id: 1, title: '行测言语理解 2 篇 + 精读', duration: 120, done: true },
  { id: 2, title: '申论概括题 5 道', duration: 60, done: false },
  { id: 3, title: '资料分析 20 题', duration: 90, done: false }
]

const weekTrend = [1.5, 2.2, 2.0, 3.2, 2.8, 3.8, 4.0]
const progress = 66

function buildLine(): string {
  const w = 700
  const h = 150
  const max = Math.max(...weekTrend)
  const min = Math.min(...weekTrend)
  const stepX = w / (weekTrend.length - 1)
  return weekTrend
    .map((v, i) => {
      const x = i * stepX
      const y = h - 20 - ((v - min) / (max - min || 1)) * (h - 50)
      return `${x},${y}`
    })
    .join(' ')
}
function buildArea(): string {
  const line = buildLine()
  return `M${line.replaceAll(' ', ' L')} L700,150 L0,150 Z`
}
</script>

<template>
  <div class="home">
    <header class="nav">
      <div class="logo">
        <div class="logo-mark">公</div>
        <span>考公打卡</span>
      </div>
      <nav class="menu">
        <span class="active">首页</span>
        <span>规划</span>
        <span>打卡</span>
        <span>数据</span>
        <span>社区</span>
      </nav>
      <div class="avatar">{{ avatarText }}</div>
    </header>

    <section class="hero">
      <div>
        <h1>砚边磨剑，来日方长。</h1>
        <p>早上好，{{ nickname }}。今天是你坚持打卡的第 7 天。</p>
      </div>
      <div class="date-pill">2026 年 10 月 23 日 · 周五</div>
    </section>

    <section class="grid-top">
      <div class="card countdown">
        <div class="card-label"><span>EXAM COUNTDOWN · 公考倒计时</span><span class="pill">福州大学 · 软件工程</span></div>
        <div class="count-num">128<em>天后初试</em></div>
        <div class="tags">
          <span class="pill green">行测</span>
          <span class="pill green">申论</span>
          <span class="pill green">面试</span>
        </div>
      </div>

      <div class="card">
        <div class="card-label"><span>TODAY · 今日进度</span><span class="pill">2 / 3</span></div>
        <div class="ring-row">
          <svg width="110" height="110" viewBox="0 0 110 110" class="ring">
            <circle cx="55" cy="55" r="44" class="ring-bg"/>
            <circle cx="55" cy="55" r="44" class="ring-fg" :stroke-dasharray="276" :stroke-dashoffset="276 - (276 * progress / 100)"/>
          </svg>
          <div>
            <div class="ring-num">{{ progress }}%</div>
            <div class="ring-desc">今日目标完成度<br/>继续保持 ✦</div>
          </div>
        </div>
      </div>
    </section>

    <section class="grid-bottom">
      <div class="card">
        <div class="card-label"><span>TASKS · 今日待办</span><span class="pill">3 项</span></div>
        <div v-for="t in todayTasks" :key="t.id" class="task" :class="{ done: t.done }">
          <div class="checkbox" :class="{ done: t.done }">{{ t.done ? '✓' : '' }}</div>
          <span class="t">{{ t.title }}</span>
          <span class="sub">{{ t.duration }} min</span>
        </div>
      </div>

      <div class="card">
        <div class="card-label"><span>STREAK · 连续打卡</span><span class="pill green">🔥 7 天</span></div>
        <div class="streak-row">
          <div class="flame">🔥</div>
          <div>
            <div class="streak-num">7<small> 天连续</small></div>
            <div class="streak-desc">距离下一个奖章还差 23 天</div>
          </div>
        </div>
      </div>

      <div class="card wide">
        <div class="card-label"><span>WEEK · 本周学习时长</span><span class="pill">38.5 h</span></div>
        <svg class="trend" viewBox="0 0 700 150" preserveAspectRatio="none">
          <defs>
            <linearGradient id="area" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0" stop-color="#557A9A" stop-opacity=".22"/>
              <stop offset="1" stop-color="#557A9A" stop-opacity="0"/>
            </linearGradient>
          </defs>
          <path :d="buildArea()" fill="url(#area)"/>
          <polyline :points="buildLine()" fill="none" stroke="#557A9A" stroke-width="2.5"/>
        </svg>
        <div class="stat-row">
          <div class="stat"><div class="n">38.5</div><div class="l">本周时长 (h)</div></div>
          <div class="stat"><div class="n">86%</div><div class="l">打卡率</div></div>
          <div class="stat"><div class="n">4.2</div><div class="l">日均 (h)</div></div>
          <div class="stat"><div class="n">12</div><div class="l">已完成任务</div></div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.home { max-width: 1200px; margin: 0 auto; padding: 0 24px 60px; }
.nav { display: flex; align-items: center; padding: 20px 0; }
.logo { display: flex; align-items: center; gap: 10px; font-family: var(--font-serif); font-weight: 700; font-size: 19px; color: var(--text-primary); }
.logo-mark { width: 36px; height: 36px; border-radius: 50%; background: var(--color-primary); color: #FFF8E7; display: flex; align-items: center; justify-content: center; font-family: var(--font-serif); font-weight: 700; box-shadow: 0 4px 12px rgba(85,122,154,.25); }
.menu { display: flex; gap: 36px; margin-left: 60px; font-size: 14px; color: var(--text-regular); }
.menu .active { color: var(--text-primary); font-weight: 600; position: relative; }
.menu .active::after { content: ""; position: absolute; left: 50%; transform: translateX(-50%); bottom: -8px; width: 20px; height: 2px; border-radius: 2px; background: var(--color-accent-deep); }
.avatar { margin-left: auto; width: 38px; height: 38px; border-radius: 50%; background: #fff; border: 1.5px solid var(--border); display: flex; align-items: center; justify-content: center; font-weight: 600; color: var(--color-primary-active); }

.hero { display: flex; justify-content: space-between; align-items: flex-end; margin: 12px 0 28px; }
.hero h1 { font-family: var(--font-serif); font-size: 34px; font-weight: 900; }
.hero p { color: var(--text-regular); margin-top: 8px; font-size: 14px; }
.date-pill { padding: 8px 18px; border: 1px solid var(--border); border-radius: 30px; background: rgba(255,253,246,.7); font-size: 13px; color: var(--text-regular); }

.grid-top { display: grid; grid-template-columns: 1.2fr 1fr; gap: 20px; }
.grid-bottom { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px; }
.card { background: var(--bg-card); border-radius: var(--radius-lg); padding: 24px; border: 1px solid rgba(234,223,194,.6); box-shadow: var(--shadow-card); }
.card.wide { grid-column: 1 / -1; }
.card-label { font-size: 12px; letter-spacing: 2px; color: var(--text-secondary); margin-bottom: 14px; display: flex; justify-content: space-between; align-items: center; }
.pill { padding: 4px 12px; border-radius: 20px; font-size: 12px; background: #F3E8C8; color: var(--text-regular); }
.pill.green { background: var(--color-primary-light); color: var(--color-primary-active); }
.count-num { font-family: var(--font-serif); font-size: 80px; font-weight: 900; line-height: 1; }
.count-num em { font-style: normal; font-size: 20px; color: var(--text-regular); margin-left: 10px; font-family: sans-serif; font-weight: 400; }
.tags { display: flex; gap: 8px; margin-top: 16px; }
.ring-row { display: flex; align-items: center; gap: 24px; }
.ring { transform: rotate(-90deg); }
.ring-bg { fill: none; stroke: #EFE6D0; stroke-width: 8; }
.ring-fg { fill: none; stroke: var(--color-primary); stroke-width: 8; stroke-linecap: round; transition: stroke-dashoffset .6s; }
.ring-num { font-family: var(--font-serif); font-size: 26px; font-weight: 700; }
.ring-desc { color: var(--text-regular); font-size: 13px; margin-top: 4px; }

.task { display: flex; align-items: center; gap: 12px; padding: 12px 0; border-bottom: 1px dashed var(--border-light); font-size: 14px; }
.task:last-child { border-bottom: none; }
.checkbox { width: 20px; height: 20px; border: 2px solid var(--border); border-radius: 50%; flex-shrink: 0; background: #fff; display: flex; align-items: center; justify-content: center; color: #FFF8E7; font-size: 12px; }
.checkbox.done { background: var(--color-primary); border-color: var(--color-primary); }
.task.done .t { text-decoration: line-through; color: var(--text-secondary); }
.task .sub { margin-left: auto; color: var(--text-secondary); font-size: 12px; }

.streak-row { display: flex; align-items: center; gap: 16px; }
.flame { width: 56px; height: 56px; border-radius: 18px; background: linear-gradient(135deg, #E8C9A8, var(--color-accent)); display: flex; align-items: center; justify-content: center; font-size: 28px; }
.streak-num { font-family: var(--font-serif); font-size: 36px; font-weight: 900; color: var(--color-accent-deep); }
.streak-num small { font-size: 14px; color: var(--text-regular); font-family: sans-serif; font-weight: 400; }
.streak-desc { color: var(--text-regular); font-size: 13px; margin-top: 4px; }

.trend { width: 100%; height: 160px; }
.stat-row { display: flex; justify-content: space-around; margin-top: 10px; }
.stat { text-align: center; }
.stat .n { font-family: var(--font-serif); font-size: 24px; font-weight: 700; color: var(--color-primary-active); }
.stat .l { font-size: 12px; color: var(--text-secondary); margin-top: 4px; }
</style>
