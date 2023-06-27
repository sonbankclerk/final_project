<template>

    <div id="myjoin">
        <h2 class="join_title">회원가입</h2>

        <!-- 아이디 입력 폼 -->
        <div class="form_group">
            <label for="id" :class="{'input_label': id}">ID*</label>
            <input type="text" v-model="id" placeholder="ID" class="input_field">
        

        <!-- 아이디 유효성검사 메시지 -->
        <p class="input_error" v-if="id && !validateId(id)">영문과 숫자 8자 이상 16자 이하로 입력해주세요.</p>
         </div>

        <!-- 중복체크 메시지 -->
        <span v-if="msg === '사용가능한 아이디'" class="success_message">{{ msg }}</span>
        <span v-else class="error_message">{{ msg }}</span>
        <button v-on:click="idcheck">중복체크(수정예정)</button><br/>
        
        <!-- 비밀번호 입력 폼 -->
        <div class="form_group">
            <label for="pwd" :class="{'input_label': pwd}">Password*</label>
            <input type="password" v-model="pwd" placeholder="Password" class="input_field">

        <!-- 비밀번호 유효성검사 메시지 -->
        <p class="input_error" v-if="pwd && !validatePassword(pwd)">영문, 숫자, 특수문자를 조합해서 입력해주세요. (4-12자)</p>
        </div>

        <!-- 이메일 입력 폼 -->
        <div class="form_group">
            <label for="email" :class="{input_label: email}">Email*</label>
            <input type="text" v-model="emailId" placeholder="예) intheham@tistory.com" class="input_field">
        </div>

        email: <input type="text" v-model="emailId">@
        <select v-model="emailDomain">
            <option>naver.com</option>
            <option>gmail.com</option>
            <option>daum.net</option>
        </select>

        <!-- 이메일 인증 버튼 -->
        <button v-on:click="authEmail" id="authEmail">인증번호 받기</button><br/>

        <!-- 인증확인 -->
        인증번호(수정예정): <input type="text" v-model="authKey" id="authKey" disabled>
        <input type="text" v-model="compKey" style="display: none;"> <!-- 서버사이드에서 받은 인증키 비교 위해 저장해놓을 더미용 -->
        <button v-on:click="checkKey" disabled id="checkKey">인증번호 확인</button>

        <!-- 인증번호 받기 누르면 시작되는 타이머 -->
        <div id="timer">
            <span id="displayedTime" style="color:lightgray">3분0초</span>
        </div><br/>

        <!-- 성별 -->
        <div class="form_group">
            <label for="gender" :class="{'input_label': gender}">성별*</label>
            <select v-model="gender" placeholder="선택하세요" class="input_field">
                <option value="male">남성</option>
                <option value="female">여성</option>
            </select>
        </div>

        <!-- 닉네임 입력폼 -->
        <div class="form_group">
            <label for="nickname" :class="{'input_label': nickname}">닉네임*</label>
            <input type="text" v-model="nickname" placeholder="닉네임" class="input_field">
        </div>

        <!-- 이미지 선택 -->
        <div class="form_group">
            <label for="f1" :class="{'input_label': f1}">프로필사진</label>
            <input type="file" id="f1" placeholder="사진을 선택하세요." class="input_field">
        </div>
        img(수정예정):<input type="file" id="f1"><br/>

        <!-- 가입버튼 -->
        <button v-on:click="join" :disabled="!isJoinable">가입</button>
        <!-- <button v-else v-on:click="join" :disabled="isJoinable" class="joinBtn">가입</button> -->
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
            emailId:'',
            emailDomain:'',
            compKey:'',
            startTimer:'',
            authComplete:0,
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

        //회원가입
        join(){
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

        //아이디 정규화(8~12자리 이상 영문)
        validateId(id){
            const pattern = /^[a-zA-Z0-9]{8,12}$/;
            return pattern.test(id);
        },

        //비밀번호 정규화
        validatePassword(password){
            //4~12자리, 공백X, 한글X, 영문+숫자, 영대문자1개 포함, 특수문자포함
            const pattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{4,12}$/;
            return pattern.test(password);
        },

        //이메일 인증
        authEmail() {
            const self = this;
            clearInterval(self.startTimer);
            self.email = self.emailId + '@' + self.emailDomain;
            self.$axios.get('http://localhost:8081/members/email/' + self.email)
            .then(function(res){
                if(res.status == 200){
                    let exist = res.data.exist;
                    let key = res.data.key;
                    let authEmail = document.getElementById('authEmail');
                    let authKey = document.getElementById('authKey');
                    let checkKey = document.getElementById('checkKey');
                    let displayedTime = document.getElementById('displayedTime');
                    if(key == null) {
                        alert(exist);
                    } else {
                        authEmail.innerText = '인증번호 재발급'
                        authKey.disabled = false;
                        authKey.focus();
                        checkKey.disabled = false;
                        self.compKey = key;
                        let secondsLeft = 180; // 3분
                        self.startTimer = setInterval(function() {
                            let minutes = Math.floor(secondsLeft / 60);
                            let seconds = secondsLeft % 60;
                            let timerText = minutes + "분" + seconds + "초";
                            displayedTime.style.color = 'black';
                            displayedTime.innerText = timerText;
                            if(secondsLeft <= 0) {
                                clearInterval(self.startTimer);
                                authKey.disabled = true;
                                checkKey.disabled = true;
                                self.compKey = '';
                            }
                            secondsLeft --;
                        }, 1000);

                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },

        //이메일 인증 완료
        checkKey() {
            const self = this;
            let displayedTime = document.getElementById('displayedTime');
            if(self.authKey == self.compKey) {
                clearInterval(self.startTimer);
                displayedTime.innerText = "인증이 완료되었습니다."
                self.authComplete = 0;
            } else {
                alert('인증번호가 틀렸습니다.')
                self.authComplete = 1;
            }
        }
    }
}
</script>

<style scoped>
#myjoin{
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

/* 회원가입 */
.join_title{
    padding-bottom: 46px;
    text-align: center;
    font-size: 28px;
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
    padding: 8px;
    width: 100%;
    border: none;
    border-bottom: 1px solid #ccc;
    background-color: transparent;
    outline: none;
}

/* 유효성 검사 메시지 */
.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
}

/* 가입버튼*/
.joinBtn{
  display: block;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 12px;
  background-color: #000000;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
}



  
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