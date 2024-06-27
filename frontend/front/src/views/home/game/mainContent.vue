<!--
作者: Tzpeach3
日期: 2024-06-27
作用: 展示和操作当前房间内物品的组件，提供物品的提示信息和拿取功能。
-->

<template>
    <!-- 主内容容器，背景图根据当前地点的背景图动态变化 -->
    <div class="main-content" :style="{ backgroundImage: `url(${currentLocation.backgroundImage})` }">
        <div class="map">
            <!-- 判断当前位置是否有物品 -->
            <div v-if="currentLocation.items.length > 0" class="items">
                <!-- 遍历当前地点的物品 -->
                <div class="item" v-for="(item, index) in currentLocation.items" :key="item.id + '-' + index"
                    @mouseenter="showTooltip(index)" @mouseleave="hideTooltip">
                    <!-- 物品图片和名称 -->
                    <img :src="item.imageId" :alt="item.name" />
                    <p>{{ item.name }}</p>
                    <!-- 物品信息的提示框 -->
                    <div v-if="tooltipIndex === index && !takingItem" class="tooltip">
                        <p>名称: {{ item.name }}</p>
                        <p>描述: {{ item.description }}</p>
                        <p>重量: {{ item.weight }}</p>
                        <p>物品类型：
                            {{
                            item.isMagic === 1 ? '魔法物品' :
                            item.isMagic === 2 ? '永久物品' :
                            '一次性物品'
                            }}
                        </p>
                        <!-- 拿取按钮 -->
                        <button @click.stop="takeItem(index)">拿取</button>
                    </div>
                </div>
            </div>
            <div v-else>
                <!-- 没有物品时显示的信息 -->
                <p>当前位置没有可获取的物品。</p>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import mapImage from '../../../assets/images/map.jpg';
    import bananaImage from '../../../assets/images/banana.jpg';
    import cookieImage from '../../../assets/images/cookie.jpg';
    import meatImage from '../../../assets/images/meat.jpg';
    import shitImage from '../../../assets/images/shit.png';
    import zooGate from '../../../assets/images/zooGate.jpg';
    import monkeyZone from '../../../assets/images/monkeyZone.jpg';
    import lionZone from '../../../assets/images/lionZone.jpg';
    import elephantZone from '../../../assets/images/elephantZone.jpg';
    import panadaZone from '../../../assets/images/panadaZone.jpg';
    import zooExport from '../../../assets/images/export.jpg';

    export default {
        props: {
            currentRoomId: {
                type: Number,
                required: true
            }
        },
        data() {
            return {
                locationMap: {
                    1: {
                        name: '入口',
                        backgroundImage: zooGate,
                        items: [],
                    },
                    2: {
                        name: '猴子园区',
                        backgroundImage: monkeyZone,
                        items: [],
                    },
                    3: {
                        name: '狮子园区',
                        backgroundImage: lionZone,
                        items: [],
                    },
                    4: {
                        name: '大象园区',
                        backgroundImage: elephantZone,
                        items: [],
                    },
                    5: {
                        name: '熊猫园区',
                        backgroundImage: panadaZone,
                        items: [],
                    },
                    6: {
                        name: '出口',
                        backgroundImage: zooExport,
                        items: [],
                    },
                },
                itemMap: {
                    1: { id: 1, name: '地图', imageId: mapImage, color: '#ffcc99', borderColor: '#cc9966' },
                    2: { id: 2, name: '香蕉', imageId: bananaImage, color: '#ffcc99', borderColor: '#cc9966' },
                    3: { id: 3, name: '魔法饼干', imageId: cookieImage, color: '#ffcc99', borderColor: '#cc9966' },
                    4: { id: 4, name: '生肉', imageId: meatImage, color: '#ffcc99', borderColor: '#cc9966' },
                    5: { id: 5, name: '大便', imageId: shitImage, color: '#ffcc99', borderColor: '#cc9966' }
                },
                tooltipIndex: null, // 当前提示框的索引
                takingItem: false // 是否正在拿取物品的标志
            };
        },
        computed: {
            // 计算当前地点
            currentLocation() {
                return this.locationMap[this.currentRoomId] || this.locationMap[1];
            }
        },
        watch: {
            currentRoomId: {
                handler(newVal) {
                    this.updateLocation(newVal); // 房间ID变化时更新当前地点信息
                },
                immediate: true // 立即执行监听器
            }
        },
        created() {
            this.listenDropItem(); // 组件创建时监听放下物品事件
            this.listenUpdateItem(); // 组件创建时监听更新物品事件
        },
        methods: {
            // 监听放下物品事件
            listenDropItem() {
                this.$root.$on('drop-item', () => {
                    this.updateLocation(this.currentRoomId); // 放下物品后更新当前位置的物品列表
                });
            },
            // 监听更新物品事件
            listenUpdateItem() {
                this.$root.$on('update-item', () => {
                    this.updateLocation(this.currentRoomId); // 放下物品后更新当前位置的物品列表
                });
            },
            // 更新当前地点的物品列表
            updateLocation(roomId) {
                const token = localStorage.getItem('token'); // 获取用户 token
                axios.post('http://10.78.250.34:8081/room/check',
                    { roomID: roomId },
                    { headers: { 'token': token } }
                )
                    .then(response => {
                        const responseData = response.data;
                        if (responseData.code === 200 && responseData.data) {
                            const items = responseData.data.map(item => {
                                return {
                                    id: item.itemID,
                                    name: item.itemName,
                                    description: item.description,
                                    isMagic: item.isMagic,
                                    weight: item.weight,
                                    imageId: this.itemMap[item.itemID].imageId  // 获取物品对应的图片
                                };
                            });
                            this.$set(this.locationMap[roomId], 'items', items);
                        } else {
                            console.error('获取物品列表失败:', responseData.message);
                        }
                    })
                    .catch(error => {
                        console.error('获取物品列表出错:', error);
                    });
            },
            // 显示提示框
            showTooltip(index) {
                this.tooltipIndex = index;
            },
            // 隐藏提示框
            hideTooltip() {
                this.tooltipIndex = null;
            },
            // 拿取物品
            takeItem(index) {
                const item = this.currentLocation.items[index];
                const token = localStorage.getItem('token'); // 获取用户 token
                const playerName = localStorage.getItem('playerName'); // 获取玩家名称
                axios.post('http://10.78.250.34:8081/item/take',
                    {
                        itemID: item.id,
                        playerName: playerName
                    },
                    { headers: { 'token': token } }
                )
                    .then(response => {
                        const responseData = response.data;
                        if (responseData.code === 200) {
                            localStorage.setItem('message', `你成功拿取${item.name}`);
                            this.currentLocation.items.splice(index, 1); // 更新前端物品列表
                            this.$root.$emit('take-item'); // 通知主组件拿走物品
                        } else {
                            localStorage.setItem('message', `拿取物品失败,你的负重剩余量不足`);
                            this.$root.$emit('take-item'); // 通知主组件拿走物品
                            console.error('拿取物品失败,你的负重剩余量不足:', responseData.message);
                        }
                    })
                    .catch(error => {
                        console.error('拿取物品出错:', error);
                    });
            }
        }
    };
