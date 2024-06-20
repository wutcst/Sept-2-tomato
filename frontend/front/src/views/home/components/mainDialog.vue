<template>
  <div class="dialog">
    <div class="dialog-content">
      <img width="100px" src="../../../assets/images/npc.jpg" alt="">
      <div class="text-display">
        <!-- 动态显示文本的区域 -->
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
</template>

<script>
export default {
  data() {
    return {
      currentLocation: '入口',
      dialogContent: '欢迎来到动物园！',
      displayedText: '',
      displayTimeout: null // 用于存储定时器的引用
    }
  },
  methods: {
    move(direction) {
      // 取消之前的文本显示定时器
      if (this.displayTimeout) {
        clearTimeout(this.displayTimeout);
        this.displayTimeout = null;
        this.displayedText = this.dialogContent; // 立即显示完整的文本
      }

      // 切换方向并更新对话内容
      switch (direction) {
        case 'north':
          this.currentLocation = '北部园区';
          this.dialogContent = '你来到了北部园区，这里有各种北方的动物。';
          break;
        case 'south':
          this.currentLocation = '南部园区';
          this.dialogContent = '你来到了南部园区，这里有各种南方的动物。';
          break;
        case 'east':
          this.currentLocation = '东部园区';
          this.dialogContent = '你来到了东部园区，这里有各种东方的动物。';
          break;
        case 'west':
          this.currentLocation = '西部园区';
          this.dialogContent = '你来到了西部园区，这里有各种西方的动物。';
          break;
        default:
          break;
      }
      
      // 重新开始逐字显示文本内容
      this.displayedText = '';
      this.startTextAnimation();
    },
    startTextAnimation() {
      let index = 0;
      const animate = () => {
        if (index < this.dialogContent.length) {
          this.displayedText += this.dialogContent[index++];
          this.displayTimeout = setTimeout(animate, 50); // 递归调用以逐字显示文本
        } else {
          this.displayTimeout = null;
        }
      };
      animate();
    }
  },
  beforeDestroy() {
    // 组件销毁时清理定时器
    if (this.displayTimeout) {
      clearTimeout(this.displayTimeout);
      this.displayTimeout = null;
    }
  }
}
</script>

<style scoped>
@import "https://unpkg.com/element-ui/lib/theme-chalk/index.css";

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

.stay{
  font-size:18px;
  padding:10px 13px;
  margin-top:10px;
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
</style>
