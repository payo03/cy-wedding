<template>
  <div class="insta-layout"
    :class="{ 'has-background': !!userInfo?.imageUrl }"
    :style="userInfo?.imageUrl ? { backgroundImage: `url(${userInfo.imageUrl})` } : {}"
  >

    <NoticeSection :userInfo="userInfo" />
    <header class="insta-header">
      <span class="insta-title">🌸 가장 예쁜 순간에 투표해주세요 🌸</span>
    </header>

    <div class="main-content">
      <router-view @updateUserInfo="updateUserInfo" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NoticeSection from './components/Notice.vue'
import './styles/App.css'

const userInfo = ref(null)

const updateUserInfo = (newInfo) => {
  userInfo.value = newInfo
}
</script>