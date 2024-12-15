<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import avatar from '@/assets/default.png'
import { ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo.js'
import useUserPicStore from '@/stores/userPic.js'

const userInfoStore = useUserInfoStore()
const userPicStore = useUserPicStore()
const uploadRef = ref()

// 用户头像地址
const imgUrl = ref('')

// 图片上传成功的回调函数
const uploadSuccess = (file) => {
    const reader = new FileReader()

    reader.onload = (e) => {
        const base64Image = e.target.result

        try {
            // 更新store中的头像信息
            userPicStore.setPic(userInfoStore.info.id, 
                base64Image)

        // 更新显示的图片
        imgUrl.value = userPicStore.getPic(userInfoStore.info.id)
        console.log(imgUrl.value)
        ElMessage.success('头像更新成功')
    } catch (error) {
        console.error('保存头像失败:', error)
        ElMessage.error('头像更新失败')
    }
}

reader.readAsDataURL(file.raw)
}

// 头像更新
const updateAvatar = async () => {
    try {
        uploadRef.value.$el.querySelector('input').click()
    } catch (error) {
        console.error('更新头像失败:', error)
        ElMessage.error('更新头像失败')
    }
}

// 文件上传前的验证
const beforeUpload = (file) => {
    const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type)
    if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
    }

    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
        ElMessage.error('图片大小不能超过2MB!')
        return false
    }

    return true
}

// 组件挂载时从store获取最新头像
onMounted(() => {
    const currentUserId = userInfoStore.info.id
    const userPic = userPicStore.getPic(currentUserId)
    if (userPic) {
        imgUrl.value = userPic
    }
})
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
                <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false" :auto-upload="false"
                    :before-upload="beforeUpload" :on-change="uploadSuccess">
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else :src="avatar" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large" @click="updateAvatar">
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
            object-fit: cover;
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
    }
}
</style>