<template>
    <div>
        <el-button @click="openChat">打开聊天</el-button>
        <el-dialog :visible.sync="dialogVisible" title="聊天室" width="50%">
            <div class="chat-container">
                <div class="chat-header">
                    <p>当前在线人数: {{ onlineUsers }}</p>
                </div>
                <div class="chat-content">
                    <div v-for="message in messages" :key="message.id" class="chat-message">
                        <span><strong>{{ message.user }}:</strong> {{ message.text }}</span>
                    </div>
                </div>
                <div class="chat-footer">
                    <el-button class="emoji" @click="openEmojiPicker">😀</el-button>
                    <el-input v-model="newMessage" placeholder="输入消息..." @keyup.enter="sendMessage"></el-input>
                    <el-button @click="sendMessage">发送</el-button>
                </div>
            </div>
        </el-dialog>
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
            dialogVisible: false,
            emojiDialogVisible: false,
            onlineUsers: 0,
            messages: [],
            newMessage: '',
            emojiArr,
            ws: null
        };
    },
    created() {
        this.connectToWebSocket();
    },
    methods: {
        openChat() {
            this.dialogVisible = true;
        },
        openEmojiPicker() {
            this.emojiDialogVisible = true;
        },
        selectEmoji(emoji) {
            this.newMessage += emoji;
        },
        connectToWebSocket() {
            this.ws = new WebSocket('ws://10.78.250.34:8081/webSocket/' + localStorage.getItem('playerName'));

            this.ws.onopen = () => {
                console.log('WebSocket connected');
            };
            this.ws.onmessage = (event) => {
                try {
                    let data = JSON.parse(event.data);
                    // alert(data.type);
                    switch (data.type) {
                        case 'update':
                            this.$root.$emit('update-item'); // 通知主组件拿走物品
                            break;
                        case 'message':
                            this.messages.push(data);
                            break;
                        case 'onlineUsers':
                            this.onlineUsers = data.count;
                            break;
                        default:
                            console.log('Unknown message type:', data);
                    }
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                }
            };

            this.ws.onclose = () => {
                console.log('WebSocket closed');
                this.onlineUsers = 0;
            };

            this.ws.onerror = (error) => {
                console.log('WebSocket error:', error);
            };
        },
        sendMessage() {
            if (this.newMessage.trim() !== '') {
                this.sendMessageToServer({
                    type: 'message',
                    text: this.newMessage,
                    user: this.getPlayerName()
                });
                this.newMessage = '';
            }
        },
        sendMessageToServer(message) {
            this.ws.send(JSON.stringify(message));
        },
        getPlayerName() {
            return localStorage.getItem('playerName') || '未知用户';
        }
    },
    beforeDestroy() {
        if (this.ws) {
            this.ws.close();
        }
    }
};
</script>

<style scoped>
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
