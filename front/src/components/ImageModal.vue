<template>
  <div class="modal" @click.self="emit('close')">
    <button class="close-button" @click="emit('close')">×</button>

    <div class="modal-content">

      <button v-if="isPrev" class="nav-button left" @click="handlePrev">←</button>
      <img :src="image.imageUrl" 
        class="modal-image" 
        :class="transitionClass"
        @touchstart="onTouchStart"
        @touchend="onTouchEnd"/>
      <button v-if="isNext" class="nav-button right" @click="handleNext">→</button>
      
      <div class="button-row">
        <button class="vote-button" :disabled="isLoading" @click="handleVote">👍</button>
        <button v-if="isAdminUser" :disabled="isLoading" class="delete-button" @click="handleDelete">🗑️</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from '@/utils/axios'
import '../styles/ImageModal.css'

const transitionClass = ref('')

const props = defineProps({
  image: Object,
  user: Object,
  isPrev: Boolean,
  isNext: Boolean
})
const emit = defineEmits(['close', 'voted', 'next', 'prev'])
const isLoading = ref(false)

const isAdminUser = computed(() => {
  return props.user?.admin || props.user?.domainAdmin
})

const handleVote = async () => {
  if (isLoading.value) return

  isLoading.value = true
  const confirmMessage = props.user.vote ?
    '♻️ 선택한 사진으로 재투표 하시겠습니까?' :
    '🎀 선택한 사진에 투표 하시겠습니까?'

  const confirmVote = window.confirm(confirmMessage)
  if (!confirmVote) {
    isLoading.value = false
    return
  }

  try {
    const res = await axios.post('/image/vote', {
      fileName: props.image.fileName,
    })

    const { success, message } = res.data
    if (success) {
      emit('voted')
    }
    alert(message)
  } catch (e) {
    const errorMessage = e.response?.data?.message || '❌ 서버 오류 발생 ❌'

    alert(errorMessage)
  } finally {
    isLoading.value = false
    emit('close')
  }
}

const handleDelete = async () => {
  if (!props.image) return
  isLoading.value = true

  const confirmDelete = window.confirm('정말 이 사진을 삭제하시겠습니까?')
  if (!confirmDelete) {
    isLoading.value = false
    return
  }

  try {
    const res = await axios.post('/image/delete', {
      fileName: props.image.fileName,
    })

    const { success, message } = res.data
    alert(message)

    if (success) {
      emit('voted')
      emit('close')
    }
  } catch (e) {
    const errorMessage = e.response?.data?.message || '❌ 삭제 중 오류 발생 ❌'

    alert(errorMessage)
  } finally {
    isLoading.value = false
    emit('close')
  }
}

const handlePrev = () => {
  animateSwipe('right')
  setTimeout(() => emit('prev'), 10)
}

const handleNext = () => {
  animateSwipe('left')
  setTimeout(() => emit('next'), 10)
}

// Swipe 처리
const animateSwipe = (direction) => {
  transitionClass.value = direction === 'left' ? 'slide-left' : 'slide-right'

  setTimeout(() => {
    transitionClass.value = ''
  }, 300)
}

let startX = 0
const onTouchStart = (e) => {
  if (!e.touches) return
  startX = e.touches[0].clientX
}

const onTouchEnd = (e) => {
  if (!e.changedTouches) return
  const endX = e.changedTouches[0].clientX
  const diff = endX - startX

  if (Math.abs(diff) < 50) return // 스와이프 인식 최소 거리

  if (diff > 0) {
    animateSwipe('right')
    emit('prev') // 오른쪽으로 스와이프 → 이전
  } else {
    animateSwipe('left')
    emit('next') // 왼쪽으로 스와이프 → 다음
  }
}
</script>