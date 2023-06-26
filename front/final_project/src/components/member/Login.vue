<template>
    <div id="login">
        <h3>옷짱(로고)</h3>
        <div class="form-group">
            <input type="text" id="id" v-model="id" placeholder="ID" class="input-field" @focus="cPlaceholder($event)">
        </div>
        <div class="form-group">
            <input type="password" id="pwd" v-model="pwd" placeholder="Password" class="input-field" @focus="cPlaceholder($event)"><br/>
        </div>
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
        },
        
        //로그인, 패스워드 포커스시 placeholder 값 초기화
        cPlaceholder(event){
            event.target.placeholder= '';
        },

    }
}
</script>

<style scoped>
#login{
    display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background-color: #ffffff;
  max-width: 400px;
  margin: 0 auto;
  }

h3 {
    margin: 0;
    margin-bottom: 20px;
    text-align: center;
  }

  
  .form-group{
    margin-bottom:10px;
    width: 100%;
  }

  label{
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
    text-align: left;
  }
  
  .input-field{
    width: 100%;
  padding: 8px;
  border: none;
  border-bottom: 1px solid #ccc;
  background-color: transparent;
  outline: none;
  }

  .input-field:focus{
    border-bottom: 2px solid #000000;
  }

  button {
  display: block;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 12px;
  background-color: #ebebeb;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
}

button:hover {
  background-color: #000000;
}
</style>