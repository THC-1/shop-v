<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { Sku } from '@/api/sku'

const props = defineProps<{
  attributes: Record<string, string[]>
  skus: Sku[]
  modelValue: Record<string, string>
}>()

const emit = defineEmits<{
  'update:modelValue': [value: Record<string, string>]
  'skuSelected': [sku: Sku | null]
}>()

const selectedSpecs = ref<Record<string, string>>({})

watch(() => props.modelValue, (newVal) => {
  selectedSpecs.value = { ...newVal }
}, { immediate: true, deep: true })

function parseSpecValues(specValuesJson: string): Record<string, string> {
  try {
    return JSON.parse(specValuesJson)
  } catch {
    return {}
  }
}

function isOptionAvailable(specName: string, specValue: string): boolean {
  if (props.skus.length === 0) return true

  const tempSpecs = { ...selectedSpecs.value, [specName]: specValue }

  return props.skus.some(sku => {
    const skuSpecValues = parseSpecValues(sku.specValues)
    return Object.entries(tempSpecs).every(([key, val]) => {
      if (!val) return true
      return skuSpecValues[key] === val
    })
  })
}

function selectSpec(specName: string, value: string, disabled: boolean) {
  if (disabled) return
  selectedSpecs.value = { ...selectedSpecs.value, [specName]: value }
  emit('update:modelValue', selectedSpecs.value)
  updateSelectedSku()
}

function updateSelectedSku() {
  const allSelected = Object.keys(props.attributes).every(
    key => selectedSpecs.value[key]
  )
  if (!allSelected) {
    emit('skuSelected', null)
    return
  }
  const matchedSku = props.skus.find(sku => {
    const specValues = parseSpecValues(sku.specValues)
    return Object.entries(selectedSpecs.value).every(
      ([key, val]) => specValues[key] === val
    )
  })
  emit('skuSelected', matchedSku || null)
}

function isSelected(specName: string, value: string): boolean {
  return selectedSpecs.value[specName] === value
}
</script>

<template>
  <div class="space-y-4">
    <div v-for="(values, specName) in attributes" :key="specName" class="spec-group">
      <div class="flex items-center text-sm mb-2">
        <span class="text-gray-500 w-20">{{ specName }}</span>
      </div>
      <div class="flex flex-wrap gap-2">
        <button
          v-for="value in values"
          :key="value"
          class="px-4 py-2 border rounded cursor-pointer transition-all text-sm"
          :class="[
            isSelected(specName, value)
              ? 'border-taobao bg-taobao/5 text-taobao'
              : isOptionAvailable(specName, value)
                ? 'border-gray-300 hover:border-taobao text-gray-700'
                : 'border-gray-200 bg-gray-100 text-gray-400 cursor-not-allowed'
          ]"
          :disabled="!isOptionAvailable(specName, value)"
          @click="selectSpec(specName, value, !isOptionAvailable(specName, value))"
        >
          {{ value }}
        </button>
      </div>
    </div>
  </div>
</template>