<template>
  <router-view />
</template>

<style>
  html, body, #app {
    margin: 0;
    padding: 0;
    min-height: 100vh;
  }
  body {
    font-family: "PingFang SC", "Microsoft YaHei", "Helvetica Neue", Inter, system-ui, sans-serif;
    color: var(--text-primary);
    background:
      radial-gradient(900px 500px at 85% -10%, rgba(201,123,74,.18), transparent 60%),
      radial-gradient(700px 400px at -10% 20%, rgba(85,122,154,.12), transparent 60%),
      linear-gradient(180deg, var(--bg-page-1), var(--bg-page-2));
  }
</style>
