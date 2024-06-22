import Vue from 'vue';
import axios from 'axios';
import {
    VueAxios
} from 'axios';

import { Notification } from 'element-ui';
// 创建 axios 实例
const service = axios.create({
    baseURL: '/api', // api base_url
    timeout: 1000000, // 请求超时时间
    withCredentials: true,
    crossDomain: true
});

let flag = true;

let requestMethod = '';//记录每一次请求的方式//get请求不控制重复

// request interceptor
service.interceptors.request.use(config => {
    if (localStorage.getItem("token")) {
        config.headers.token = localStorage.getItem("token") || ""
    }
    return config;
}, (err) => {
    console.log(err);
});


// response interceptor
service.interceptors.response.use(response => {
    const status = response.data.status || response.data.code;
    if (response.headers.token) {
        // console.log("设置token",response.headers.token)
        localStorage.setItem('token', response.headers.token);
    }
    if (response.data.code == 401) {
        Notification({
            title: '警告',
            message: 'token过期，即将退出登录',
            type: 'error',
        });
        setTimeout(() => {
            window.location.reload();
        }, 1500);
    } else if (status != 200) {
        return Promise.reject(response.data);
    }

    return response.data;
}, (err) => {
    console.log(err);
});


const installer = {
    vm: {},
    install(Vue) {
        Vue.use(VueAxios, service);
    }
};


export {
    installer as VueAxios,
    service as axios
};
