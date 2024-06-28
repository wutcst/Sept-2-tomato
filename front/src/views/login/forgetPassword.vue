<!--
作者: Tzpeach3
日期: 2024-06-27
作用: 修改密码页面组件。包含一个供用户输入用户名、新密码和确认密码的表单，并通过向后端发送请求来处理修改密码过程。
-->

<template>
    <div class="forgetPassword">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <!-- 标题图片 -->
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <!-- 页面标题 -->
                        <h2>修改密码</h2>
                        <!-- 使用form元素包裹密码输入框 -->
                        <form @submit.prevent="changePassword">
                            <div class="changeDisplay border2">
                                <!-- 用户名输入框 -->
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <!-- 新密码输入框 -->
                                <el-input type="password" placeholder="新密码" v-model="newPassword"></el-input>
                                <!-- 确认密码输入框 -->
                                <el-input type="password" placeholder="确认密码" v-model="confirmPassword"></el-input>
                            </div>
                            <div class="clear"></div>
                            <div class="register">
                                <!-- 修改密码按钮，类型为submit -->
                                <div><button type="submit" class="useButton">修改密码</button></div>
                                <!-- 返回登录界面链接 -->
                                <div><router-link to="/" class="regist">返回登陆界面</router-link></div>
                            </div>
                        </form>
                    </div>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                // 用户名
                username: '',
                // 新密码
                newPassword: '',
                // 确认密码
                confirmPassword: ''
            };
        },
        mounted() {
            // 添加背景样式
            document.body.classList.add('loginBac');
        },
        destroyed() {
            // 移除背景样式
            document.body.classList.remove('loginBac');
        },
        methods: {
            // 修改密码方法
            changePassword() {
                // 验证用户名和密码不能为空
                if (!this.username || !this.newPassword || !this.confirmPassword) {
                    this.$message.error('用户名和密码不能为空');
                    return;
                }
                // 检查密码长度是否在5到8位之间
                if (this.newPassword.length < 5 || this.newPassword.length > 8) {
                    this.$message.error('密码长度必须在5到8位之间');
                    return;
                }
                // 检查两次输入的密码是否一致
                if (this.newPassword === this.confirmPassword) {
                    // 准备修改密码请求的数据
                    const data = {
                        playerName: this.username,
                        passWord: this.newPassword
                    };
                    // 使用axios发送修改密码请求
                    this.$axios.post('http://10.78.250.34:8081/player/reset-password', data)
                        .then(response => {
                            console.log(response.data);
                            // 处理修改密码成功的情况
                            if (response.data.code === 200) { // 根据后端返回的状态码判断请求是否成功
                                // 跳转到登录页面
                                this.$router.push("/login-main");
                                // 显示成功消息
                                this.$message.success(response.data.message); 
                            } else {
                                // 显示失败消息
                                this.$message.error(response.data.message); 
                            }
                        })
                        .catch(error => {
                            // 显示错误消息
                            this.$message.error(error.response.data.message);
                        });
                } else {
                    // 显示密码不一致的错误消息
                    this.$message.error('两次密码不一致');
                }
            }
        }
    };
</script>

<style scoped>
    @import url("../../assets/css/css_mine.css");

    h2 {
        margin-left: 150px;
        margin-right: 150px;
        border-radius: 10px;
    }
</style>
