<template>

    <h3>게시글 등록</h3>
    <hr class="addhr">
    <div class="container">
        <div>
            <img style="width: 250px; height: 300px;" :src="currentPhoto"><br>
            <div class="cir" style="margin-right: 12%; margin-left: 12%;">
                <div v-for="(photo, index) in photos" :key="index" class="circle" :class="{ active: currentPhotoIndex === index }" @click="changePhoto(index)"></div>
            </div>

            <div>
                <input type="text" v-model="tag" placeholder="예) #오피스룩 #안유진 #미미" style="margin-bottom: 1%; width: 20%; object-fit: contain;">
            </div>

            <div>
                <input type="file" id="f1" @change="handleFile(0)">
                <br>
                <input type="file" id="f2" @change="handleFile(1)">
                <br>
                <input type="file" id="f3" @change="handleFile(2)">
            </div>
        </div>    
    </div>
    
    <div>
        <button style="margin-top: 5px;" @click="add">등록</button>  
    </div>
</template>

<script>
export default {
    data() {
        return {
            tag: '',
            files: [],
            photos: [null, null, null],
            currentPhotoIndex: 0,
            memnum: sessionStorage.getItem('memnum')
        }
    },
    computed: {
        currentPhoto() {
            return this.photos[this.currentPhotoIndex];
        }
    },
    methods: {
        handleFile(index) {
            const file = event.target.files[0];
            this.photos[index] = URL.createObjectURL(file);
            this.files[index] = file;
        },
        changePhoto(index) {
            this.currentPhotoIndex = index;
        },
        add() {
            const self = this;
            let formData = new FormData();
            formData.append('tag', self.tag);
            formData.append('memnum', self.memnum);
            this.files.forEach((file) => {
                formData.append('mfArr', file);
                
            });
            self.$axios.post('http://localhost:8081/ocommunity', formData, { headers: {"Content-Type": "multipart/form-data"} })
            .then(function(){
                self.$router.push('/listboard');
            });
        }
    }
}
</script>

<style scoped>
body {
    object-fit: contain;
}
.container {
    /* display: flex; */
    align-items: center;
}
.circle {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: rgb(228, 213, 213);
    margin-right: 10px;
    display: inline-block;
    cursor: pointer;
}

.active {
    background-color: rgb(24, 186, 235);
}

.addhr {
    width: 25%;
    margin-left: auto;
    margin-right: auto;
    border: solid 5px #336399;;
}
</style>
