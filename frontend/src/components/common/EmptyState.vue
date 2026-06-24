<template>
  <div class="empty-state">
    <div class="empty-glow"></div>
    <div class="empty-icon">
      <el-icon :size="56">
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
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-16) var(--space-8);
  text-align: center;
  background: var(--gradient-surface);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
  border: 1px dashed var(--color-border);
  overflow: hidden;
}

.empty-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.06) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.empty-icon {
  color: var(--color-primary);
  margin-bottom: var(--space-5);
  opacity: 0.6;
  position: relative;
  z-index: 1;
}

.empty-title {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  margin-bottom: var(--space-2);
  position: relative;
  z-index: 1;
}

.empty-description {
  font-size: var(--text-base);
  color: var(--color-text-2);
  max-width: 400px;
  margin-bottom: var(--space-6);
  line-height: 1.6;
  position: relative;
  z-index: 1;
}

.empty-actions {
  display: flex;
  gap: var(--space-3);
  position: relative;
  z-index: 1;
}
</style>
