<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-title">运维值守系统</div>
      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" class="login-button">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { FormInstance } from 'element-plus'

export default defineComponent({
  name: 'LoginPage',
  components: {
    User,
    Lock
  },
  setup() {
    const userStore = useUserStore()
    const router = useRouter()
    const route = useRoute()

    const loginFormRef = ref<FormInstance>()
    const loading = ref(false)

    const loginForm = reactive({
      username: '',
      password: ''
    })

    const loginRules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }

    const handleLogin = () => {
      if (!loginFormRef.value) return

      loginFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            console.log('开始登录请求，表单数据：', loginForm)
            await userStore.login(loginForm)
            const redirect = route.query.redirect?.toString() || '/'
            router.push(redirect)
            ElMessage.success('登录成功')
          } catch (error: any) {
            console.error('登录失败：', {
              message: error.message,
              response: error.response?.data,
              status: error.response?.status
            })
            ElMessage.error(error.response?.data?.message || error.message || '登录失败')
          } finally {
            loading.value = false
          }
        }
      })
    }

    return {
      loginForm,
      loginRules,
      loading,
      loginFormRef,
      handleLogin
    }
  }
})
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-form {
  width: 400px;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  background-color: white;
}

.login-title {
  margin-bottom: 30px;
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.login-button {
  width: 100%;
}

:deep(.el-input__wrapper) {
  padding-left: 0;
}

:deep(.el-input__prefix) {
  margin-right: 8px;
}
</style> 