<template>
    <div class="container">
      <form @submit.prevent="handle" class="login-form">
        <h1 class="form-title">论坛入口</h1>
        <div class="form-group">
          <div class="input-icon">
            <i class="fas fa-user"></i>
            <input type="text" id="username" v-model="username" placeholder="用户名" required />
          </div>
        </div>
        <div class="form-group">
          <div class="input-icon">
            <i class="fas fa-lock"></i>
            <input type="password" id="password" v-model="password" placeholder="密码" required />
          </div>
        </div>
        <button type="submit">登录</button>
        <div class="additional-options">
          <button type="button" @click="goToRegister">还没有账号？点击注册</button>
          <button type="button" @click="goToForgotPassword">忘记密码</button>
        </div>
      </form>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import apiClient from '@/api';
  const username = ref('');
  const password = ref('');
  const errorMessage = ref('');
  const router = useRouter();
  const handle = async () => {
      errorMessage.value = '';
      try {
          console.log(username.value, password.value);

          const response = await apiClient.loginUser({ "username": username.value, "password": password.value });
          if (response.data.code !== 0) {
              errorMessage.value = response.data.msg;
              return;
          }
          console.log('登录成功:', response.data);
          localStorage.setItem('token', response.data.data); 
          router.push('/home');
      } catch (error) {
          console.error('登录失败:', error);
      }
  };
  const goToRegister = () => {
      router.push({ name: 'register' });
  };
  
  const goToForgotPassword = () => {
      router.push({ name: 'forgot-password' });
  };
  </script>
  
  <style scoped>
  .container {
    background-image: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    background-size: cover;
    background-position: center;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  
  .login-form {
    background: rgba(255, 255, 255, 0.95);
    padding: 40px;
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
    width: 400px;
    transition: all 0.3s ease;
  }
  
  .form:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 40px rgba(31, 38, 135, 0.5);
  }
  
  .form-group {
      margin-bottom: 20px;
      /* 增加底部外边距 */
  }
  
  
  .form-group input {
    width: 100%;
    padding: 12px;
    box-sizing: border-box;
    border: 2px solid #e2e8f0;
    border-radius: 8px;
    font-size: 16px;
    transition: all 0.3s ease;
  }
  
  .form-group input:focus {
    border-color: #667eea;
    outline: none;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.25);
  }
  
  .form-title {
    margin-bottom: 40px;
    text-align: center;
    color: #4a5568;
    font-size: 28px;
    font-weight: 700;
  }
  
  button[type="submit"] {
    width: 100%;
    padding: 15px;
    background-color: #667eea;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 18px;
    font-weight: 600;
    transition: all 0.3s ease;
  }
  
  button[type="submit"]:hover {
    background-color: #5a67d8;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
  }
  
  .additional-options {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
  
  .additional-options button {
    background: none;
    border: none;
    cursor: pointer;
    color: #667eea;
    text-decoration: none;
    font-size: 14px;
    transition: all 0.3s ease;
  }
  
  .additional-options button:hover {
    color: #5a67d8;
    text-decoration: underline;
  }
  
  .input-icon {
    position: relative;
  }
  
  .input-icon i {
    position: absolute;
    left: 12px;
    top: 50%;
    transform: translateY(-50%);
    color: #a0aec0;
  }
  
  .input-icon input {
    padding-left: 40px;
  }
  
  @media (max-width: 480px) {
    .form {
      width: 90%;
      padding: 30px;
    }
  
    .form-title {
      font-size: 24px;
    }
  
    .additional-options {
      flex-direction: column;
      align-items: center;
    }
  
    .additional-options button {
      margin-top: 10px;
    }
  }
  </style>
  
  