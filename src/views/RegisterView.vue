<template>
    <div class="container">
        <form @submit.prevent="handleRegister" class="form">
            <h1 class="form-title">注册</h1>
            <div class="form-group">
                <input type="text" id="username" v-model="username" placeholder="用户名" required />
            </div>
            <div class="form-group">
                <input type="password" id="password" v-model="password" placeholder="密码" required />
            </div>
            <div class="form-group">
                <input type="password" id="confirmPassword" v-model="confirmPassword" placeholder="确认密码" required />
            </div>
            <button type="submit">注册</button>
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

const handleRegister = async () => {
    errorMessage.value = '';
    try {
        console.log(username.value, password.value);
        const response = await apiClient.registerUser({ "username": username.value, "password": password.value });
        if (response.data.code !== 0) {
            errorMessage.value = response.data.msg;
            return;
        }
        console.log('登录成功:', response.data);
        router.push('/home');
    } catch (error) {
        console.error('注册失败:', error);
        return;
    }
};
</script>

<style scoped>

.container {
    background-image: url('../assets/register-background.img');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 100vw;
}

.form {
    background: rgba(255, 255, 255, 0.8);
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
    box-sizing: border-box;
}

.form-group {
    margin-bottom: 20px;
}

.form-group input {
    width: 100%;
    padding: 12px;
    box-sizing: border-box;
}

.form-title {
    margin-bottom: 40px;
    text-align: center;
}

button[type="submit"] {
    width: 100%;
    padding: 15px;
    background-color: #4b4c5a;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

button[type="submit"]:hover {
    background-color: #5a63a3;
}
</style>