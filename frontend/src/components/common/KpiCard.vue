<template>
  <div class="kpi-card" :class="{ clickable: clickable }" @click="$emit('click')">
    <div class="kpi-glow" :style="{ background: gradient }"></div>
    <div class="kpi-content">
      <div class="kpi-header">
        <div class="kpi-icon" :style="{ background: iconBg, color: iconColor }">
          <el-icon :size="22">
            <component :is="icon" />
          </el-icon>
        </div>
        <div v-if="trend" class="kpi-trend" :class="trendType">
          <el-icon :size="12"><ArrowUp v-if="trendType === 'up'" /><ArrowDown v-else /></el-icon>
          <span>{{ trend }}</span>
        </div>
      </div>
      <div class="kpi-value">{{ value }}</div>
      <div class="kpi-label">{{ label }}</div>
      <div v-if="hint" class="kpi-hint">{{ hint }}</div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  icon: { type: [String, Object], required: true },
  value: { type: [String, Number], required: true },
  label: { type: String, required: true },
  hint: { type: String, default: '' },
  iconBg: { type: String, default: 'rgba(37, 99, 235, 0.1)' },
  iconColor: { type: String, default: 'var(--color-primary)' },
  gradient: { type: String, default: 'linear-gradient(135deg, rgba(37, 99, 235, 0.12) 0%, rgba(124, 58, 237, 0.08) 100%)' },
  clickable: { type: Boolean, default: false },
  trend: { type: String, default: '' },
  trendType: { type: String, default: 'up' }
})

defineEmits(['click'])

const gradient = computed(() => props.gradient)
</script>

<style scoped>
.kpi-card {
  position: relative;
  background: var(--gradient-surface);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-2);
  border: 1px solid rgba(226, 232, 240, 0.8);
  padding: var(--space-5);
  overflow: hidden;
  transition: transform var(--t-normal) var(--ease-smooth),
              box-shadow var(--t-normal) var(--ease-smooth);
}

.kpi-card.clickable {
  cursor: pointer;
}

.kpi-card.clickable:hover {
  transform: translateY(-4px);
  box-shadow: var(--elevation-3);
}

.kpi-glow {
  position: absolute;
  top: -50%;
  right: -30%;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.5;
  pointer-events: none;
}

.kpi-content {
  position: relative;
  z-index: 1;
}

.kpi-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-4);
}

.kpi-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: var(--radius);
  flex-shrink: 0;
}

.kpi-trend {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
}

.kpi-trend.up {
  background-color: var(--color-success-light);
  color: var(--color-success);
}

.kpi-trend.down {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.kpi-value {
  font-size: var(--text-2xl);
  font-weight: var(--font-extrabold);
  color: var(--color-text-1);
  line-height: 1.1;
  letter-spacing: -0.03em;
  margin-bottom: var(--space-2);
}

.kpi-label {
  font-size: var(--text-sm);
  color: var(--color-text-2);
  font-weight: var(--font-medium);
}

.kpi-hint {
  font-size: var(--text-xs);
  color: var(--color-text-3);
  margin-top: var(--space-2);
}
</style>
