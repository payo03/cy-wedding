<template>
  <div class="modal" @click.self="emit('close')">
    <div class="modal-content email-modal-content">
      <button class="close-button" @click="emit('close')">×</button>

      <h2>사진 전송</h2>

      <div class="form-group">
        <label for="email">이메일 주소 ▼</label>
        <input type="email" id="email" v-model="emailAddress" placeholder="abc@email.com, test@email.com" />
      </div>

      <div class="form-group row-aligned"> <label for="plan">플랜 선택:</label>
        <select id="plan" v-model="selectedPlan">
          <option value="ECO">Eco</option>
          <option value="PREMIUM">Premium</option>
        </select>
      </div>

      <div class="button-row">
        <button class="send-action-button" :disabled="isLoading" @click="handleSendEmail">✉️ 전송</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from '@/utils/axios';
import '../styles/EmailModal.css';
import '../styles/Common.css';

const emit = defineEmits(['close', 'emailSent']);

const emailAddress = ref('');
const selectedPlan = ref('ECO');
const isLoading = ref(false);

const handleSendEmail = async () => {
  if (!emailAddress.value || isLoading.value) {
    alert('이메일 주소를 입력해주세요.');
    return;
  }

  isLoading.value = true;
  try {
    // 이메일전송 비동기
    const response = await axios.post('/image/email', {
      emailAddress: emailAddress.value,
      plan: selectedPlan.value,
    });
    const { message } = response.data;

    alert(message);
    emit('emailSent');
    emit('close');
  } catch (e) {
    const errorMessage = e.response?.data?.message || '❌ 이메일 전송 실패 ❌';

    alert(errorMessage);
  } finally {
    isLoading.value = false;
  }
};
</script>