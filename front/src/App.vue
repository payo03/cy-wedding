<script setup>
import NoticeSection from './components/Notice.vue'
import './styles/App.css'
</script>

<template>
  <div class="insta-layout">
    <NoticeSection />
    <header class="insta-header">
      <span class="insta-title">🌸 가장 예쁜 순간에 투표해주세요 🌸</span>
    </header>
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>