<template>
  <div class="image-list-scroll-wrapper">
    <div class="image-columns">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="image-item"
        @click="openModal(image)"
      >
        <img :src="image.url" alt="이미지" />
      </div>

      <!-- 모달 -->
      <div v-if="selectedImage" class="modal" @click.self="closeModal">
        <!-- 닫기 버튼은 모달 바깥쪽에 위치 -->
        <button class="close-button" @click="closeModal">×</button>

        <div class="modal-content" :class="{ closing: isClosing }">
          <img :src="selectedImage.url" class="modal-image" alt="확대 이미지" />
          <button class="vote-button" @click="vote">🥇</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios'
import '../styles/ImageList.css'

const images = ref([])
const selectedImage = ref(null)
const isClosing = ref(false)

onMounted(async () => {
  try {
    const response = await axios.get('/image/list')
    const { success, images: imageList, message } = response.data

    if (success) {
      images.value = imageList
    }
  } catch (e) {
    console.error('이미지 목록 에러:', e)
    const errorMessage = e.response?.data?.message || '❌ 서버 오류 발생 ❌'
    alert(errorMessage)
  }
})

const openModal = (image) => {
  selectedImage.value = image
}

const closeModal = () => {
  isClosing.value = true
  setTimeout(() => {
    selectedImage.value = null
    isClosing.value = false
  }, 250)
}

const vote = async () => {
  if (!selectedImage.value) return

  try {
    const response = await axios.post('/image/vote', {
      imageName: selectedImage.value.name,
    })

    const { success, message } = response.data

    if (success) {
      alert(message)
      closeModal()
    } else {
      alert(`${message}`)
    }
  } catch (e) {
    console.error('투표 실패:', e)
    const errorMessage = e.response?.data?.message || '❌ 서버 오류 발생 ❌'
    alert(errorMessage)
  }
}
</script>