<template>
    <div>
        <input type="file" multiple @change="FileUpload" :disabled="selectedFiles.length >= 3">
        <div class="image-container">
            <button @click="perImg">이전</button>
            <div v-for="image in previewImages" :key="image.name" class="image-wrapper">
                <img :src="image.url" :alt="image.name">
            </div>
            <button @click="nextImg">다음</button>
            <!-- <button class="btn btn-primaty">부트스트랩</button> -->
        </div>
        <br>
        <button class="ml-3">등록</button>
    </div>
</template>

<style scoped>
.image-container {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 10px;
    justify-content: center;
}

.image-wrapper {
    width: 100px;
    height: 100px;
    margin: 5px;
    border: 5px solid #ccc;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
}

.image-wrapper img {
    max-width: 100%;
    max-height: 100%;
    object-fit: cover;
}

input[type="file"] {
    margin-bottom: 10px;
}

input[type="text"] {
    width: 30%;
    padding: 5px;
    margin-bottom: 10px;
}

button {
    padding: 10px 20px;
    background-color: #4cd3f5;
    color: white;
    border: none;
    cursor: pointer;
}

button:hover {
    background-color: #4550a0;
}
</style>

<script>
export default {
    data() {
        return {
            selectedFiles: [],
            previewImages: []
        };
    },
    methods: {
        FileUpload(event) {
            const files = event.target.files;
            const newFiles = Array.from(files).slice(0, 3 - this.selectedFiles.length);
            for (let i = 0; i < newFiles.length; i++) {
                const file = newFiles[i];
                const reader = new FileReader();
                reader.onload = (e) => {
                    this.previewImages.push({
                        name: file.name,
                        url: e.target.result,
                    });
                };
                reader.readAsDataURL(file);
                this.selectedFiles.push(file);
            }
        },
        
    },
};
</script>
