<template>
  <input type="text" v-model="query">
  <input type="button" value="검색" @click="search">
  {{ query }}
  <input v-if="list.length != 0" type="button" value="낮은 가격 순" @click="asc">
  <input v-if="list.length != 0" type="button" value="높은 가격 순" @click="desc">
  <ul v-for="(dto,i) in list" :key="i">
    <li>
      productId : {{ dto.productId }}
    </li>
    <li>
      title : <span :id="i"></span>
    </li>
    <li>
      link : <a :href="dto.link">구매하러 가기</a>
    </li>
    <li>
      image : <img :src="dto.img">  
    </li>
    <li>
      mallName : {{ dto.mallName }}
    </li>
    <li>
      maker : {{ dto.maker }}
    </li>
    <li>
      price : {{ dto.price }}
    </li>
  </ul>
</template>

<script>
export default{
  name:'naverShoppingList',
  data(){
    return{
      query :'',
      list:[]
    }
  },
  methods: {
    search(){
      alert("here")
      let self = this;
      self.$axios.get(`http://localhost:8081/naver/${self.query}`)
      .then(res => {
        if(res.status == 200){
          self.list = res.data.list;
          setTimeout(() => {
            for(let i = 0 ; i < self.list.length; i++){
              let span = document.getElementById(i);
              span.innerHTML = self.list[i].title;
              console.log(span);
            }
          }, 100);
        }else{
          alert("실패 시발!")
        }
      })
    },
    asc(){
      let self = this;
      self.list.sort(function(a,b){
        return a.price - b.price;
      })
    },
    desc(){
      let self = this;
      self.list.sort(function(a,b){
        return b.price - a.price;
      })
    }
  }
}
</script>