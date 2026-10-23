<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { sendCode, login } from '../api/auth'

const router = useRouter()
const form = reactive({ email: '', password: '', code: '' })

async function onSendCode() {
  if (!form.email) { ElMessage.warning('请先输入邮箱'); return }
  await sendCode(form.email)
  ElMessage.success('验证码已发送')
}

async function onLogin() {
  const res: any = await login({ email: form.email, password: form.password })
  localStorage.setItem('token', res.token)
  localStorage.setItem('user', JSON.stringify(res.user))
  ElMessage.success('登录成功')
  router.push('/')
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="logo">
        <div class="logo-mark">公</div>
        <h1>考公打卡</h1>
      </div>
      <p class="sub">砚边磨剑，来日方长</p>

      <el-input v-model="form.email" placeholder="邮箱" class="field" />
      <el-input v-model="form.password" type="password" placeholder="密码" class="field" />

      <div class="code-row">
        <el-input v-model="form.code" placeholder="验证码" />
        <el-button @click="onSendCode">发送验证码</el-button>
      </div>

      <el-button type="primary" class="submit" @click="onLogin">登 录</el-button>
    </div>
  </div>
</template>

<style scoped>
.login-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; }
.login-card { width: 380px; background: var(--bg-card); border-radius: 20px; padding: 36px; border: 1px solid rgba(234,223,194,.6); box-shadow: var(--shadow-pop); }
.logo { display: flex; align-items: center; gap: 10px; justify-content: center; }
.logo-mark { width: 36px; height: 36px; border-radius: 50%; background: var(--color-primary); color: #FFF8E7; display: flex; align-items: center; justify-content: center; font-family: var(--font-serif); font-weight: 700; }
.logo h1 { font-family: var(--font-serif); font-size: 20px; }
.sub { text-align: center; color: var(--text-secondary); font-size: 13px; margin: 8px 0 24px; }
.field { margin-bottom: 14px; }
.code-row { display: flex; gap: 10px; margin-bottom: 18px; }
.submit { width: 100%; }
</style>
