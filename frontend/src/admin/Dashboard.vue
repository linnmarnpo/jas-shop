<template>
  <div class="min-h-screen p-6 mt-3 bg-gray-100">
    <!-- Title -->
    <h1 class="mb-6 text-2xl font-bold">Admin Dashboard</h1>

    <!-- Stat Cards -->
    <div class="grid grid-cols-1 gap-4 mb-6 md:grid-cols-4">
      <div class="card">
        <p class="text-gray-500">Total Orders</p>
        <h2 class="mt-3 text-2xl font-bold">{{ orders.length }}</h2>
      </div>
      <div class="p-4 bg-yellow-500 shadow rounded-xl">
        <p class="text-sm font-medium text-white/80">Pending Orders</p>
        <h2 class="mt-3 text-3xl font-bold">{{ pendingOrders.length }}</h2>
      </div>
      <div class="p-4 bg-green-500 shadow rounded-xl">
        <p class="text-sm font-medium text-white/80">Delivered Orders</p>
        <h2 class="mt-3 text-3xl font-bold">{{ deliveredOrders.length }}</h2>
      </div>
      <div class="p-4 bg-red-500 shadow rounded-xl">
        <p class="text-sm font-medium text-white/80">Cancelled Orders</p>
        <h2 class="mt-3 text-3xl font-bold">{{ cancelledOrders.length }}</h2>
      </div>
    </div>

    <!-- Sales + Out of Stock -->
    <div class="grid grid-cols-1 gap-6 md:grid-cols-12">
      <!-- Sales Overview -->
      <div class="p-4 bg-white shadow rounded-xl md:col-span-9">
        <h2 class="mb-3 font-semibold">Sales Overview</h2>
        <canvas ref="salesChart"></canvas>
      </div>

      <!-- Out of Stock -->
      <div class="p-4 bg-white shadow rounded-xl md:col-span-3">
        <h2 class="mb-3 font-semibold">Out of Stock</h2>

        <div v-if="outOfStockProducts.length > 0" class="flex flex-col gap-3">
          <div
            v-for="item in outOfStockProducts"
            :key="item.id"
            class="p-3 transition border border-red-200 rounded-lg bg-red-50 hover:shadow-sm"
          >
            <p class="text-sm font-semibold text-gray-800">
              {{ item.name }}
            </p>

            <p
              v-for="variant in item.productVariants.filter(
                (v) => v.stockQuantity === 0,
              )"
              :key="variant.id"
              class="mt-0.5 text-xs text-gray-500"
            >
              Size {{ variant.size }} · {{ variant.color }}
            </p>

            <p class="mt-2 text-xs font-medium text-red-600">Out of stock</p>
          </div>
        </div>

        <!-- Empty State -->
        <div v-else class="py-6 text-sm text-center text-gray-400">
          🎉 No products are out of stock
        </div>
      </div>
    </div>

    <!-- Admin Uploaded Products -->
    <div class="p-4 mt-8 bg-white shadow rounded-xl">
      <h2 class="mb-4 font-semibold">RecentlyUploaded Products</h2>

      <div class="overflow-x-auto">
        <table class="w-full text-sm">
          <thead class="text-gray-500 border-b">
            <tr>
              <th class="py-2 text-left">Product</th>
              <th class="py-2 text-center">Category</th>
              <th class="py-2 text-center">Price</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="product in productStore.products"
              :key="product"
              class="border-b hover:bg-gray-50"
            >
              <td class="flex items-center gap-3 py-3">
                <img
                  :src="productStore.getPrimaryImage(product)"
                  class="object-cover w-10 h-10 rounded"
                />
                <div>
                  <p class="font-medium text-gray-800">{{ product.name }}</p>
                </div>
              </td>
              <td>{{ product.category.name }}</td>
              <td>{{ product.price.toFixed(2) }} $</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount } from "vue";
import axios from "../service/axios";
import Chart from "chart.js/auto";
import { formatDistanceToNow } from "date-fns";
import { useProductStore } from "../stores/product";

