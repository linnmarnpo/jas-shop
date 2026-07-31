import Home from "../pages/Home.vue";
import LoginRegister from "../pages/auth/LoginRegister.vue";
import Login from "../pages/auth/Login.vue";
import Register from "../pages/auth/Register.vue";
import Detail from "../pages/Detail.vue";
import AdHome from "../admin/Home.vue";
import AdDashboard from "../admin/Dashboard.vue";

// product
import AddProduct from "../admin/Products/AddProduct.vue";
import ProductList from "../admin/Products/ProductList.vue";

// categories
import CategoryList from "../admin/Categories/CategoryList.vue";
import AddCategory from "../admin/Categories/AddCategory.vue";

const routes = [
  { path: "/", component: Home, name: "home" },

  {
    path: "/shop",
    component: () => import("../pages/Shop.vue"),
    name: "shop",
  },

  {
    path: "/checkout",
    component: () => import("../pages/Checkout.vue"),
    name: "checkout",
    meta: { requiresAuth: true },
    props: true,
  },

  {
    path: "/login-register",
    component: LoginRegister,
    name: "LoginRegister",
    redirect: { name: "Login" },
    children: [
      {
        path: "/login",
        component: Login,
        name: "Login",
      },
      {
        path: "/register",
        component: Register,
        name: "Register",
      },
    ],
  },
  {
    path: "/verify-email",
    component: () => import("../pages/auth/VerifyEmail.vue"),
    name: "VerifyEmail",
  },
  {
    path: "/forgot-password",
    component: () => import("../pages/auth/ForgotPassword.vue"),
    name: "ForgotPassword",
  },
  {
    path: "/password-reset/:token",
    component: () => import("../pages/auth/ResetPassword.vue"),
    name: "ResetPassword",
  },
  {
    path: "/dashboard",
    component: () => import("../pages/Dashboard.vue"),
    name: "userDashboard",
    redirect: { name: "orders" },
    meta: { requiresAuth: true },
    children: [
      {
        path: "orders",
        component: () => import("../pages/OrderList.vue"),
        name: "orders",
      },
      {
        path: "edit-account",
        component: () => import("../pages/AccountDetails.vue"),
        name: "editAccount",
      },
    ],
  },
  {
    path: "/shop/:productName",
    component: Detail,
    name: "Detail",
    props: true,
  },
  {
    path: "/admin",
    component: AdHome,
    name: "AdminHome",
    redirect: { name: "AdminDashboard" },
    meta: { requiresAuth: true, role: "ADMIN" },
    children: [
      {
        path: "dashboard",
        component: AdDashboard,
        name: "AdminDashboard",
      },
      { path: "products", component: ProductList, name: "ProductList" },
      { path: "products/add", component: AddProduct, name: "addProduct" },
      {
        path: "products/edit/:id",
        component: () => import("../admin/Products/EditProduct.vue"),
        name: "editProduct",
        props: true,
      },
      { path: "categories", component: CategoryList, name: "CategoryList" },
      { path: "categories/add", component: AddCategory, name: "addCategory" },
      {
        path: "orders",
        component: () => import("../admin/Orders/OrderList.vue"),
        name: "OrderList",
      },
    ],
  },
];

export default routes;
