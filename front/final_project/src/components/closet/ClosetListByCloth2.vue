<template>
    <h3>옷장 전체리스트</h3>
    <div>
        <router-link to="/closetadd">옷 등록</router-link><br />
        옷이름 검색: <input type="search" v-model="cloth"><button v-on:click="clothserach">검색</button>

        <div class="mouseout" v-on:mouseout="hideselect">
            <ul v-for="(maintag, index) in maintags" v-bind:value="maintag" v-bind:key="maintag">
                <li v-on:mouseover="selectsub(index)" v-on:click="getall(index)">{{ maintag }}</li>
            </ul><br />
            <div class="follow" id="follow">
                <ul v-for="(subtag, index) in subtags" v-bind:value="subtag" v-bind:key="subtag">
                    <li class="second" v-on:click="listbytag(subtag, index)" id="sub">{{ subtag }}</li>
                </ul>
            </div>
        </div><br/>

        <div>
            <b-card-group deck v-for="(row, index) in additionalCloset" :key="index" style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                <b-card v-for="closet in row" :key="closet.closetnum"
                    :img-src="'http://localhost:7878/closets/img/' + memnum + '/' + closet.closetnum" img-top
                    style="max-width:200px; height: auto; flex: 0 0 250px;">
                    <a v-on:click="favorite(closet.closetnum)">
                        <span v-text="closet.favorite == 1 ? '★' : '☆'" class="star"></span>
                    </a>
                    <b-card-text>
                        {{ closet.maintag }}<br />
                        {{ closet.subtag }}<br />
                        <a v-on:click="detail(closet.closetnum)">{{ closet.cloth }}</a><br />
                    </b-card-text>
                    <template #footer>
                        <small class="text-muted">
                            <b-button v-on:click="deletecloth(closet.closetnum)">삭제</b-button>
                        </small>
                    </template>
                </b-card>
            </b-card-group>
        </div><br />
        <b-button href="#" variant="primary">Go somewhere</b-button>
        <b-button v-on:click="moreBtn">더보기</b-button>
    </div>
</template>

<script>
export default {
    name: 'OotwSelect',
    data() {
        return {
            tag: this.$route.query.tag,
            index: this.$route.query.index,
            closetlist: [],
            additionalCloset: [],
            closetPerPage: 5,
            currentPage: 1,
            maintags: ['전체', '아우터', '상의', '하의', '기타', '신발'],
            subtags: [],
            memnum: sessionStorage.getItem('memnum'),
            cloth: ''
        }
    },
    created: function () { // 해당 컴포넌트가 처음 실행될 때만 적용... 그 다음부터는 변경된 컴포넌트(같은 컴포넌트로 이동할 때 적용이 안됨)
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        let cloth = this.$route.query.cloth
        self.$axios.get('http://localhost:7878/closets/clothes/' + cloth)
            .then(function (res) {
                if (res.status == 200) {
                    // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default 걸러서 리스트에 넣기
                    self.closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
                    const addtionalRow = self.closetlist.slice(0, self.closetPerPage);
                    self.additionalCloset.push(addtionalRow);
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        getall(index) {
            // const self = this;
            if (index == 0) { // 메인태그 '전체'일 때만 전체 리스트 보여주기 설정
                location.reload();
                // self.$axios.get('http://localhost:7878/closets')
                //     .then(function (res) {
                //         if (res.status == 200) {
                //             self.closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
                //             self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                //         } else {
                //             alert('에러코드: ' + res.status)
                //         }
                //     })
            }
        },
        moreBtn() {
            const self = this;
            // 처음에 3개(0~3..0, 1, 2)를 보여준다면 더보기 버튼을 눌렀을 때 3개가 추가되어 0~6(0, 1, 2, 3, 4, 5)이 되어야 함
            // 추가할 시작 번호 = 현재페이지 * 보여주는 리스트 개수 = 1 * 3 = 3
            // 추가할 끝날 번호 = 추가할 시작 번호 + 보여주는 리스트 개수 = 3 + 3 = 6
            // 처음 보여지는 배열에 원래 배열의 3~6을 slice해서 껴넣는다.. => then, 0~6
            // 그리고 현재페이지++ 시켜서 위 과정을 반복할 수 있게 한다.
            // ex) more = 현재페이지(2) * 보여주는 리스트 개수(3) = 2 * 3 = 6
            // end = more(6) + 보여주는 리스트 개수(3) = 9
            // 0~6 + 6~9 = 0~9 .. 0, 1, 2, 3, 4, 5, 6, 7, 8.. 9개가 보여짐
            const startIndex = (self.currentPage) * self.closetPerPage;
            const endIndex = startIndex + self.closetPerPage;
            if (startIndex > self.closetlist.length) {
                // 더 이상 표시할 아이템이 없으면 더보기 버튼을 비활성화
                return;
            }
            const addtionalRow = self.closetlist.slice(startIndex, endIndex)
            self.additionalCloset.push(addtionalRow);
            // self.displayedcloset = [...self.displayedcloset, ...self.closetlist.slice(startIndex, endIndex)];
            self.currentPage++;
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
                            location.reload();
                            // self.additionalCloset = self.additionalCloset.map(row => row.filter(closet => closet.closetnum != closetnum));
                            // self.additionalCloset = self.additionalCloset.filter(closet => closet.closetnum != closetnum);
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
        selectsub(index) {
            const self = this;
            if (index == 0) {
                self.subtags = []
            } else if (index == 1) {
                self.subtags = ['아우터(전체)', '가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', 'etc']
            } else if (index == 2) {
                self.subtags = ['상의(전체)', '민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', 'etc']
            } else if (index == 3) {
                self.subtags = ['하의(전체)', '반바지', '치마', '면바지', '청바지', '레깅스', 'etc']
            } else if (index == 4) {
                self.subtags = ['기타(전체)', '스타킹', '히트텍', '기모제품', '목도리', 'etc']
            } else if (index == 5) {
                self.subtags = ['신발(전체)', '샌들', '슬리퍼', '운동화', '등산화', '구두', 'etc']
            }
        },
        listbytag(subtag, index) {
            const self = this;
            self.$router.push({ name: 'ClosetlistByTag', query: { tag: subtag, index: index } });
        },
        clothserach() {
            const self = this;
            self.$router.push({ name: 'ClosetlistByCloth', query: { cloth: self.cloth } });
        }
    }
}
</script>

<style scoped>
h3 {
    margin: 40px 0 0;
}

.star {
    margin-left: 150px;
    margin-top: 1000px;
    cursor: pointer;
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