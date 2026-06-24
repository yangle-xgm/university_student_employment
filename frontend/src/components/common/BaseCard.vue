<template>
  <div class="base-card">
    <slot />
  </div>
</template>

<script setup>
</script>

<style scoped>
.base-card {
  background-color: var(--color-card-bg);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
  padding: var(--space-5);
  transition: box-shadow var(--t-normal) var(--ease-default);
}

.base-card:hover {
  box-shadow: var(--elevation-2);
}
</style>
