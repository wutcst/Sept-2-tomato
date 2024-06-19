<template>
    <el-container style="height: 100vh;">
        <el-aside width="200px" class="hiddenChange">
            <NavMenu />
        </el-aside>
        <el-container>
            <el-header style="text-align: right; font-size: 12px;width: 100%;">
               
                <el-dropdown>
  <span class="el-dropdown-link" style="font-size: 10px;">
     <span>
                    <el-avatar style="font-size:small">{{ userName}}</el-avatar>
                </span><i class="el-icon-arrow-down el-icon--right"></i>
  </span>
  <el-dropdown-menu slot="dropdown">
    <el-dropdown-item divided><router-link to="/homeUse" class="useDec">日历</router-link></el-dropdown-item>
     <el-dropdown-item divided><router-link to="/login-main" class="useDec">退出</router-link></el-dropdown-item>
  </el-dropdown-menu>
</el-dropdown>
            </el-header>
            <el-main><router-view /></el-main>
        </el-container>
    </el-container>
</template>

<script>
    import axios from 'axios';
    import NavMenu from './navMenu.vue'
    export default {
        methods: {
            fetchUserData() {
      // 发送HTTP请求到后端获取用户数据
      // 假设您的后端接口地址是 http://localhost:8080/user/{userId}，并且userId存储在localStorage中
      const userId = localStorage.getItem('userId'); // 假设userId是用户的id
      axios.get(`http://localhost:8080/user/${userId}`)
          .then(response => {
            // 获取到用户数据后解构并更新userData对象
          const {name} = response.data.data;
          this.userName = name;
          })
          .catch(error => {
            console.error('Error fetching user data:', error);
          });
    },
        },
        components: { NavMenu },
        data(){
            return {
            userName:''
            }
        },
         mounted() {
    // 在组件挂载后获取用户数据
    this.fetchUserData();
  },
    };
</script>

<style scoped>
    /* 重置一些用户代理样式 */
    body,
    html {
        margin: 0;
        padding: 0;
        width: 100%;
        height: 100%;
        overflow: hidden;
        /* 确保没有额外的滚动条 */
        background-image: none;
        /* 去除背景图片 */
    }
     .el-dropdown-link {
    cursor: pointer;
    color: #2c4864;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }

    .el-container {
        background-color: var(--main-theme);
    }

    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 200px;
        min-height: 400px;
    }

    .el-avatar {
        margin-top: 10px;
    }

    .el-dropdown {
        font-size: 25px;
    }

    .el-header {
        background-color: var(--main-header-theme);
        line-height: 60px;
        width: auto;
    }

    .useDec {
        text-decoration: none;
        color: #303133;
    }

    @media screen and (max-width: 800px) {
        .hiddenChange {
            display: none;
        }
    }
</style>