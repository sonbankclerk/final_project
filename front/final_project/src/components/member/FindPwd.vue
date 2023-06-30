<template>
<h2>비밀번호 찾기</h2>
<h3>ID</h3>
<input type="text" v-model="id" placeholder="가입하신 아이디"><br/>
<h3>이메일 주소</h3>
<input type="text" v-model="email" placeholder="예) intheham@tistory.com"><br/>
<button v-on:click="find">인증번호 발송하기</button>
</template>

<script>
export default{
    data(){
        return{
            id:'',
            email:'',
            msg:''
        }
    },

    methods:{
        find(){
            const self = this;
            console.log(this.id)
            let form = new FormData();
            form.append("id",self.id);
            self.$axios.post('http://localhost:8081/members/findPwd/' + self.email,
            form)
            .then(function(res){
                console.log(self.id)
                console.log(self.email)
                if(res.status==200){
                    let randomPwd = res.data.randomPwd.pwd;
                    // let id = res.data.randomPwd
                    // let email = res.data.randomPwd
                    alert(randomPwd)
                    if(randomPwd!=null){
                        self.id = randomPwd.id;
                        self.email = randomPwd.email;
                        self.msg='';
                    }else{
                        self.msg='';
                    }
                }else{
                    alert('에러코드:'+self.status)
                }
            });
        }
    }
}
</script>

<style scoped>
</style>