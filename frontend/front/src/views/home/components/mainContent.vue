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
import zooGate from '../../../assets/images/zooGate.jpg';
import monkeyZone from '../../../assets/images/monkeyZone.jpg';
import lionZone from '../../../assets/images/lionZone.jpg';
import elephantZone from '../../../assets/images/elephantZone.jpg';
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
          name: '出口',
          backgroundImage: zooExport,
          items: [],
        },
      },
      itemMap: {
        1: { id: 1, name: '地图', imageId: mapImage, color: '#ffcc99', borderColor: '#cc9966' },
        2: { id: 2, name: '香蕉', imageId: bananaImage, color: '#ffcc99', borderColor: '#cc9966' },
        3: { id: 3, name: '魔法饼干', imageId: cookieImage, color: '#ffcc99', borderColor: '#cc9966' }
      }
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
      immediate: true // 在组件创建时立即触发一次侦听器
    }
  },
  methods: {
    updateLocation(roomId) {
      // 这里是从 API 获取物品类别号的示例代码，你需要根据你的 API 进行调整
      fetch(`/api/getItemsByRoomId?roomId=${roomId}`)
        .then(response => response.json())
        .then(data => {
          const items = data.map(itemId => this.itemMap[itemId]);
          this.$set(this.locationMap[roomId], 'items', items);
        })
        .catch(error => {
          console.error('Error fetching items:', error);
        });

      // 更新背景图片等信息
      this.currentLocation = this.locationMap[roomId] || this.locationMap[1];
    },
    getItemImage(item) {
      return item.imageId;
    },
    takeItem(index) {
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
</style>
