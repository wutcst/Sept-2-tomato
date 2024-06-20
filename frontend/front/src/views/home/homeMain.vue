<template>
  <el-container class="home-container">
    <el-container>
      <el-main class="main-content">
        <main-content :currentRoomId="currentRoomId"></main-content>
        <div class="dialog-container">
          <div class="dialog">
            <div class="dialog-content">
              <img width="100px" src="../../assets/images/npc.jpg" alt="">
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
        </div>
      </el-main>
      <el-aside width="300px" class="sidebar-container">
        <app-sidebar></app-sidebar>
      </el-aside>
    </el-container>
  </el-container>
</template>

<script>
import MainContent from './components/mainContent.vue';
import AppSidebar from './components/appSidebar.vue';
import axios from 'axios';

export default {
  name: 'home-main',
  components: {
    MainContent,
    AppSidebar,
  },
  data() {
    return {
      areaMap: {
        '入口': { roomId: 1, north: '猴子园区', east: '狮子园区'},
        '猴子园区': { roomId: 2, south: '入口',east:'大象园区' },
        '狮子园区': { roomId: 3, west: '入口'},
        '大象园区': { roomId: 4, south:'出口',west: '猴子园区' },
        '出口': { roomId: 5, north: '大象园区', west: '狮子园区'},
      },
      directMap:{
        stay:{id:0,text:'停留'},
        north:{id:1,text:'北方向'},
        south:{id:2,text:'南方向'},
        east:{id:3,text:'东方向'},
        west:{id:4,text:'西方向'},
      },
      currentLocation: '入口',
      currentRoomId: 1, // 初始化为入口的 roomId
      dialogContent: '欢迎来到动物园！',
      displayedText: '',
      displayTimeout: null
    };
  },
  methods: {
    // 获取存储在 localStorage 中的 playerId
    getPlayerId() {
      return localStorage.getItem('playerId');
    },
    async fetchPlayerRoomId(playerId) {
      try {
        // 发送 GET 请求获取当前玩家的 roomId
        const response = await axios.get(`/api/player/${playerId}/currentRoomId`);
        const roomId = response.data.roomId; // 假设后端返回的数据结构是 { roomId: 1 } 这样的格式
        this.currentLocation = this.getAreaNameByRoomId(roomId); // 根据 roomId 更新当前位置
        this.currentRoomId = roomId; // 更新 currentRoomId
      } catch (error) {
        console.error('Failed to fetch player roomId:', error);
        // 处理错误情况
      }
    },
    getAreaNameByRoomId(roomId) {
      // 根据 roomId 查找对应的园区名称
      for (const area in this.areaMap) {
        if (this.areaMap[area].roomId === roomId) {
          return area;
        }
      }
      return '入口'; // 默认返回入口，或者根据实际情况处理
    },
    move(direction) {
      if (this.displayTimeout) {
        clearTimeout(this.displayTimeout);
        this.displayTimeout = null;
        this.displayedText = this.dialogContent;
      }

      const currentArea = this.currentLocation;
      const nextArea = this.areaMap[currentArea][direction];
      
      if (nextArea) {
        // Valid move
        this.currentLocation = nextArea;
        const nextRoomId = this.areaMap[nextArea].roomId;  // 获取下一个园区的 roomId
        this.currentRoomId = nextRoomId; // 更新 currentRoomId
        this.dialogContent = `往${this.directMap[direction].text}走，你来到了${nextArea}（园区号：${nextRoomId}）。`;

        // 向后端发送请求更新用户的房间Id
        this.updatePlayerRoomId(nextRoomId);  // 使用 this.updatePlayerRoomId
      } else if(this.directMap[direction].id===0)
          this.dialogContent = `你现在在${this.currentLocation}`;
      else {
        // Invalid move (dead end)
        this.dialogContent = `往${this.directMap[direction].text}走是死路，你现在在${this.currentLocation}`;
      }

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
    },
    // 更新玩家房间Id的方法
    updatePlayerRoomId(nextRoomId) {
      const playerId = this.getPlayerId();  // 获取 playerId
      if (!playerId) {
        console.error('playerId 未找到');
        return;
      }
      axios.put(`/api/player/${playerId}/room`, { roomId: nextRoomId })  // 使用 playerId 访问后端 API
        .then(response => {
          console.log('玩家房间Id更新成功：', response.data);
          // 可以根据后端的返回进行适当的处理
        })
        .catch(error => {
          console.error('更新玩家房间Id失败：', error);
          // 处理更新失败的情况
        });
    },
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
.home-container {
  background-image: url("../../assets/images/background.png");
  background-size: 100%;
  height: 100vh;
  width: auto;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.dialog-container {
  margin-top: auto;
  height: 150px;
  width: 100%;
  background-color: #fff;
  box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.1);
}

.sidebar-container {
  display: flex;
  flex-direction: column;
}

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

.stay {
  font-size: 18px;
  padding: 10px 13px;
  margin-top: 10px;
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
