<script setup>
function goToUpload() {
  // TODO: 사진 업로드 페이지로 이동
  alert('사진 업로드 기능은 준비 중입니다.');
}
function goToVote() {
  // TODO: 사진 투표 페이지로 이동
  alert('사진 투표 기능은 준비 중입니다.');
}
</script>

<template>
  <div class="container">
    <section class="notice">
      <h2>공지사항</h2>
      <p>사진 업로드 및 투표는 7월 30일까지 가능합니다. 많은 참여 부탁드립니다!</p>
    </section>
    <main class="main-actions">
      <button class="main-btn" @click="goToUpload">사진 업로드</button>
      <button class="main-btn" @click="goToVote">사진 투표</button>
    </main>
  </div>
</template>

<style scoped>
.container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ff 100%);
}
.notice {
  width: 100%;
  background: #6366f1;
  color: #fff;
  padding: 2rem 1rem 1.2rem 1rem;
  text-align: center;
  box-shadow: 0 2px 8px rgba(99,102,241,0.08);
  border-radius: 0 0 1.5rem 1.5rem;
  margin-bottom: 3rem;
}
.notice h2 {
  margin: 0 0 0.5rem 0;
  font-size: 1.5rem;
  font-weight: 700;
}
.notice p {
  margin: 0;
  font-size: 1.1rem;
}
.main-actions {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  margin-top: 4rem;
}
.main-btn {
  font-size: 2rem;
  padding: 2rem 4rem;
  border: none;
  border-radius: 2rem;
  background: #6366f1;
  color: #fff;
  font-weight: bold;
  box-shadow: 0 4px 24px rgba(99,102,241,0.15);
  cursor: pointer;
  transition: background 0.2s, transform 0.2s;
}
.main-btn:hover {
  background: #4f46e5;
  transform: translateY(-2px) scale(1.03);
}
</style>
