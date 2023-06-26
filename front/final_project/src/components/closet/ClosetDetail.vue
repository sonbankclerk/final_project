<template>
    <div>
        <h3>{{ cloth }} 정보</h3>
        <table>
            <tr>
                <td>
                    <label for="imgtag"><img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closetnum" id="thumbimg"></label>
                    <input type="file" id="imgtag" style="display: none" accept="image/*" v-on:change="thumbnail">
                </td>
            </tr>
        </table>
        <table border="1">
            <tr>
                <th>메인태그</th>
                <td><input type="text" v-model="maintag" readonly></td>
            </tr>
            <tr>
                <th>서브태그</th>
                <td><input type="text" v-model="subtag" readonly></td>
            </tr>
            <tr>
                <th>옷 이름</th>
                <td><input type="text" v-model="cloth"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <button v-on:click="change(closetnum)">수정</button>
                    <button v-on:click="close">닫기</button>
                </td>
            </tr>
        </table>
    </div>
</template>

<script>
export default {
    name: 'ClosetDetail',
    data() {
        return {
            num: this.$route.query.closetnum,
            closetnum: '',
            cloth: '',
            maintag: '',
            subtag: '',
            memnum: '',
            uploadimg: ''
        }
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        self.$axios.get('http://localhost:7878/closets/' + self.num)
            .then(function (res) {
                if (res.status == 200) {
                    let dto = res.data.dto
                    if (dto != null) {
                        self.closetnum = dto.closetnum
                        self.cloth = dto.cloth
                        self.maintag = dto.maintag
                        self.subtag = dto.subtag
                    } else {
                        alert("정보가 없습니다.")
                    }
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        change(closetnum) {
            const self = this;
            let formdata = new FormData();
            if(self.uploadimg == null){
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                })    
            } else {
                formdata.append('newf', self.uploadimg)
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth, formdata)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                            location.reload();
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        thumbnail() {
            const file = document.getElementById('imgtag');
            if (file.files && file.files[0]) {
                const reader = new FileReader();
                const self = this;
                reader.onload = function () {
                    document.getElementById('thumbimg').src = reader.result;
                    self.uploadimg = file.files[0];
                };
                reader.readAsDataURL(file.files[0]);
            }
        }
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