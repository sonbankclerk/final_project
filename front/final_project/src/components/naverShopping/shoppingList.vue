<template>
  <div class="search-wrapper">
    <input type="text" v-model="query" placeholder="검색어를 입력해주세요." @keyup.enter="search">
    <img src="../../assets/Search-icon.png" alt="click" @click="search" class="search" @mouseover="cursorChange" >
    <span v-if="list.length != 0" class="price-buttons">
      <input type="button" value="낮은 가격 순" @click="asc">
      <input type="button" value="높은 가격 순" @click="desc">
    </span>
  </div>
  
  <span v-if="list.length == 0" > 
    <div class="cover">
      <p class="first-parallel"></p>
    </div>
    <div class="cover">
      <p class="second-parallel"></p>
    </div>
    <div class="cover">
      <p class="third-parallel"></p>
    </div>
    <div class="cover">
      <p class="forth-parallel"></p>
    </div>
    <div class="cover">
      <p class="fifth-parallel"></p>
    </div>
  </span>
  <span v-else>
    <br>
    <div class="main">
      <ul v-for="(dto,i) in list" :key="i">
        <li class="list-item">
          <div class="card" @click="shoppingLink(i)" @mouseover="cursorChange">
            <div class="imgPosition">
              <img :src="dto.img">
            </div>
            <br>
            <span style="font-weight: bold;" class="moll-name">{{ dto.mallName }}</span>
            <br>
            <div class="item-content" :id="i">
            </div><br>
            {{ dto.price }}원
          </div>
        </li>
      </ul>
    </div>
    </span>
  </template>

