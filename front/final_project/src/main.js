import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

//createApp(App).use(store).use(router).mount('#app')
const app = createApp(App)//vue 객체 생성
app.use(store)// 전역 저장 라이브러리 사용
app.use(router)//라우터 사용설정 . url 별로 실행될 component등록
app.config.globalProperties.$axios = axios;
app.mount('#app')

