<script setup>
import NoticeSection from './components/NoticeSection.vue'
import './styles/App.css'
</script>

<template>
  <div class="insta-layout">
    <NoticeSection />
    <header class="insta-header">
      <span class="insta-title">임채연 <span class="wedding-emoji">💍</span> 송찬영</span>
    </header>
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>