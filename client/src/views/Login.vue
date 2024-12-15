<script setup>
import { ref, reactive } from 'vue'
import { User, Lock, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { userRegisterService, userLoginService } from '@/api/user.js'
import { useTokenStore } from '@/stores/token.js'
import { useRouter } from 'vue-router'

const isRegister = ref(false)
const router = useRouter()
const tokenStore = useTokenStore()

const formData = reactive({
  username: '',
  password: '',
  rePassword: ''
})

const rememberMe = ref(false)

const rules = {
  username: [
    { required: true, message: 'Please enter a username', trigger: 'blur' },
    { min: 5, max: 16, message: 'Length should be 5-16 characters', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter a password', trigger: 'blur' },
    { min: 5, max: 16, message: 'Length should be 5-16 characters', trigger: 'blur' }
  ],
  rePassword: [
    { required: true, message: 'Please confirm your password', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error('Passwords do not match'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const register = async () => {
  try {
    const result = await userRegisterService(formData)
    ElMessage.success(result.msg || 'Registration successful')
    isRegister.value = false
  } catch (error) {
    ElMessage.error('Registration failed')
  }
}

const login = async () => {
  try {
    const result = await userLoginService(formData)
    ElMessage.success(result.msg || 'Login successful')
    tokenStore.setToken(result.data)
    router.push('/article/category')
  } catch (error) {
    ElMessage.error('Login failed')
  }
}

const toggleForm = () => {
  isRegister.value = !isRegister.value
  Object.assign(formData, { username: '', password: '', rePassword: '' })
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-form">
        <h2 class="title">{{ isRegister ? 'Create Account' : 'Welcome Back' }}</h2>
        <p class="subtitle">{{ isRegister ? 'Sign up to get started' : 'Sign in to continue' }}</p>
        
        <el-form :model="formData" :rules="rules" size="large">
          <el-form-item prop="username">
            <el-input v-model="formData.username" :prefix-icon="User" placeholder="Username" />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input v-model="formData.password" :prefix-icon="Lock" type="password" placeholder="Password" />
          </el-form-item>
          
          <el-form-item v-if="isRegister" prop="rePassword">
            <el-input v-model="formData.rePassword" :prefix-icon="Lock" type="password" placeholder="Confirm Password" />
          </el-form-item>
          
          <div v-if="!isRegister" class="remember-forgot">
            <el-checkbox v-model="rememberMe">Remember me</el-checkbox>
            <el-link type="primary">Forgot password?</el-link>
          </div>
          
          <el-button type="primary" class="submit-btn" @click="isRegister ? register() : login()">
            {{ isRegister ? 'Sign Up' : 'Sign In' }}
          </el-button>
        </el-form>
        
        <div class="toggle-form">
          <span>{{ isRegister ? 'Already have an account?' : "Don't have an account?" }}</span>
          <el-button link type="primary" @click="toggleForm">
            {{ isRegister ? 'Sign In' : 'Sign Up' }}
            <el-icon class="el-icon--right">
              <component :is="isRegister ? ArrowLeft : ArrowRight" />
            </el-icon>
          </el-button>
        </div>
      </div>
      <div class="login-image"></div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  display: flex;
  width: 900px;
  height: 600px;
  background-color: #ffffff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.login-form {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-image {
  flex: 1;
  background-image: url('@/assets/login_bg.img');
  background-size: cover;
  background-position: center;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.el-form-item {
  margin-bottom: 20px;
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
}

.toggle-form {
  margin-top: 20px;
  text-align: center;
  
  span {
    color: #666;
  }
}

// Transitions
.el-form-item {
  transition: all 0.3s ease;
}

.submit-btn {
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
}

@media (max-width: 768px) {
  .login-box {
    flex-direction: column;
    width: 100%;
    height: auto;
  }
  
  .login-image {
    display: none;
  }
}
</style>