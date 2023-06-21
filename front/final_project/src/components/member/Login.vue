<template>
    <div id="login">
        <h3>로그인폼</h3>
        id: <input type="text" v-model="id"> <br/>
        pwd: <input type="password" v-model="pwd"><br/>
        <button v-on:click="login">로그인</button>
    </div>
</template>

<script>
export default{
    name: 'MemLogin',
    data(){
        return{
            id:'',
            pwd:''
        }
    },
    methods:{
        login(){
            const self = this;
            const form = new FormData();
            form.append('id', self.id)
            form.append('pwd',self.pwd)

            self.$axios.post('http://localhost:8081/members/login', form)
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        sessionStorage.setItem('token', res.data.token)
                        sessionStorage.setItem('memnum', res.data.memnum)
                        alert('로그인 성공')
                        location.href='/'
                    }else{
                        alert('로그인 실패')
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        }
    }
}
</script>

<style scoped>
h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }
</style>