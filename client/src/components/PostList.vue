<template>
  <section class="forum-posts">
    <h2>Latest Posts</h2>
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      Loading posts...
    </div>
    <ul v-else>
      <li
        v-for="post in posts"
        :key="post.id"
        class="post-item"
        @click="$emit('select-post', post)"
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
</template>

<script setup>
defineProps({
  posts: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  categories: {
    type: Array,
    required: true
  }
})

defineEmits(['select-post'])

const getCategoryName = (categoryId) => {
  const category = props.categories.find(cat => cat.id === categoryId)
  return category ? category.name : 'Uncategorized'
}
</script>

<style scoped>
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
</style>