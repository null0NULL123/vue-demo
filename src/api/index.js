import axios from 'axios';
const apiClient = axios.create({
    baseURL: '/api',
    withCredentials: true,
    timeout: 30000,
});
import {useTokenStore} from '@/stores/token.js'
apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.code === 'ERR_NETWORK') {
        console.log(error.message);
    } else if (error.response && error.response.status === 401) {
        const tokenStore = useTokenStore();
        tokenStore.removeToken();
        window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);


apiClient.interceptors.request.use(
  (config) => {
    const tokenStore = useTokenStore();
    if(tokenStore.token){
        config.headers.Authorization = tokenStore.token
    }
    console.log('request', config);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default {
    async getCategoryList(params) {
        console.log(params);
        return apiClient.get('/category');
    },
    async addCategory(category) {
        return apiClient.post('/category', category);
    },
    async updateCategory(category) {
        return apiClient.put('/category', category);
    },
    getCategoryDetail(id) {
        return apiClient.get(`/category/detail?id=${id}`);
    },
    deleteCategory(id) {
        return apiClient.delete(`/category?id=${id}`);
    },
    registerUser(user) {
        const params = new URLSearchParams();
        params.append('username', user.username);
        params.append('password', user.password);
        return apiClient.post('/user/register', params, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
    },
    loginUser(user) {
        const params = new URLSearchParams();
        params.append('username', user.username);
        params.append('password', user.password);
    
        return apiClient.post('/user/login', params, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
    },
    getUserInfo() {
        return apiClient.get('/user/userInfo');
    },
    updateUser(user) {
        return apiClient.put('/user/update', user);
    },
    updateAvatar(avatarUrl) {
        return apiClient.patch(`/user/updateAvatar?avatarUrl=${avatarUrl}`);
    },
    updatePassword(passwords) {
        return apiClient.patch('/user/updatePwd', passwords);
    },
    getArticleList(params) {
        const categoryId = params.categoryId || '';
        const state = params.state || '';
        return apiClient.get(`/article?pageNum=${params.pageNum}&pageSize=${params.pageSize}&categoryId=${categoryId}&state=${state}`);
    },
    getArticleDetail(id) {
        return apiClient.get(`/article/detail?id=${id}`);
    },
    addArticle(article) {
        return apiClient.post('/article', article);
    },
    updateArticle(article) {
        return apiClient.put('/article', article);
    },
    deleteArticle(id) {
        return apiClient.delete(`/article?id=${id}`);
    },
    sendMessage(message) {
        return apiClient.post('/message', message);
    },
    getMessageHistory() {
        return apiClient.get('/message');
    },
    getUserMessageList() {
        return apiClient.get('/message/userlist');
    }
};