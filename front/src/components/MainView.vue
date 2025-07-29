<template>
  <div class="main-actions">
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileSelect"
    />

    <!-- 관리자 전용 QR 생성 영역 -->
    <button v-if="userInfo?.admin" class="main-btn qr" @click="openQRModal">
      <span class="icon">🧾</span> QR 생성 <span class="icon">🧾</span>
    </button>

    <button class="main-btn upload" @click="fileUpload">
      <span class="icon">💍</span> 사진 업로드 <span class="icon">💍</span>
    </button>

    <button class="main-btn vote" @click="goToImageList">
      <span class="icon">🎀</span> 사진 투표 <span class="icon">🎀</span>
    </button>

    <!-- 업로드 사진 미리보기 모달 -->
    <transition name="modal-fade" @after-leave="cleanupModal">
      <ImageUploadModal
        v-if="showModal"
        :previewUrl="previewUrl"
        @confirm="confirmUpload"
        @close="cancelUpload"
      />
    </transition>

    <!-- 관리자 QR 생성 모달 -->
    <transition name="modal-fade">
      <QRModal v-if="showQRModal" @close="closeQRModal" />
    </transition>
  </div>

  <div v-if="isLoading" class="fullscreen-loader">
    <div class="spinner"></div>
    <p>업로드 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/utils/axios'
import ImageUploadModal from '@/components/ImageUploadModal.vue'
import QRModal from '@/components/QRModal.vue'
import '../styles/MainView.css'
import '../styles/Common.css'

const userInfo = ref(null)
const fileInput = ref(null)
const selectedFile = ref(null)
const previewUrl = ref(null)
const router = useRouter()
const showModal = ref(false)
const showQRModal = ref(false)
const isLoading = ref(false)

onMounted(async () => {
  try {
    const response = await axios.get('/user/check')

    userInfo.value = response.data
  } catch (error) {
    console.error('유저 정보 로딩 실패:', error)

    alert('❌ QR로 접속해주세요 ❌')
  }
})

const openQRModal = () => {
  showQRModal.value = true
}
const closeQRModal = () => {
  showQRModal.value = false
}

const fileUpload = () => {

  const isAdmin = userInfo.value.admin
  const isUpload = userInfo.value.upload

  if (!isAdmin && isUpload) {
    alert('❌ 업로드한 사진이 존재합니다. ❌')
    return
  }

  fileInput.value?.click()
}

const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  selectedFile.value = file
  previewUrl.value = URL.createObjectURL(file)
  showModal.value = true
}

const confirmUpload = async () => {
  if (!selectedFile.value) return

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  isLoading.value = true
  try {
    const response = await axios.post('/image/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    const { success, message } = response.data
    if (success) {
      setTimeout(() => {
        showModal.value = false
        selectedFile.value = null
        previewUrl.value = null

        alert(message)
        isLoading.value = false
        router.push({ name: 'ImageList' })
      }, 2000)
    } else {
      alert(message)
      isLoading.value = false
    }
  } catch (error) {
    console.error(error)
    const errorMessage = error.response?.data?.message || '❌ 서버 오류 발생 ❌'

    isLoading.value = false
    alert(errorMessage)
  }
}

const cancelUpload = () => {
  showModal.value = false
}

const cleanupModal = () => {
  selectedFile.value = null
  previewUrl.value = null
}

const goToImageList = () => {
  router.push({ name: 'ImageList' })
}
</script>