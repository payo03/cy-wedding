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
      <transition name="modal-fade" @after-leave="selectedImage = null">
        <ImageModal
          v-if="showModal"
          :image="selectedImage"
          :user="user"
          @close="closeModal"
          @voted="fetchImageList"
        />
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios'
import ImageModal from '@/components/ImageModal.vue'
import '../styles/ImageList.css'
import '../styles/Common.css'

const images = ref([])
const user = ref(null)
const selectedImage = ref(null)
const showModal = ref(false)

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
  }
}

onMounted(fetchImageList)

const openModal = (image) => {
  selectedImage.value = image
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}
</script>