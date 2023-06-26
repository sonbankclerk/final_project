<template>
  <div v-if = "!prepared">
    <notYet></notYet>      
  </div>
  <div v-else>
    theme : {{ theme }} <br>
    memnum : {{ memnum }} <br>
    gender : {{ gender }} <br>
    roundcnt : {{ roundcnt }} <br>
    dto : {{ dto }}
    <img :src="'http://localhost:8081/members/imgs/' + memnum" alt="왜 안나옴 ㅋㅋ">
    <div v-if="chk">
      <input type="file">
      <button @:click="applyBattle">신청 하기</button>
    </div>
    <div v-else>
      이미 신청하였습니다.
    </div>
  </div>
</template>

<script>
import notYet from '@/components/battle/notYet.vue'
export default {
  name : 'battleApply',
  data(){
    return{
      theme:'',
      memnum: sessionStorage.getItem("memnum"),
      gender: '',
      roundcnt: 0,
      dto : {},
      chk : true,
      prepared : false
    }
  },
  components:{
    notYet : notYet
  },
  created: function(){
    let token = sessionStorage.getItem('token');
    const self = this;
    
    // dto 로그인 정보로 저장하기.
    if(self.memnum == undefined){
      alert("로그인 후 사용 가능합니다.");
      location.href ="/";
    }
    self.$axios.get(`http://localhost:8081/members/${this.memnum}`,{headers:{'token':token}})
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        this.dto = res.data.dto;
        this.gender = self.dto.gender;
      }else{
        alert("오류 발생으로 인한 로그인 정보 불러오기 실패");
      }
    });
  

    // 대결 테마 들고 오기.
    self.$axios.get('http://localhost:8081/battles/info')
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        // 대결 테마가 변경되어있는 지 확인하는 if문
        if(res.data.changeTheme){
          this.theme = res.data.theme;
          this.roundcnt = res.data.roundcnt;
          self.prepared = true;
        }
      }else{
        alert("오류 발생으로 인한 테마 정보 불러오기 실패");
      }
    })
    
    // 신청 유무 확인하기.
    self.$axios.get(`http://localhost:8081/battles/chk/${this.memnum}`)
    .then(res =>{
      if(res.status == 200){
        self.chk = res.data.chk;
      }else{
        alert("체크 확인 불가.")
      }
    })

  },
  methods:{

    // 신청 하기 버튼 클릭 시 실행되는 메소드
    applyBattle(){
      const self = this;
      let file = document.querySelector("input");
      alert(file)
      let formdata = new FormData();
      formdata.append("memnum",this.memnum);
      formdata.append("theme",this.theme);
      formdata.append("gender",this.gender);
      formdata.append("roundcnt",this.roundcnt);
      formdata.append("mf",file.files[0]);
      
      self.$axios.post('http://localhost:8081/battles',formdata,
      {headers : {"Content-Type":"multipart/form-data"}})
      .then(res =>{
        if(res.status == 200 || res.data.flag){
          alert("신청 완료.");
          location.href = "/"
        }else{
          alert("오류 발생으로 인한 신청 실패")
        }
      })
    }
  }
}
</script>

<style>

</style>