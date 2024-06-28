<template>
  <div class="sidebar">
    <p>当前负重: {{ usedWeight }}/{{ totalWeight }}</p>
    <div class="inventory">
      <div
        class="item"
        v-for="(item, index) in displayedInventory"
        :key="item.itemID + '-' + index"
        @mouseenter="showTooltip(index)"
        @mouseleave="hideTooltip"
        @click.stop="toggleDropConfirmation(index)"
        :style="{ backgroundColor: item.color, border: '1px solid ' + item.borderColor }"
      >
        <img :src="item.imageId" :alt="item.name" />
        <div v-if="tooltipIndex === index && !confirmingDrop" class="tooltip">
          <p>名称: {{ item.itemName }}</p>
          <p>描述: {{ item.description }}</p>
          <p>重量: {{ item.weight }}</p>
          <p>类型：
              {{ 
                item.isMagic === 1 ? '魔法物品' :
                item.isMagic === 2 ? '永久物品' :
                '一次性物品'
              }}
          </p>
          <button @click.stop="dropItem(index)">放下</button>
          <button @click.stop="useItem(index)">使用</button>
          <button @click.stop="feedItem(index)">投喂</button>
        </div>
      </div>
      <div class="item empty" v-for="n in emptySlots" :key="'empty-' + n"></div>
    </div>
    <MapLayer :visible="isMapLayerVisible" @close="isMapLayerVisible = false" />
  </div>
</template>

<script>
import axios from 'axios';
import MapLayer from './mapLayer.vue'; // 导入地图图层组件
import mapImage from '../../../assets/images/map.jpg';
import bananaImage from '../../../assets/images/banana.jpg';
import cookieImage from '../../../assets/images/cookie.jpg';
import meatImage from '../../../assets/images/meat.jpg';
import shitImage from '../../../assets/images/shit.png';

