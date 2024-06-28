<!--
作者：Tzpeach3
时间：2024-06-27
描述：多人聊天组件，用于显示玩家聊天和实现物品资源共享。
-->
<template>
    <div>
        <!-- 打开聊天按钮 -->
        <el-button class="font" @click="openChat">打开聊天</el-button>

        <!-- 聊天室对话框 -->
        <el-dialog :visible.sync="dialogVisible" title="聊天室" width="50%">
            <div class="chat-container">
                <!-- 聊天室头部，显示当前在线人数 -->
                <div class="chat-header">
                    <p>当前在线人数: {{ onlineUsers }}</p>
                </div>

                <!-- 聊天内容展示区域 -->
                <div class="chat-content">
                    <div v-for="message in messages" :key="message.id" class="chat-message">
                        <span><strong>{{ message.user }}:</strong> {{ message.text }}</span>
                    </div>
                </div>

                <!-- 聊天输入框及发送按钮 -->
                <div class="chat-footer">
                    <el-button class="emoji" @click="openEmojiPicker">😀</el-button>
                    <el-input v-model="newMessage" placeholder="输入消息..." @keydown.enter="sendMessage"></el-input>
                    <el-button @click="sendMessage">发送</el-button>
                </div>
            </div>
        </el-dialog>

        <!-- 表情选择对话框 -->
        <el-dialog :visible.sync="emojiDialogVisible" title="选择表情" width="70%">
            <div class="emoji-container">
                <span v-for="emoji in emojiArr" :key="emoji.className" @click="selectEmoji(emoji.icon)">
                    {{ emoji.icon }}
                </span>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { emojiArr } from '../../../assets/js/emojiArr';

export default {
    data() {
        return {
            dialogVisible: false,       // 控制聊天室对话框显示状态
            emojiDialogVisible: false,  // 控制表情选择对话框显示状态
            onlineUsers: '0',           // 当前在线人数
            messages: [],               // 聊天消息列表
            newMessage: '',             // 新消息输入
            emojiArr,                   // 表情数组
            ws: null                    // WebSocket 实例
        };
    },
    created() {
        // 组件创建时连接 WebSocket
        this.connectToWebSocket();
    },
    methods: {
        // 打开聊天室对话框
        openChat() {
            this.dialogVisible = true;
        },

        // 打开表情选择对话框
        openEmojiPicker() {
            this.emojiDialogVisible = true;
        },

        // 选择表情
        selectEmoji(emoji) {
            this.newMessage += emoji;
        },

        // 连接到 WebSocket
        connectToWebSocket() {
            // 使用玩家名称作为 WebSocket 连接地址
            this.ws = new WebSocket('ws://10.78.250.34:8081/webSocket/' + localStorage.getItem('playerName'));

            // WebSocket 打开时触发
            this.ws.onopen = () => {
                console.log('WebSocket connected');
            };

            // 接收到 WebSocket 消息时触发
            this.ws.onmessage = (event) => {
                try {
                    let data = JSON.parse(event.data);
                    switch (data.type) {
                        case 'update':
                            this.$root.$emit('update-item'); // 通知主组件拿走物品
                            break;
                        case 'message':
                            this.messages.push(data);       // 添加新消息到列表
                            break;
                        case 'onlineUsers':
                            this.onlineUsers = data.text;   // 更新在线用户数
                            break;
                        default:
                            console.log('Unknown message type:', data);
                    }
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                }
            };

            // WebSocket 关闭时触发
            this.ws.onclose = () => {
                console.log('WebSocket closed');
                this.onlineUsers = '0';  // 在线用户数置为 0
            };

            // WebSocket 错误时触发
            this.ws.onerror = (error) => {
                console.log('WebSocket error:', error);
            };
        },

        // 发送消息
        sendMessage() {
            // 确保输入非空白消息后发送
            if (this.newMessage.trim() !== '') {
                this.sendMessageToServer({
                    type: 'message',
                    text: this.newMessage,
                    user: this.getPlayerName()  // 获取当前玩家名称
                });
                this.newMessage = '';  // 清空消息输入框
            }
        },

        // 发送消息到服务器
        sendMessageToServer(message) {
            this.ws.send(JSON.stringify(message));  // 发送 JSON 格式的消息
        },

        // 获取当前玩家名称
        getPlayerName() {
            return localStorage.getItem('playerName') || '未知用户';
        }
    },

    // 组件销毁前关闭 WebSocket 连接
    beforeDestroy() {
        if (this.ws) {
            this.ws.close();
        }
    }
};
</script>

<style scoped>
.font {
    font-size: 18px; 
}

.chat-container {
    display: flex;
    flex-direction: column;
    height: 400px;  
}

.chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.chat-content {
    background-color: rgb(240, 233, 225); 
    flex: 1;
    overflow-y: auto; 
}

.chat-message {
    margin: 5px 0;  
}

.chat-footer {
    display: flex;  
}

.emoji-container {
    display: flex;
    flex-wrap: wrap;  
}

.emoji-container span {
    cursor: pointer;
    margin: 10px;  
    font-size: 24px;  
}
</style>
