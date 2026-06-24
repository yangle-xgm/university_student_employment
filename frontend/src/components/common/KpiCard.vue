<template>
  <div class="kpi-card" :class="{ clickable: clickable }" @click="$emit('click')">
    <div class="kpi-icon" :style="{ backgroundColor: iconBg, color: iconColor }">
      <el-icon :size="24">
        <component :is="icon" />
      </el-icon>
    </div>
    <div class="kpi-content">
      <div class="kpi-value">{{ value }}</div>
      <div class="kpi-label">{{ label }}</div>
      <div v-if="hint" class="kpi-hint">{{ hint }}</div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: { type: [String, Object], required: true },
  value: { type: [String, Number], required: true },
  label: { type: String, required: true },
  hint: { type: String, default: '' },
  iconBg: { type: String, default: 'var(--color-primary-light)' },
  iconColor: { type: String, default: 'var(--color-primary)' },
  clickable: { type: Boolean, default: false }
})

defineEmits(['click'])
</script>

<style scoped>
.kpi-card {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  background-color: var(--color-card-bg);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
  padding: var(--space-5);
  transition: transform var(--t-normal) var(--ease-default),
              box-shadow var(--t-normal) var(--ease-default);
}

.kpi-card.clickable {
  cursor: pointer;
}

.kpi-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: var(--elevation-2);
}

.kpi-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  border-radius: var(--radius);
  flex-shrink: 0;
}

.kpi-content {
  flex: 1;
  min-width: 0;
}

.kpi-value {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  line-height: 1.2;
}

.kpi-label {
  font-size: var(--text-sm);
  color: var(--color-text-2);
  margin-top: var(--space-1);
}

.kpi-hint {
  font-size: var(--text-xs);
  color: var(--color-text-3);
  margin-top: var(--space-1);
}
</style>
