<template>
  <div class="modal" @click.self="emit('close')">
    <img :src="previewUrl" class="modal-image" alt="미리보기" />

    <div class="button-row">
      <button class="modal-button upload" @click="emit('confirm')">✅</button>
      <button class="modal-button cancel" @click="emit('close')">❌</button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import '../styles/ImageUploadModal.css'
import '../styles/Common.css'

defineProps({ previewUrl: String })
const emit = defineEmits(['close', 'confirm'])
</script>