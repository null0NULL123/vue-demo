<script setup>
import {
    Management,
    Promotion,
    Message,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    HomeFilled,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import {userInfoService} from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
const getUserInfo = async()=>{
    //调用接口
    let result = await userInfoService();
    //数据存储到pinia中
    userInfoStore.setInfo(result.data);
}

getUserInfo();
//条目被点击后,调用的函数
import {useRouter} from 'vue-router'
const router = useRouter();
import {ElMessage,ElMessageBox} from 'element-plus'
const handleCommand = (command)=>{
    //判断指令
    if(command === 'logout'){
        //退出登录
        ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //退出登录
            //1.清空pinia中存储的token以及个人信息
            tokenStore.removeToken()
            userInfoStore.removeInfo()

            //2.跳转到登录页面
            router.push('/login')
            ElMessage({
                type: 'success',
                message: '退出登录成功',
            })
            
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了退出登录',
            })
        })
    }else{
        //路由
        router.push('/user/'+command)
    }
}
</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div class="content-width">
                    <div><strong>{{ userInfoStore.info.nickname }}</strong></div>
                    <el-menu
                        mode="horizontal"
                        background-color="#ffffff"
                        text-color="#303133"
                        active-text-color="#409EFF"
                        router
                        class="top-menu"
                        style="flex: 1; justify-content: flex-end;">
                        <el-menu-item index="/article/category"><el-icon><HomeFilled /></el-icon>帖子首页</el-menu-item>
                        <el-menu-item index="/article/manage"><el-icon><EditPen /></el-icon>我的帖子</el-menu-item>
                        <el-menu-item index="/massage/list"><el-icon><Message /></el-icon>我的私信</el-menu-item> 
                    </el-menu>
                    <el-dropdown @command="handleCommand">
                        <div class="el-dropdown__box">
                            <el-avatar :size="36" :src="userInfoStore.info.avatar || avatar" />
                            <el-icon><CaretBottom /></el-icon>
                        </div>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
                                <el-dropdown-item command="resetPassword">重置密码</el-dropdown-item>
                                <el-dropdown-item command="avatar">更换头像</el-dropdown-item>
                                <el-dropdown-item command="logout">退出登录</el-dropdown-item>

                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                    <!-- 添加用户头像下拉菜单 -->
                </div>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <div class="content-width">
                    <router-view></router-view>
                </div>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>
                <div class="content-width">
                    Forum ©2024 Created by null0NULL123
                </div>
            </el-footer>
        </el-container>
    </el-container>
</template>


<style lang="scss" scoped>
.layout-container {
    min-height: 100vh;
    background-color: #f6f8fa;

    .content-width {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .el-aside {
        background: #ffffff;
        box-shadow: 0 4px 12px rgba(0,0,0,.05);
        transition: all 0.3s ease;
        width: 240px;

        &__logo {
            height: 80px;
            background: url('@/assets/login_title.jpg') no-repeat center;
            background-size: 100px;
            margin: 20px 0;
            opacity: 0.9;
            transition: opacity 0.3s;

            &:hover {
                opacity: 1;
            }
        }

        .el-menu {
            border-right: none;
            padding: 10px;
            
            .el-menu-item, .el-sub-menu__title {
                height: 50px;
                line-height: 50px;
                margin: 8px 0;
                border-radius: 8px;
                
                &:hover {
                    background-color: #f5f7fa !important;
                }
                
                &.is-active {
                    background: linear-gradient(90deg, rgba(64,158,255,0.1) 0%, rgba(64,158,255,0) 100%) !important;
                    &::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 50%;
                        transform: translateY(-50%);
                        width: 4px;
                        height: 20px;
                        background-color: #409EFF;
                        border-radius: 0 4px 4px 0;
                    }
                }
            }
        }
    }

    .el-header {
        background-color: #ffffff;
        box-shadow: 0 2px 10px rgba(0,0,0,.05);
        padding: 0;

        .content-width {
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        strong {
            color: #2c3e50;
            font-size: 16px;
            font-weight: 600;
        }

        .el-dropdown__box {
            display: flex;
            align-items: center;
            padding: 6px 12px;
            border-radius: 30px;
            transition: all 0.3s ease;
            background-color: #f8f9fa;

            &:hover {
                background-color: #eef2f7;
                transform: translateY(-1px);
            }

            .el-avatar {
                width: 36px;
                height: 36px;
                border: 2px solid #fff;
                box-shadow: 0 2px 8px rgba(0,0,0,.1);
                transition: transform 0.3s;

                &:hover {
                    transform: scale(1.05);
                }
            }

            .el-icon {
                color: #606266;
                margin-left: 8px;
                font-size: 12px;
            }
        }
    }

    .el-main {
        padding: 0;
        min-height: calc(100vh - 120px);

        .content-width {
            padding-top: 20px;
            padding-bottom: 20px;
        }
    }

    .el-footer {
        padding: 0;
        height: 60px;
        background-color: #ffffff;

        .content-width {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #909399;
        }
    }
    
}

@media screen and (max-width: 1200px) {
    .layout-container {
        .content-width {
            padding: 0 15px;
        }
    }
}

@media screen and (max-width: 768px) {
    .layout-container {
        .content-width {
            padding: 0 10px;
        }
    }
}
</style>