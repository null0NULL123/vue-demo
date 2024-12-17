<template>
    <div class="zhihu-like-container">
      <header class="header">
        <el-input
          v-model="searchTitle"
          placeholder="搜索帖子"
          prefix-icon="el-icon-search"
          @keyup.enter="searchArticles"
          class="search-input"
          clearable
        >
          <template #append>
            <el-button @click="searchArticles">搜索</el-button>
          </template>
        </el-input>
      </header>
  
      <main class="main-content">
        <aside class="sidebar">
          <h3>热榜</h3>
          <ul class="hot-search-list">
            <li v-for="(item, index) in hotSearchList" :key="index" @click="setSearchTitle(item)">
              <span class="hot-search-rank">{{ index + 1 }}</span>
              {{ item }}
            </li>
          </ul>
        </aside>
  
        <section class="article-list">
          <el-card v-for="article in filteredArticles" :key="article.id" class="article-card">
            <template #header>
              <div class="article-header">
                <h2 @click="showArticleDetail(article.id)">{{ article.title }}</h2>
                <el-tag size="small">{{ article.categoryName }}</el-tag>
              </div>
            </template>
            <div class="article-content">
              <p v-html="sanitizeAndTruncateContent(article.content)"></p>
            </div>
            <div class="article-footer">
              <span>{{ formatDate(article.createTime) }}</span>
              <el-button type="text" @click="showArticleDetail(article.id)">阅读全文</el-button>
            </div>
          </el-card>
        </section>
      </main>
  
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="filteredArticles.length"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
      />
  
      <el-dialog
        v-model="dialogVisible"
        :title="currentArticle.title"
        width="70%"
        :before-close="handleClose"
        class="article-detail-dialog"
      >
        <div class="article-content" v-html="currentArticle.content"></div>
        <div v-if="currentArticle.coverImg" class="article-image">
          <img :src="currentArticle.coverImg" alt="Article cover image" />
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, watch, computed } from 'vue'
  import { ElMessage } from 'element-plus'
  import { articleCategoryListService, articleListService, articleDetailService } from '@/api/article.js'
  import  useArticlePicStore  from '@/stores/articlePic.js'
  import useUserStore from '@/stores/user.js'
  const userStore = useUserStore()
  const articlePicStore = useArticlePicStore()
  const categorys = ref([])
  const articles = ref([])
  const searchTitle = ref('')
  const categoryId = ref('')
  const pageNum = ref(1)
  const pageSize = ref(10)
  const total = ref(0)
  const dialogVisible = ref(false)
  const currentArticle = ref({})
  const avatar = ref('')
  const filteredArticles = computed(() => {
    let result = articles.value.filter(article => article.state == '已发布')
    if (!searchTitle.value) {
      return result
    }
    return result.filter(article => 
      article.title.toLowerCase().includes(searchTitle.value.toLowerCase()) ||
      article.content.toLowerCase().includes(searchTitle.value.toLowerCase())
    )
  })
  
  const articleCategoryList = async () => {
    try {
      const result = await articleCategoryListService()
      categorys.value = result.data
    } catch (error) {
      console.error('Failed to fetch categories:', error)
      ElMessage.error('获取分类列表失败')
    }
  }
  
  const articleList = async () => {
    try {
      const params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value || null,
        title: searchTitle.value || null
      }
      const result = await articleListService(params)
      
      articles.value = result.data.items
      total.value = result.data.total
      
      articles.value.forEach(article => {
        const category = categorys.value.find(c => c.id === article.categoryId)
        article.categoryName = category ? category.categoryName : '未分类'
        userStore.setInfo(article.createUser)
      })
      console.log(userStore.info)
    } catch (error) {
      console.error('Failed to fetch articles:', error)
      ElMessage.error('获取帖子列表失败')
    }
  }
  
  const searchArticles = () => {
    if (searchTitle.value) {
      // 如果有搜索词,只在前端筛选
      pageNum.value = 1
    } else {
      // 如果搜索词为空,重新从后端获取所有文章
      articleList()
    }
  }
  
  const onSizeChange = (size) => {
    pageSize.value = size
    if (!searchTitle.value) {
      articleList()
    }
  }
  
  const onCurrentChange = (num) => {
    pageNum.value = num
    if (!searchTitle.value) {
      articleList()
    }
  }
  
  const sanitizeAndTruncateContent = (content) => {
    // 使用 DOMParser 来解析 HTML
    const parser = new DOMParser();
    const doc = parser.parseFromString(content, 'text/html');
    
    // 获取纯文本内容
    const textContent = doc.body.textContent || "";
    
    // 截断内容
    const truncated = textContent.length > 100 ? textContent.slice(0, 100) + '...' : textContent;
    
    // 转义 HTML 特殊字符
    return truncated.replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&#039;');
  }
  
  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
  }
  
  const showArticleDetail = async (id) => {
    try {
      const result = await articleDetailService(id)
      console.log(result)
      currentArticle.value = result.data
      currentArticle.value.coverImg = articlePicStore.getPic(currentArticle.value.coverImg) || avatar
      dialogVisible.value = true
    } catch (error) {
      console.error('Failed to fetch article detail:', error)
      ElMessage.error('获取帖子详情失败')
    }
  }
  
  const handleClose = (done) => {
    done()
  }
  
  onMounted(() => {
    userStore.removeInfo()
    articleCategoryList()
    articleList()
  })

  watch(searchTitle, (newValue, oldValue) => {
    if (newValue === '' && oldValue !== '') {
      articleList()
    } else {
      searchArticles()
    }
  })

  const hotSearchList = ref([
    'Vue 3 新特性',
    'Element Plus 使用技巧',
    '前端性能优化',
    'TypeScript 入门指南',
    'Vite 构建工具',
    'Composition API 最佳实践',
    'Pinia 状态管理',
    'Vue Router 4 教程',
    'SSR 服务端渲染',
    'Web Components 开发'
  ])

  const setSearchTitle = (title) => {
    searchTitle.value = title
    searchArticles()
  }
  </script>
  
  <style scoped>
  .zhihu-like-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .header {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 50px;
  }
  
  .search-input {
    width: 600px;
  }
  
  .main-content {
    display: flex;
    gap: 20px;
  }
  
  .sidebar {
    width: 200px;
    margin-right: 60px;
  }
  
  .article-list {
    flex-grow: 1;
  }
  
  .article-card {
    margin-bottom: 20px;
  }
  
  .article-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .article-header h2 {
    cursor: pointer;
    color: #1a1a1a;
  }
  
  .article-header h2:hover {
    color: #0066ff;
  }
  
  .article-content {
    color: #646464;
  }
  
  .article-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;
    color: #8590a6;
  }
  .article-detail-dialog {
    .el-dialog__body {
      max-height: 70vh;
      overflow-y: auto;
    }

    .article-content {
      font-size: 16px;
      line-height: 1.6;
      color: #333;
      margin-bottom: 20px;
    }

    .article-image {
      text-align: center;
      margin-top: 20px;

      img {
        max-width: 100%;
        height: auto;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      }
    }
  }
  .hot-search-list {
    list-style-type: none;
    padding: 0;
    margin: 0;
  }

  .hot-search-list li {
    cursor: pointer;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
    transition: background-color 0.3s;
  }

  .hot-search-list li:hover {
    background-color: #f5f5f5;
  }

  .hot-search-rank {
    display: inline-block;
    width: 20px;
    height: 20px;
    line-height: 20px;
    text-align: center;
    background-color: #f0f0f0;
    color: #999;
    border-radius: 50%;
    margin-right: 8px;
  }

  .hot-search-list li:nth-child(-n+3) .hot-search-rank {
    background-color: #ff4d4f;
    color: #fff;
  }
  </style>