/* ==============================
   State
============================== */
const salesChart = ref(null);
const orders = ref([]);
const isLoading = ref(false);
let chartInstance = null;

const productStore = useProductStore();

/* ==============================
   Fetch Orders
============================== */
const getOrders = async () => {
  isLoading.value = true;

  try {
    const res = await axios.get("/orders/admin");

    orders.value = res.data.map((order) => ({
      ...order,
      rawOrderDate: order.orderDate,
      formattedDate: formatDistanceToNow(new Date(order.orderDate), {
        addSuffix: true,
      }),
      updating: false,
    }));
  } catch (error) {
    console.error("Failed to fetch orders:", error);
  } finally {
    isLoading.value = false;
  }
};

/* ==============================
   Order Statistics
============================== */
const pendingOrders = computed(() =>
  orders.value.filter((order) => order.orderStatus === "PENDING"),
);

const deliveredOrders = computed(() =>
  orders.value.filter((order) => order.orderStatus === "DELIVERED"),
);

const cancelledOrders = computed(() =>
  orders.value.filter((order) => order.orderStatus === "CANCELLED"),
);

/* ==============================
   Out Of Stock Products
============================== */
const outOfStockProducts = computed(() => {
  return productStore.products.filter((product) =>
    product.productVariants.some((variant) => variant.stockQuantity === 0),
  );
});

/* ==============================
   Get Current Week Range
============================== */
const getCurrentWeekRange = () => {
  const today = new Date();

  const firstDay = new Date(today);
  firstDay.setDate(today.getDate() - today.getDay());

  const lastDay = new Date(firstDay);
  lastDay.setDate(firstDay.getDate() + 6);

  firstDay.setHours(0, 0, 0, 0);
  lastDay.setHours(23, 59, 59, 999);

  return { firstDay, lastDay };
};

/* ==============================
   Calculate Weekly Sales
============================== */
const getWeeklySales = (ordersList) => {
  const weeklyTotals = Array(7).fill(0);

  const { firstDay, lastDay } = getCurrentWeekRange();

  ordersList.forEach((order) => {
    const date = new Date(order.rawOrderDate);

    if (date >= firstDay && date <= lastDay) {
      const day = date.getDay();
      weeklyTotals[day] += order.totalAmount;
    }
  });

  return weeklyTotals;
};

/* ==============================
   Create Chart
============================== */
const createChart = (data) => {
  if (chartInstance) {
    chartInstance.destroy();
  }

  chartInstance = new Chart(salesChart.value, {
    type: "bar",
    data: {
      labels: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
      datasets: [
        {
          label: "Weekly Sales ($)",
          data: data,
          backgroundColor: "#3b82f6",
          borderRadius: 6,
          barThickness: 28,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: true,
        },
        tooltip: {
          callbacks: {
            label: function (context) {
              return "$" + context.raw.toFixed(2);
            },
          },
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          title: {
            display: true,
            text: "Sales Amount ($)",
          },
        },
        x: {
          title: {
            display: true,
            text: "Days of Week",
          },
        },
      },
    },
  });
};

/* ==============================
   Lifecycle
============================== */
onMounted(async () => {
  await getOrders();
  await productStore.getLatestProducts();

  const weeklyData = getWeeklySales(orders.value);
  createChart(weeklyData);
});

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.destroy();
  }
});
</script>

<style scoped>
.card {
  @apply bg-white p-4 rounded-xl shadow;
}

/* Out of Stock */
.stock-card {
  @apply bg-white border border-gray-200 rounded-lg p-3 transition;
}

.stock-card:hover {
  @apply border-gray-300 shadow-sm;
}

.stock-name {
  @apply text-sm font-semibold text-gray-800;
}

.stock-meta {
  @apply text-xs text-gray-400 mt-0.5;
}

.stock-status {
  @apply text-xs text-red-500 font-medium mt-2;
}

/* Product status */
.status-in {
  @apply text-xs font-medium text-green-600 bg-green-100 px-2 py-0.5 rounded-full;
}

.status-out {
  @apply text-xs font-medium text-red-600 bg-red-100 px-2 py-0.5 rounded-full;
}
</style>
