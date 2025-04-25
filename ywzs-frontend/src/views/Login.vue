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
            const requestData = {
              username: loginForm.username,
              password: loginForm.password  // 密码应该是明文
            }
            console.log('登录请求数据：', {
              username: requestData.username,
              password: '******'  // 日志中隐藏实际密码
            })
            
            const response = await fetch('/api/auth/login', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestData),
              credentials: 'include'  // 重要：包含 cookies
            })

            let data
            try {
              data = await response.json()
              console.log('登录响应数据：', data)
            } catch (e) {
              console.error('解析响应数据失败：', e)
              throw new Error('服务器响应格式错误')
            }

            if (!response.ok || !data.success) {
              throw new Error(data.message || '登录失败')
            }

            if (data.data?.user) {
              // 保存用户信息到 store
              userStore.setUser(data.data.user)
              const redirect = route.query.redirect?.toString() || '/'
              await router.push(redirect)
              ElMessage.success('登录成功')
            } else {
              console.error('登录响应数据格式不正确：', data)
              throw new Error('登录响应数据格式错误')
            }
          } catch (error: any) {
            console.error('登录失败：', error)
            ElMessage.error(error.message || '登录失败')
            userStore.clearUser()
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