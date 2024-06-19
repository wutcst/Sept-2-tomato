<template>
    <el-aside class="nav-menu">
        <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
            :collapse="isCollapse" router>
            <el-menu-item index="/homeUse">
                <i class="el-icon-location"></i>
                <span slot="title">首页</span>
            </el-menu-item>
            <el-submenu index="2">
                <template slot="title">
                    <i class="el-icon-menu"></i>
                    <span>功能菜单</span>
                </template>
                <el-menu-item index="/userManage" class="el-icon-s-custom">用户管理</el-menu-item>
                <el-menu-item index="/moneyManage" class="el-icon-s-order">余额管理</el-menu-item>
            </el-submenu>
            <el-submenu index="3">
                <template slot="title">
                    <i class="el-icon-cloudy"></i>
                    <span>主题切换</span>
                </template>
                <el-menu-item class="el-icon-sunrise-1" @click="switchTheme('blue')">蓝色风格</el-menu-item>
                <el-menu-item class="el-icon-moon-night" @click="switchTheme('yellow')">黄色风格</el-menu-item>
            </el-submenu>
            <el-menu-item>
                <i class="el-icon-document"></i>
                <span slot="title">导航四</span>
            </el-menu-item>
            <el-menu-item>
                <span slot="title">
          <span @click="showLogoutDialog" class="exit">
            <i class="el-icon-setting"></i>退出登录
          </span>
        </span>
            </el-menu-item>
        </el-menu>
        <el-dialog
      title="退出登录确认"
      :visible.sync="logoutDialogVisible"
      width="30%"
      center>
      <span>确定要退出登录吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelLogout">取消</el-button>
        <el-button type="primary" @click="confirmLogout">确定</el-button>
      </span>
    </el-dialog>
    </el-aside>
</template>

<style scoped>
    .nav-menu {
        width: 200px;
        /* 设置导航栏的宽度 */
        display: flex;
        /* 使用 Flexbox 布局 */
        flex-direction: column;
        /* 垂直排列子元素 */
    }

    .el-menu {
        background-color: var(--main-theme);
    }

    .exit {
        text-decoration: none;
        color: #303133;
    }
</style>

<script>
    export default {
        data() {
            return {
                isCollapse: false, // 默认为 false
                logoutDialogVisible: false // 控制退出登录确认框的显示与隐藏
            };
        },
        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            showLogoutDialog() {
      this.logoutDialogVisible = true; // 显示退出登录确认框
    },
    cancelLogout() {
      this.logoutDialogVisible = false; // 取消退出登录操作，隐藏确认框
    },
    confirmLogout() {
      // 执行退出登录操作，比如清除用户登录状态等
      // 然后跳转到登录页面
      this.$router.push('/login-main');
    },
    switchTheme(theme) {
        if (theme === 'blue') {
            // 切换到蓝色风格
            document.documentElement.style.setProperty('--main-theme', '#dbe6f6');
            document.documentElement.style.setProperty('--main-header-theme', '#5486d7');
        } else if (theme === 'yellow') {
            // 切换到黄色风格
            document.documentElement.style.setProperty('--main-theme', '#efe3ce');
            document.documentElement.style.setProperty('--main-header-theme', '#c48e44');
        }
    }
}
};
</script>