<template>
    <div id="Mypage">
        <h3>마이페이지</h3>
        id:<input type="text" v-model="id" readonly><br/>
        pwd:<input type="password" v-model="pwd"><br/>
        email:<input type="text" v-model="email" readonly><br/>
        gender:{{ gender }}<br/>
        nickname:<input type="text" v-model="nickname"><br/>
        img:<img :src= "'http://localhost:8081/members/imgs/' + num"><br/>
        <button v-on:click="edit">수정</button>
        <button v-on:click="out">탈퇴</button>
        <button v-on:click="logout">로그아웃</button>
    </div>
</template>
<script>
export default{
    name: 'MyPage',
    data(){
        return{
            id:'',
            pwd:'',
            email:'',
            gender:'',
            nickname:'',
            num : sessionStorage.getItem('memnum')
        }
    },

    created:function(){
        let token = sessionStorage.getItem('token')
        
        const self = this;

        self.$axios.get('http://localhost:8081/members/'+self.num,
        {headers:{'token':token}})
        .then(function(res){
            if(res.status==200){
                let dto = res.data.dto
                if(dto!=null){
                    self.id=dto.id
                    self.pwd=dto.pwd
                    self.email=dto.email
                    self.gender=dto.gender
                    self.nickname=dto.nickname
                }else{
                    alert('없는 아이디거나 만료된 토큰')
                }
            }else{
                alert('에러코드:'+self.status)
            }
        });
    },

    methods:{
        edit(){
            const self = this
            let formdata = new FormData();
            formdata.append('num',self.num)
            formdata.append('pwd',self.pwd)
            formdata.append('nickname', self.nickname)
             let token = sessionStorage.getItem('token')
            self.$axios.put('http://localhost:8081/members',formdata,
            {headers:{'token':token}})
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        let dto = res.data.dto
                        alert(dto.pwd)
                        alert(dto.nickname)
                        // self.pwd = dto.pwd
                        // self.nickname = dto.nickname
                    }else{
                        alert("false가 넘어옴")
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },
        logout(){
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('memnum')
            console.log('memnum')
            location.href='/'
        },
        out(){
            const self = this;
            let token = sessionStorage.getItem('token')
            self.$axios.delete('http://localhost:8081/members/'+self.num,
            {headers:{'token':token}})
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        alert('탈퇴완료')
                        self.logout()
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        }
    }
}
</script>

<style scope>
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