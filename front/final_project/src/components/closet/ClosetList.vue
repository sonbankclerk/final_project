<template>
    <div>
        <router-link to="/closetadd">옷 등록</router-link>
        <ul>
            <li v-on:mouseover="selectsub(0)" v-on:mouseout="cancelselect">아우터</li>
            <li v-on:mouseover="selectsub(1)" v-on:mouseout="cancelselect">상의</li>
            <li v-on:mouseover="selectsub(2)" v-on:mouseout="cancelselect">하의</li>
            <li v-on:mouseover="selectsub(3)" v-on:mouseout="cancelselect">기타</li>
            <li v-on:mouseover="selectsub(4)" v-on:mouseout="cancelselect">acc</li>
        </ul><br/>
        <div class="follow">
        <ul v-for="(subtag, index) in subtags" v-bind:value="subtag" v-bind:key="subtag">
            <li><a v-on:click="listbytag(index)" id="sub">{{ subtag }}</a></li>
        </ul>
        </div>
        <h3>옷장 전체리스트</h3>
        <table border="1">
            <tr>
                <th>옷 이미지</th>
                <th>옷 이름</th>
                <th>메인태그</th>
                <th>서브태그</th>
                <th>즐겨찾기</th>
                <th>삭제하기</th>
            </tr>
            <tr v-for="closet in closetlist" :key="closet.closetnum">
                <td><img :src="'http://localhost:7878/closets/img/' + closet.closetnum"></td>
                <td><a v-on:click="detail(closet.closetnum)">{{ closet.cloth }}</a></td>
                <td>{{ closet.maintag }}</td>
                <td>{{ closet.subtag }}</td>
                <td><a v-on:click="favorite(closet.closetnum)"><span v-text="closet.favorite === 1 ? '★' : '☆'"></span></a></td>
                <!-- 삼항연산자.. something === x ? 'a' : 'b' ... x일 때 a .. x가 아닐 때 b .. 해당 변수에 대응하여 값을 제공하는 동적 기능-->
                <td><a v-on:click="deletecloth(closet.closetnum)">X</a></td>
            </tr>
        </table>
    </div>
</template>

<script>
export default {
    name: 'ClosetList',
    data() {
        return {
            closetlist: [],
            maintags: ['아우터', '상의', '하의', '기타', 'acc'],
            subtags: []
        }
    },
    created: function () {
        const self = this;
        self.$axios.get('http://localhost:7878/closets')
            .then(function (res) {
                if (res.status == 200) {
                    self.closetlist = res.data.list
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        detail(closetnum) {
            this.$router.push({ name: 'ClosetDetail', query: { closetnum: closetnum } })
        },
        deletecloth(closetnum) {
            const self = this;
            self.$axios.delete('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if(res.status == 200){
                        if(res.data.flag) {
                            location.href = "/ClosetList"
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        favorite(closetnum){
            const self = this;
            self.$axios.patch('http://localhost:7878/closets/' + closetnum)
                .then(function(res){
                    if(res.status == 200){
                        location.reload();
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        selectsub(num){
            const self = this;
            let mainnum = 0;
            if(num == 0){
                self.subtags = ['전체', '코트', '패딩', '야상']
                mainnum == 0;
            } else if(num == 1) {
                self.subtags = ['전체', '긴팔', '반팔', '맨투맨']
                mainnum == 1;
            } else if(num == 2) {
                self.subtags = ['전체', '청바지', '반바지', '슬랙스']
                mainnum == 2;
            } else if(num == 3) {
                self.subtags = ['전체', '원피스', '점프수트', '한벌옷']
                mainnum == 3;
            } else if(num == 4) {
                self.subtags = ['전체', '모자', '신발', '귀걸이']
                mainnum == 4;
            }
            return mainnum;
        },
        // cancelselect(){
        //     const self = this;
        //     self.subtags = []
        // },
        listbytag(index){
            // const self = this;
            if(index == 0) {
                alert("메인태그")
                // alert(self.maintags[num])
                // self.$axios.get('')
            } else {
                alert("서브태그")
                // self.$axios.get('')
            }

        }
    }
}
</script>

<style scoped>
h3 {
    margin: 40px 0 0;
}

table {
    margin-left: auto;
    margin-right: auto;
}

ul {
    list-style: none;
    margin-left: 35%;
}
li {
    float: left;
    margin-right: 50px;
}
</style>