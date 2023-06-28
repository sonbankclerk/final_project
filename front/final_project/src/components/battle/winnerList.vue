<template>

<div v-if ="list.length == 0">
  <notYet></notYet>
</div>
<div v-else>
    <div class="main">
        <div v-for="particle in particles" :key="particle.id" class="particule" :style="{ 
          transform: 'translate3d(' + particle.x + 'px, ' + particle.y + 'px, 1px)', 
          width: particle.size + 'px',
          height: particle.size + 'px',
          background: particle.color
        }"></div>
      </div>
    <div>
    <div class = "title">명예의 전당</div>
    <div class="winner-list">
      <ul v-for="(winner,i) in list" :key="i">
        <li class="list-winner">
        <div class="card">
          <div class="round-cnt">
            ROUNDNUM #{{ winner.roundcnt }} 
          </div>
          <div class="nickname">
            {{ nickname[i] }}
          </div>
          <div class="winner-content">
            <div class="theme">
              <span class="small-title">테마</span> <br><br>
              <span class="small-content">
                {{ winner.theme }}
              </span>  
            </div>
            <div class="bar"></div>
            <div class="gender">
              <span class="small-title">성별</span> <br><br>
              <span class="small-content">
                {{ winner.gender }}
              </span>  
            </div>
          </div>
          <div class="imgPosition">
            <img :src="'http://localhost:8081/battles/imgs/' + winner.batnum" alt="이미지 불러오기 실패">
          </div>
        </div>
       </li>
      </ul>
    </div>
  </div>
</div>
</template>

<script>
import notYet from './notYet.vue'

export default{
  name:'winnerList',
  data(){
    return{
      dto:{},
      list:[1],
      particles:[],
      nickname:[],
      colors:["#eb6383", "#fa9191", "#ffe9c5", "#b4f2e1"]
    }
  },
  mounted(){
      this.pop();
      this.render();
  },
  components : {
    notYet : notYet
  },
  created: function(){
    this.winnerList();
  },
  methods:{
    pop() {
      for (let i = 0; i < 150; i++) {
        const particle = {
          id: i,
          x: window.innerWidth * 0.5,
          y: window.innerHeight + Math.random() * window.innerHeight * 0.3,
          vel: {
            x: (Math.random() - 0.5) * 10,
            y: Math.random() * -20 - 15
          },
          mass: Math.random() * 0.2 + 0.8,
          size: Math.random() * 15 + 5,
          color: this.colors[Math.floor(Math.random() * this.colors.length)]
        };
        this.particles.push(particle);
      }
    },
    render() {
      for (let i = this.particles.length; i--; i > -1) {
        const particle = this.particles[i];
        particle.x += particle.vel.x;
        particle.y += particle.vel.y;
        particle.vel.y += 0.5 * particle.mass;
        if (
          particle.y > window.innerHeight * 1.2 ||
          particle.x > window.innerWidth
        ) {
          this.particles.splice(i, 1);
        }
      }
      requestAnimationFrame(this.render);
    },
    winnerList(){
      let self = this;
    self.$axios.get('http://localhost:8081/battles/winnerlist')
    .then(res =>{
      if(res.status == 200){
        self.list = res.data.list;
        for(let dto of self.list){
          console.log(dto.memnum.nickname);
          this.nickname.push(dto.memnum.nickname);
        }
        console.log(this.list);
      }else{
        alert("오류로 인해 명예의 전당 활성화 불가")
      }
    });
    },
  }
}
</script>

<style scoped>
  @import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Diphylleia&display=swap');
.main {
  place-items: center;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  overflow-y: scroll;
}

.particule {
  position: absolute;
  border-radius: 50%;
  box-shadow: 1px 1px 4px #eb6383;
}


/* =========================================== */

.winnerList{
  width:100%;
  padding:auto;
}

.title{
  font-family: 'Black Han Sans', sans-serif;
  font-size: 50px;
  height:150px;
  line-height: 150PX;
  color: #68a162;
  left:0;
  right:0;
  text-align: center;
}

.winner-list{
  left:0;
  right:0;
  margin:auto;
  width:1000px;
}


ul{
  float:left;
}

li{
  margin-left : 30px;
  margin-bottom: 50px;
}

.card{
  width: 200px;
  padding: 20px 50px;
  margin:auto;
  left:0;
  right:0;
  position: relative;
  box-shadow: 0 10px 30px rgba(0,0,0,.25);
  animation-name: winner-list;
  animation-duration:3s;
}

@keyframes winner-list{
  0%{
    opacity: 0;
  }
  100%{
    opacity: 1;
  }
}
.round-cnt{
  background-color:#68a162;
  width: 120px;
  margin-top:20px;
  border-radius: 5px;
  font-size: 14px;
}

.nickname{
  font-family: 'Black Han Sans', sans-serif;
  font-size: 30px;
  text-align: left;
  margin-top:14px;
  color: #68a162;
}

.winner-content{
  display: flex;
  justify-content: space-around;
  margin-top:14px;
}

.small-title{
  font-weight: bold;
  font-size: 20px;
}
.small-content{
  color: #68a162;
  font-weight: bold;
}
.bar{
  width : 1px;
  height: 50px;
  background-color:black;
}

.imgPosition{
  display:absolute;
  left:0;
  right:0;
  margin: 25px auto;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  overflow: hidden;
}

img{
  width: 100%;
  height: 100%;
}


</style>