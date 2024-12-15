<script setup>
import { ref, computed, onMounted } from 'vue'
import { messageListService, messageGetService, messageSendService } from '@/api/message.js'
import { getUserInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import useUserPicStore from '@/stores/userPic.js'
import useUserStore from '@/stores/user.js'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
const userStore = useUserStore()
const userInfoStore = useUserInfoStore()
const userPicStore = useUserPicStore()

// 当前用户信息
const currentUser = {
    id: userInfoStore.info.id,
    name: userInfoStore.info.nickname,
    avatar: userPicStore.getPic(userInfoStore.info.id)
}

// 联系人列表
const contacts = ref([])
// 所有聊天记录
const allMessages = ref({})
const allUsers = ref({})
const getUsers = async () => {
    console.log(userStore.info);
    for(const i of userStore.info){
        const data = {
            id: i
        }
        const result = await getUserInfoService(data)
        console.log(result);
        allUsers.value[result.data] = i
    }
    console.log(allUsers);
}
// 获取联系人列表
const getContactList = async () => {
    try {
        const data = {
            sender_id: currentUser.id
        }
        const result = await messageListService(data)
        for(let i = 0; i < result.data.data.length; i++){
            result.data.data[i].avatar = userPicStore.getPic(result.data.data[i].id) || ''
        }
        console.log(result);
        contacts.value = result.data.data
    } catch (error) {
        console.error('获取联系人列表失败:', error)
    }
}
const currentMessages = ref([]) 
// 获取与特定用户的聊天记录
const getChatHistory = async (userId) => {
    try {
        const data = {
            recipient_id: userId,
            sender_id: currentUser.id
        }
        console.log(data);
        const result = await messageGetService(data)
        console.log('获取到的聊天记录:', result);
        // 确保 result.data 是数组
        const messageList = Array.isArray(result.data) ? result.data : []

        // 初始化当前用户的消息列表
        if (!allMessages.value[userId]) {
            allMessages.value[userId] = []
        }

        // 格式化并存储消息
        allMessages.value[userId] = messageList.map(msg => ({
            id: msg.id,
            sender_id: msg.sender_id,
            recepient_id: msg.recepient_id,
            content: msg.content,
            timestamp: msg.timestamp
        }))
        console.log("与特定用户的聊天记录:",allMessages.value[userId]);
        return allMessages.value[userId].reverse()    
    } catch (error) {
        console.error('获取聊天记录失败:', error)
    }
}

// 当前选中的联系人
const selectedContact = ref({
    id: null,
    name: null,
    avatar: null
})

// 监听选中联系人变化
const handleContactSelect = async (contact) => {
    console.log(contact);
    selectedContact.value = {
        id: contact.id,
        name: contacts.value.find(c => c === contact)?.username,
        avatar: contacts.value.find(c => c === contact)?.avatar
    }
    console.log(selectedContact.value);
    if (contact) {
        const messages = await getChatHistory(contact)
        currentMessages.value = messages || []
    }
}

// 输入框内容
const inputMessage = ref('')

// // TODO: 计算当前聊天记录
// const currentMessages = computed(() => {
//     console.log("当前聊天记录:",allMessages.value[selectedContact.id]);

//     if (!selectedContact.id) return []
//     return allMessages.value[selectedContact.id] || []
// })

// 发送消息
const sendMessage = async () => {
    console.log(selectedContact.value.id);
    if (!inputMessage.value.trim() || !selectedContact.value.id) return

    try {

        const newMessage = {
            sender_id: currentUser.id,
            content: inputMessage.value,
            timestamp: new Date().toISOString(),
            recipient_id: selectedContact.value.id,

        }
        console.log(newMessage);
        let result = await messageSendService(newMessage)
        console.log(result);
        // 更新本地消息列表
        if (!allMessages.value[selectedContact.value.id]) {
            allMessages.value[selectedContact.value.id] = []
        }
        allMessages.value[selectedContact.value.id].push(newMessage)


        inputMessage.value = ''
    } catch (error) {
        console.error('发送消息失败:', error)
    }
}

// 组件挂载时获取联系人列表
onMounted(() => {
    getContactList()
    getUsers()
})

// 添加搜索相关的响应式变量
const searchKeyword = ref('')
const searchResults = ref([])
const showSearchDialog = ref(false)
const searchLoading = ref(false)

// 搜索用户方法
const searchUsers = async () => {
    if (!searchKeyword.value.trim()) {
        ElMessage.warning('请输入搜索关键词')
        return
    }
    
    try {
        searchLoading.value = true
        const data = {
            sender_id: currentUser.id,
            recipient_id: allUsers.value[searchKeyword.value],
            content: "你好",
            timestamp: new Date().toISOString()
        }
        const result = await messageSendService(data)
        // 过滤掉当前用户自己
        
    } catch (error) {
        console.error('搜索用户失败:', error)
        ElMessage.error('搜索失败')
    } finally {
        searchLoading.value = false
    }
}

// 选择用户开始聊天
const startChat = (user) => {
    showSearchDialog.value = false
    handleContactSelect({
        id: user.id,
        username: user.username,
        avatar: userPicStore.getPic(user.id)
    })
}
</script>
<template>
    <el-card class="page-container">

        <el-container class="massage-container">
            <!-- 联系人列表 -->
            <el-aside width="300px" class="contact-list">
                <!-- 添加搜索按钮 -->
                <div class="search-header">
                    <el-button 
                        type="primary" 
                        :icon="Search"
                        @click="showSearchDialog = true"
                    >
                        查找用户
                    </el-button>
                </div>

                <div class="contacts">
                    <div v-for="contact in contacts" :key="contact"
                        :class="['contact-item', { active: selectedContact?.id === contact.id }]"
                        @click="handleContactSelect(contact.id)">
                        <el-avatar :size="40" :src="contact.avatar" />
                        <div class="contact-info">
                            <div class="contact-name">{{ contact.username }}</div>
                            <div class="last-message">{{ contact.lastMessage }}</div>
                        </div>
                        <div class="contact-time">{{ contact.lastTime }}</div>
                    </div>
                </div>
            </el-aside>

            <!-- 聊天窗口 -->
            <el-container class="chat-container" v-if="selectedContact">
                <el-header class="chat-header" height="60px">
                    <span>{{ selectedContact.name }}</span>
                </el-header>

                <el-main class="message-container">
                    <div v-for="msg in currentMessages" :key="msg.id"
                        :class="['message-item', msg.sender_id === currentUser.id ? 'sent' : 'received']">

                        <div class="message-content">
                            <div class="message-bubble">{{ msg.content }}</div>
                            <div class="message-time">{{ msg.timestamp }}</div>
                        </div>
                    </div>
                </el-main>

                <el-footer class="input-area" height="auto">
                    <div class="input-wrapper">
                        <el-input v-model="inputMessage" type="textarea" :rows="3" placeholder="请输入消息"
                            @keyup.enter.native.prevent="sendMessage" />
                        <div class="button-wrapper">
                            <el-button type="primary" class="send-button" @click="sendMessage">
                                发送
                            </el-button>
                        </div>
                    </div>
                </el-footer>
            </el-container>

            <!-- 未选择联系人时的提示 -->
            <el-main v-else class="no-contact">
                <el-empty description="请选择联系人开始聊天" />
            </el-main>
        </el-container>

        <!-- 添加搜索对话框 -->
        <el-dialog
            v-model="showSearchDialog"
            title="搜索用户"
            width="500px"
        >
            <div class="search-container">
                <el-input
                    v-model="searchKeyword"
                    placeholder="输入用户名搜索"
                    @keyup.enter="searchUsers"
                >
                    <template #append>
                        <el-button :icon="Search" @click="searchUsers" />
                    </template>
                </el-input>

                <div class="search-results" v-loading="searchLoading">
                    <div 
                        v-for="user in searchResults" 
                        :key="user.id"
                        class="search-item"
                        @click="startChat(user)"
                    >
                        <el-avatar 
                            :size="40" 
                            :src="userPicStore.getPic(user.id)" 
                        />
                        <div class="user-info">
                            <div class="username">{{ user.username }}</div>
                            <div class="email">{{ user.email }}</div>
                        </div>
                    </div>
                    <el-empty 
                        v-if="!searchLoading && searchResults.length === 0"
                        description="暂无搜索结果" 
                    />
                </div>
            </div>
        </el-dialog>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    height: calc(100vh - 160px); // 调整整体高度

    :deep(.el-card__body) {
        height: 100%;
        padding: 0;
    }

    .massage-container {
        height: 100%;

        .contact-list {
            height: 100%;
            background: #fff;
            border-right: 1px solid #dcdfe6;

            .contacts {
                height: 100%;
                overflow-y: auto;
            }
        }

        .chat-container {
            height: 100%;
            display: flex;
            flex-direction: column;

            .chat-header {
                flex-shrink: 0; // 防止头部压缩
                padding: 0 20px;
                height: 60px;
                line-height: 60px;
                border-bottom: 1px solid #dcdfe6;
                background: #fff;
            }

            .message-container {
                flex: 1;
                padding: 20px;
                overflow-y: auto;
                background: #f5f7fa;
            }

            .input-area {
                flex-shrink: 0;
                padding: 20px;
                background: #fff;
                border-top: 1px solid #dcdfe6;

                .input-wrapper {
                    display: flex;
                    flex-direction: column;
                    gap: 10px;

                    .el-input {
                        width: 100%;
                    }

                    .button-wrapper {
                        display: flex;
                        justify-content: flex-end;

                        .send-button {
                            width: 50px;
                            height: 30px;
                        }
                    }
                }
            }
        }

        .no-contact {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f5f7fa;
        }
    }
}

