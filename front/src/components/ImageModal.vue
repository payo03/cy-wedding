<template>
  <div class="modal" @click.self="emit('close')">
    <button class="close-button" @click="emit('close')">×</button>

    <div class="modal-content">
      <img :src="image.url" class="modal-image" alt="확대 이미지" />
      <button class="vote-button" @click="handleVote">
        👍
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/utils/axios'
import '../styles/ImageModal.css'

const props = defineProps({ image: Object })
const emit = defineEmits(['close', 'voted'])
const isHovered = ref(false)

const handleVote = async () => {
  if (!props.image) return

  const confirmVote = window.confirm('정말 이 사진에 투표하시겠습니까?')
  if (!confirmVote) return

  try {
    const res = await axios.post('/image/vote', {
      fileName: props.image.name,
    })

    const { success, message } = res.data
    if (success) {
      emit('voted')
      emit('close')
    }
    alert(message)
  } catch (e) {
    console.error('투표 실패:', e)
    const errorMessage = e.response?.data?.message || '❌ 서버 오류 발생 ❌'
    alert(errorMessage)
  }
}
</script>