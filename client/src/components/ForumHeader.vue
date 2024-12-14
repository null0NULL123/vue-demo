<template>
  <header class="forum-header">
    <h1>Vue Forum</h1>
    <nav>
      <button @click="navigateTo('home')" class="nav-button">主页</button>
      <button @click="navigateTo('my-posts')" class="nav-button">我的帖子</button>
      <button @click="navigateTo('create-post')" class="nav-button">发布帖子</button>
      <button @click="navigateTo('messages')" class="nav-button">我的私信</button>
      <button @click="navigateTo('profile')" class="nav-button">个人资料</button>
      <button @click="logout" class="nav-button">退出</button>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (route) => {
  router.push({ name: route })
}

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.forum-header h1 {
  font-size: 28px;
  color: #1a73e8;
  font-weight: 700;
}

.nav-button {
  background: none;
  border: none;
  color: #5f6368;
  font-weight: 500;
  cursor: pointer;
  transition: color 0.3s ease;
  padding: 5px 10px;
  margin-left: 10px;
}

.nav-button:hover {
  color: #1a73e8;
}
</style>