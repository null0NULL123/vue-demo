<template>
    <div v-if="post" class="post-detail">
      <div class="post-content">
        <h2>{{ post.title }}</h2>
        <p>{{ post.content }}</p>
        <div class="post-meta">
          <span><i class="fas fa-user"></i> {{ post.author }}</span>
          <span><i class="fas fa-calendar"></i> {{ formatDate(post.date) }}</span>
        </div>
      </div>
      <div class="comments-section">
        <h3>评论</h3>
        <ul>
          <li v-for="comment in post.comments" :key="comment.id" class="comment">
            <div class="comment-content">
              <p>{{ comment.content }}</p>
              <div class="comment-meta">
                <span><i class="fas fa-user"></i> {{ comment.author }}</span>
                <span><i class="fas fa-calendar"></i> {{ formatDate(comment.date) }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  
  const props = defineProps({
    post: {
      type: Object,
      required: true
    }
  });
  
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
  };
  </script>
  
  <style scoped>
  .post-detail {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .post-content h2 {
    color: #1a73e8;
    font-size: 24px;
    margin-bottom: 15px;
  }
  
  .post-content p {
    color: #202124;
    font-size: 16px;
    line-height: 1.6;
    margin-bottom: 20px;
  }
  
  .post-meta, .comment-meta {
    font-size: 0.9em;
    color: #5f6368;
    display: flex;
    gap: 15px;
  }
  
  .post-meta span, .comment-meta span {
    display: flex;
    align-items: center;
  }
  
  .post-meta i, .comment-meta i {
    margin-right: 5px;
  }
  
  .comments-section {
    margin-top: 30px;
  }
  
  .comments-section h3 {
    color: #1a73e8;
    font-size: 20px;
    margin-bottom: 15px;
  }
  
  .comment {
    background-color: #f8f9fa;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 15px;
  }
  
  .comment-content p {
    color: #202124;
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 10px;
  }
  </style>
  