<template>
  <section class="notice-section">
    <p>사진 업로드 : 1회</p>
    <p>투표 : 1번 (재투표 👌)</p>
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
