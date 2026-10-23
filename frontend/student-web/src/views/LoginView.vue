<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { sendCode, login, register } from '../api/auth'

const router = useRouter()
const mode = ref<'login' | 'register'>('login')

const form = reactive({
  username: '',
  email: '',
  password: '',
  code: ''
})

async function onSendCode() {
  if (!form.email) { ElMessage.warning('请先输入邮箱'); return }
  await sendCode(form.email)
  ElMessage.success('验证码已发送（后端日志可看）')
}

async function onSubmit() {
  if (mode.value === 'login') {
    const res: any = await login({ email: form.email, password: form.password })
    localStorage.setItem('token', res.token)
    localStorage.setItem('user', JSON.stringify(res.user))
    ElMessage.success('登录成功')
    router.push('/')
  } else {
    await register({
      username: form.username,
      email: form.email,
      password: form.password,
      code: form.code
    })
    ElMessage.success('注册成功，请登录')
    mode.value = 'login'
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="logo">
        <div class="logo-mark">公</div>
        <h1>考公打卡</h1>
      </div>
      <p class="sub">砚边磨剑，来日方长</p>

      <div class="tabs">
        <span :class="{ active: mode === 'login' }" @click="mode = 'login'">登录</span>
        <span :class="{ active: mode === 'register' }" @click="mode = 'register'">注册</span>
      </div>

      <el-input v-if="mode === 'register'" v-model="form.username" placeholder="用户名" class="field" />
      <el-input v-model="form.email" placeholder="邮箱" class="field" />
      <el-input v-model="form.password" type="password" placeholder="密码" class="field" show-password />

      <div v-if="mode === 'register'" class="code-row">
        <el-input v-model="form.code" placeholder="验证码" class="code-input" />
        <el-button round @click="onSendCode">发送验证码</el-button>
      </div>

      <el-button type="primary" class="submit" round @click="onSubmit">
        {{ mode === 'login' ? '登 录' : '注 册' }}
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #F8F1DE 0%, #FBF6E9 100%);
}
.auth-card {
  width: 400px;
  background: #FFFDF6;
  border-radius: 24px;
  padding: 40px 36px;
  border: 1px solid rgba(234, 223, 194, .6);
  box-shadow: 0 20px 60px rgba(85, 122, 154, .12);
}
.logo { display: flex; align-items: center; gap: 12px; justify-content: center; }
.logo-mark {
  width: 40px; height: 40px; border-radius: 50%;
  background: #557A9A; color: #FFF8E7;
  display: flex; align-items: center; justify-content: center;
  font-family: serif; font-weight: 700; font-size: 18px;
  box-shadow: 0 4px 12px rgba(85,122,154,.3);
}
.logo h1 { font-family: serif; font-size: 22px; color: #4A3826; }
.sub { text-align: center; color: #8B7355; font-size: 13px; margin: 10px 0 24px; }

.tabs {
  display: flex; justify-content: center; gap: 32px;
  margin-bottom: 24px; padding-bottom: 12px;
  border-bottom: 1px solid #EFE6D0;
}
.tabs span {
  cursor: pointer; font-size: 15px; color: #8B7355;
  padding-bottom: 10px; position: relative; transition: color .2s;
}
.tabs span.active { color: #557A9A; font-weight: 600; }
.tabs span.active::after {
  content: ""; position: absolute; left: 50%; transform: translateX(-50%);
  bottom: -13px; width: 24px; height: 3px;
  border-radius: 3px; background: #C97B4A;
}

.field :deep(.el-input__wrapper) {
  border-radius: 24px; padding: 4px 16px;
  box-shadow: 0 0 0 1px #E8DCC0 inset;
  transition: box-shadow .2s;
}
.field :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #557A9A inset;
}
.field { margin-bottom: 14px; }

.code-row { display: flex; gap: 10px; margin-bottom: 18px; }
.code-input { flex: 1; }
.code-input :deep(.el-input__wrapper) {
  border-radius: 24px; padding: 4px 16px;
  box-shadow: 0 0 0 1px #E8DCC0 inset;
}

.submit {
  width: 100%;
  height: 44px;
  font-size: 15px;
  background: linear-gradient(135deg, #557A9A, #6B95B8);
  border: none;
}
.submit:hover {
  background: linear-gradient(135deg, #4A6B8A, #557A9A);
}
</style>
