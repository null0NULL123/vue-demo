<template>
    <div>
      <Navbar />
      <main>
        <div class="posts-container">
          <div v-for="post in posts" :key="post.id" class="post">
            <h2 class="post-title">{{ post.title }}</h2>
            <p class="post-content">{{ post.content }}</p>
            <p class="post-author">作者: {{ post.author }}</p>
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import Navbar from '@/components/Navbar.vue';
  
  // 示例帖子数据
  const posts = ref([
    { id: 1, title: '帖子标题1', content: '这是帖子内容1', author: '作者1' },
    { id: 2, title: '帖子标题2', content: '这是帖子内容2', author: '作者2' },
    { id: 3, title: '帖子标题3', content: '这是帖子内容3', author: '作者3' }
  ]);
  </script>
  
  <style scoped>
  .posts-container {
    padding: 20px;
  }
  
  .post {
    background: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  
  .post-title {
    font-size: 24px;
    margin-bottom: 10px;
  }
  
  .post-content {
    font-size: 16px;
    margin-bottom: 10px;
  }
  
  .post-author {
    font-size: 14px;
    color: #666;
  }
  </style>