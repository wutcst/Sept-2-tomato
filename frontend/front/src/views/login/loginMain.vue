<template>
    <div class="login-main">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <h2>登陆界面</h2>
                        <!-- 使用form元素包裹登录表单 -->
                        <form @submit.prevent="login">
                            <div class="changeDisplay loginPart">
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <el-input type="password" placeholder="密码" v-model="password"></el-input>
                                <div class="login">
                                    <!-- 将按钮类型改为submit -->
                                    <el-button type="success" native-type="submit">登录</el-button>
                                </div>
                            </div>

                            <div class="clear"></div>

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
                username: '',
                password: ''
            };
        },
        mounted() {
            document.body.classList.add('loginBac');
        },
        destroyed() {
            document.body.classList.remove('loginBac');
        },
        methods: {
            login() {
                // 发送登录请求到后端
                this.$axios.post('http://localhost:8080/login', {
                    name: this.username,
                    password: this.password
                })
                    .then(response => {
                        // 处理登录成功的情况
                        console.log(response.data);
                        if (response.data.code === 401) { // 根据后端返回的状态码判断请求是否成功
                            this.$message.error(response.data.msg); // 显示失败消息
                        } else {
                            // 将用户的id存储到localStorage中
                            localStorage.setItem('userId', response.data.data);
                            this.$router.push("/home-main");
                            this.$message.success(response.data.msg); // 显示成功消息
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
</style>