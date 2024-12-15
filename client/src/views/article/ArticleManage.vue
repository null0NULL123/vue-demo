<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'
import { useTokenStore } from '@/stores/token.js'
import useUserInfoStore from '@/stores/userInfo.js'
import {
    articleCategoryListService,
    articleListService,
    articleAddService,
    articleDeleteService
} from '@/api/article.js'
import useArticlePicStore from '@/stores/articlePic.js'
const articlePicStore = useArticlePicStore()
const categorys = ref([])
const articles = ref([])
const categoryId = ref('')
const state = ref('')
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(5)
const visibleDrawer = ref(false)
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})

const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()

const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}

const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

const articleCategoryList = async () => {
    let result = await articleCategoryListService()
    categorys.value = result.data
}

const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        state: state.value ? state.value : null,
    }
    let originResult = await articleListService(params)
    let result = originResult.data.items.filter(item => item.createUser == userInfoStore.info.id)
    total.value = result.length
    articles.value = result

    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId == categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}
const imgUrl = ref('')
const filename = ref('')
const uploadSuccess = async (file) => {
    const reader = new FileReader()
    filename.value = file.name
    console.log(filename.value)
    reader.onload = (e) => {
        const base64Image = e.target.result
        try {
            // 更新store中的图片信息

            // 更新显示的图片
            imgUrl.value = base64Image
            ElMessage.success('图片更新成功')
        } catch (error) {
            console.error('保存图片失败:', error)
            ElMessage.error('图片更新失败')
        }
    }
    reader.readAsDataURL(file.raw)
}

const addArticle = async (clickState) => {
    articleModel.value.state = clickState
    articleModel.value.coverImg = filename.value
    articlePicStore.setPic(filename.value,
        imgUrl.value)

    let result = await articleAddService(articleModel.value)
    ElMessage.success(result.msg ? result.msg : '添加成功')
    console.log(result)
    visibleDrawer.value = false
    articleList()
}

const deleteArticle = async (id) => {
    try {
        await ElMessageBox.confirm('确定要删除这篇文章吗？', '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        })
        await articleDeleteService(id)
        ElMessage.success('删除成功')
        articleList()
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除文章失败:', error)
            ElMessage.error('删除失败')
        }
    }
}

articleCategoryList()
articleList()
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>帖子管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true">添加帖子</el-button>
                </div>
            </div>
        </template>
        <el-form inline>
            <el-form-item label="帖子分类：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="帖子标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row.id)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        <el-drawer v-model="visibleDrawer" title="添加帖子" direction="rtl" size="50%">
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="帖子标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="帖子分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="帖子封面">
                    <el-upload class="avatar-uploader" :auto-upload="false" :show-file-list="false"
                        :on-change="uploadSuccess">
                        <img v-if="imgUrl" :src="imgUrl" class="avatar" alt="" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="帖子内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="addArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
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
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
