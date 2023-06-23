<template>

    <div id="myjoin">
        <h3>회원가입폼</h3>
            
        id: <input type="text" v-model="id">
        <button v-on:click="idcheck">중복체크</button><br/>
        {{ msg }}<br/> 

        pwd: <input type="password" v-model="pwd"> <br/>
        <span v-if="!validatePassword(pwd)" style="color: red;">비밀번호 형식이 올바르지 않다.</span><br/>

        email: <input type="text" v-model="email"><br/>
        gender: <select v-model="gender">
                    <option value="male">남성</option>
                    <option value="female">여성</option>
                </select><br/>
        nickname: <input type="text" v-model="nickname"><br/>
        img:<input type="file" id="f1"><br/>
        <button v-on:click="join" :disabled="!isJoinable">가입</button>
    </div>
</template>

<script>
export default{
    name: 'MemJoin',
    data(){
        return{
            id: '',
            pwd:'',
            email:'',
            gender:'',
            nickname:'',
            msg:''
        }
    },

    computed:{
        isJoinable(){
            return this.validatePassword(this.pwd) && this.msg === '사용가능한 아이디';
        },
    },

    methods:{

        //이미지가입
        join(){
            // //아이디 중복
            // if(this.msg !== '사용가능한 아이디'){
            //     alert('아이디 중복검사를 통과해야 합니다.')
            //     return;
            // }

            //비밀번호 유효성 검사
            // if(this.validatePassword(this.pwd)){
            //     alert('비밀번호 형식이 올바르지 않다.')
            //     return;
            // }


            const self = this;
            
            let formdata = new FormData();
            formdata.append('id', self.id)
            formdata.append('pwd', self.pwd)
            formdata.append('email', self.email)
            formdata.append('gender', self.gender)
            formdata.append('nickname', self.nickname)

            const file = document.getElementById('f1')

            //이미지O 회원가입
            if(file.files[0]){
                formdata.append('mf', file.files[0]);
    
                self.$axios.post('http://localhost:8081/members', formdata,
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
            }else{
                //이미지X 회원가입
                self.$axios.post('http://localhost:8081/members/omem', formdata)
                .then(function(res){
                    if(res.status == 200){
                        let d = res.data.d
                        alert(d.id)
                        location.href='/'
                    }else{
                        alert('에러코드:'+res.status)
                    }
                });
            }
        },

        //중복체크
        idcheck(){
            const self =this;
            self.$axios.get('http://localhost:8081/members/check/'+self.id)
            .then(function(res){
                if(res.status == 200){
                    if(res.data.tf){
                        self.msg='사용가능한 아이디'
                    }else{
                        self.msg='중복된 아이디'
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },

        //비밀번호 정규화
        validatePassword(password){
            //4~12자리, 공백X, 한글X, 영문+숫자, 영대문자1개 포함, 특수문자포함
            const pattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{4,12}$/;
            return pattern.test(password);
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