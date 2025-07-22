<template>
  <section class="notice-section">
    <p>사진 업로드 및 투표는</p>
    <p>🙏1번만 가능합니다🙏</p>
    <button class="home-button" @click="goHome">HOME</button>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'
import '../styles/NoticeSection.css'

const router = useRouter()
const goHome = () => {
  router.push({ name: 'HomeView' })
}
</script>
