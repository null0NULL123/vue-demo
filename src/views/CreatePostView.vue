<template>
    <div class="create-post-container">
      <header class="create-post-header">
        <h1>发布新帖子</h1>
        <nav>
          <button @click="navigateTo('home')" class="nav-button">返回主页</button>
        </nav>
      </header>
  
      <main class="create-post-main">
        <form @submit.prevent="submitPost" class="create-post-form">
          <div class="form-group">
            <label for="title">标题</label>
            <input
              type="text"
              id="title"
              v-model="title"
              required
              placeholder="请输入帖子标题"
            />
          </div>
  
          <div class="form-group">
            <label for="category">分类</label>
            <select id="category" v-model="categoryId" required>
              <option value="">请选择分类</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>
  
          <div class="form-group">
            <label for="content">内容</label>
            <textarea
              id="content"
              v-model="content"
              required
              placeholder="请输入帖子内容"
              rows="10"
            ></textarea>
          </div>
  
          <button type="submit" class="submit-button" :disabled="isSubmitting">
            {{ isSubmitting ? '发布中...' : '发布帖子' }}
          </button>
        </form>
      </main>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  const title = ref('');
  const content = ref('');
  const categoryId = ref('');
  const isSubmitting = ref(false);
  
  const categories = ref([
    { id: 1, name: '综合讨论' },
    { id: 2, name: 'Vue.js' },
    { id: 3, name: 'React' },
    { id: 4, name: 'Angular' },
  ]);
  
  const navigateTo = (route) => {
    router.push({ name: route });
  };
  
  const submitPost = async () => {
    if (isSubmitting.value) return;
  
    isSubmitting.value = true;
  
    try {
      // 这里应该调用API来提交帖子
      // const response = await api.createPost({ title: title.value, content: content.value, categoryId: categoryId.value });
      
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 1000));
  
      // 提交成功后重置表单
      title.value = '';
      content.value = '';
      categoryId.value = '';
  
      // 显示成功消息
      alert('帖子发布成功！');
  
      // 导航到主页或帖子列表页
      router.push({ name: 'home' });
    } catch (error) {
      console.error('发布帖子失败:', error);
      alert('发布帖子失败，请稍后重试。');
    } finally {
      isSubmitting.value = false;
    }
  };
  
  const checkToken = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      router.push('/login');
    }
  };
  
  onMounted(() => {
    checkToken();
  });
  </script>
  
  <style scoped>
  .create-post-container {
    font-family: 'Roboto', sans-serif;
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f0f2f5;
    min-height: 100vh;
  }
  
  .create-post-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .create-post-header h1 {
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
  }
  
  .nav-button:hover {
    color: #1a73e8;
  }
  
  .create-post-main {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .create-post-form {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
  }
  
  .form-group label {
    margin-bottom: 5px;
    color: #5f6368;
    font-weight: 500;
  }
  
  .form-group input,
  .form-group select,
  .form-group textarea {
    padding: 10px;
    border: 1px solid #d2d6dc;
    border-radius: 5px;
    font-size: 16px;
  }
  
  .form-group textarea {
    resize: vertical;
  }
  
  .submit-button {
    background-color: #1a73e8;
    color: white;
    border: none;
    padding: 12px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    font-weight: 500;
    transition: background-color 0.3s ease;
  }
  
  .submit-button:hover:not(:disabled) {
    background-color: #1557b0;
  }
  
  .submit-button:disabled {
    background-color: #a0aec0;
    cursor: not-allowed;
  }
  </style>
  
  