<template>
    <div class="regist-main">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <h2>注册</h2>
                        <!-- 使用form元素包裹注册表单 -->
                        <form @submit.prevent="register">
                            <div class="changeDisplay border2">
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <el-input type="password" placeholder="密码" v-model="password"></el-input>
                                <el-input type="email" placeholder="电子邮箱" v-model="email"></el-input>
                            </div>
                            <div class="reRegist"></div>
                            <div class="clear"></div>

                            <div class="register">
                                <!-- 将按钮类型改为submit -->
                                <div><button type="submit" class="useButton">注册</button></div>
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
                username: '',
                password: '',
                email: '',
            };
        },
        mounted() {
            document.body.classList.add('loginBac');
        },
        destroyed() {
            document.body.classList.remove('loginBac');
        },
        methods: {
            // 验证邮箱格式
            validateEmail(email) {
                const emailRegex = /\S+@\S+\.\S+/;
                return emailRegex.test(email);
            },
            register() {
                if (!this.validateEmail(this.email)) {
                    this.$message.error('请输入有效的邮箱地址！');
                    return;
                }

                if (this.password.length < 5 || this.password.length > 8) {
                    this.$message.error('密码长度必须在5-8位之间！');
                    return;
                }

               
                // 发送注册请求到后端
                axios.post('http://localhost:8080/register', {
                    name: this.username,
                    password: this.password,
                    email: this.email,
                })
                    .then(response => {
                        if (response.data.code === 400 || response.data.code === 401 || response.data.code === 500) {
                            this.$message.error(response.data.msg); // 显示失败消息
                        } else {
                            this.$message.success(response.data.msg);
                            this.$router.push("/login-main");
                        }
                    })
                    .catch(error => {
                        this.$message.error(error.response.data.msg);
                    });
            },
        }
    };
</script>

<style scoped>
    @import url("../../assets/css/css_mine.css");

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