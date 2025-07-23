<template>
  <div class="image-list-scroll-wrapper">
    <div class="image-columns">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="image-item"
        @click="openModal(image)"
      >
        <img :src="image" alt="업로드 이미지" />
      </div>

      <!-- 모달 -->
      <div v-if="selectedImage" class="modal" @click.self="closeModal">
        <!-- 닫기 버튼은 모달 바깥쪽에 위치 -->
        <button class="close-button" @click="closeModal">×</button>

        <div class="modal-content" :class="{ closing: isClosing }">
          <img :src="selectedImage" class="modal-image" alt="확대 이미지" />
          <button class="vote-button">🥇</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import '../styles/ImageList.css'

const images = ref([])
const selectedImage = ref(null)
const isClosing = ref(false)

onMounted(async () => {
  try {
    const response = await axios.get('/api/image/list')
    images.value = response.data
  } catch (e) {
    console.error('이미지 목록 불러오기 실패:', e)
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
  }, 250) // 애니메이션 시간과 일치
}

</script>