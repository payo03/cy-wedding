<template>
  <section class="notice-section">
    <p>사진 업로드 : {{ userInfo?.maxUploads ?? 1 }} 회</p>
    <p>투표 : {{ userInfo?.maxVotes ?? 1 }}번 (재투표 👌)</p>
    <button class="home-button" @click="goHome">🏠</button>
  </section>
</template>

<script setup>
import { useRouter } from 'vue-router'
import '../styles/Notice.css'

const router = useRouter()

const props = defineProps({
  userInfo: Object
})

const goHome = () => {
  router.push({ name: 'MainView' })
}
</script>
