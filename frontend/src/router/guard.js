import { useAuthStore } from "../stores/auth";
import { isTokenValid } from "../stores/jwt-helper";

export function setupAuthGuard(router) {
  router.beforeEach(async (to, from, next) => {
    const auth = useAuthStore();
    const tokenValid = isTokenValid();

    // Route requires authentication
    if (to.meta.requiresAuth) {
      if (!tokenValid) {
        await auth.handleLogout();
        return next({ name: "LoginRegister", query: { redirect: to.fullPath } });
      }

      // Fetch user profile if not already loaded in state
      if (!auth.user) {
        try {
          await auth.getUser();
        } catch (error) {
          console.error("Auth guard failed to fetch user profile:", error);
          await auth.handleLogout();
          return next({ name: "LoginRegister", query: { redirect: to.fullPath } });
        }
      }

      // Role permission check
      if (to.meta.role && auth.role !== to.meta.role) {
        return next({ name: "home" });
      }
    }

    // Prevent logged in users from visiting Login/Register pages (only if profile is actually loaded)
    if ((to.name === "Login" || to.name === "Register" || to.name === "LoginRegister") && tokenValid && auth.user) {
      return next(auth.role === "ADMIN" ? { name: "AdminHome" } : { name: "userDashboard" });
    }

    next();
  });
}

