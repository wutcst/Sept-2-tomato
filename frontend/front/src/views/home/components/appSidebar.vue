<template>
  <div class="sidebar">
    <h2>背包 ({{ usedSpace }}/{{ totalSpace }})</h2>
    <div class="inventory">
      <div
        class="item"
        v-for="(item, index) in inventoryWithImages"
        :key="index"
        @click="selectItem(index)"
        :style="{ backgroundColor: item.color, border: '1px solid ' + item.borderColor }"
      >
        <img :src="item.image" :alt="item.name" />
      </div>
      <div class="item empty" v-for="n in freeSpace" :key="'empty-' + n"></div>
    </div>
    <!-- <div class="controls">
      <el-button type="primary" @click="addItem('地图')">添加地图</el-button>
      <el-button type="danger" @click="removeItem()">移除最后一个物品</el-button>
    </div> -->
  </div>
</template>

<script>
import mapImage from '../../../assets/images/map.jpg';
// import bananaImage from '../../../assets/images/banana.jpg';
// import cookieImage from '../../../assets/images/cookie.jpg';
export default {
  name: 'AppSidebar',
  data() {
    return {
      totalSpace: 6, // 背包总空间
      usedSpace: 0,  // 已使用空间
      inventory: [], // 背包中的物品列表
      items: {
        地图: { name: '地图', image: mapImage, color: '#ffcc99', borderColor: '#cc9966' },
      }
    }
  },
  computed: {
    inventoryWithImages() {
      return this.inventory.map(item => this.items[item]);
    },
    freeSpace() {
      return this.totalSpace - this.usedSpace;
    }
  },
  methods: {
    addItem(itemName) {
      if (this.usedSpace < this.totalSpace) {
        this.inventory.push(itemName);
        this.usedSpace++;
      } else {
        alert('背包已满！');
      }
    },
    removeItem() {
      if (this.inventory.length > 0) {
        this.inventory.pop();
        this.usedSpace--;
      } else {
        alert('背包已空！');
      }
    },
    selectItem(index) {
      // 可以在这里添加处理选中物品的逻辑
      console.log('选中了物品：', this.inventory[index]);
    }
  }
}
</script>

<style scoped>
.sidebar {
  border-radius: 10px;
  background-color: rgb(255, 255, 255,0.7);
  padding: 5px;
  margin: 20px;
  margin-bottom: 0px;
}

.inventory {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 背包格子布局，每行3个格子 */
  gap: 10px; /* 格子之间的间隔 */
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
</style>
