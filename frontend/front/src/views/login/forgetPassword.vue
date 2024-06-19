<template>
    <div class="forgetPassword">
        <el-container>
            <el-main>
                <div class="container">
                    <div class="login-form">
                        <div class="title">
                            <img src="../../assets/images/title.png" alt="">
                        </div>
                        <h2>修改密码</h2>
                        <!-- 添加form元素包裹密码输入框 -->
                        <form @submit.prevent="changePassword">
                            <div class="changeDisplay border2">
                                <el-input placeholder="用户名" v-model="username"></el-input>
                                <el-input type="email" placeholder="邮箱" v-model="email"></el-input>
                            </div>
                            <div class="changeDisplay border3">
                                <el-input type="password" placeholder="新密码" v-model="newPassword"></el-input>
                                <el-input type="password" placeholder="确认密码" v-model="confirmPassword"></el-input>
                            </div>
                            <div class="clear"></div>
                            <div class="register">
                                <!-- 将按钮类型改为submit -->
                                <div><button type="submit" class="useButton">修改密码</button></div>
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
                username: '',
                email: '',
                newPassword: '',
                confirmPassword: ''
            };
        },
        mounted() {
            document.body.classList.add('loginBac');
        },
        destroyed() {
            document.body.classList.remove('loginBac');
        },
        methods: {
            changePassword() {
                // 验证用户名和密码不能为空
                if (!this.username || !this.email || !this.newPassword || !this.confirmPassword) {
                    this.$message.error('用户名和密码不能为空');
                    return;
                }
                // 检查密码长度是否在5到8位之间
                if (this.newPassword.length < 5 || this.newPassword.length > 8) {
                    this.$message.error('密码长度必须在5到8位之间');
                    return;
                }
                // 在这里添加修改密码的逻辑
                if (this.newPassword === this.confirmPassword) {
                    // 发送修改密码请求等等
                    const data = {
                        username: this.username,
                        email: this.email,
                        newPassword: this.newPassword
                    };
                    // 使用axios或其他方式发送请求
                    this.$axios.post('/reset-password', data)
                        .then(response => {
                            console.log(response.data);
                            if (response.data.code === 30000) { // 根据后端返回的状态码判断请求是否成功
                                this.$router.push("/login-main");
                                this.$message.success(response.data.msg); // 显示成功消息
                            } else {
                                this.$message.error(response.data.msg); // 显示失败消息
                            }
                        })
                        .catch(error => {
                            this.$message.error(error.response.data.msg);
                        });
                } else {
                    this.$message.error('两次密码不一致');
                }
            }
        }
    };
</script>

<style scoped>
    @import url("../../assets/css/css_mine.css");
</style>