.contact-item {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    cursor: pointer;
    transition: background-color 0.3s;
    border-bottom: 1px solid #f0f0f0;

    &:hover {
        background-color: #f5f7fa;
    }

    &.active {
        background-color: #e6f1fc;
    }

    .el-avatar {
        flex-shrink: 0;
        margin-right: 12px;
    }

    .contact-info {
        flex: 1;
        min-width: 0; // 防止文本溢出

        .contact-name {
            font-size: 14px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
        }

        .last-message {
            font-size: 12px;
            color: #909399;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    }

    .contact-time {
        font-size: 12px;
        color: #909399;
        margin-left: 10px;
    }
}

// 聊天窗口中的消息样式
.message-item {
    display: flex;
    margin-bottom: 20px;

    .avatar {
        flex-shrink: 0;
        margin: 0 12px;
    }

    .message-content {
        max-width: 70%;

        .message-bubble {
            padding: 10px 15px;
            border-radius: 4px;
            background-color: #fff;
            word-break: break-all;
        }

        .message-time {
            font-size: 12px;
            color: #909399;
            margin-top: 4px;
        }
    }

    &.sent {
        flex-direction: row-reverse;

        .message-bubble {
            background-color: #e6f1fc;
        }

        .message-time {
            text-align: right;
        }
    }
    &.received{
        flex-direction: row;
        .message-bubble{
            background-color: #e6f1fc;
        }
        .message-time{
            text-align: left;
        }
    }
}

.search-header {
    padding: 10px;
    border-bottom: 1px solid #dcdfe6;
    
    .el-button {
        width: 100%;
    }
}

.search-container {
    .search-results {
        margin-top: 20px;
        max-height: 400px;
        overflow-y: auto;

        .search-item {
            display: flex;
            align-items: center;
            padding: 10px;
            cursor: pointer;
            transition: background-color 0.3s;

            &:hover {
                background-color: #f5f7fa;
            }

            .user-info {
                margin-left: 12px;

                .username {
                    font-size: 14px;
                    font-weight: 500;
                    color: #303133;
                }

                .email {
                    font-size: 12px;
                    color: #909399;
                    margin-top: 4px;
                }
            }
        }
    }
}
</style>