export default {
  name: 'AppSidebar',
  components: {
    MapLayer // 注册地图图层组件
  },
  data() {
    return {
      totalWeight: 100, // 最大负重
      usedWeight: 0,    // 当前负重
      inventory: [],    // 背包中的物品列表
      items: {
        地图: { itemName: '地图', imageId: mapImage, color: '#ffcc99', borderColor: '#cc9966' },
        香蕉: { itemName: '香蕉', imageId: bananaImage, color: '#ffcc99', borderColor: '#cc9966' },
        魔法饼干: { itemName: '魔法饼干', imageId: cookieImage, color: '#ffcc99', borderColor: '#cc9966' },
        生肉: { itemName: '生肉', imageId: meatImage, color: '#ffcc99', borderColor: '#cc9966' },
        大便: { itemName: '大便', imageId: shitImage, color: '#ffcc99', borderColor: '#cc9966' },
      },
      maxDisplayItems: 9, // 每页最多显示的物品数量
      tooltipIndex: null,
      confirmingDrop: false,
      itemToDropIndex: null,
      isMapLayerVisible: false // 控制地图图层的显示
    }
  },
  computed: {
    displayedInventory() {
      return this.inventory.slice(0, this.maxDisplayItems).map(item => {
        return {
          ...this.items[item.itemName],
          ...item
        };
      });
    },
    emptySlots() {
      return Math.max(0, this.maxDisplayItems - this.displayedInventory.length);
    }
  },
  created() {
    this.listenTakeItem(); // 组件创建时监听拿取物品事件
  },
  methods: {
    listenTakeItem() {
      this.$root.$on('take-item', () => {
        this.fetchPlayerInfo();
        this.fetchInventoryItems();
      });
    },
    showTooltip(index) {
      this.tooltipIndex = index;
    },
    hideTooltip() {
      this.tooltipIndex = null;
    },
    toggleDropConfirmation(index) {
      this.confirmingDrop = !this.confirmingDrop;
      this.itemToDropIndex = index;
    },
    dropItem(index) {
      const item = this.inventory[index];
      const token = localStorage.getItem('token');
      const playerName = localStorage.getItem('playerName');
      axios.post('http://10.78.250.34:8081/item/drop', { itemID: item.itemID, playerName: playerName }, { headers: { 'token': token } })
        .then(response => {
          if (response.data.code === 200) {
            localStorage.setItem('message', `成功放下物品：${item.itemName}`);
            this.fetchPlayerInfo(); // 刷新玩家信息
            this.fetchInventoryItems(); // 刷新物品列表
            this.$root.$emit('drop-item'); // 通知主组件放下物品
          } else {
            console.error('放下物品失败:', response.data.message);
          }
        })
        .catch(error => {
          console.error('放下物品出错:', error);
        })
        .finally(() => {
          this.confirmingDrop = false;
          this.itemToDropIndex = null;
        });
    },
    useItem(index) {
      const item = this.inventory[index];
      if (item.itemName === '地图') {
        this.isMapLayerVisible = true; // 显示地图图层
      } else {
        const token = localStorage.getItem('token');
        const playerName = localStorage.getItem('playerName');
        axios.post('http://10.78.250.34:8081/item/use', { itemID: item.itemID, playerName: playerName }, { headers: { 'token': token } })
          .then(response => {
            if (response.data.code === 200) {
              this.fetchPlayerInfo(); // 刷新玩家信息
              this.fetchInventoryItems(); // 刷新物品列表
              localStorage.setItem('message', response.data.message);
              this.$root.$emit('use-item'); // 通知主组件物品已被使用
            } else {
              console.error('使用物品失败:', response.data.message);
            }
          })
          .catch(error => {
            console.error('使用物品出错:', error);
          });
      }
    },
    // 投喂物品
    feedItem(index) {
      const item = this.inventory[index];
      const token = localStorage.getItem('token'); // 获取用户 token
      const playerName = localStorage.getItem('playerName'); // 获取玩家名称
      axios.post('http://10.78.250.34:8081/item/feed', 
        {
          itemID: item.itemID,
          playerName: playerName
        },
        { headers: { 'token': token } }
      )
      .then(response => {
        const responseData = response.data;
        if (responseData.code === 200) {
          this.fetchPlayerInfo();
          this.fetchInventoryItems();
          localStorage.setItem('message', responseData.message);
          this.$root.$emit('feed-item'); // 通知主组件物品已被投喂
        } else {
          console.error('投喂物品失败:', responseData.message);
        }
      })
      .catch(error => {
        console.error('投喂物品出错:', error);
      });
    },
    fetchPlayerInfo() {
      const token = localStorage.getItem('token');
      const playerName = localStorage.getItem('playerName');
      axios.post('http://10.78.250.34:8081/player/getPlayerInfo', { playerName }, { headers: { 'token': token } })
        .then(response => {
          if (response.data.code === 200) {
            const data = response.data.data;
            this.usedWeight = data.currentWeight;
            this.totalWeight = data.maxCarryWeight;
          } else {
            console.error('获取玩家信息失败:', response.data.message);
          }
        })
        .catch(error => {
          console.error('获取玩家信息出错:', error);
        });
    },
    fetchInventoryItems() {
      const token = localStorage.getItem('token');
      const playerName = localStorage.getItem('playerName');
      axios.post('http://10.78.250.34:8081/item/getItems', { playerName }, { headers: { 'token': token } })
        .then(response => {
          if (response.data.code === 200) {
            this.inventory = response.data.data;
            this.usedWeight = this.inventory.reduce((acc, item) => acc + item.weight, 0);
          } else {
            console.error('获取背包物品失败:', response.data.message);
          }
        })
        .catch(error => {
          console.error('获取背包物品出错:', error);
        });
    },
    selectItem(index) {
      console.log('选中了物品：', this.inventory[index]);
    }
  },
  mounted() {
    this.fetchPlayerInfo();
    this.fetchInventoryItems();
  }
}
</script>

<style scoped>
.sidebar {
  border-radius: 10px;
  opacity: 0.7;
  background: url('../../../assets/images/bag.jpg') no-repeat;
  background-size: cover;
  padding: 5px;
  margin: 20px;
  margin-bottom: 0px;
}

.inventory {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 背包格子布局，每行3个格子 */
  gap: 10px; /* 格子之间的间隔 */
}

button{
  font-size: 10px;
}
.item {
  position: relative;
  width: 60px;
  height: 60px;
  margin-left: 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease, border-color 0.3s ease; /* 添加过渡效果 */
}

.item.empty {
  background-color: transparent;
  border: 1px dashed #ccc; /* 空格子的边框样式 */
}

.item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 鼠标悬停时改变背景颜色和边框颜色 */
.item:hover {
  background-color: #fff;
  border-color: #999;
}

.tooltip {
  position: absolute;
  font-size: 10px;
  top: -100px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 10px;
  border-radius: 5px;
  z-index: 1000;
  width: 100px;
  text-align: middle;
}

.tooltip p {
  margin: 5px 0;
}

.tooltip button {
  margin-top: 10px;
  padding: 5px 10px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.tooltip button:hover {
  background-color: #0056b3;
}
</style>
