<template>
    <div>
        <router-link to="/ootwadd">Ootw 등록</router-link><br />
        
        <h3>Outfit Of the Weather</h3>
        <table border="1">
            <tr>
                <th>Ootw num</th>
                <th>날짜</th>
                <th>Comment</th>
                <th>기온</th>
                <th>날씨</th>
                <th>삭제하기</th>
            </tr>
            <tr v-for="ootw in ootwlist" :key="ootw.ootwnum">
                <td>{{ ootw.ootwnum }}</td>
                <td>{{ ootw.odate }}</td>
                <td><a v-on:click="detail(ootw.ootwnum)">{{ ootw.comments }}</a></td>
                <td>{{ ootw.temp }}</td>
                <td>{{ ootw.weather }}</td>
                <td><a v-on:click="deletOootw(ootw.ootwtnum)">X</a></td>
            </tr>
        </table>
    </div>
</template>

<script>
    export default {
        name: 'OotwList',
        data () {
            return {
                ootwlist: [],
                weather: ''
            }
        },
        created:function() {
            const self = this;
            self.$axios.get('http://localhost:7878/boards')
                .then(function(res) {
                    if(res.status == 200){
                        self.ootwlist = res.data.list
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },
        methods: {

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
</style>