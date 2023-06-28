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
            email:''
        }
    },

    methods:{
        find(){
            const self = this;
            self.$axious.get("http://localhost:8081/members/email/" + self.email)
            // .then(function(res){
            //     if(res.status==200){
            //         let dto = res.dto.data;
                    
            //     }else{
            //         alert('에러코드:'+self.status)
            //     }
            // });
        }
    }
}
</script>

<style scoped>
</style>