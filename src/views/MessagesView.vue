<template>
  <div class="messages-container">
    <header class="messages-header">
      <h1>私信</h1>
      <nav>
        <router-link to="/home" class="nav-link">返回主页</router-link>
      </nav>
    </header>

    <main class="messages-main">
      <aside class="conversation-list">
        <h2>对话列表</h2>
        <ul>
          <li
            v-for="conversation in conversations"
            :key="conversation.id"
            @click="selectConversation(conversation)"
            :class="{ active: selectedConversation?.id === conversation.id }"
          >
            <img :src="conversation.avatar" :alt="conversation.name" class="avatar" />
            <div class="conversation-info">
              <h3>{{ conversation.name }}</h3>
              <p>{{ conversation.lastMessage }}</p>
            </div>
          </li>
        </ul>
      </aside>

      <section class="message-area" v-if="selectedConversation">
        <header class="message-header">
          <img :src="selectedConversation.avatar" :alt="selectedConversation.name" class="avatar" />
          <h2>{{ selectedConversation.name }}</h2>
        </header>
        <div class="message-list" ref="messageList">
          <div
            v-for="message in messages"
            :key="message.id"
            :class="['message', { 'sent': message.sent }]"
          >
            <p>{{ message.content }}</p>
            <span class="message-time">{{ formatTime(message.timestamp) }}</span>
          </div>
        </div>
        <form @submit.prevent="sendMessage" class="message-input">
          <input
            v-model="newMessage"
            type="text"
            placeholder="输入消息..."
            required
          />
          <button type="submit">发送</button>
        </form>
      </section>
      <div v-else class="no-conversation">
        <p>选择一个对话开始聊天</p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const conversations = ref([
  { id: 1, name: '张三', avatar: 'https://via.placeholder.com/40', lastMessage: '好的，明天见！' },
  { id: 2, name: '李四', avatar: 'https://via.placeholder.com/40', lastMessage: '你看到最新的公告了吗？' },
  { id: 3, name: '王五', avatar: 'https://via.placeholder.com/40', lastMessage: '周末有空吗？' },
]);

const selectedConversation = ref(null);
const messages = ref([]);
const newMessage = ref('');
const messageList = ref(null);

const selectConversation = (conversation) => {
  selectedConversation.value = conversation;
  // 在实际应用中，这里应该从API获取消息历史
  messages.value = [
    { id: 1, content: '你好！', timestamp: new Date(Date.now() - 3600000), sent: false },
    { id: 2, content: '最近怎么样？', timestamp: new Date(Date.now() - 3500000), sent: true },
    { id: 3, content: '一切都好，你呢？', timestamp: new Date(Date.now() - 3400000), sent: false },
  ];
};

const sendMessage = () => {
  if (newMessage.value.trim()) {
    messages.value.push({
      id: messages.value.length + 1,
      content: newMessage.value,
      timestamp: new Date(),
      sent: true
    });
    newMessage.value = '';
  }
};

const formatTime = (timestamp) => {
  return new Intl.DateTimeFormat('zh-CN', { hour: '2-digit', minute: '2-digit' }).format(timestamp);
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messageList.value) {
      messageList.value.scrollTop = messageList.value.scrollHeight;
    }
  });
};

watch(messages, scrollToBottom, { deep: true });

const checkToken = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    router.push('/login');
  }
};

onMounted(() => {
  checkToken();
});
</script>

<style scoped>
.messages-container {
  font-family: 'Roboto', sans-serif;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.messages-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.messages-header h1 {
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

.messages-main {
  display: flex;
  gap: 20px;
  height: calc(100vh - 140px);
}

.conversation-list {
  flex: 1;
  background-color: #ffffff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.conversation-list h2 {
  color: #1a73e8;
  margin-bottom: 15px;
}

.conversation-list ul {
  list-style-type: none;
  padding: 0;
}

.conversation-list li {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.conversation-list li:hover, .conversation-list li.active {
  background-color: #e8f0fe;
}

.conversation-list .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.conversation-info h3 {
  margin: 0;
  font-size: 16px;
  color: #202124;
}

.conversation-info p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #5f6368;
}

.message-area {
  flex: 2;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.message-header {
  display: flex;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.message-header .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.message-header h2 {
  margin: 0;
  font-size: 18px;
  color: #202124;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.message {
  max-width: 70%;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 10px;
  background-color: #f1f3f4;
  align-self: flex-start;
}

.message.sent {
  background-color: #d2e3fc;
  align-self: flex-end;
  margin-left: auto;
}

.message p {
  margin: 0;
  font-size: 14px;
  color: #202124;
}

.message-time {
  display: block;
  font-size: 12px;
  color: #5f6368;
  margin-top: 5px;
  text-align: right;
}

.message-input {
  display: flex;
  padding: 20px;
  border-top: 1px solid #e0e0e0;
}

.message-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #d2d6dc;
  border-radius: 5px;
  font-size: 14px;
}

.message-input button {
  margin-left: 10px;
  padding: 10px 20px;
  background-color: #1a73e8;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.message-input button:hover {
  background-color: #1557b0;
}

.no-conversation {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.no-conversation p {
  color: #5f6368;
  font-size: 18px;
}

@media (max-width: 768px) {
  .messages-main {
    flex-direction: column;
  }

  .conversation-list, .message-area, .no-conversation {
    flex: none;
  }

  .conversation-list {
    height: 200px;
  }

  .message-area, .no-conversation {
    height: calc(100vh - 380px);
  }
}
</style>

