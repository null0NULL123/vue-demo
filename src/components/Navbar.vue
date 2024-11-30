<template>
    <div>
      <nav class="navbar">
        <h1 class="navbar-title">{{ routeTitle }}</h1>
        <div class="navbar-search">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="搜索帖子..."
            @input="searchPosts"
          />
        </div>
        <button class="post-button" @click="toPost" title="发帖">发帖</button>

        <div class="navbar-icons">
          <button @click="toHome" title="主页">
            <img src="@/assets/home-icon.jpg" alt="主页">
          </button>
          <button @click="toMessages" title="我的私信">
            <img src="@/assets/messages-icon.jpg" alt="我的私信">
          </button>
          <button @click="toContent" title="内容管理">
            <img src="../assets/content-icon.jpg" alt="内容管理">
          </button>
          <button @click="toProfile" title="个人信息">
            <img src="../assets/profile-icon.jpg" alt="个人信息">
          </button>
          <button @click="toSettings" title="设置">
            <img src="../assets/settings-icon.png" alt="设置">
          </button>
          <button @click="logout" title="登出">
            <img src="../assets/logout-icon.jpg" alt="登出">
          </button>
        </div>
      </nav>
    </div>
  </template>
  
  <script setup>
  import { ref, watch, onMounted } from 'vue';
  import { useRouter, useRoute } from 'vue-router';
  
  const routeTitle = ref('主页');
  const searchQuery = ref('');
  const router = useRouter();
  const route = useRoute();
  
  const updateRouteTitle = (routeName) => {
    const routeTitles = {
      home: '主页',
      messages: '我的私信',
      content: '内容管理',
      profile: '个人信息',
      settings: '设置'
    };
    routeTitle.value = routeTitles[routeName] || '主页';
  };
  
  watch(route, (to) => {
    updateRouteTitle(to.name);
  });
  
  onMounted(() => {
    updateRouteTitle(route.name);
  });
  
  const toHome = () => {
    router.push({ name: 'home' });
  };
  
  const toMessages = () => {
    router.push({ name: 'messages' });
  };
  
  const toContent = () => {
    router.push({ name: 'content' });
  };
  
  const toProfile = () => {
    router.push({ name: 'profile' });
  };
  
  const toSettings = () => {
    router.push({ name: 'settings' });
  };
  
  const toPost = () => {
    router.push({ name: 'post' });
  };
  
  const logout = () => {
    router.push({ name: 'login' });
  };
  
  const searchPosts = () => {
    // 触发搜索逻辑
    console.log('Searching for:', searchQuery.value);
  };
  </script>
  
  <style scoped>
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    background-color: white;
  }
  
  .navbar-title {
    color: black;
    font-size: 24px;
    font-family: 'Microsoft YaHei', sans-serif;
    margin-left: 20px; /* 调整这个值来将标题向右移动 */
    margin-right: auto;
  }
  
  .navbar-search {
    margin-left: 20px;
  }
  
  .navbar-search input {
    padding: 20px 100px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 40px;
    margin-right: 100px;
  }
  
  .navbar-icons {
    display: flex;
    align-items: center;
  }
  
  .navbar-icons button {
    background: none;
    border: none;
    cursor: pointer;
    margin-left: 10px;
    position: relative;
  }
  
  .navbar-icons img {
    width: 24px;
    height: 24px;
  }
  
  .navbar-icons button:hover img {
    filter: brightness(0.8);
  }
  
  .post-button {
    background-color: #4394ff;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 40px;
    cursor: pointer;
    margin-right: 20px; /* 调整这个值来扩大发帖按钮与其他图标的间距 */
  }
  
  .post-button:hover {
    background-color: #357ae8;
  }
  </style>