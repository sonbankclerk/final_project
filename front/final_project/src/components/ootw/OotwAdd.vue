<template>
    <div>
        <h3>Ootw 게시글 등록하기</h3>
        <!-- 옷장에서 옷 꺼내서 이미지 저장시키기 버튼-->
        <label for="addLabel1">
            <img :src="thumbImg1">
        </label>
        <button v-on:click="modalOpen(1)" id="addLabel1" style="display:none"></button>
        <label for="addLabel2">
            <img :src="thumbImg2">
        </label>
        <button v-on:click="modalOpen(2)" id="addLabel2" style="display:none"></button>
        <label for="addLabel3">
            <img :src="thumbImg3">
        </label>
        <button v-on:click="modalOpen(3)" id="addLabel3" style="display:none"></button>
        <label for="addLabel4">
            <img :src="thumbImg4">
        </label>
        <button v-on:click="modalOpen(4)" id="addLabel4" style="display:none"></button>
        <label for="addLabel5">
            <img :src="thumbImg5">
        </label><br />
        <button v-on:click="modalOpen(5)" id="addLabel5" style="display:none"></button>
        날짜: <input type="date" v-model="odate"><br/>
        날씨: <input type="text" v-model="weather"><br />
        기온: <input type="text" v-model="temp"><br />
        Comment<br />
        <textarea v-model="comments" cols="30" rows="5" style="resize:none" maxlength="100"
            placeholder="커멘트는 최대 50자까지 입력 가능합니다."></textarea><br/>
        <button v-on:click="addBoard">등록</button>
    </div>


    <!-- 옷장에서 옷 선택하는 모달창 -->
    <div class="modal-wrap" v-show="modalCheck" @click="modalClose">
        <div class="modal-container" @click.stop="">
            <div>
                <div class="mouseout" v-on:mouseout="hideselect">
                    <ul v-for="(maintag, index) in maintags" v-bind:value="maintag" v-bind:key="maintag">
                        <li v-on:mouseover="selectsub(index)" v-on:click="getall(index)">{{ maintag }}</li>
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
                        <th>체크박스</th>
                    </tr>
                    <tr v-for="closet in displayedcloset" :key="closet.closetnum">
                        <td><img :src="'http://localhost:7878/closets/img/' + closet.memnum.memnum + '/' + closet.closetnum"></td>
                        <td>{{ closet.cloth }}</td>
                        <td>{{ closet.maintag }}</td>
                        <td>{{ closet.subtag }}</td>
                        <td><button v-on:click="addCloth(closet.closetnum)">추가</button></td>
                    </tr>
                </table>
                <button v-on:click="moreBtn">더보기</button>
            </div>

            <div class="modal-btn">
                <button @click="modalClose">취소</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'OotwAdd',
    data() {
        return {
            thumbImg1: 'http://localhost:7878/closets/img/addimg',
            thumbImg2: 'http://localhost:7878/closets/img/addimg',
            thumbImg3: 'http://localhost:7878/closets/img/addimg',
            thumbImg4: 'http://localhost:7878/closets/img/addimg',
            thumbImg5: 'http://localhost:7878/closets/img/addimg',
            modalCheck: false,
            closetlist: [],
            displayedcloset: [],
            closetPerPage: 3,
            currentPage: 1,
            maintags: ['전체', '아우터', '상의', '하의', '기타', 'acc'],
            subtags: [],
            cloth: '',
            memnum: '', // 객체가 아닌 정수로 판별됨
            odate: '',
            weather: '',
            temp: '',
            comments: '',
            thumbnum: null,
            closetnum1: null,
            closetnum2: null,
            closetnum3: null,
            closetnum4: null,
            closetnum5: null,
            // formdata null이면 axios로 전달이 안되기 때문에 default 넣어주고 보냄..
            // default 999999999에 기본 이미지 넣어주고 리스트에서는 filter 메서드로 안보여주게 함
            // 값 넘기기 위해 만들어 놓은 더미용
            closetnum: [999999999, 999999999, 999999999, 999999999, 999999999]
        }
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        self.$axios.get('http://localhost:7878/closets')
            .then(function (res) {
                if (res.status == 200) {
                    self.closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
                    self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        modalOpen(num) {
            const self = this;
            self.modalCheck = !self.modalCheck;
            self.thumbnum = num;
        },
        modalClose() {
            const self = this;
            self.modalCheck = !self.modalCheck;
            self.getall(); // 모달창 닫을 때 이전 내용 상태로 닫히므로 전체 리스트 함수 다시 적용
        },
        getall(index) {
            const self = this;
            if(index == 0){
                self.$axios.get('http://localhost:7878/closets')
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
                            self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        moreBtn() {
            const self = this;
            const startIndex = self.currentPage * self.closetPerPage;
            const endIndex = startIndex + self.closetPerPage;
            self.displayedcloset = [...self.displayedcloset, ...self.closetlist.slice(startIndex, endIndex)];
            self.currentPage ++;
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
                            self.displayedcloset = res.data.list.filter(closet => closet.closetnum != 999999999);
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                var subtag = self.subtags[index]
                self.$axios.get('http://localhost:7878/closets/subtags/' + subtag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.displayedcloset = res.data.list.filter(closet => closet.closetnum != 999999999);
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        addCloth(closetnum) {
            const self = this;
            if (self.thumbnum == 1) {
                self.thumbImg1 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                self.closetnum[0] = closetnum
            } else if (self.thumbnum == 2) {
                self.thumbImg2 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                self.closetnum[1] = closetnum
            } else if (self.thumbnum == 3) {
                self.thumbImg3 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                self.closetnum[2] = closetnum
            } else if (self.thumbnum == 4) {
                self.thumbImg4 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                self.closetnum[3] = closetnum
            } else if (self.thumbnum == 5) {
                self.thumbImg5 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                self.closetnum[4] = closetnum
            }
            this.modalCheck = !this.modalCheck
        },
        addBoard() {
            const self = this
            var year = self.odate.substring(0, 4);
            var month = self.odate.substring(5, 7);
            var day = self.odate.substring(8, 10);
            var date = year + "/" + month + "/" + day;
            let formdata = new FormData();
            formdata.append('memnum', self.memnum)
            formdata.append('odate', date)
            formdata.append('weather', self.weather)
            formdata.append('temp', self.temp)
            formdata.append('comments', self.comments)
            formdata.append('closetnum', self.closetnum)
            self.$axios.post('http://localhost:7878/boards', formdata)
                .then(function(res) {
                    if(res.status == 200){
                        location.href = '/ootwlist'
                    } else {
                        alert('에러코드:' + res.status)
                    }
                })
        }
        // check() { // 체크박스 1개만 체크되도록 설정
        //     const maxAllowed = 1;
        //     const checkboxes = document.querySelectorAll('input[type=checkbox]');
        //     checkboxes.forEach(function (checkbox) {
        //         checkbox.addEventListener('click', function () {
        //             const checkedCount = document.querySelectorAll('input[type=checkbox]:checked').length;
        //             if (checkedCount > maxAllowed) {
        //                 checkbox.checked = false;
        //             }
        //         });
        //     });
        // }
    }
}
</script>

<style scoped>
h3 {
    margin: 40px 0 0;
}

img {
    width: 100px;
    height: 100px;
}

table {
    margin-left: auto;
    margin-right: auto;
}

ul {
    margin-left: 8%;
    cursor: pointer;
}

li {
    list-style: none;
    float: left;
    margin-right: 30px;
    cursor: pointer;
}

.follow {
    clear: left;
}

/* dimmed */
.modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
}


/* modal or popup */
.modal-container {
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 550px;
    background: #fff;
    border-radius: 10px;
    padding: 20px;
    box-sizing: border-box;
}
</style>