<template>
  <div class="modal" @click.self="emit('close')">
    <img :src="previewUrl" class="modal-image" alt="미리보기" />

    <div class="button-row">
      <button class="modal-button upload" :disabled="loading" @click="handleConfirm">✅</button>
      <button class="modal-button cancel" :disabled="loading" @click="emit('close')">❌</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { defineProps, defineEmits } from 'vue'
import '../styles/ImageUploadModal.css'
import '../styles/Common.css'

defineProps({ previewUrl: String })
const emit = defineEmits(['close', 'confirm'])
const loading = ref(false)

const handleConfirm = () => {
  if (loading.value) return
  loading.value = true
  
  emit('confirm')
}
</script>