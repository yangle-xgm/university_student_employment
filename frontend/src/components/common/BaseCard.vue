<template>
  <div class="base-card" :class="{ hoverable }" :style="customStyle">
    <slot />
  </div>
</template>

<script setup>
defineProps({
  hoverable: { type: Boolean, default: true },
  padding: { type: String, default: '' },
  background: { type: String, default: '' }
})

const customStyle = (props) => {
  const style = {}
  if (props.padding) style.padding = props.padding
  if (props.background) style.background = props.background
  return style
}
</script>

<style scoped>
.base-card {
  background: var(--gradient-surface);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-2);
  border: 1px solid rgba(226, 232, 240, 0.8);
  padding: var(--space-5);
  transition: transform var(--t-normal) var(--ease-smooth),
              box-shadow var(--t-normal) var(--ease-smooth),
              border-color var(--t-fast) var(--ease-default);
  position: relative;
  overflow: hidden;
}

.base-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.8) 50%, transparent 100%);
  pointer-events: none;
}

.base-card.hoverable:hover {
  transform: translateY(-3px);
  box-shadow: var(--elevation-3);
  border-color: rgba(37, 99, 235, 0.15);
}
</style>
