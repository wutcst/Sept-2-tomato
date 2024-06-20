<template>
  <el-container class="home-container">
    <el-container>
      <el-main class="main-content">
        <main-content></main-content>
        <div class="dialog-container">
          <main-dialog></main-dialog>
        </div>
      </el-main>
      <el-aside width="300px" class="sidebar-container">
        <app-sidebar></app-sidebar>
      </el-aside>
    </el-container>
  </el-container>
</template>

<script>
import MainContent from './components/mainContent.vue'
import AppSidebar from './components/appSidebar.vue'
import MainDialog from './components/mainDialog.vue'

export default {
  name: 'home-main',
  components: {
    MainContent,
    AppSidebar,
    MainDialog
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
</style>
