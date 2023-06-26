<template>
    <div>
        <h3> 나 뭐 입었더라?</h3>
        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + clothnum1">
        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + clothnum2">
        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + clothnum3">
        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + clothnum4">
        <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + clothnum5"><br/>
        아우터 / 상의 / 하의 / 기타 / acc<br/><br/>
        날짜: <input type="text" v-model="odate" readonly><br/>
        날씨: <input type="text" v-model="weather" readonly><br/>
        기온: <input type="text" v-model="temp" readonly><br/>
        이 날의 커멘트<br/>
        <textarea v-model="comments" cols="30" rows="5" style="resize:none" maxlength="100" readonly></textarea><br/>
        
    </div>
</template>

<script>
    export default {
        name: 'OotwDetail',
        data () {
            return {
                ootwnum : this.$route.query.ootwnum,
                clothnum1: '',
                clothnum2: '',
                clothnum3: '',
                clothnum4: '',
                clothnum5: '',
                memnum: '',
                odate: '',
                weather: '',
                temp: '',
                comments: ''
            }
        },
        created:function() {
            const self = this;
            self.memnum = sessionStorage.getItem('memnum')
            self.$axios.get('http://localhost:7878/boards/' + self.ootwnum)
                .then(function(res) {
                    if(res.status == 200) {
                        let dto = res.data.dto;
                        let list = res.data.list;
                        self.odate = dto.odate
                        var year = dto.odate.substring(0, 4);
                        var month = dto.odate.substring(5, 7);
                        var day = dto.odate.substring(8, 10);
                        self.odate = year + "년 " + month + "월 " + day + "일";
                        self.weather = dto.weather
                        self.temp = dto.temp
                        self.comments = dto.comments
                        // for(let i=0; i<5; i++){
                        //     if(list[i] == null) {
                        //         list[i].closetnum.closetnum = 999999999;
                        //     } else {
                                
                        //     }
                        //     return;
                        // }
                        self.clothnum1 = list[0].closetnum.closetnum;
                        self.clothnum2 = list[1].closetnum.closetnum;
                        self.clothnum3 = list[2].closetnum.closetnum;
                        self.clothnum4 = list[3].closetnum.closetnum;
                        self.clothnum5 = list[4].closetnum.closetnum;
                        // alert('http://localhost:7878/closets/img/' + self.memnum + '/' + self.clothnum1)
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
img {
    width: 200px;
    height: 200px;
}
table {
    margin-left: auto;
    margin-right: auto;
}
</style>