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
          <div class="date-wrapper">
            <Datepicker
                v-model="uploadStart"
                format="yyyy-MM-dd HH:mm"
                locale="ko"
                :enable-time-picker="true"
                :is24="true"
                :clearable="false"
            />
          </div>
        </div>

        <div class="form-group row-aligned">
          <label>종료 : </label>
          <div class="date-wrapper">
            <Datepicker
                v-model="uploadEnd"
                format="yyyy-MM-dd HH:mm"
                locale="ko"
                :enable-time-picker="true"
                :is24="true"
                :clearable="false"
            />
          </div>
        </div>
      </div>

      <!-- 투표 가능 시간 -->
      <div class="form-group">
        <label>투표 시간 ▼</label>

        <div class="form-group row-aligned">
          <label>시작 :</label>
          <div class="date-wrapper">
            <Datepicker
                v-model="votingStart"
                format="yyyy-MM-dd HH:mm"
                locale="ko"
                :enable-time-picker="true"
                :is24="true"
                :clearable="false"
            />
          </div>
        </div>

        <div class="form-group row-aligned">
          <label>종료 : </label>
          <div class="date-wrapper">
            <Datepicker
                v-model="votingEnd"
                format="yyyy-MM-dd HH:mm"
                locale="ko"
                :enable-time-picker="true"
                :is24="true"
                :clearable="false"
            />
          </div>
        </div>
      </div>

      <div class="button-row">
        <button class="action-button" :disabled="isLoading" @click="submitTimes">
          적용
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

import axios from '@/utils/axios'

const props = defineProps({
  userInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close'])

const isLoading = ref(false)

const uploadStart = ref(props.userInfo.uploadStart ? new Date(props.userInfo.uploadStart) : null)
const uploadEnd = ref(props.userInfo.uploadEnd ? new Date(props.userInfo.uploadEnd) : null)
const votingStart = ref(props.userInfo.votingStart ? new Date(props.userInfo.votingStart) : null)
const votingEnd = ref(props.userInfo.votingEnd ? new Date(props.userInfo.votingEnd) : null)

const submitTimes = async () => {
  if (isLoading.value) return

  isLoading.value = true
  try {
    await axios.post('/qr/time', {
      uploadStart: uploadStart.value?.toISOString(),
      uploadEnd: uploadEnd.value?.toISOString(),
      votingStart: votingStart.value?.toISOString(),
      votingEnd: votingEnd.value?.toISOString()
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

<style scoped>
.date-wrapper :deep(.dp__input) {
  font-size: 1.1rem;
  padding: 1.2rem 1.5rem;
  height: 3.5rem;
  border-radius: 1.2rem;
  border: 2px solid rgba(248, 225, 234, 0.7);
  background-color: #fcfcfc;
  font-family: 'Gowun Dodum', sans-serif;
  color: #4d4d4d;
  box-sizing: border-box;
  width: 100%;
  text-align: right;
}
</style>