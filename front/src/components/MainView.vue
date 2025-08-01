<template>
  <div class="main-actions">
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileSelect"
    />

    <!-- 관리자 전용 QR 영역 -->
    <div v-if="isAbleLv2" class="half-btn-group">
      <button v-if="isAbleLv1" class="main-btn qr half" @click="openQRModal">
        <span class="icon">🧾</span> QR 생성
      </button>

      <!-- 생성시간 조정 영역 -->
      <button class="main-btn sunset half" @click="openTimeModal">
        <span class="icon">⏰</span> 조정 <span class="icon">⏰</span>
      </button>
    </div>

    <button class="main-btn upload" @click="fileUpload" :disabled="!isUpload">
      <span class="icon">💍</span> 사진 업로드 <span class="icon">💍</span>
    </button>

    <button class="main-btn vote" @click="goToImageList" :disabled="!isVote">
      <span class="icon">🎀</span> 사진 투표 <span class="icon">🎀</span>
    </button>

    <!-- 업로드 사진 미리보기 모달 -->
    <teleport to="body">
      <transition name="modal-fade" @after-leave="cleanupModal">
        <ImageUploadModal
          v-if="showModal"
          :previewUrl="previewUrl"
          @confirm="confirmUpload"
          @close="cancelUpload"
        />
      </transition>
    </teleport>

    <!-- 관리자 QR 생성 모달 -->
    <teleport to="body">
      <transition name="modal-fade">
        <QRModal v-if="showQRModal" @close="closeQRModal" />
      </transition>
    </teleport>
    <!-- 관리자 시간 조정 모달 -->
    <teleport to="body">
      <transition name="modal-fade">
        <TimeModal v-if="showTimeModal"
          :userInfo="userInfo"
          @close="closeTimeModal" 
        />
      </transition>
    </teleport>
  </div>

  <div v-if="isLoading" class="fullscreen-loader">
    <div class="spinner"></div>
    <p>업로드 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/utils/axios'
import ImageUploadModal from '@/components/ImageUploadModal.vue'
import QRModal from '@/components/QRModal.vue'
import TimeModal from '@/components/TimeModal.vue'
import { parseUtcStringAsKst, isBetweenNow } from '@/utils/datetime'
import '../styles/MainView.css'
import '../styles/AdminModal.css'
import '../styles/Common.css'

const router = useRouter()

const backgroundImage = ref('')

const userInfo = ref(null)
const fileInput = ref(null)
const selectedFile = ref(null)
const previewUrl = ref(null)

const showModal = ref(false)
const showQRModal = ref(false)
const showTimeModal = ref(false)

const isLoading = ref(false)
const now = ref(Date.now())

const emit = defineEmits(['updateUserInfo'])

const isAbleLv1 = computed(() => {
  return userInfo.value?.admin
})
const isAbleLv2 = computed(() => {
  return userInfo.value?.admin || (userInfo.value?.domainAdmin && userInfo.value?.plan === 'P')
})
const isAbleLv3 = computed(() => {
  return userInfo.value?.admin || userInfo.value?.domainAdmin
})

onMounted(async () => {
  try {
    const response = await axios.get('/user/check')

    userInfo.value = response.data
    emit('updateUserInfo', response.data)
    if (!response.data || !response.data.qrCode) {
      alert('❌ 잘못된 접근입니다 ❌')
      return
    }

    if (userInfo.value.imageUrl) {
      backgroundImage.value = `url(${userInfo.value.imageUrl})`
    }
  } catch (error) {
    console.error('유저 정보 로딩 실패:', error)

    alert('❌ QR로 접속해주세요 ❌')
  }
})

onUnmounted(() => {
  clearInterval(intervalId)
})

// 30초마다 현재 시간 업데이트
const intervalId = setInterval(() => {
  now.value = new Date()
}, 30000)

const isUpload = computed(() => {
  if (!userInfo.value) return false

  const uploadStart = parseUtcStringAsKst(userInfo.value.uploadStart)
  const uploadEnd = parseUtcStringAsKst(userInfo.value.uploadEnd)

  return isBetweenNow(uploadStart, now.value, uploadEnd)
})
const isVote = computed(() => {
  if (!userInfo.value) return false

  const votingStart = parseUtcStringAsKst(userInfo.value.votingStart)
  const votingEnd = parseUtcStringAsKst(userInfo.value.votingEnd)

  return isBetweenNow(votingStart, now.value, votingEnd)
})

const openQRModal = () => (showQRModal.value = true)
const closeQRModal = () => (showQRModal.value = false)
const openTimeModal = () => (showTimeModal.value = true)
const closeTimeModal = () => (showTimeModal.value = false)
const cancelUpload = () => (showModal.value = false)
const cleanupModal = () => {
  selectedFile.value = null
  previewUrl.value = null
}

const fileUpload = () => {

  const isUpload = userInfo.value.upload
  if (!isAbleLv3 && isUpload) {
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

const goToImageList = () => {
  router.push({ name: 'ImageList' })
}
</script>