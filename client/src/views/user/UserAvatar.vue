<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
import { ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo.js'

const userInfoStore = useUserInfoStore()
const uploadRef = ref()

// 从localStorage获取头像
const getStoredAvatar = () => {
  return userInfoStore.info.userPic || avatar
}

// 用户头像地址
const imgUrl = ref(getStoredAvatar())

// 图片上传成功的回调函数
const uploadSuccess = (file) => {
  // 创建文件读取器
  const reader = new FileReader()
  
  reader.onload = (e) => {
    // 获取Base64格式的图片数据
    const base64Image = e.target.result
    
    try {
      // 保存到localStorage
      localStorage.setItem('userAvatar', base64Image)
      
      // 更新显示的图片
      imgUrl.value = base64Image
      
      // 更新store中的用户头像
      userInfoStore.info.userPic = base64Image
      
      ElMessage.success('头像更新成功')
    } catch (error) {
      console.error('保存头像失败:', error)
      ElMessage.error('头像更新失败')
    }
  }
  
  // 读取文件内容
  reader.readAsDataURL(file.raw)
}

// 头像更新
const updateAvatar = async () => {
  try {
    // 直接触发文件选择
    uploadRef.value.$el.querySelector('input').click()
  } catch (error) {
    console.error('更新头像失败:', error)
    ElMessage.error('更新头像失败')
  }
}

// 文件上传前的验证
const beforeUpload = (file) => {
  // 验证文件类型
  const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  
  // 验证文件大小（这里限制为2MB）
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  
  return true
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>更换头像</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-upload 
          ref="uploadRef" 
          class="avatar-uploader" 
          :show-file-list="false"
          :auto-upload="false"
          :before-upload="beforeUpload"
          :on-change="uploadSuccess">
          <img v-if="imgUrl" :src="imgUrl" class="avatar" />
          <img v-else src="avatar" width="278" />
        </el-upload>
        <br />
        <el-button 
          type="primary" 
          :icon="Plus" 
          size="large"
          @click="updateAvatar">
          选择图片
        </el-button>
      </el-col>
    </el-row>
  </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
      object-fit: cover; // 确保图片填充整个容器
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
  }
}

// 添加一些响应式样式
@media screen and (max-width: 768px) {
  .avatar-uploader {
    :deep() {
      .avatar {
        width: 200px;
        height: 200px;
      }
      
      .el-icon.avatar-uploader-icon {
        width: 200px;
        height: 200px;
      }
    }
  }
}
</style>