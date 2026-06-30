<template>
  <div class="home">
    <h1>🎩 So Rich 大富翁</h1>
    <p>在线多人对战，成为最终地产大亨！</p>
    <button @click="enterLobby">进入大厅</button>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function enterLobby() {
  router.push('/lobby')
}
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  text-align: center;
}

h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
}

button {
  padding: 12px 32px;
  font-size: 1.1rem;
  background: #4a90d9;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

button:hover {
  background: #357abd;
}
</style>
