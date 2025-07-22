<template>
  <div class="image-list-container">
    <div class="image-columns">
      <div
        v-for="(column, colIndex) in imageColumns"
        :key="colIndex"
        class="image-column"
      >
        <div
          v-for="(image, index) in column"
          :key="index"
          class="image-item"
          @click="openModal(image)"
        >
          <img :src="image" alt="업로드 이미지" />
        </div>
      </div>
    </div>

    <!-- 모달 -->
    <div v-if="selectedImage" class="modal" @click.self="closeModal">
      <img :src="selectedImage" class="modal-image" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const images = ref([])
const selectedImage = ref(null)

// 화면 크기에 따라 분할 수 결정
const columnCount = ref(3)
const updateColumnCount = () => {
  columnCount.value = window.innerWidth < 768 ? 2 : 3
}

onMounted(async () => {
  window.addEventListener('resize', updateColumnCount)
  updateColumnCount()

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
  selectedImage.value = null
}

const imageColumns = computed(() => {
  const cols = Array.from({ length: columnCount.value }, () => [])
  images.value.forEach((img, index) => {
    cols[index % columnCount.value].push(img)
  })
  return cols
})
</script>  