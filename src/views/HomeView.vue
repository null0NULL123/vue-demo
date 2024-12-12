<template>
  <div class="forum-container">
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

    <main class="forum-main">
      <aside class="forum-categories">
        <h2>Categories</h2>
        <ul>
          <li v-for="category in categories" :key="category.id">
            <a
              href="#"
              @click.prevent="filterByCategory(category.id)"
              :class="{ active: selectedCategory === category.id }"
            >
              {{ category.name }}
            </a>
          </li>
        </ul>
      </aside>

      <section class="forum-posts" v-if="!selectedPost">
        <h2>Latest Posts</h2>
        <div v-if="loading" class="loading">
          <div class="spinner"></div>
          Loading posts...
        </div>
        <ul v-else>
          <li
            v-for="post in filteredPosts"
            :key="post.id"
            class="post-item"
            @click="selectPost(post)"
          >
            <h3>{{ post.title }}</h3>
            <p>{{ post.excerpt }}</p>
            <div class="post-meta">
              <span><i class="fas fa-user"></i> {{ post.author }}</span>
              <span><i class="fas fa-comments"></i> {{ post.comments.length }}</span>
              <span><i class="fas fa-tag"></i> {{ getCategoryName(post.categoryId) }}</span>
            </div>
          </li>
        </ul>
      </section>

      <section v-else class="forum-post-detail">
        <button @click="closePostDetail" class="back-button">
          <i class="fas fa-arrow-left"></i> 返回帖子列表
        </button>
        <PostDetail :post="selectedPost" />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '@/api';
import PostDetail from '../components/PostDetail.vue';

const router = useRouter();

const categories = ref([]);
const posts = ref([]);
const loading = ref(false);
const selectedCategory = ref(null);
const selectedPost = ref(null);

const getCategoryName = (categoryId) => {
  const category = categories.value.find(cat => cat.id === categoryId);
  return category ? category.name : 'Uncategorized';
};

const checkToken = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    router.push('/login');
  }
};

const fetchCategories = async () => {
  try {
    const response = await apiClient.getCategoryList({ pageNum: 1, pageSize: 10 });
    categories.value = response.data;
  } catch (error) {
    console.error('Failed to fetch categories:', error);
  }
};

const fetchPosts = async () => {
  loading.value = true;
  try {
    const response = await apiClient.getArticleList();
    posts.value = response.data;
  } catch (error) {
    console.error('Failed to fetch posts:', error);
  } finally {
    loading.value = false;
  }
};

const filterByCategory = (categoryId) => {
  selectedCategory.value = categoryId;
};

const filteredPosts = computed(() => {
  if (selectedCategory.value) {
    return posts.value.filter(post => post.categoryId === selectedCategory.value);
  }
  return posts.value;
});

const selectPost = (post) => {
  selectedPost.value = post;
};

const closePostDetail = () => {
  selectedPost.value = null;
};

const navigateTo = (route) => {
  router.push({ name: route });
};

const logout = () => {
  localStorage.removeItem('token');
  router.push('/login');
};

onMounted(() => {
  checkToken();
  fetchCategories();
  fetchPosts();
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap');
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css');

.forum-container {
  font-family: 'Roboto', sans-serif;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

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

.forum-main {
  display: flex;
  gap: 20px;
}

.forum-categories {
  flex: 1;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.forum-categories h2 {
  color: #1a73e8;
  margin-bottom: 15px;
}

.forum-categories ul {
  list-style-type: none;
  padding: 0;
}

.forum-categories li {
  margin-bottom: 10px;
}

.forum-categories a {
  text-decoration: none;
  color: #5f6368;
  transition: color 0.3s ease;
  display: block;
  padding: 8px 12px;
  border-radius: 5px;
}

.forum-categories a:hover,
.forum-categories a.active {
  color: #1a73e8;
  background-color: #e8f0fe;
}

.forum-posts {
  flex: 3;
}

.forum-posts h2 {
  color: #1a73e8;
  margin-bottom: 15px;
}

.post-item {
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.post-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.post-item h3 {
  margin-top: 0;
  color: #202124;
  font-size: 18px;
}

.post-item p {
  color: #5f6368;
  font-size: 14px;
  line-height: 1.5;
}

.post-meta {
  font-size: 0.9em;
  color: #80868b;
  margin-top: 15px;
  display: flex;
  justify-content: space-between;
}

.post-meta span {
  display: flex;
  align-items: center;
}

.post-meta i {
  margin-right: 5px;
}

.loading {
  text-align: center;
  color: #5f6368;
  font-style: italic;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #1a73e8;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.forum-post-detail {
  flex: 3;
}

.back-button {
  background-color: #1a73e8;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  margin-bottom: 20px;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #1557b0;
}

.back-button i {
  margin-right: 5px;
}
</style>