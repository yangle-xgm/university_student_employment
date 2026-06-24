<template>
  <div class="empty-state">
    <div class="empty-icon">
      <el-icon :size="48">
        <component :is="icon" />
      </el-icon>
    </div>
    <h3 class="empty-title">{{ title }}</h3>
    <p v-if="description" class="empty-description">{{ description }}</p>
    <div v-if="$slots.default" class="empty-actions">
      <slot />
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: { type: [String, Object], default: 'Search' },
  title: { type: String, default: '暂无数据' },
  description: { type: String, default: '' }
})
</script>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12) var(--space-6);
  text-align: center;
  background-color: var(--color-card-bg);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
}

.empty-icon {
  color: var(--color-text-3);
  margin-bottom: var(--space-4);
}

.empty-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--color-text-1);
  margin-bottom: var(--space-2);
}

.empty-description {
  font-size: var(--text-base);
  color: var(--color-text-2);
  max-width: 400px;
  margin-bottom: var(--space-5);
}

.empty-actions {
  display: flex;
  gap: var(--space-3);
}
</style>
