<template>
    <div id="login">
        <h3>옷짱(로고)</h3>
        
        <!-- 아이디 입력 폼 -->
        <div class="form_group">
            <div :class="{'input_box_error' : hasIdError}">
            <label for ="id" :class="{'input_label': id}">ID</label>
            <input type="text" id="id" v-model="id" placeholder="ID" :class="{'input_field': true, 'input_field_error': hasIdError }" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateId($event)">
        </div>

        <!-- 아이디 유효성 검사 -->
        <p class="input_error" v-if="hasIdError">영문과 숫자 8자 이상 16자 이하로 입력해주세요.</p>
        </div>

        <!-- 패스워드 입력 폼 -->
        <div class="form_group">
            <div :class="{'input_box_error' : hasPwdError}">
            <label for="pwd" :class="{'input_label': pwd}">Password</label>
            <input type="password" id="pwd" v-model="pwd" placeholder="Password" :class="{'input_field': true, 'input_field_error': hasPwdError }" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validatePwd($event)"><br/>
        </div>

        <!-- 패스워드 유효성 검사 -->
        <p class="input_error" v-if="hasPwdError">대문자, 영문, 숫자, 특수문자를 조합해서 입력해주세요. (4-12자)</p>
        </div>

        <!-- 로그인 버튼 -->
        <!-- <button v-on:click ="login" :disabled="!isEnabled" :class="{'loginBtn': isEnabled, 'loginBtn_disabled': !isEnabled}" >로그인</button> -->
        <!-- 유효성검사 없는 로그인버튼 -->
        <button v-on:click ="login"  :class="{'loginBtn': isEnabled}" >로그인</button>

        <!-- 회원가입, 아이디찾기, 비밀번호찾기 -->
        <ul class="look_box">
            <li class="look_list">
                <a href="../Join" class="look_link">가입</a>
            </li>
            <li class="look_list">
                <a href="../FindId" class="look_link">아이디 찾기</a>
            </li>
            <li class="look_list">
                <a href="../FindPwd" class="look_link">비밀번호 찾기</a>
            </li>
        </ul>
    </div>
    
</template>

<script>
export default{
    name: 'MemLogin',
    data(){
        return{
            id:'',
            pwd:'',
            hasIdError:false,
            hasPwdError:false,
            isEnabled: false
        }
    },

    computed:{
        isFormValid(){
            return !this.hasIdError && !this.hasPwdError;
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
        
        //로그인, 패스워드 포커스시 초기화, 복구
        cPlaceholder(event){
            event.target.previousElementSibling.classList.add('active');
            event.target.placeholder= '';
        },
        rPlaceholder(event){
            const inputField = event.target;
            const label = inputField.previousElementSibling;
            if(!inputField.value){
                label.classList.remove('active');
                if(inputField.id ==='id'){
                    inputField.placeholder = 'ID';
                }else if(inputField.id === 'pwd'){
                    inputField.placeholder = 'Password';
                }
            }
        },

        //아이디 정규화(8~12자리 이상 영문)
        validateId(event){
            const id = event.target.value;
            const regex = /^[a-zA-Z0-9]{8,12}$/;
            this.hasIdError = !regex.test(id);
            this.enabledState();
        },

        //비밀번호 정규화
        validatePwd(event){
            const pwd = event.target.value;
            const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{4,12}$/;
            this.hasPwdError = !regex.test(pwd);
            this.enabledState();
        },

        enabledState(){
            if(!this.hasIdError && !this.hasPwdError){
                this.isEnabled = true;
            }else{
                this.isEnabled = false;
            }
        }
    }
}
</script>

<style scoped>
.loginBtn{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    background-color: #000000;
    color: #fff;
    font-weight: bold;
}

.loginBtn:hover{
    cursor:pointer;
}

.loginBtn_disabled{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    background-color: #ededed;
    color: #fff;
    font-weight: bold;
}

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

  
.form_group{
    margin-bottom:10px;
    width: 100%;
}

label{
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
    text-align: left;
    font-size: 13px;
    padding-left: 8px;
}
  
.input_field{
    position: relative;
    width: 100%;
  padding: 8px;
  border: none;
  border-bottom: 1px solid #ccc;
  background-color: transparent;
  outline: none;
}

#login .input_field:focus{
    border-bottom: 2px solid #000000;
}
  
.input_field:focus + .input_label{
    top: -10px;
    font-size: 10px;
    color: #000000;
}

.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
    padding-left: 8px;
}

.input_field_error, 
.input_box_error .input_field,
.input_box_error .input_label,
.input_box_error .input_error{
    border-bottom-color: 2px solid #f15746;
}

button {
  display: block;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 12px;
  background-color: #000000;
  color: #fff;
  font-weight: bold;
}


.look_box{
    width: 100%;
    display: flex;
    justify-content: center;
    padding-top: 20px;
}

.look_list{
   display: inline-flex;
   align-items: flex-start;
   flex: 1;
}

.look_link{
    margin: auto;
    display: inline-flex;
    font-size: 13px;
}

</style>