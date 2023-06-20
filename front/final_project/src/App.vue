<template>
  <nav>
    <router-link to="/"><img src="./assets/logo.png" alt=""></router-link> 
    <router-link to="/about">옷장</router-link> |
    <router-link to="/about">커뮤니티</router-link> |
    <router-link to="/about">명예의 전당</router-link> |
    <span v-if="chkDay <= 3 && chkDay > 0"><router-link to="/about">배틀 투표</router-link> |</span>
    <span v-else-if="chkDay == 4"><router-link to="/about">이 주의 우승자</router-link> |</span>
    <span v-else><router-link to="/about">배틀 신청</router-link> |</span>
    <router-link to="/about">로그인</router-link> |
    <router-link to="/about">회원가입</router-link>
  </nav>
  <router-view/>
</template>

<script>
export default{
  data(){
    return{
    }
  },
  computed:{
    chkDay(){
      // 요일 별 배틀 변경.
      let now = new Date();
      return now.getDay();
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

img{
  width: 70px;
  height: 70px;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
