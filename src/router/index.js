import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import HomeView from "@/views/HomeView.vue";
import ProfileView from "@/views/ProfileView.vue";
import MessagesView from "@/views/MessagesView.vue";
import SettingsView from "@/views/SettingsView.vue";
import ContentView from "@/views/ContentView.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/",
      redirect: "/login",
    },
    {
      path: "/home",
      name: "home",
      component: HomeView,
    },
    {
      path: "/profile",
      name: "profile",
      component: ProfileView,
    },
    {
        path: "/content",
        name: "content",
        component: ContentView,
      },
    {
      path: "/messages",
      name: "messages",
      component: MessagesView,
    },
    {
        path: "/settings",
        name: "settings",
        component: SettingsView,
    },
  ],
});

export default router;
