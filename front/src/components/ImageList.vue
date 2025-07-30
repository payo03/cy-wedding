<template>
  <div class="image-list-scroll-wrapper">
    <div class="image-columns">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="image-item"
        :class="{ 'my-image': image.qrCode === user?.qrCode }"
        @click="openModal(image)"
      >
        <!-- 메달 표시: voteTop3 배열에 포함되어 있을 때 -->
        <div
          v-if="getMedal(image)"
          class="medal-label"
          :class="getMedalClass(image)"
        >
          {{ getMedal(image) }}
        </div>

        <img v-if="image.imageUrl" :src="image.imageUrl" alt="이미지" />
        <div v-if="image.voteQRCode === user?.qrCode" class="voted-mark">✔</div>
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
          v-if="showEmailModal"
          @close="closeEmailModal"
          @emailSent="handleEmailSent"
        />
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
import { ref, onMounted, computed } from 'vue'
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

const voteTop3 = computed(() => {
  return [...images.value]
    .sort((a, b) => b.count - a.count)
    .slice(0, 3)
})

const getMedal = (image) => {
  const index = voteTop3.value.findIndex(i => i.fileName === image.fileName)
  if (index === 0) return '🥇'
  if (index === 1) return '🥈'
  if (index === 2) return '🥉'
  return null
}

const getMedalClass = (image) => {
  const index = voteTop3.value.findIndex(i => i.fileName === image.fileName)
  if (index === 0) return 'gold'
  if (index === 1) return 'silver'
  if (index === 2) return 'bronze'
  return ''
}

onMounted(fetchImageList)

const openModal = (image) => {
  selectedImage.value = image
  showModal.value = true
}
const closeModal = () => {
  showModal.value = false
}

const openEmailModal = () => {
  showEmailModal.value = true
}
const closeEmailModal = () => {
  showEmailModal.value = false
}
</script>