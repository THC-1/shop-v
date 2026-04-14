import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { listCartItems, addCartItem, deleteCartItem, updateCartItemQuantity, toggleCartItemSelected, clearCart as clearCartApi, removeCartItems } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref<any[]>([])
  const loading = ref(false)

  const totalCount = computed(() => items.value.length)

  const selectedItems = computed(() => items.value.filter((item: any) => item.selected))

  const totalPrice = computed(() =>
    selectedItems.value.reduce((sum: number, item: any) => sum + Number(item.totalPrice || item.price * item.quantity || 0), 0)
  )

  async function fetchCartItems() {
    loading.value = true
    try {
      items.value = await listCartItems()
    } finally {
      loading.value = false
    }
  }

  async function addToCart(productId: number, skuId: number, quantity: number) {
    await addCartItem({ productId, skuId, quantity })
    await fetchCartItems()
  }

  async function updateQuantity(id: number, quantity: number) {
    await updateCartItemQuantity(id, quantity)
    await fetchCartItems()
  }

  async function removeItem(id: number) {
    await deleteCartItem(id)
    await fetchCartItems()
  }

  async function toggleSelect(id: number) {
    await toggleCartItemSelected(id)
    await fetchCartItems()
  }

  async function clearAll() {
    await clearCartApi()
    items.value = []
  }

  async function removeCheckedItems() {
    const ids = selectedItems.value.map((item: any) => item.id)
    if (ids.length > 0) {
      await removeCartItems(ids)
      await fetchCartItems()
    }
  }

  return { items, loading, totalCount, selectedItems, totalPrice, fetchCartItems, addToCart, updateQuantity, removeItem, toggleSelect, clearAll, removeCheckedItems }
})
