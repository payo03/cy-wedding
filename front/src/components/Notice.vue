<template>
  <section class="notice-section">
    <p>사진 업로드 및 투표는</p>
    <p>🙏1번만 가능합니다🙏</p>
    <button class="home-button" @click="goHome">🏠</button>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'
import '../styles/Notice.css'

const router = useRouter()
const goHome = () => {
  router.push({ name: 'MainView' })
}
</script>
