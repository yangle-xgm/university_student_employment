<template>
  <div class="section-header">
    <div class="section-titles">
      <h2 class="section-title">{{ title }}</h2>
      <p v-if="subtitle" class="section-subtitle">{{ subtitle }}</p>
    </div>
    <div v-if="$slots.default" class="section-actions">
      <slot />
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, default: '' }
})
</script>

<style scoped>
.section-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: var(--space-4);
  margin-bottom: var(--space-5);
}

.section-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  letter-spacing: -0.02em;
  line-height: 1.2;
}

.section-subtitle {
  font-size: var(--text-sm);
  color: var(--color-text-2);
  margin-top: var(--space-1);
}

.section-actions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-shrink: 0;
}
</style>
