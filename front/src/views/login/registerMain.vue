<!--
作者: Tzpeach3
日期: 2024-06-27
作用: 注册页面组件。包含一个供用户输入用户名和密码的表单，并通过向后端发送请求来处理注册过程。
-->

<template>
    <div class="regist-main">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <!-- 标题图片 -->
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <!-- 页面标题 -->
                        <h2>注册</h2>
                        <!-- 使用form元素包裹注册表单 -->
                        <form @submit.prevent="register">
                            <div class="changeDisplay border2">
                                <!-- 用户名输入框 -->
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <!-- 密码输入框 -->
                                <el-input type="password" placeholder="密码" v-model="password"></el-input>
                            </div>
                            <div class="reRegist"></div>
                            <div class="clear"></div>

                            <!-- 注册和返回登录链接 -->
                            <div class="register">
                                <!-- 注册按钮，类型为submit -->
                                <div><button type="submit" class="useButton">注册</button></div>
                                <!-- 返回登录界面链接 -->
                                <div><router-link to="/" class="regist">返回登录界面</router-link></div>
                            </div>
                        </form>
                    </div>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        data() {
            return {
                // 用户名
                username: '',
                // 密码
                password: '',
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
            // 注册方法
            register() {
                // 验证密码长度
                if (this.password.length < 5 || this.password.length > 8) {
                    this.$message.error('密码长度必须在5-8位之间！');
                    return;
                }

                // 发送注册请求到后端
                axios.post('http://10.78.250.34:8081/player/register', {
                    playerName: this.username,
                    passWord: this.password,
                })
                .then(response => {
                    // 处理注册成功的情况
                    if (response.data.code === 200) {
                        // 显示成功消息
                        this.$message.success(response.data.message);
                        // 跳转到登录页面
                        this.$router.push("/login-main");
                    } else {
                        // 显示失败消息
                        this.$message.error('注册失败'); 
                    }
                })
                .catch(error => {
                    // 显示错误消息
                    this.$message.error(error.response.data.message);
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

    .el-date-editor.el-input,
    .el-date-editor.el-input__inner {
        width: 100%;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #fff;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409eff;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        background-color: #fff;
        width: 178px;
        height: 133px;
        line-height: 138px;
        text-align: center;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
    }

    .hStyle {
        text-align: center;
        margin: 0px;
    }

    @media screen and (max-width: 800px) {
        .avatar-uploader-icon {
            margin-left: -13%;
        }

        .textUse {
            margin-left: -8%;
        }

        .hStyle {
            margin-left: 4%;
        }
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
