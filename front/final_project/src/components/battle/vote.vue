<template>
  <div v-if="!prepared">
    투표를 준비 중 입니다.
  </div>
  <div v-else>
    <div :class = "firstCandidate.memnum.nickname" @:click="voteCandidate(1)">
      {{ firstCandidate }}
    </div>
    <div :class = "secondCandidate.memnum.nickname" @:click="voteCandidate(2)">
      {{ secondCandidate }}
    </div>
  </div>

</template>

<script>
export default{
  data(){
    return {
      dto:{},
      memnum : sessionStorage.getItem("memnum"),
      firstCandidate:{},
      secondCandidate:{}
    }
  },
  created: function(){
    // 현재 로그인 상태 확인.
    let self = this;
    self.$axios.get(`http://localhost:8081/members/${this.memnum}`)
    .then(res =>{
      if(res.status == 200){
          this.dto = res.data.dto;
      }else{
        alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
      }
    })
    .catch(error =>{
      alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
    });

    // 투표대상인 후보들 리스트 뽑기.
    self.$axios.get("http://localhost:8081/battles")
    .then(res =>{
      if(res.status == 200 && res.data.len == 2){
        this.firstCandidate = res.data.list[0];
        this.secondCandidate = res.data.list[1];
        this.prepared = true;
      }else{
        alert("오류 발생으로 인한 후보들 정보 불러오기 실패")
      }})
      .catch(error => {
        alert("오류 발생으로 인한 후보들 정보 불러오기 실패")
        consoel.log(error);
      })
      
  },
  methods:{
    
    // 투표 하기 버튼 클릭시 이벤트 리스너.
    voteCandidate(num){
      let self = this;
      let path = '';
      if(num == 1){
        path = `http://localhost:8081/battles/${this.firstCandidate.batnum}`;
      }else if(num == 2){
        path = `http://localhost:8081/battles/${this.secondCandidate.batnum}`;
      }
      self.$axios.post(path)
      .then(res => {
        if(res.status == 200){
          if(num == 1){
            this.firstCandidate = res.data.dto;
          }else if(num == 2){
            this.secondCandidate = res.data.dto;
          }
        }else{
          alert("오류 발생으로 인해 투표가 제대로 진행되지 않았습니다.");
        }
      })
      .catch(error =>{
        alert("오류 발생으로 인해 투표가 제대로 진행되지 않았습니다.");
      });
    }
  }
}
</script>

