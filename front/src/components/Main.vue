<template>
  <div class="main-actions">

    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileSelect"/>

    <button class="main-btn upload" @click="triggerFileInput">
      <span class="icon">💍</span> 사진 업로드
    </button>

    <button class="main-btn vote" @click="goToImageList">
      <span class="icon">🎀</span> 사진 투표
    </button>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import '../styles/Main.css'
import axios from 'axios'
import { useRouter } from 'vue-router'  // 상단에 추가

const fileInput = ref(null)
const router = useRouter()

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileSelect = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  try {
    const response = await axios.post(
      '/api/image/upload', 
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )

    alert('이미지 업로드가 성공하였습니다!');
    router.push({ name: 'ImageList' })
  } catch (error) {

    alert('에러가 발생하였습니다...');
    alert(error);
  }
}

const goToImageList = () => {
  router.push({ name: 'ImageList' })
}
</script>