<template>
    <div>
        <h3>옷장에 옷 등록하기</h3>
        <label for="imgpath">
            <img id="preview">
        </label>
        <input type="file" id="imgpath" style="display:none" accept="image/*">
        메인태그:
        <select v-model="maintag">
            <option value="아우터">아우터</option>
            <option value="상의">상의</option>
            <option value="하의">하의</option>
            <option value="기타">기타</option>
            <option value="acc">acc</option>
        </select>
        서브태그:
        <select v-model="subtag">
            <option v-for="subtags in sublist" value={{ subtags }}></option>
        </select>
    </div>
</template>

<script>
export default {
    name: 'ClosetAdd',
    data() {
        return {
            sublist:[
                ]
        }
    },
    created: function () {

    },
    methods: {
    }
}
</script>

<style>
h3 {
    margin: 40px 0 0;
}
</style>