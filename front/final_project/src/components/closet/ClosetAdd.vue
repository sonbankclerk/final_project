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
            maintags: ['아우터', '상의', '하의', '기타', '신발'],
            subtags: '',
            clothname: ''
        }
    },
    created: function () {
        this.memnum = sessionStorage.getItem('memnum')
    },
    methods: {
        thumbnail() {
            const file = document.getElementById('imgtag'); // type file에 올려진 file을 상수 file에 저장한다.
            if (file.files[0]) { // file은 하나만 올리므로 files 배열의 [0]만 null인지 아닌지 확인한다.
                // FileReader(): 자바스크립트 API.. 파일을 비동기적으로 읽을 수 있으며, 주로 파일의 내용을 읽어서 데이터를 가져오는 데 사용한다.
                const reader = new FileReader(); // FileReader의 새로운 객체를 생성(new)하여(인스턴스를 생성하여) reader 변수에 담는다. 
                const self = this;
                reader.onload = function() { // reader.onload 이벤트 핸들러는 파일 읽기가 완료되었을 때 호출되는 콜백 함수를 정의하는 역할을 한다.
                    self.thumbimg = reader.result; // reader.result 속성은 파일의 데이터를 담고 있는 문자열을 제공함, 이 문자열은 일반적으로 이미지 파일의 경우 base64 인코딩된 이미지 데이터를 포함한다.
                    // 따라서 인코딩된 이미지 데이터를 thumbnail에 담아서 이미지를 미리보기 해주는 역할을 수행한다.
                    self.uploadimg = file.files[0];  // 업로드한 파일을 uploadimg에 담아서 back에 보내는 역할을 한다.
                };
                reader.readAsDataURL(file.files[0]);
                // FileReader의 객체를 사용하여 여러 방식으로 데이터를 읽을 수 있다.
                // 여기에서 사용된 readAsDataURL 메서드는 파일을 데이터 url형식으로 읽는다.
                // 업로드한 파일을 해당 메서드가 읽고, 파일 읽기가 완료되면 read.onload 이벤트 핸들러 내부의 작업이 시작된다.
            }
        },
        updatesub() {
            this.selectedsub = '';
            if (this.selectedmain == '아우터') {
                this.subtags = ['가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', 'etc']
            } else if (this.selectedmain == '상의') {
                this.subtags = ['민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', 'etc']
            } else if (this.selectedmain == '하의') {
                this.subtags = ['반바지', '치마', '면바지', '청바지', '레깅스', 'etc']
            } else if (this.selectedmain == '기타') {
                this.subtags = ['스타킹', '히트텍', '기모제품', '목도리', 'etc']
            } else if (this.selectedmain == '신발') {
                this.subtags = ['샌들', '슬리퍼', '운동화', '등산화', '구두', 'etc']
            }
        },
        addcloset() {
            const self = this;
            let formdata = new FormData();
            if(self.uploadimg == ''){
                alert("옷 이미지를 등록해주세요.")
            } else {
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