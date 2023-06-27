<template>
    <div>
        <router-link to="/ootwadd">Ootw 등록</router-link><br />
        <h3>Outfit Of the Weather</h3>
        <input type="date" v-model="date1" id="date1"> ~ <input type="date" v-model="date2" id="date2">
        <button v-on:click="searchByDate">검색</button>
        <table border="1">
            <tr>
                <th>Ootw num</th>
                <th>날짜</th>
                <th>Comment</th>
                <th>기온</th>
                <th>날씨</th>
                <th>삭제하기</th>
            </tr>
            <tr v-for="ootw in displayedootw" :key="ootw.ootwnum">
                <td>{{ ootw.ootwnum }}</td>
                <td>{{ ootw.odate }}</td>
                <td><a v-on:click="detail(ootw.ootwnum)">{{ ootw.comments }}</a></td>
                <td>{{ ootw.temp }}</td>
                <td>{{ ootw.weather }}</td>
                <td><a v-on:click="deleteOootw(ootw.ootwnum)">X</a></td>
            </tr>
        </table>
        <button v-on:click="moreBtn">더보기</button>
    </div>
</template>

<script>
export default {
    name: 'OotwList',
    data() {
        return {
            ootwlist: [],
            displayedootw: [],
            ootwPerPage: 1,
            currentPage: 1,
            weather: '',
            date1: '',
            date2: ''
        }
    },
    created: function () {
        const self = this;
        self.$axios.get('http://localhost:7878/boards')
            .then(function (res) {
                if (res.status == 200) {
                    self.ootwlist = res.data.list;
                    self.displayedootw = self.ootwlist.slice(0, self.ootwPerPage);
                } else {
                    alert('에러코드: ' + res.status);
                }
            })
    },
    methods: {
        moreBtn() {
            const self = this;
            const startIndex = self.currentPage * self.ootwPerPage;
            const endIndex = startIndex + self.ootwPerPage;
            self.displayedootw = self.displayedootw.concat(self.ootwlist.slice(startIndex, endIndex));
            // self.displayedootw = [...self.displayedootw, ...self.ootwlist.slice(startIndex, endIndex)];
            self.currentPage++;
        },
        detail(ootwnum) {
            this.$router.push({ name: 'OotwDetail', query: { ootwnum: ootwnum } })
        },
        deleteOootw(ootwnum) {
            const self = this;
            self.$axios.delete('http://localhost:7878/boards/' + ootwnum)
                .then(function (res) {
                    if (res.status == 200) {
                        self.displayedootw = self.ootwlist.filter(ootw => ootw.ootwnum != ootwnum)
                    } else {
                        alert('에러 코드: ' + res.status)
                    }
                })
        },
        searchByDate() {
            const self = this;
            if (self.date2 < self.date1 || self.date1 == '' || self.date2 == '') {
                alert('날짜 범위를 다시 정해주세요.')
            } else {
                location.reload();
                self.$axios.get('http://localhost:7878/boards/dates/' + self.date1 + "/" + self.date2)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.ootwlist = res.data.list;
                            self.displayedootw = self.ootwlist.slice(0, self.ootwPerPage);
                        } else {
                            alert('에러코드: ' + res.status);
                        }
                    });
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

a {
    cursor: pointer;
}
</style>