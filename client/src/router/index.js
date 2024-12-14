import { createRouter, createWebHistory } from "vue-router";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/LoginView.vue"),
    },
    {
      path: "/",
      redirect: "/home",
    },
    {
        path: "/register",
        name: "register",
        component: () => import("@/views/RegisterView.vue"),
    },
    {
        path: "/home",
        name: "home",
        component: () => import("@/views/HomeView.vue"),
    },
    {
        path: "/profile",
        name: "profile",
        component: () => import("@/views/ProfileView.vue"),
    },
    {
        path: "/messages",
        name: "messages",
        component: () => import("@/views/MessagesView.vue"),
    },
    {
        path: "/create-post",
        name: "create-post",
        component: () => import("@/views/CreatePostView.vue"),
    },
    {
        path: "/my-posts",
        name: "my-posts",
        component: () => import("@/views/MyPostsView.vue"),
    },
    // {
    //     path: "/category",
    //     name: "category",
    //     component: () => import("@/views/CategoryView.vue"),
    // },

  ],
});

export default router;
