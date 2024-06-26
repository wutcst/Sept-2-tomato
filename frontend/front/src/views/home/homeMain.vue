<template>
    <el-container class="home-container">
        <el-container>
            <el-main class="main-content">
                <main-content :currentRoomId="currentRoomId"></main-content>
                <div class="main-chat"><main-chat></main-chat></div>
                <div class="button-container">  
                    <el-button  class="exit" @click="showExitDialog = true">退出游戏</el-button>
                    <el-button class="back" @click="move('back')">返回</el-button>
                </div>
                <div class="dialog-container">
                    <div class="dialog">
                        <div class="dialog-content">
                            <img width="100px" src="../../assets/images/npc.jpg" alt="">
                            <div class="text-display">
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
                <h2 class="location"><span>{{ locationMap[currentRoomId] }}</span></h2>
            </el-aside>
        </el-container>

        <!-- 确认退出对话框 -->
        <el-dialog
            title="确认退出"
            :visible.sync="showExitDialog"
            width="30%"
            @close="showExitDialog = false">
            <span>您确定要退出游戏吗？</span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="showExitDialog = false">取消</el-button>
                <el-button type="primary" @click="confirmExit">确认</el-button>
            </span>
        </el-dialog>
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
                currentRoomId: 1,
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
                visitedDirection: [],
                backDirection: '',
                showExitDialog: false // 控制退出对话框的显示
            };
        },
        created() {
            this.displayedText = '';
            this.startTextAnimation();
            this.fetchUserInfo();
            this.listenFeedItem();
            this.listenUseItem();
            this.listenTakeItem();
            this.listenDropItem();
        },
        methods: {
            listenFeedItem() {
                this.$root.$on('feed-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            listenUseItem() {
                this.$root.$on('use-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                });
            },
            listenTakeItem() {
                this.$root.$on('take-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            listenDropItem() {
                this.$root.$on('drop-item', () => {
                    this.dialogContent = this.getPlayMessage();
                    this.displayedText = '';
                    this.startTextAnimation();
                    this.fetchUserInfo();
                });
            },
            getPlayerName() {
                return localStorage.getItem('playerName');
            },
            getPlayToken() {
                return localStorage.getItem('token');
            },
            getPlayMessage() {
                return localStorage.getItem('message');
            },
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
                        this.currentRoomId = this.playerInfo.currentRoomID;
                    } else {
                        console.error('获取用户信息出错:', response.data.message);
                    }
                } catch (error) {
                    console.error('请求出错:', error);
                }
            },
            async move(direction) {
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
                    'back': 5
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
                    if (direction === 'back') {
                        if (this.visitedDirection.length > 0) {
                            const previousDirection = this.visitedDirection.pop();
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
            startTextAnimation() {
                let index = 0;
                const animate = () => {
                    if (index < this.dialogContent.length) {
                        this.displayedText += this.dialogContent[index++];
                        this.displayTimeout = setTimeout(animate, 50);
                    } else {
                        this.displayTimeout = null;
                    }
                };
                animate();
            },
            getDirectionText(direction) {
                const directionTexts = {
                    'east': '东',
                    'south': '南',
                    'west': '西',
                    'north': '北',
                };
                return directionTexts[direction];
            },
            exitGame() {
                this.showExitDialog = true;
            },
            confirmExit() {
                localStorage.removeItem('playerName');
                localStorage.removeItem('token');
                this.$router.push('/login-main');
            }
        },
        beforeDestroy() {
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

    .main-chat{
        display: flex;
        flex-direction: row-reverse
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

    .button-container {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
    }

    .back,
    .exit {
        font-size: 18px;
        padding: 10px 10px;
    }
</style>
