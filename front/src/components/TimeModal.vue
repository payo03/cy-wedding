<template>
  <div class="modal" @click.self="$emit('close')">
    <div class="modal-content admin-modal-content">
      <button class="close-button" @click="$emit('close')">×</button>

      <h2>⏰ 시간 조정</h2>

      <!-- 업로드 가능 시간 -->
      <div class="form-group">
        <label>업로드 시간 ▼</label>

        <div class="form-group row-aligned">
          <label>시작 :</label>
          <DatePicker v-model="uploadStart" :format="'YYYY-MM-DD HH:mm'" :is24="true" />
        </div>

        <div class="form-group row-aligned">
          <label>종료 : </label>
          <DatePicker v-model="uploadEnd" :format="'YYYY-MM-DD HH:mm'" :is24="true" />
        </div>
      </div>

      <!-- 투표 가능 시간 -->
      <div class="form-group">
        <label>투표 시간 ▼</label>

        <div class="form-group row-aligned">
          <label>시작 :</label>
          <DatePicker v-model="votingStart" :format="'YYYY-MM-DD HH:mm'" :is24="true" />
        </div>

        <div class="form-group row-aligned">
          <label>종료 : </label>
          <DatePicker v-model="votingEnd" :format="'YYYY-MM-DD HH:mm'" :is24="true" />
        </div>
      </div>

      <div class="button-row">
        <button class="action-button" :disabled="isLoading" @click="submitTimes">적용</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import DatePicker from 'vue3-date-time-picker'
import 'vue3-date-time-picker/dist/main.css'

import axios from '@/utils/axios'

const props = defineProps({
  userInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close'])

const isLoading = ref(false)

const uploadStart = ref(props.userInfo.uploadStart || '')
const uploadEnd = ref(props.userInfo.uploadEnd || '')
const votingStart = ref(props.userInfo.votingStart || '')
const votingEnd = ref(props.userInfo.votingEnd || '')

const submitTimes = async () => {
  if (isLoading.value) return

  isLoading.value = true
  try {
    await axios.post('/qr/time', {
      uploadStart: uploadStart.value,
      uploadEnd: uploadEnd.value,
      votingStart: votingStart.value,
      votingEnd: votingEnd.value
    })

    alert('업로드, 투표시간이 조정되었습니다')
    emit('close')
  } catch (e) {
    const errorMessage = e.response?.data?.message || '❌ 시간 조정 실패 ❌'
    alert(errorMessage)
  } finally {
    isLoading.value = false
  }
}
</script>
