<template>
  <div v-if="visible" class="map-layer">
    <button @click="closeMapLayer">关闭</button>
    <img src="../../../assets/images/zooMap.jpg" alt="地图" />
  </div>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      required: true
    }
  },
  methods: {
    closeMapLayer() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>
.map-layer {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.map-layer img {
  max-width: 90%;
  max-height: 90%;
}

.map-layer button {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  background-color: #ff0000;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.map-layer button:hover {
  background-color: #cc0000;
}
</style>
