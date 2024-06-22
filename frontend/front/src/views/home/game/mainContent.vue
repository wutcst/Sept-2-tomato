<template>
    <div class="main-content" :style="{ backgroundImage: `url(${currentLocation.backgroundImage})` }">
        <div class="map">
            <!-- <img class="location" src="../../../assets/images/Zoo.png" alt="">
        <h2><span class="locationWord">{{ currentLocation.name }}</span></h2> -->
            <div v-if="currentLocation.items.length > 0" class="items">
                <div class="item" v-for="(item, index) in currentLocation.items" :key="item.id + '-' + index"
                    @mouseenter="showTooltip(index)" @mouseleave="hideTooltip">
                    <img :src="item.imageId" :alt="item.name" />
                    <p>{{ item.name }}</p>
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
                        <button @click.stop="takeItem(index)">拿取</button>
                    </div>
                </div>
            </div>
            <div v-else>
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
                tooltipIndex: null,
                takingItem: false
            };
        },
        computed: {
            currentLocation() {
                return this.locationMap[this.currentRoomId] || this.locationMap[1];
            }
        },
        watch: {
            currentRoomId: {
                handler(newVal) {
                    this.updateLocation(newVal);
                },
                immediate: true
            }
        },
        created() {
            this.listenDropItem(); // 组件创建时监听放下物品事件
        },
        methods: {
            // 监听放下物品事件
            listenDropItem() {
                this.$root.$on('drop-item', () => {
                    this.updateLocation(this.currentRoomId); // 放下物品后更新当前位置的物品列表
                });
            },
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
            showTooltip(index) {
                this.tooltipIndex = index;
            },
            hideTooltip() {
                this.tooltipIndex = null;
            },
            // confirmTakeItem(index) {
            //   this.takingItem = true;
            //   const item = this.currentLocation.items[index];
            //   if (confirm(`你确定要拿取 ${item.name} 吗?`)) {
            //     this.takeItem(index);
            //   }
            //   this.takingItem = false;
            // },
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
                            // alert(`你拿到了${item.name}`);
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