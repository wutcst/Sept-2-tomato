<template>
  <div class="main-content" :style="{ backgroundImage: `url(${currentLocation.backgroundImage})` }">
    <div class="map">
      <h1>当前位置: <span>{{ currentLocation.name }}</span></h1>
      <div class="items">
        <div
          class="item"
          v-for="(item, index) in currentLocation.items"
          :key="index"
          @click="takeItem(index)"
        >
          <img :src="getItemImage(item)" :alt="item.name" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import mapImage from '../../../assets/images/map.jpg';
import bananaImage from '../../../assets/images/banana.jpg';
import cookieImage from '../../../assets/images/cookie.jpg';
import zooGate from '../../../assets/images/zooGate.jpg'
export default {
  data() {
    return {
      currentLocation: {
        name: '入口',
        backgroundImage: zooGate,
        items: [
          { id: 1, name: '地图', imageId: mapImage, color: '#ffcc99', borderColor: '#cc9966' },
          { id: 2, name: '香蕉', imageId: bananaImage, color: '#ffcc99', borderColor: '#cc9966' },
          { id: 3, name: '魔法饼干', imageId: cookieImage, color: '#ffcc99', borderColor: '#cc9966' },
          // 其他物品信息
        ],
      },
    };
  },
  methods: {
    getItemImage(item) {
      return item.imageId; // 根据物品的 imageId 拼接图片路径
    },
    takeItem(index) {
      // 取出物品的逻辑，这里简单示例直接从当前位置移除物品
      const item = this.currentLocation.items.splice(index, 1)[0];
      alert(`你拿到了${item.name}`);
    },
  },
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
  background-size: cover; /* 背景图片充满容器 */
  background-position: center; /* 居中对齐 */
}
.map {
  background-color: rgba(255, 255, 255, 0.7); /* 地图区域背景色 */
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
  transform: scale(1.1); /* 鼠标悬停时放大图片 */
}
</style>
