<template>
    <div class="container">

        <form @submit.prevent="handleSubmit" class="login-form">
            <h1 class="form-title">论坛入口</h1>
            <div class="form-group">
                <input type="text" id="username" v-model="username" placeholder="用户名" required />
            </div>
            <div class="form-group">
                <input type="password" id="password" v-model="password" placeholder="密码" required />
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
const loading = ref(false);
const errorMessage = ref('');
const router = useRouter();
const token = ref('');
const handleSubmit = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    console.log(username.value,password.value);

    const response = await apiClient.loginUser({"username":username.value,"password":password.value });
    if (response.data.code !== 0) {
      errorMessage.value = response.data.message;
      return;
    }
    console.log('登录成功:', response.data);
    token.value = response.data.data;
    console.log(token);
    router.push('/home'); // 登录成功后跳转到主页
  } catch (error) {
    console.error('登录失败:', error);
  } finally {
    loading.value = false;
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
    background-image: url('../assets/login-background.jpg');
    /* 设置背景图片 */
    background-size: cover;
    /* 使背景图片覆盖整个容器 */
    background-position: center;
    /* 居中对齐背景图片 */
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
}

.login-form {
    background: rgba(255, 255, 255, 0.8);
    padding: 40px;
    /* 增加内边距 */
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 400px;
    /* 增加表单宽度 */
}

.form-group {
    margin-bottom: 20px;
    /* 增加底部外边距 */
}

.form-group label {
    display: block;
    margin-bottom: 10px;
    /* 增加底部外边距 */
}

.form-group input {
    width: 100%;
    padding: 12px;
    /* 增加内边距 */
    box-sizing: border-box;
}

.form-title {
    /* 增加底部外边距 */
    margin-bottom: 40px;
    /* 设置字体居中 */
    text-align: center;
}

button[type="submit"] {
    width: 100%;
    padding: 15px;
    /* 增加内边距 */
    background-color: #4b4c5a;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

button[type="submit"]:hover {
    background-color: #5a63a3;
}

.additional-options {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
    /* 增加顶部外边距 */
}

.additional-options button {
    background: none;
    border: none;
    cursor: pointer;
    color: #5a63a3;
    text-decoration: underline;
}

.additional-options button:hover {
    color: #001eff;
}


</style>