<template>
  <div class="modal" @click.self="emit('close')">
    <div class="modal-content admin-modal-content">
      <button class="close-button" @click="emit('close')">×</button>

      <h2>QR 생성</h2>

      <div class="form-group">
        <label for="domain">도메인 ▼</label>
        <input
          id="domain"
          type="text"
          v-model="qrDomain"
          placeholder="ex, cy-wedding -> /qr/cy-wedding/[텍스트]"
        />
      </div>

      <div class="form-group row-aligned">
        <label for="prefix">텍스트</label>
        <input
          id="prefix"
          type="text"
          v-model="qrPrefix"
          placeholder="guest, friend, etc..."
        />
      </div>

      <div class="form-group row-aligned">
        <label for="count">개수</label>
        <input
          id="count"
          type="number"
          min="1"
          v-model.number="qrCount"
        />
      </div>

      <div class="button-row">
        <button class="action-button" :disabled="isLoading" @click="handleGenerateQR">
          🧾 QR 생성
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from '@/utils/axios'
import '../styles/AdminModal.css'
import '../styles/Common.css'

const emit = defineEmits(['close'])

const qrDomain = ref('')
const qrPrefix = ref('')
const qrCount = ref(1)

const isLoading = ref(false)

const handleGenerateQR = async () => {
  if (isLoading.value) return

  if (!qrPrefix.value.trim() || qrCount.value < 1) {
    alert('텍스트와 개수를 올바르게 입력하세요.')
    return
  }

  isLoading.value = true
  try {
    await axios.post('/qr/create', {
      domain: qrDomain.value,
      prefix: qrPrefix.value,
      count: qrCount.value,
    })

    const origin = window.location.origin
    const urls = Array.from({ length: qrCount.value }, (_, i) =>
      `${origin}/qr/${qrDomain.value}/${qrPrefix.value}${i + 1}`
    )
    const blob = new Blob([urls.join('\n')], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)

    const a = document.createElement('a')
    a.href = url
    a.download = `qr_${qrDomain.value}_${qrPrefix.value}.txt`
    a.click()
    URL.revokeObjectURL(url)

    alert(`🎉 QR 경로 ${qrCount.value}개가 생성되어 다운로드되었습니다.`)
    window.open('https://genqrcode.com/ko/multiple', '_blank')
    emit('close')
  } catch (err) {
    console.error(err)
    alert('❌ 서버 통신 중 오류가 발생했습니다. ❌')
  } finally {
    isLoading.value = false
  }
}
</script>