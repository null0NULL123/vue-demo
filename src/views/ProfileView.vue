<template>
    <div class="profile-container">
        <header class="profile-header">
            <h1>个人资料</h1>
            <nav>
                <router-link to="/home" class="nav-link">返回主页</router-link>
            </nav>
        </header>

        <main class="profile-main">
            <section class="profile-info">
                <div class="avatar-container">
                    <img :src="user.avatar" alt="User Avatar" class="avatar" />
                    <button @click="changeAvatar" class="change-avatar-btn">更换头像</button>
                </div>
                <h2>{{ user.username }}</h2>
                <p>{{ user.email }}</p>
                <p>加入时间：{{ formatDate(user.joinDate) }}</p>
            </section>

            <section class="profile-stats">
                <h3>统计信息</h3>
                <ul>
                    <li>发帖数：{{ user.postCount }}</li>
                    <li>评论数：{{ user.commentCount }}</li>
                    <li>获赞数：{{ user.likeCount }}</li>
                </ul>
            </section>

            <section class="profile-settings">
                <h3>设置</h3>
                <form @submit.prevent="updateProfile">
                    <div class="form-group">
                        <label for="email">电子邮箱</label>
                        <input type="email" id="email" v-model="user.email" required />
                    </div>
                    <div class="form-group">
                        <label for="password">新密码</label>
                        <input type="password" id="password" v-model="newPassword" />
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">确认新密码</label>
                        <input type="password" id="confirmPassword" v-model="confirmPassword" />
                    </div>
                    <div class="form-group">
                        <label>
                            <input type="checkbox" v-model="user.emailNotifications" />
                            接收邮件通知
                        </label>
                    </div>
                    <button type="submit" class="update-btn">更新资料</button>
                </form>
            </section>
        </main>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/api';  // 添加 api 导入
const router = useRouter();

const user = ref({
    username: 'JohnDoe',
    email: 'johndoe@example.com',
    avatar: 'https://via.placeholder.com/150',
    joinDate: '2023-01-01',
    postCount: 15,
    commentCount: 45,
    likeCount: 120,
    emailNotifications: true
});

const newPassword = ref('');
const confirmPassword = ref('');

const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
};
// 获取用户信息
const fetchUserInfo = async () => {
    try {
        const response = await api.getUserInfo();
        user.value = { ...user.value, ...response.data };
    } catch (error) {
        console.error('获取用户信息失败:', error);
        alert('获取用户信息失败');
    }
};
// 更新更换头像方法
const changeAvatar = async () => {
    // 这里可以添加文件上传逻辑，现在仅做示例
    const newAvatarUrl = 'https://new-avatar-url.com/image.jpg';
    try {
        await api.updateAvatar(newAvatarUrl);
        user.value.avatar = newAvatarUrl;
        alert('头像更新成功');
    } catch (error) {
        console.error('更新头像失败:', error);
        alert('更新头像失败');
    }
};

// 更新更新资料方法
const updateProfile = async () => {
    try {
        // 如果有新密码，先验证并更新密码
        if (newPassword.value) {
            if (newPassword.value !== confirmPassword.value) {
                alert('新密码和确认密码不匹配');
                return;
            }
            await api.updatePassword({
                newPassword: newPassword.value
            });
        }

        // 更新用户信息
        await api.updateUser({
            email: user.value.email,
            emailNotifications: user.value.emailNotifications
        });

        alert('资料更新成功');
        newPassword.value = '';
        confirmPassword.value = '';
    } catch (error) {
        console.error('更新资料失败:', error);
        alert('更新资料失败');
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
    fetchUserInfo();  // 添加获取用户信息的调用
});
</script>

<style scoped>
.profile-container {
    font-family: 'Roboto', sans-serif;
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f0f2f5;
    min-height: 100vh;
}

.profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.profile-header h1 {
    font-size: 28px;
    color: #1a73e8;
    font-weight: 700;
}

.nav-link {
    text-decoration: none;
    color: #5f6368;
    font-weight: 500;
    transition: color 0.3s ease;
}

.nav-link:hover {
    color: #1a73e8;
}

.profile-main {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
}

.profile-info,
.profile-stats,
.profile-settings {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.profile-info {
    grid-column: 1 / -1;
    text-align: center;
}

.avatar-container {
    position: relative;
    display: inline-block;
}

.avatar {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    object-fit: cover;
}

.change-avatar-btn {
    position: absolute;
    bottom: 0;
    right: 0;
    background-color: #1a73e8;
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 12px;
}

.profile-info h2 {
    margin-top: 10px;
    color: #202124;
}

.profile-info p {
    color: #5f6368;
}

.profile-stats ul {
    list-style-type: none;
    padding: 0;
}

.profile-stats li {
    margin-bottom: 10px;
    color: #5f6368;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    color: #5f6368;
}

.form-group input[type="email"],
.form-group input[type="password"] {
    width: 100%;
    padding: 8px;
    border: 1px solid #d2d6dc;
    border-radius: 5px;
}

.update-btn {
    background-color: #1a73e8;
    color: white;
    border: none;
    padding: 10px 15px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
}

.update-btn:hover {
    background-color: #1557b0;
}
</style>