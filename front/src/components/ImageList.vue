<template>
  <div class="image-list-scroll-wrapper">
    <div class="image-columns">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="image-item"
        @click="openModal(image)"
      >
        <img v-if="image.imageUrl" :src="image.imageUrl" alt="이미지" />
      </div>

      <!-- 모달 -->
      <transition name="modal-fade" @after-leave="selectedImage = null">
        <ImageModal
          v-if="showModal"
          :image="selectedImage"
          :user="user"
          @close="closeModal"
          @voted="fetchImageList"
        />
      </transition>

      <!-- 이메일 전송모달(관리자) -->
      <transition name="modal-fade">
        <EmailSendModal
          v-if="showEmailModal" @close="closeEmailModal" @emailSent="handleEmailSent" />
      </transition>

      <button v-if="user?.admin" class="send-button" @click="openEmailModal">
        📷 사진 전송
      </button>
    </div>
  </div>

  <!-- Loading -->
  <div v-if="isLoading" class="fullscreen-loader">
    <div class="spinner"></div>
    <p>로딩 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios'
import ImageModal from '@/components/ImageModal.vue'
import EmailSendModal from '@/components/EmailModal.vue'
import '../styles/ImageList.css'
import '../styles/Common.css'

const images = ref([])
const user = ref(null)
const selectedImage = ref(null)
const showModal = ref(false)
const showEmailModal = ref(false)
const isLoading = ref(true)

const fetchImageList = async () => {
  try {
    const response = await axios.get('/image/list')

    const { success, images: imageList, user: userInfo } = response.data

    if (success) {
      images.value = imageList
      user.value = userInfo
    }
  } catch (e) {
    console.error('이미지 목록 에러:', e)

    const errorMessage = e.response?.data?.message || '❌ 서버 오류 발생 ❌'
    alert(errorMessage)
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchImageList)

// ImageModal
const openModal = (image) => {
  selectedImage.value = image
  showModal.value = true
}
const closeModal = () => {
  showModal.value = false
}

// EmailModal(관리자)
const openEmailModal = () => {
  showEmailModal.value = true
}
const closeEmailModal = () => {
  showEmailModal.value = false
}
</script>