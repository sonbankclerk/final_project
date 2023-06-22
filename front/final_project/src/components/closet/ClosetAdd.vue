<template>
    <div>
        <h3>옷장에 옷 등록하기</h3>
        <label for="imgtag">
            <img :src="thumbimg">
        </label>
        <input type="file" id="imgtag" style="display:none" accept="image/*" v-on:change="thumbnail"><br />
        메인태그:
        <select v-model="selectedmain" @change="updatesub">
            <option v-for="maintag in maintags" v-bind:value="maintag" v-bind:key="maintag">{{ maintag }}</option>
        </select><br />
        서브태그:
        <select v-model="selectedsub">
            <option v-for="subtag in subtags" v-bind:value="subtag" v-bind:key="subtag">{{ subtag }}</option>
        </select><br />
        옷이름:
        <input type="text" v-model="clothname"><br />
        <button v-on:click="addcloset">옷 등록하기</button>

    </div>
</template>

<script>
export default {
    name: 'ClosetAdd',
    data() {
        return {
            thumbimg: 'http://localhost:7878/closets/img/addimg',
            uploadimg: '',
            memnum: null,
            selectedmain: '',
            selectedsub: '',
            maintags: ['아우터', '상의', '하의', '기타', 'acc'],
            subtags: '',
            clothname: ''
        }
    },
    created: function () {
        this.memnum = sessionStorage.getItem('memnum')
    },
    methods: {
        thumbnail() {
            const file = document.getElementById('imgtag');
            if (file.files && file.files[0]) {
                const reader = new FileReader();
                const self = this;
                reader.onload = function() {
                    self.thumbimg = reader.result;
                    self.uploadimg = file.files[0];
                };
                reader.readAsDataURL(file.files[0]);
            }
        },
        updatesub() {
            this.selectedsub = '';
            if (this.selectedmain == '아우터') {
                this.subtags = ['코트', '패딩', '야상']
            } else if (this.selectedmain == '상의') {
                this.subtags = ['긴팔', '반팔', '맨투맨']
            } else if (this.selectedmain == '하의') {
                this.subtags = ['청바지', '반바지', '슬랙스']
            } else if (this.selectedmain == '기타') {
                this.subtags = ['원피스', '점프수트', '한벌옷']
            } else if (this.selectedmain == 'acc') {
                this.subtags = ['모자', '신발', '귀걸이']
            }
        },
        addcloset() {
            const self = this;
            // const file = document.getElementById('imgpath');
            let formdata = new FormData();
            formdata.append('f', self.uploadimg)
            formdata.append('memnum', self.memnum)
            formdata.append('cloth', self.clothname)
            formdata.append('maintag', self.selectedmain)
            formdata.append('subtag', self.selectedsub)
            self.$axios.post('http://localhost:7878/closets', formdata)
                .then(function () {
                    location.href = "/closetlist"
                })
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
</style>