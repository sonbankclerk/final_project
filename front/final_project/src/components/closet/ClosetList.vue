<template>
    <div>
        <router-link to="/closetadd">옷 등록</router-link><br />
        옷이름 검색: <input type="search" v-model="cloth"><button v-on:click="clothserach">검색</button>
            <div class="mouseout" v-on:mouseout="hideselect">
                <ul v-for="(maintag, index) in maintags" v-bind:value="maintag" v-bind:key="maintag">
                    <li v-on:mouseover="selectsub(index)" v-on:click="getall">{{ maintag }}</li>
                </ul><br />
                <div class="follow" id="follow">
                <ul v-for="(subtag, index) in subtags" v-bind:value="subtag" v-bind:key="subtag">
                    <li class="second" v-on:click="listbytag(index)" id="sub">{{ subtag }}</li>
                </ul>
                </div>
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
                <td><a v-on:click="favorite(closet.closetnum)">
                        <span v-text="closet.favorite == 1 ? '★' : '☆'"></span>
                    </a>
                </td>
                <!-- 삼항연산자.. something == x ? 'a' : 'b' ... x일 때 a .. x가 아닐 때 b .. 해당 변수에 대응하여 값을 제공하는 동적 기능-->
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
            maintags: ['전체', '아우터', '상의', '하의', '기타', 'acc'],
            subtags: [],
            cloth: ''
        }
    },
    created: function () { // 해당 컴포넌트가 처음 실행될 때만 적용... 그 다음부터는 변경된 컴포넌트(같은 컴포넌트로 이동할 때 적용이 안됨)
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
        getall() {
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
        detail(closetnum) {
            this.$router.push({ name: 'ClosetDetail', query: { closetnum: closetnum } })
        },
        deletecloth(closetnum) {
            const self = this;
            self.$axios.delete('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        if (res.data.flag) {
                            self.closetlist = self.closetlist.filter(closet => closet.closetnum != closetnum);
                            // filter() method: 자바스크립트의 배열 method..
                            // 주어진 배열(self.closetlist)을 method 내부에 있는 조건에 만족하는 열들을 새로운 배열로 생성하는 method
                            // 기존에 있는 배열의 요소 closet 객체의 closetnum 속성들을 모두 꺼내서 입력된 변수 closetnum과 비교한다.
                            // 입력된 변수와 기존 배열 속성이 같지 않을 때(true) 그 속성들로만 배열을 다시 생성한다.
                            // 즉 삭제를 위해 입력된 변수 closetnum이 기존 배열 속성 closetnum과 일치하므로 해당 열은 새로운 배열에 속할 수 없다. 
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        favorite(closetnum) {
            const self = this;
            self.$axios.patch('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        const updatedCloset = self.closetlist.find(closet => closet.closetnum == closetnum);
                        // find() method: 자바스크립트의 배열 method
                        // 주어진 배열(self.closetlist)에서 내부 조건에 만족하는 열을 찾는 method
                        // 즐겨찾기 같은 경우는 좋아요 갱신에 만족하는 하나의 열을 찾아야 하므로 find() method를 사용함
                        // filter() method를 사용하면 조건에 만족하는 하나의 열만 새로운 배열로 생성돼서 보여줌
                        // 무튼, 조건에 만족하는 열을 찾아서 상수 updatedCloset에 저장한다.
                        // 그리고 아래의 if문에서 해당 상수가 null이 아닐 경우,
                        // 요소 updatedCloset 객체의 favorite 속성을 조건부 연산자를 사용하여 업데이트한다.
                        // 조건부 연산자(삼항연산자): 조건식에 만족하면 true인 0 값을, 만족하지 않으면 false인 1 값을 준다. 0과 1은 아래 예시.
                        // span tag에서 1일 때는 ★, 0일 때는 ☆로 표시가 되도록 조건부 연산자를 지정해 놨었는데,
                        // 내가 이미 좋아요가 된(favorite이 1) 게시글을 다시 누르면 back단 method에 의해 favorite이 0으로 바뀌고(서버 따로),
                        // front에서는 아래의 조건부 연산자에 의해 기존에 favorite이 1이었으므로 0으로 바꿔서 별 모양을 0에 맞는 ☆로 자동으로 변환(앞단 따로)
                        if (updatedCloset) {
                            updatedCloset.favorite = updatedCloset.favorite == 1 ? 0 : 1;
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        clothserach() {
            const self = this;
            var cloth = self.cloth;
            self.$axios.get('http://localhost:7878/closets/clothes/' + cloth)
                .then(function (res) {
                    if (res.status == 200) {
                        self.closetlist = res.data.list
                        self.cloth = '';
                    } else {
                        alert('에러코드' + res.status)
                    }
                })
        },
        selectsub(index) {
            const self = this;
            if (index == 0) {
                self.subtags = []
            } else if (index == 1) {
                self.subtags = ['아우터(전체)', '코트', '패딩', '야상']
            } else if (index == 2) {
                self.subtags = ['상의(전체)', '긴팔', '반팔', '맨투맨']
            } else if (index == 3) {
                self.subtags = ['하의(전체)', '청바지', '반바지', '슬랙스']
            } else if (index == 4) {
                self.subtags = ['기타(전체)', '원피스', '점프수트', '한벌옷']
            } else if (index == 5) {
                self.subtags = ['acc(전체)', '모자', '신발', '귀걸이']
            }
        },
        // hideselect(){
        //     document.getElementById("follow").style.display = 'none';
        //     // const self = this;
        //     // self.subtags = []
        // },
        listbytag(index) {
            const self = this;
            if (index == 0) {
                var maintag = self.subtags[0].split('(', 1)
                self.$axios.get('http://localhost:7878/closets/maintags/' + maintag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                var subtag = self.subtags[index]
                self.$axios.get('http://localhost:7878/closets/subtags/' + subtag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
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
    margin-left: 27%;
    cursor: pointer;
}

li {
    list-style: none;
    float: left;
    margin-right: 80px;
    cursor: pointer;
}

.follow {
    clear: left;
}
</style>