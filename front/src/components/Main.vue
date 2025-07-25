<template>
  <div class="main-actions">
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileSelect"
    />

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

onMounted(async () => {
  try {
    const response = await axios.get('/user/check')

    userInfo.value = response.data
  } catch (error) {
    console.error('유저 정보 로딩 실패:', error)

    alert('❌ QR로 접속해주세요 ❌')
  }
})

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

  try {
    const response = await axios.post('/image/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    const { success, message } = response.data
    alert(message)

    if (success) {
      selectedFile.value = null
      previewUrl.value = null
      showModal.value = false

      router.push({ name: 'ImageList' })
    }
  } catch (error) {
    console.error(error)

    const errorMessage = error.response?.data?.message || '❌ 서버 오류 발생 ❌'
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