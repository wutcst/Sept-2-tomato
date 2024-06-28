<!--
作者: Tzpeach3
日期: 2024-06-27
作用: 显示地图层的组件。
-->

<template>
    <!-- 如果 visible 为 true，则显示地图层 -->
    <div v-if="visible" class="map-layer">
        <!-- 关闭按钮，点击时调用 closeMapLayer 方法 -->
        <button @click="closeMapLayer">关闭</button>
        <!-- 地图图片 -->
        <img src="../../../assets/images/zooMap.jpg" alt="地图" />
    </div>
</template>

<script>
    export default {
        // 接收 visible 作为 prop，用于控制地图层的显示和隐藏
        props: {
            visible: {
                type: Boolean,
                required: true
            }
        },
        methods: {
            // 关闭地图层的方法，通过 $emit 触发 'close' 事件
            closeMapLayer() {
                this.$emit('close');
            }
        }
    }
</script>

<style scoped>
    /* 地图层样式 */
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

    /* 地图图片样式 */
    .map-layer img {
        max-width: 90%; 
        max-height: 90%; 
    }

    /* 关闭按钮样式 */
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

    /* 关闭按钮悬停效果 */
    .map-layer button:hover {
        background-color: #cc0000; 
    }
</style>
