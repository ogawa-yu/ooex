import Vue from 'vue';
import Sample from './components/sample.vue';
Vue.config.devtools = true;
Vue.config.debug = true;

new Vue(Sample).$mount('#app');
