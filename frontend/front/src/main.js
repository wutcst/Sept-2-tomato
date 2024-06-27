import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router';
import store from './store';
import App from './App.vue';
import axios from 'axios'
// 设置axios的baseURL
axios.defaults.baseURL = 'http://localhost:8080';
Vue.use(ElementUI);
Vue.config.productionTip = false
//其他vue组件中就可以this.$axios调用使用
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');