<template>
    <el-container class="home-container">
        <el-container>
            <el-main class="main-content">
                <main-content :currentRoomId="currentRoomId"></main-content>
                <main-chat></main-chat>
                <el-button class="back" @click="move('back')">返回</el-button>
                <div class="dialog-container">
                    <div class="dialog">
                        <div class="dialog-content">
                            <img width="100px" src="../../assets/images/npc.jpg" alt="">
                            <div class="text-display">
                                <!-- 动态显示文本的区域 -->
                                <p>{{ displayedText }}</p>
                            </div>
                        </div>
                        <div class="controls">
                            <div class="row">
                                <el-button class="north" @click="move('north')">北</el-button>
                            </div>
                            <div class="row">
                                <el-button class="west" @click="move('west')">西</el-button>
                                <el-button class="stay" @click="move('stay')">S</el-button>
                                <el-button class="east" @click="move('east')">东</el-button>
                            </div>
                            <div class="row">
                                <el-button class="south" @click="move('south')">南</el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </el-main>
            <el-aside width="300px" class="sidebar-container">
                <app-sidebar></app-sidebar>
                <img src="../../assets/images/Zoo.png" alt="">
                <h2 class="location"><span>{{ locationMap[currentRoomId] }}</span></h2> <!-- 修改此行 -->
            </el-aside>
        </el-container>
    </el-container>
</template>

