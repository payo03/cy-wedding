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
import axios from '@/utils/axios'
import { useRouter } from 'vue-router'

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
      '/image/upload', 
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )

    const { success, message, fileName } = response.data

    alert(`${message}`)
    if (success) {
      router.push({ name: 'ImageList' })
    }
  } catch (error) {
    console.error(error)
    const errorMessage = error.response?.data?.message || '❌ 서버 오류 발생 ❌'

    alert(errorMessage)
  }
}

const goToImageList = () => {
  router.push({ name: 'ImageList' })
}
</script>