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
        <div v-if="getMedalInfo(image)" class="medal-label" :class="getMedalInfo(image).class">
          {{ getMedalInfo(image).medal }}
        </div>

        <img v-if="image.imageUrl" :src="image.imageUrl" alt="이미지" />
        <div v-if="image.voteQRCode === user?.qrCode" class="voted-mark">✔</div>
      </div>

      <!-- 모달 -->
      <transition name="modal-fade">
        <ImageModal
          v-if="showModal"
          :image="images[selectedIndex]"
          :user="user"
          :is-prev="isPrev"
          :is-next="isNext"
          @close="closeModal"
          @voted="fetchImageList"
          @next="handleNext"
          @prev="handlePrev"
        />
      </transition>

      <!-- 이메일 전송모달(관리자) -->
      <transition name="modal-fade">
        <EmailSendModal
          v-if="showEmailModal"
          @close="closeEmailModal"
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
import '../styles/AdminModal.css'
import '../styles/Common.css'

const images = ref([])
const user = ref(null)
const selectedIndex = ref(null)
const isPrev = computed(() => selectedIndex.value > 0)
const isNext = computed(() => selectedIndex.value < images.value.length - 1)

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

const voteMedalMap = computed(() => {
  const map = new Map()
  voteTop3.value.forEach((img, index) => {
    map.set(img.fileName, index)
  })
  return map
})

const getMedalInfo = (image) => {
  const index = voteMedalMap.value.get(image.fileName)
  if (index === 0) return { medal: '🥇', class: 'gold' }
  if (index === 1) return { medal: '🥈', class: 'silver' }
  if (index === 2) return { medal: '🥉', class: 'bronze' }
  return null
}

onMounted(fetchImageList)

const openModal = (image) => {
  const index = images.value.findIndex(i => i.fileName === image.fileName)
  selectedIndex.value = index
  showModal.value = true
}
const closeModal = () => {
  showModal.value = false
}
const handleNext = () => {
  if (selectedIndex.value < images.value.length - 1) {
    selectedIndex.value++
  }
}
const handlePrev = () => {
  if (selectedIndex.value > 0) {
    selectedIndex.value--
  }
}

const openEmailModal = () => {
  showEmailModal.value = true
}
const closeEmailModal = () => {
  showEmailModal.value = false
}
</script>