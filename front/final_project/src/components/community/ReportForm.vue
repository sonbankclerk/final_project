<template>
    <div class="black-bg" v-if="modalon">
        <div class="white-bg">
            <h4>신고</h4>
            <label>
                <input type="radio" v-model="reportContent" value="report1">
                홍보성/도배성
            </label>
            <br/>
            <label>
                <input type="radio" v-model="reportContent" value="report2">
                스팸
            </label>
            <br/>
            <label>
                <input type="radio" v-model="reportContent" value="report3">
                음란성
            </label>
            <br/>
            <label>
                <input type="radio" v-model="reportContent" value="report4">
                기타
            </label>
            <br/>
            <button @click="submitReport">신고하기</button>
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            reportContent: []
        };
    },
    methods: {
        submitReport(){
            console.log(this.reportContent);
            this.closeModal();
        },
        closeModal(){
            this.$emit('close');
        }
    }
}
</script>
<style scoped>
body {
    margin: 0;
}
div {
    box-sizing: border-box;
}
.black-bg {
    width: 100%; height: 100%;
    background-color: rgba(0, 0, 0, 0.3);
    position: fixed;
    padding: 20px;
}
.white-bg {
    width: 100%; background: white;
    border-radius: 20px;
    padding: 20px;
}

</style>