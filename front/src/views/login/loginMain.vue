<!--
作者: Tzpeach3
日期: 2024-06-27
作用: 登录页面组件。包含一个供用户输入用户名和密码的表单，并通过向后端发送请求来处理登录过程。
-->

<template>
    <div class="login-main">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <!-- 标题图片 -->
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <!-- 页面标题 -->
                        <h2>登陆界面</h2>
                        <!-- 使用form元素包裹登录表单 -->
                        <form @submit.prevent="login">
                            <div class="changeDisplay loginPart">
                                <!-- 用户名输入框 -->
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <!-- 密码输入框 -->
                                <el-input type="password" placeholder="密码" v-model="password"></el-input>
                                <div class="login">
                                    <!-- 登录按钮，类型为submit -->
                                    <el-button type="success" native-type="submit">登录</el-button>
                                </div>
                            </div>

                            <div class="clear"></div>

                            <!-- 注册和忘记密码链接 -->
                            <div class="register">
                                <div><router-link to="/regist-main" class="regist">注册</router-link></div>
                                <div><router-link to="/forget-password" class="regist">忘记密码？</router-link></div>
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
                // 密码
                password: ''
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
            // 登录方法
            login() {
                // 发送登录请求到后端
                this.$axios.post('http://10.78.250.34:8081/player/login', {
                    playerName: this.username,
                    passWord: this.password
                })
                .then(response => {
                    // 处理登录成功的情况
                    console.log(response.data);
                    if (response.data.code === 200) { // 根据后端返回的状态码判断请求是否成功
                        // 将用户的id存储到localStorage中
                        localStorage.setItem('token', response.data.data[0]);
                        localStorage.setItem('playerName', response.data.data[1]);
                        // 跳转到主页
                        this.$router.push("/home-main");
                        // 显示成功消息
                        this.$message.success(response.data.message); 
                    } else {
                        // 显示失败消息
                        this.$message.error(response.data.message); 
                    }
                })
                .catch(error => {
                    // 处理登录失败的情况
                    this.$message.error('登录失败，请重试');
                    console.error('登录错误:', error);
                });
            },
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
