<template>
    <div id="myjoin">
        <h3>회원가입폼</h3>
        id: <input type="text" v-model="id"> <br/>
        pwd: <input type="password" v-model="pwd"> <br/>
        email: <input type="text" v-model="email"><br/>
        gender: <select v-model="gender">
                    <option value="male">남성</option>
                    <option value="female">여성</option>
                </select><br/>
        nickname: <input type="text" v-model="nickname"><br/>
        img:<input type="file" id="f1"><br/>
        <button v-on:click="join">가입</button>
    </div>
</template>

<script>
export default{
    name: 'MemJoin',
    data(){
        return{
            id:'',
            pwd:'',
            email:'',
            gender:'',
            nickname:'',
        }
    },
    methods:{
        join(){
            const self = this;
            
            let formdata = new FormData();
            formdata.append('id', self.id)
            formdata.append('pwd', self.pwd)
            formdata.append('email', self.email)
            formdata.append('gender', self.gender)
            formdata.append('nickname', self.nickname)

            const file = document.getElementById('f1')
            formdata.append('mf', file.files[0]);

            self.$axios.post('http://localhost:8082/members', formdata,
            { headers: { "Content-Type": "multipart/form-data" } })
            .then(function(res){
                if(res.status == 200){
                    let dto = res.data.dto
                    alert(dto.id)
                    location.href='/'
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