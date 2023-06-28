<template>
    <h3>아이디찾기(주의! 아이디3자리 이상 아닐 경우 에러나옴)</h3>
    <h4>가입시 등록한 이메일 입력하면 해당 아이디를 알려드립니다.</h4>
    <input type="text" v-model="email" placeholder="가입하신 이메일">
    <button v-on:click="find">아이디 찾기</button><br/>
    {{ idMasked }}<br/>
    <a href="../FindPwd" v-if="idMasked">비밀번호찾기</a>
</template>

<script>
export default{
    data(){
        return{
            id:'',
            email:'',
            idMasked:''
        }
    },
    
    methods:{
        find(){
            const self = this;
            self.$axios.get('http://localhost:8081/members/email/find/'+self.email)
            .then(function(res){
                if(res.status==200){
                    let dto = res.data.dto
                    if(dto!=null){
                        self.id=dto.id;
                        let str = self.id;
                        let idPre = str.substring(0, 3); //찾은 아이디 앞에 3자리 뽑기
                        self.idMasked = idPre+ '*'.repeat(str.length -3); //나머지는 *로 대체(repeat:문자열이 있는만큼 반복, length -3: 앞에서 세 자리 뺀 길이)
                    }else{
                        alert('없는 이메일입니다.')
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