<script>
    import MainContent from './game/mainContent.vue';
    import AppSidebar from './game/appSidebar.vue';
    import MainChat from './chat/chatMain.vue';
    import axios from 'axios';

    export default {
        name: 'home-main',
        components: {
            MainContent,
            AppSidebar,
            MainChat
        },
        data() {
            return {
                playerInfo: null,
                currentRoomId: 1, // 初始化为入口的 roomId
                dialogContent: `欢迎进入《勇闯动物园》!${this.getPlayerName()}`,
                displayedText: '',
                displayTimeout: null,
                locationMap: {
                    1: '入口',
                    2: '猴子园区',
                    3: '狮子园区',
                    4: '大象园区',
                    5: '熊猫园区',
                    6: '出口'
                },
                visitedDirection: [], // 记录访问的方向
                backDirection: '',
            };
        },
        created() {
            this.displayedText = '';
            this.startTextAnimation();
            this.fetchUserInfo();
            this.listenFeedItem(); // 组件创建时监听投喂物品事件
            this.listenUseItem(); // 组件创建时监听使用物品事件
            this.listenTakeItem(); // 组件创建时监听拿走物品事件
            this.listenDropItem();// 组件创建时监听放下物品事件
        },
        methods: {
            // 监听投喂物品事件
            listenFeedItem() {
                this.$root.$on('feed-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            // 监听使用物品事件
            listenUseItem() {
                this.$root.$on('use-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                });
            },
            // 监听拿走物品事件
            listenTakeItem() {
                this.$root.$on('take-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            // 监听放下物品事件
            listenDropItem() {
                this.$root.$on('drop-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            // 获取存储在 localStorage 中的 playerName
            getPlayerName() {
                return localStorage.getItem('playerName');
            },
            // 获取存储在 localStorage 中的 token
            getPlayToken() {
                return localStorage.getItem('token');
            },
            // 获取存储在 localStorage 中的 message
            getPlayMessage() {
                return localStorage.getItem('message');
            },
            // 获取用户信息
            async fetchUserInfo() {
                const playerName = this.getPlayerName();
                const token = this.getPlayToken();
                if (!playerName || !token) {
                    console.error('本地存储中没有找到 playerName 或 token');
                    return;
                }
                try {
                    const response = await axios.post(
                        'http://10.78.250.34:8081/player/getPlayerInfo',
                        { playerName: playerName },
                        { headers: { 'token': token } }
                    );
                    if (response.data.code === 200) {
                        this.playerInfo = response.data.data;
                        console.log('用户信息:', this.playerInfo);
                        this.currentRoomId = this.playerInfo.currentRoomID; // 设置当前房间ID
                    } else {
                        console.error('获取用户信息出错:', response.data.message);
                    }
                } catch (error) {
                    console.error('请求出错:', error);
                }
            },
            // 移动方法
            async move(direction) {
                // 清除显示文本的定时器
                if (this.displayTimeout) {
                    clearTimeout(this.displayTimeout);
                    this.displayTimeout = null;
                    this.displayedText = this.dialogContent;
                }

                const playerName = this.getPlayerName();
                const token = this.getPlayToken();
                const directionsMap = {
                    'east': 0,
                    'south': 1,
                    'west': 2,
                    'north': 3,
                    'stay': 4,
                    'back': 5 // 添加返回操作
                };
                const directionsBack = {
                    'west': 'east',
                    'north': 'south',
                    'east': 'west',
                    'south': 'north',
                };
                const locationMap = {
                    1: '入口',
                    2: '猴子园区',
                    3: '狮子园区',
                    4: '大象园区',
                    5: '熊猫园区',
                    6: '出口'
                };

                if (!playerName || !token) {
                    console.error('本地存储中没有找到 playerName 或 token');
                    return;
                }

                try {
                    if (direction === 'back') { // 处理返回操作
                        if (this.visitedDirection.length > 0) {
                            const previousDirection = this.visitedDirection.pop(); // 获取上一个前进方向
                            this.backDirection = directionsBack[previousDirection];
                            const response = await axios.post('http://10.78.250.34:8081/room/go', {
                                playerName: playerName,
                                direction: directionsMap[this.backDirection],
                            }, {
                                headers: { 'token': token }
                            });

                            if (response.data.code === 200) {
                                const roomData = response.data.data;
                                this.currentRoomId = roomData.roomID;
                                this.dialogContent = `你回到了${roomData.roomName}。 ${roomData.description}`;
                            } else this.dialogContent = `无法往${this.getDirectionText(direction)}走，你现在在${locationMap[this.currentRoomId]}。`;
                        } else {
                            this.dialogContent = `无法返回,你已经到了最初的地方。`;
                        }
                    } else {
                        const response = await axios.post('http://10.78.250.34:8081/room/go', {
                            playerName: playerName,
                            direction: directionsMap[direction],
                        }, {
                            headers: { 'token': token }
                        });

                        if (response.data.code === 200) {
                            const roomData = response.data.data;
                            this.visitedDirection.push(direction);
                            this.currentRoomId = roomData.roomID;
                            this.dialogContent = `往${this.getDirectionText(direction)}走，你来到了${roomData.roomName}。 ${roomData.description}`;
                        } else {
                            if (directionsMap[direction] === 4)
                                this.dialogContent = `你停留在原地，你现在在${locationMap[this.currentRoomId]}。`;
                            else this.dialogContent = `无法往${this.getDirectionText(direction)}走，你现在在${locationMap[this.currentRoomId]}。`;
                        }
                    }
                } catch (error) {
                    console.error('请求出错:', error);
                }

                this.displayedText = '';
                this.startTextAnimation();
            },
            // 文本动画
            startTextAnimation() {
                let index = 0;
                const animate = () => {
                    if (index < this.dialogContent.length) {
                        this.displayedText += this.dialogContent[index++];
                        this.displayTimeout = setTimeout(animate, 50); // 递归调用以逐字显示文本
                    } else {
                        this.displayTimeout = null;
                    }
                };
                animate();
            },
            // 获取方向文本
            getDirectionText(direction) {
                const directionTexts = {
                    'east': '东',
                    'south': '南',
                    'west': '西',
                    'north': '北',
                };
                return directionTexts[direction];
            },
        },
        beforeDestroy() {
            // 组件销毁时清理定时器
            if (this.displayTimeout) {
                clearTimeout(this.displayTimeout);
                this.displayTimeout = null;
            }
        }
    }
</script>

<style scoped>
    .location {
        position: relative;
        left: 35%;
        top: -36%;
    }

    .home-container {
        background: url("../../assets/images/background.png") no-repeat;
        background-size: 100%;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        position: absolute;
    }

    .main-content {
        flex: 1;
        width: 95%;
        height: 80%;
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
    }

    .dialog-container {
        margin-top: auto;
        height: 150px;
        width: 100%;
        background-color: #fff;
        box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.1);
    }

    .sidebar-container {
        display: flex;
        flex-direction: column;
    }

    .dialog {
        background-color: #fff;
        padding: 10px;
        box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .dialog-content {
        flex: 1;
    }

    .controls {
        margin-left: 20px;
        display: flex;
        align-items: center;
        flex-direction: column;
    }

    .row {
        display: flex;
        justify-content: space-around;
        align-items: center;
        width: 100%;
    }

    .north {
        font-size: 18px;
        padding: 10px 10px;
    }


    .east,
    .west {
        font-size: 18px;
        padding: 10px 10px;
    }

    .stay {
        font-size: 18px;
        padding: 10px 13px;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .south {
        font-size: 18px;
        padding: 10px 10px;
    }

    .text-display {
        display: inline-block;
        font-size: 16px;
        padding-top: 0px;
        line-height: 1.5;
    }
</style>