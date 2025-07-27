<template>
  <div class="modal" @click.self="emit('close')">
    <button class="close-button" @click="emit('close')">×</button>

    <div class="modal-content">
      <img :src="image.url" class="modal-image" alt="확대 이미지" />
      
      <div class="button-row">
        <button class="vote-button" :disabled="loading" @click="handleVote">👍</button>
        <button v-if="user.admin" :disabled="loading" class="delete-button" @click="handleDelete">🗑️</button>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/utils/axios'
import '../styles/ImageModal.css'
import '../styles/Common.css'

const props = defineProps({ 
    image: Object,
    user: Object
})
const emit = defineEmits(['close', 'voted'])
const loading = ref(false)

const handleVote = async () => {
  if (!props.image || loading.value) return

  loading.value = true
  const confirmMessage = props.user.vote ?
    '♻️ 선택한 사진으로 재투표 하시겠습니까?' :
    '🎀 선택한 사진에 투표 하시겠습니까?'

  const confirmVote = window.confirm(confirmMessage)
  if (!confirmVote) {
    loading.value = false
    return
  }

  try {
    const res = await axios.post('/image/vote', {
      fileName: props.image.name,
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
    loading.value = false
    emit('close')
  }
}

const handleDelete = async () => {
  if (!props.image) return
  loading.value = true

  const confirmDelete = window.confirm('정말 이 사진을 삭제하시겠습니까?')
  if (!confirmDelete) {
    loading.value = false
    return
  }

  try {
    const res = await axios.post('/image/delete', {
      fileName: props.image.name,
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
    loading.value = false
    emit('close')
  }
}
</script>