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
    <div v-if="userInfo?.admin" class="qr-generator">
      <div class="qr-inputs">
        <input v-model="qrPrefix" type="text" placeholder="텍스트 입력" class="qr-input half" />
        <input v-model.number="qrCount" type="number" min="1" placeholder="개수" class="qr-input half" />
      </div>
      <button class="generate-btn" @click="generateQR">
        <span class="icon">🧾</span> QR 생성 <span class="icon">🧾</span>
      </button>
    </div>

    <button class="main-btn upload" @click="fileUpload">
      <span class="icon">💍</span> 사진 업로드 <span class="icon">💍</span>
    </button>

    <button class="main-btn vote" @click="goToImageList">
      <span class="icon">🎀</span> 사진 투표 <span class="icon">🎀</span>
    </button>

    <!-- 모달 컴포넌트 -->
    <transition name="modal-fade" @after-leave="cleanupModal">
      <ImageUploadModal
        v-if="showModal"
        :previewUrl="previewUrl"
        @confirm="confirmUpload"
        @close="cancelUpload"
      />
    </transition>
  </div>

  <div v-if="isLoading" class="fullscreen-loader">
    <div class="spinner"></div>
    <p>업로드 중입니다...</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'
import axios from '@/utils/axios'
import ImageUploadModal from '@/components/ImageUploadModal.vue'
import '../styles/Main.css'
import '../styles/Common.css'

const userInfo = ref(null)
const fileInput = ref(null)
const selectedFile = ref(null)
const previewUrl = ref(null)
const router = useRouter()
const showModal = ref(false)
const qrPrefix = ref('')
const qrCount = ref(1)
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

const generateQR = async () => {
  if (!qrPrefix.value || qrCount.value < 1) {
    alert('텍스트와 개수를 올바르게 입력하세요.')
    return
  }

  try {
    // 1. 서버에 QR 생성 요청
    await axios.post('/qr/create', {
      prefix: qrPrefix.value,
      count: qrCount.value,
    })

    // 2. 클라이언트에서 텍스트 파일 생성
    const origin = window.location.origin
    const lines = []
    for (let i = 1; i <= qrCount.value; i++) {
      lines.push(`${origin}/qr/${qrPrefix.value}${i}`)
    }

    const textContent = lines.join('\n')
    const blob = new Blob([textContent], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)

    const a = document.createElement('a')
    a.href = url
    a.download = `qr_${qrPrefix.value}.txt`
    document.body.appendChild(a)

    a.click()

    document.body.removeChild(a)
    URL.revokeObjectURL(url)

    alert(`🎉 QR 경로 ${qrCount.value}개가 생성되어 다운로드되었습니다. 🎉`)
    window.open('https://genqrcode.com/ko/multiple', '_blank')
  } catch (error) {
    console.error('QR 생성 요청 실패:', error)
    alert('❌ 서버와 통신 중 오류가 발생했습니다. ❌')
  }
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