</script>

<style scoped>
    .main-content {
        flex: 1;
        background-color: #fff;
        padding: 20px;
        justify-content: flex-end;
        border-radius: 10px;
        margin-bottom: 10px;
        background-size: cover;
        background-position: center;
    }

    .map {
        background-color: rgba(255, 255, 255, 0.7);
        padding: 10px;
        border-radius: 5px;
    }

    .items {
        display: flex;
        flex-wrap: wrap;
    }

    .item {
        margin: 5px;
        padding: 5px;
        cursor: pointer;
        text-align: center;
        /* 居中显示物品名称 */
        position: relative;
        /* 为了定位 tooltip */
    }

    .item img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: 4px;
        border: 1px solid #ccc;
        transition: transform 0.3s ease;
    }

    .item:hover img {
        transform: scale(1.1);
    }

    .tooltip {
        position: absolute;
        top: -100px;
        left: 50%;
        transform: translateX(-50%);
        background-color: rgba(0, 0, 0, 0.7);
        color: white;
        padding: 10px;
        border-radius: 5px;
        z-index: 1000;
        width: 150px;
        text-align: left;
    }

    button {
        margin-top: 10px;
        padding: 5px 10px;
        cursor: pointer;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
        transition: background-color 0.3s ease;
    }

    button:hover {
        background-color: #0056b3;
    }
</style>