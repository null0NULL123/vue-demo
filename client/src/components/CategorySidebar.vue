<template>
  <aside class="forum-categories">
    <h2>Categories</h2>
    <ul>
      <li v-for="category in categories" :key="category.id">
        <a
          href="#"
          @click.prevent="$emit('select-category', category.id)"
          :class="{ active: selectedCategory === category.id }"
        >
          {{ category.name }}
        </a>
      </li>
    </ul>
  </aside>
</template>

<script setup>
defineProps({
  categories: {
    type: Array,
    required: true
  },
  selectedCategory: {
    type: Number,
    default: null
  }
})

defineEmits(['select-category'])
</script>

<style scoped>
.forum-categories {
  flex: 1;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.forum-categories h2 {
  color: #1a73e8;
  margin-bottom: 15px;
}

.forum-categories ul {
  list-style-type: none;
  padding: 0;
}

.forum-categories li {
  margin-bottom: 10px;
}

.forum-categories a {
  text-decoration: none;
  color: #5f6368;
  transition: color 0.3s ease;
  display: block;
  padding: 8px 12px;
  border-radius: 5px;
}

.forum-categories a:hover,
.forum-categories a.active {
  color: #1a73e8;
  background-color: #e8f0fe;
}
</style>