<script>
export default{
  name:'naverShoppingList',
  data(){
    return{
      query :'',
      list:[],
    }
  },
  mounted(){
    const pTag1 = document.querySelector('.first-parallel')
    const pTag2 = document.querySelector('.second-parallel')
    const pTag3 = document.querySelector('.third-parallel')
    const pTag4 = document.querySelector('.forth-parallel')
    const pTag5 = document.querySelector('.fifth-parallel')
    
    // const textArr1 = 'Yummy Tasty Delicious Useful Coding Yummy Yummmmy Yummmmmmmmmy yum'.split(' ')
    const textArr1 = 'cardigan jacket fieldJacket coat padding'.split(' ')
    const textArr2 = 'sleeveless shirt longSleeve neat Sweatshirt '.split(' ')
    const textArr3 = "shorts skirt cottonPants jeans leggings".split(' ')
    const textArr4 = 'stockings heattech fleeceLined muffler hat'.split(' ')
    const textArr5 = 'sandal runningShoes boots shoes'.split(' ')
    
    let count1 = 0
    let count2 = 0
    let count3 = 0
    let count4 = 0
    let count5 = 0
    
    initTexts(pTag1, textArr1)
    initTexts(pTag2, textArr2)
    initTexts(pTag3, textArr3)
    initTexts(pTag4, textArr4)
    initTexts(pTag5, textArr5)
    
    function initTexts(element, textArray) {
      textArray.push(...textArray)
      for (let i = 0; i < textArray.length; i++) {
        element.innerHTML += `<span class= "item" id="${textArray[i]}">${textArray[i]}\u00A0\u00A0\u00A0\u00A0</span>`
      }
    }

    
    
    this.makeEvent(this.search);
    function marqueeText(count, element, direction) {
      if (count > element.scrollWidth / 2) {
        element.style.transform = `translate3d(0, 0, 0)`
        count = 0
      }
      element.style.transform = `translate3d(${direction * count}px, 0, 0)`

      return count
    }

    function animate() {
      count1++
      count2++
      count3++
      count4++
      count5++

      count1 = marqueeText(count1, pTag1, -1)
      count2 = marqueeText(count2, pTag2, 1)
      count3 = marqueeText(count3, pTag3, -1)
      count4 = marqueeText(count4, pTag4, 1)
      count5 = marqueeText(count5, pTag5, -1)

      window.requestAnimationFrame(animate)
    }
    function scrollHandler() {
      count1 += 15
      count2 += 15
      count3 += 15
      count4 += 15
      count5 += 15
    }
    window.addEventListener('scroll', scrollHandler)
    animate()
  },
  methods: {
    makeEvent(searchMethod){
      let self = this;
      let itemClass = document.querySelectorAll('.item')
      console.log(itemClass)
      for(let i = 0 ; i < itemClass.length; i++){
        itemClass[i].addEventListener("click",function(){
          
          self.query = this.id;
          alert(`${self.query}를 검색합니다.`);
          searchMethod();
        });
        itemClass[i].addEventListener("mouseover",function(){
          console.log(this)
          this.style.cursor = 'pointer';
        })
      }
    },
    search(){
      let self = this;
      if(self.query != ''){
        self.$axios.get(`http://localhost:8081/naver/${self.query}`)
        .then(res => {
          if(res.status == 200){
            self.list = res.data.list;
            setTimeout(() => {
              for(let i = 0 ; i < self.list.length; i++){
                let span = document.getElementById(i);
                span.innerHTML = self.list[i].title;
              }
              window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
            }, 100);
          }else{
            alert("실패 시발!")
          }
        })
      }else{
        let search = document.querySelector("input[type=text]");
        search.placeholder = "검색어 입력은 필수 입니다.";
      }
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
    },
    shoppingLink(i){
      let self = this;
      let link = self.list[i].link;
      window.open(link,"_blank");
    },
    cursorChange(e){
      e.target.style.cursor = "pointer";
    }
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");

.search-wrapper{
  display:flex;
  left:0;
  right:0;
  margin:50px auto 15px;
  width: 600px;
  position: relative;
}

input[type=text]{
  width: 100%;
  height: 30px; 
  border-radius: 18px;
  padding-left: 45px;
  font-size: 18px;
}

.price-buttons{
  display: flex;
  padding:0 15px;
}

input[type=button]{
  width: 100px;
  height: 40px;
  border-radius: 30px;
  background-color: #C4D7B2;
  transition : .5s;
  margin : 0 5px;
}


input[type=button]:hover{
  background-color: #85b380;
  color: #ffffff;
  cursor:pointer;
  font-weight: bold;
}

.search{
  width: 30px;
  height: 30px;
  position:absolute;
  left:8px;
  top:3px;
}


.main{
  position: absolute;
  left:0;
  right:0;
  margin:auto;
  padding-left:100px;
  width:1000px;
}

ul{
  float:left;
}
.card{
  width: 125px;
  height: 360px;
  padding: 20px 50px;
  margin-left:50px;
}

.card:hover{
  background-color: rgb(246, 245, 239);
}

img{
  width: 100%;
  height: 100%;
}

.imgPosition{
  width: 100%;
  height: 160px;
}

.moll-name{
  display:block;
  width:100%;
  white-space: nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
}
.item-content{
  width:100%;
  height:14vh;
  display:-webkit-box;
  overflow:hidden;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
}

.cover {
  width: 100vw;
  margin-bottom: 10rem;
  display: flex;
}

.cover:nth-child(1) {
  margin-top: 10vh;
  transform: rotate(-2deg);
  background-color: #85b380;
}

.cover:nth-child(2) {
  transform: rotate(2deg);
  background-color: #A0C49D;
  justify-content: flex-end;
}

.cover:nth-child(3) {
  transform: rotate(-2deg);
  background-color: #C4D7B2;
}

.cover:nth-child(4) {
  transform: rotate(2deg);
  background-color: #E1ECC8;
  justify-content: flex-end;
}

.cover:nth-child(5) {
  transform: rotate(-2deg);
  background-color: #F7FFE5;
  margin-bottom: 50vh;
}

p {
  font-family: "Pacifico", cursive;
  display: flex;
  padding: 3rem 0;
  font-size: 5rem;
}

</style>