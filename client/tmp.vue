<!-- 左侧菜单 -->
<el-aside width="200px">
    <div class="el-aside__logo"></div>
    <!-- element-plus的菜单标签 -->
    <el-menu 
        active-text-color="#409EFF" 
        background-color="#ffffff"  
        text-color="#303133"
        router>
        <el-menu-item index="/article/category">
            <el-icon>
                <Management />
            </el-icon>
            <span>帖子分类</span>
        </el-menu-item>
        <el-menu-item index="/article/manage">
            <el-icon>
                <Promotion />
            </el-icon>
            <span>帖子管理</span>
        </el-menu-item>
        <el-menu-item index="/massage/list">
            <el-icon>
                <Message />
            </el-icon>
            <span>私信管理</span>
        </el-menu-item>
        <el-sub-menu >
            <template #title>
                <el-icon>
                    <UserFilled />
                </el-icon>
                <span>个人中心</span>
            </template>
            <el-menu-item index="/user/info">
                <el-icon>
                    <User />
                </el-icon>
                <span>基本资料</span>
            </el-menu-item>
            <el-menu-item index="/user/avatar">
                <el-icon>
                    <Crop />
                </el-icon>
                <span>更换头像</span>
            </el-menu-item>
            <el-menu-item index="/user/resetPassword">
                <el-icon>
                    <EditPen />
                </el-icon>
                <span>重置密码</span>
            </el-menu-item>
            
        </el-sub-menu>
    </el-menu>
</el-aside>




<script setup>
import { ref, computed, onMounted } from 'vue'
import { messageListService, messageGetService, messageSendService } from '@/api/message.js'
import useUserInfoStore from '@/stores/userInfo.js'

const userInfoStore = useUserInfoStore()

// 当前用户信息
const currentUser = {
    id: userInfoStore.info.id,
    name: userInfoStore.info.nickname,
    avatar: userInfoStore.info.userPic
}

// 联系人列表
const contacts = ref([])
// 所有聊天记录
const allMessages = ref({})

// 获取联系人列表
const getContactList = async () => {
    try {
        const data = {
            sender_id: currentUser.id
        }
        const result = await messageListService(data)
        contacts.value = result.data.data
    } catch (error) {
        console.error('获取联系人列表失败:', error)
    }
}

// 获取与特定用户的聊天记录
const getChatHistory = async (userId) => {
    try {
        const data = {  
            recipient_id: userId,
            sender_id: currentUser.id
        }
        const result = await messageGetService(data)
        console.log('获取到的聊天记录:',result);
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
            timestamp: new Date(msg.create_time).toLocaleString()
        }))
        console.log(allMessages.value[userId]);
    } catch (error) {
        console.error('获取聊天记录失败:', error)
    }
}

// 当前选中的联系人
const selectedContact = ref({id:null,name:null,avatar:null})

// 监听选中联系人变化
const handleContactSelect = async (contact) => {

    selectedContact.id = contact
    // 当选择新联系人时，获取聊天记录
    if (contact) {
        await getChatHistory(contact)
    }
}

// 输入框内容
const inputMessage = ref('')

// 计算当前聊天记录
const currentMessages = computed(() => {
    if (!selectedContact.id) return []
    return allMessages.value[selectedContact.id] || []
})

// 发送消息
const sendMessage = async () => {
    console.log(selectedContact.id);
    if (!inputMessage.value.trim() || !selectedContact.id) return

    try {
        
        const newMessage = {
            sender_id: currentUser.id,
            content: inputMessage.value,
            timestamp: new Date().toISOString(),
            recipient_id: selectedContact.id,

        }
        console.log(newMessage);
        let result = await messageSendService(newMessage)
        console.log(result);
        // 更新本地消息列表
        if (!allMessages.value[selectedContact.id]) {
            allMessages.value[selectedContact.id] = []
        }
        allMessages.value[selectedContact.id].push(newMessage)

        // 更新联系人最后一条消息
        const contact = contacts.value.find(c => c.id === selectedContact.id)
        if (contact) {
            contact.lastMessage = inputMessage.value
            contact.lastTime = newMessage.timestamp
        }

        inputMessage.value = ''
    } catch (error) {
        console.error('发送消息失败:', error)
    }
}