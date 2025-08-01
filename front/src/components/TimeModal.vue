<template>
  <div class="modal" @click.self="$emit('close')">
    <div class="modal-content admin-modal-content">
      <button class="close-button" @click="$emit('close')">×</button>

      <h2>⏰ 시간 조정</h2>
      
      <div class="form-group row-aligned">
        <label for="maxUploads">업로드 횟수</label>
        <input
          id="maxUploads"
          type="number"
          min="1"
          v-model.number="maxUploads"
        />
      </div>

      <div class="form-group row-aligned">
        <label for="maxVotes">투표 횟수</label>
        <input
          id="maxVotes"
          type="number"
          min="1"
          v-model.number="maxVotes"
        />
      </div>

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
import { parseUtcStringAsKst } from '@/utils/datetime'
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

const maxUploads = ref(props.userInfo.maxUploads)
const maxVotes = ref(props.userInfo.maxVotes)

const uploadStart = ref(parseUtcStringAsKst(props.userInfo.uploadStart))
const uploadEnd = ref(parseUtcStringAsKst(props.userInfo.uploadEnd))
const votingStart = ref(parseUtcStringAsKst(props.userInfo.votingStart))
const votingEnd = ref(parseUtcStringAsKst(props.userInfo.votingEnd))

const submitTimes = async () => {
  if (isLoading.value) return

  isLoading.value = true
  try {
    await axios.post('/qr/time', {
        maxUploads: maxUploads.value,
        maxVotes: maxVotes.value,
        uploadStart: uploadStart.value?.toISOString().replace('Z', ''),
        uploadEnd: uploadEnd.value?.toISOString().replace('Z', ''),
        votingStart: votingStart.value?.toISOString().replace('Z', ''),
        votingEnd: votingEnd.value?.toISOString().replace('Z', '')
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