<template>
    <!-- 게시글 등록~검색까지 -->
    <div>
        <div class="add">
            <!-- 로그인 해야 게시글 등록 버튼 보임 -->
            <a v-if="isLoggedIn" href="/addform">
                <button type="button" class="btn btn-light">게시글 등록</button>
            </a>
        </div>
        <hr />
        <div>
            <p>
                <input type="search" v-model="searchTag" placeholder="예) #오피스룩 #패피">
                <button style="margin-left: 5px;" @click="search">검색</button>
            </p>
        </div>
    </div>

    <!-- 사진, 신고, 좋아요,삭제,북마크, 태그 포함할 전체 리스트 -->
    <div>
        <h3>전체 리스트</h3>
        <div v-for="comm in commlist" :key="comm.commnum">
            <div>
                <!-- <a href="/reportform">신고</a> -->
                <button @click="openReportModal"> 신고하기</button>
                <ReportForm :modalon="modalon" @close="modalon = true" />
            </div>
            <div>
                <button v-if="memnum == comm.memnum.memnum" @click="delPost(comm.commnum)">삭제</button>
            </div>
            <span v-for="i in 3" :key="i">
                <img class="img1"
                    :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + i">
                <!-- <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 1"> -->
                <!-- <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 2"> -->
                <!-- <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 3"> -->
            </span>
            <br />
            <div>{{ comm.tag }}</div>
        </div>
    </div>
</template>
  
<script>
import ReportForm from '@/components/community/ReportForm.vue'
// import ReportForm from './ReportForm.vue'

export default {

    components: {
        ReportForm
    },

    data() {
        return {
            commlist: [],
            memnum: sessionStorage.getItem('memnum'),
            modalon: false
        }
    }, 

    //게시글 리스트 띄우는 함수
    mounted() { 
        this.getCommunityList();
    },

    // 로그인 상태를 확인하는 computed 속성
    computed: {  
        isLoggedIn() {
            return sessionStorage.getItem('memnum') !== null;
        }
    },

    methods: {
        
        openReportModal(){
            this.modalon = true;
        },

        delPost(commnum) {
            const self = this;
            self.$axios.delete('http://localhost:8081/ocommunity/' + commnum )
                .then(function (res) {
                    if (res.status === 200) {
                        if (res.data.flag) {
                            self.commlist = self.commlist.filter(comm => comm.commnum != commnum);
                        }
                    } else {
                        alert("오류")
                    }
                })
            },
            
        getCommunityList() {
            this.$axios.get('http://localhost:8081/ocommunity')
                .then((response) => {
                    if (response.status === 200) {
                        this.commlist = response.data.list
                            .sort((a, b) => b.commnum - a.commnum) //게시글 최신순 정렬
                            .map((comm) => ({
                                ...comm,
                                isLiked: false,
                                memnumCount: 0
                            }));
                    } else {
                        alert('에러코드: ' + response.status);
                    }
                })
        }
    }
}
</script>
  
<style scoped>
.likeBtn {
    background: none;
    border: none;
}

.add {
    margin-left: 60%;
}

.search {
    padding-top: 5px;
}

hr {
    width: 70%;
    margin-left: auto;
    margin-right: auto;
    border: solid 3px #336399;
}

.img1 {
    width: 200px;
    height: 300px;
}
</style>
  