<template>
  <div v-if="!prepared">
    <notYet></notYet>
  </div>
  <div v-else>
    <img :src="'http://localhost:8081/battles/imgs/'+firstCandidate.batnum" @click="voteCandidate(firstCandidate.batnum)" alt="첫번째 후보">
     VS 
    <img :src="'http://localhost:8081/battles/imgs/'+secondCandidate.batnum" @click="voteCandidate(secondCandidate.batnum)" alt="두번째 후보">
  </div>

</template>

<script>
import notYet from '@/components/battle/notYet.vue'

export default{
  name : 'battleVote',
  data(){
    return {
      dto:{},
      memnum : sessionStorage.getItem("memnum"),
      firstCandidate:{},
      secondCandidate:{},
      prepared : false,
      chk : true
    }
  },
  components:{
    notYet
  },
  created: function(){
    // 현재 로그인 상태 확인.
    let self = this;
    let token = sessionStorage.getItem('token');
    if(this.memnum == null){
      alert("로그인 후 사용 가능합니다.");
      location.href = "/"
    }else{

      self.$axios.get(`http://localhost:8081/members/${this.memnum}`,
      {headers:{'token':token}})
      .then(res =>{
        if(res.status == 200){
          this.dto = res.data.dto;
        }else{
          alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
        }
      })
      
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

      // 투표 가능 유무 확인. (투표 한 번 하면 한 번더 못함.)
      self.$axios.get(`http://localhost:8081/votes/chk/${self.memnum}`)
      .then(res =>{
        if(res.status == 200){
          self.chk = res.data.chk;
        }else{
          alert("오류 발생으로 인한 투표 유무 확인 불가")
        }
      })

    }
        
  },
  methods:{
        
    // 투표 하기 버튼 클릭시 이벤트 리스너.
    voteCandidate(num){
      let self = this;
      if(!self.chk){
        alert('이미 투표 하였습니다.')
        location.href="/"
      }else{

        alert(num);
        let form = new FormData();
        form.append("memnum",self.memnum);
        form.append("batnum",num);
        self.$axios.post('http://localhost:8081/votes',form)
        .then(res => {
          if(res.status == 200){
            alert(res.data.batnum);
            location.reload();
            if(num == 1){
              this.firstCandidate = res.data.dto;
            }else if(num == 2){
              this.secondCandidate = res.data.dto;
            }

          }else{
            alert("오류 발생으로 인해 투표가 제대로 진행되지 않았습니다.");
          }
        })
      }
    }
  }
}
</script>

