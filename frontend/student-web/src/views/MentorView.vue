<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const helps = ref<any[]>([])
const unread = ref(0)

async function loadData() {
  const list: any = await request.get('/helps', { params: { targetUserId: 1 } })
  helps.value = list || []
  const u: any = await request.get('/messages/unread-count', { params: { userId: 1 } })
  unread.value = u?.count || 0
}

async function reply(id: number) {
  await request.put(`/helps/${id}/status`, null, { params: { status: 2 } })
  loadData()
}

onMounted(() => loadData())
</script>

<template>
  <div class="mentor-page">
    <header class="topbar">
      <div class="brand">督学端</div>
      <div class="badge">未读 {{ unread }}</div>
    </header>

    <div class="content">
      <div class="stats-row">
        <div class="stat-card"><div class="n">12</div><div class="l">关注考生</div></div>
        <div class="stat-card"><div class="n">{{ helps.filter((h:any)=>h.status===1).length }}</div><div class="l">待回复求助</div></div>
        <div class="stat-card"><div class="n">95%</div><div class="l">打卡率</div></div>
      </div>

      <h2>📨 收到的求助</h2>
      <div v-for="h in helps" :key="h.id" class="help-card" :class="{ pending: h.status === 1 }">
        <div class="help-title">{{ h.title }}</div>
        <div class="help-content">{{ h.content }}</div>
        <div class="help-meta">{{ h.subject }} · {{ h.createTime }}</div>
        <el-button v-if="h.status === 1" type="primary" size="small" round @click="reply(h.id)">标记已回复</el-button>
        <el-tag v-else type="success">已回复</el-tag>
      </div>
      <div v-if="!helps.length" class="empty">暂无求助</div>
    </div>
  </div>
</template>

<style scoped>
.mentor-page { min-height: 100vh; background: #F8F1DE; }
.topbar {
  background: linear-gradient(135deg, #C97B4A, #E8A87C);
  padding: 16px 32px; color: #FFF8E7;
  display: flex; justify-content: space-between; align-items: center;
}
.brand { font-family: serif; font-size: 20px; font-weight: 700; }
.badge { background: rgba(255,255,255,.25); padding: 4px 14px; border-radius: 20px; font-size: 13px; }

.content { max-width: 900px; margin: 0 auto; padding: 24px; }
.stats-row { display: flex; gap: 16px; margin-bottom: 24px; }
.stat-card {
  flex: 1; background: #FFFDF6; border-radius: 16px; padding: 20px;
  text-align: center; border: 1px solid rgba(234,223,194,.6);
}
.stat-card .n { font-family: serif; font-size: 32px; font-weight: 900; color: #C97B4A; }
.stat-card .l { color: #8B7355; font-size: 13px; margin-top: 4px; }

h2 { font-family: serif; font-size: 18px; margin: 20px 0 12px; color: #4A3826; }
.help-card {
  background: #FFFDF6; border-radius: 12px; padding: 16px 20px;
  margin-bottom: 10px; border: 1px solid rgba(234,223,194,.4);
}
.help-card.pending { border-left: 3px solid #C97B4A; }
.help-title { font-weight: 600; color: #4A3826; }
.help-content { color: #6B5540; font-size: 14px; margin: 6px 0; }
.help-meta { color: #8B7355; font-size: 12px; margin-bottom: 8px; }
.empty { text-align: center; color: #8B7355; padding: 40px; }
</style>
