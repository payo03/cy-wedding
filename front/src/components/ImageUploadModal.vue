<template>
  <div class="modal" @click.self="emit('close')">
    <img :src="previewUrl" class="modal-upload-image" />

    <p class="preview-info-text">📷 사진은 1분 뒤에 공개됩니다 📷</p>
    <div class="button-row">
      <button class="modal-button upload" :disabled="isLoading" @click="handleConfirm">✅</button>
      <button class="modal-button cancel" :disabled="isLoading" @click="emit('close')">❌</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import '../styles/ImageUploadModal.css'

defineProps({ previewUrl: String })
const emit = defineEmits(['close', 'confirm'])
const isLoading = ref(false)

const handleConfirm = () => {
  if (isLoading.value) return
  isLoading.value = true
  
  emit('confirm')
}
</script>