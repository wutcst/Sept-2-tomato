<template>
  <div class="main-content" :style="{ backgroundImage: `url(${currentLocation.backgroundImage})` }">
    <div class="map">
      <h1>当前位置: <span>{{ currentLocation.name }}</span></h1>
      <div v-if="currentLocation.items.length > 0" class="items">
        <div
          class="item"
          v-for="(item, index) in currentLocation.items"
          :key="item.id"
          @click="takeItem(index)"
        >
          <img :src="item.imageId" :alt="item.name" />
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
        // 可以根据实际需要添加更多物品类型
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
      immediate: true
    }
  },
  methods: {
    updateLocation(roomId) {
      axios.get(`/api/getItemsByRoomId?roomId=${roomId}`)
        .then(response => {
          const itemIds = response.data;
          const items = itemIds.map(itemId => this.itemMap[itemId]);
          this.$set(this.locationMap[roomId], 'items', items);
        })
        .catch(error => {
          console.error('Error fetching items:', error);
        });
    },
    takeItem(index) {
      const item = this.currentLocation.items.splice(index, 1)[0];
      alert(`你拿到了${item.name}`);
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
