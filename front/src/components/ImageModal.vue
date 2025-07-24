<template>
  <div class="modal" @click.self="emit('close')">
    <button class="close-button" @click="emit('close')">×</button>

    <div class="modal-content">
      <img :src="image.url" class="modal-image" alt="확대 이미지" />
      
      <div class="button-row">
        <button class="vote-button" @click="handleVote">👍</button>
        <button v-if="user.admin" class="delete-button" @click="handleDelete">🗑️</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import axios from '@/utils/axios'
import '../styles/ImageModal.css'
import '../styles/Common.css'

const props = defineProps({ 
    image: Object,
    user: Object
})
const emit = defineEmits(['close', 'voted'])

const handleVote = async () => {
  if (!props.image) return

  const confirmMessage = props.user.vote ?
    '♻️ 선택한 사진으로 재투표 하시겠습니까?' :
    '🎀 선택한 사진에 투표 하시겠습니까?'

  const confirmVote = window.confirm(confirmMessage)
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

const handleDelete = async () => {
  if (!props.image) return

  const confirmDelete = window.confirm('정말 이 사진을 삭제하시겠습니까?')
  if (!confirmDelete) return

  try {
    const res = await axios.post('/image/delete', {
      fileName: props.image.name,
    })

    const { success, message } = res.data
    if (success) {
      emit('voted')
    }
    alert(message)
  } catch (e) {
    console.error('삭제 실패:', e)
    const errorMessage = e.response?.data?.message || '❌ 삭제 중 오류 발생 ❌'
    alert(errorMessage)
  }
}
</script>