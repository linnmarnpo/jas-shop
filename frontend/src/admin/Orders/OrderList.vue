<template>
  <div>
    <!-- Header -->
    <div class="mt-[100px] flex justify-between items-center font-semibold">
      <h1 class="text-xl">Order List</h1>
      <div class="px-3 py-2 text-white bg-gray-800 rounded">
        {{ filteredOrders.length }} Orders
      </div>
    </div>

    <div class="px-4 py-4 mt-6 bg-white rounded shadow">
      <!-- Search -->
      <div class="flex items-center border px-3 py-2.5 rounded w-72">
        <Icon icon="icon-park-twotone:search" width="20" height="20" />
        <input
          type="number"
          v-model.number="search"
          placeholder="Search with order id..."
          class="w-full ml-3 focus:outline-none"
        />
      </div>

      <!-- Table -->
      <div class="mt-4 overflow-x-auto">
        <table class="w-full text-base text-left text-gray-700">
          <thead class="text-gray-900 uppercase bg-gray-100">
            <tr>
              <th class="px-6 py-3">Order Id</th>
              <th class="px-6 py-3">Customer Name</th>
              <th class="px-6 py-3">Customer Email</th>
              <th class="px-6 py-3">Address</th>
              <th class="px-6 py-3">Status</th>
              <th class="px-6 py-3">Ordered At</th>
            </tr>
          </thead>

          <!-- Loading -->
          <tbody v-if="isLoading">
            <tr v-for="n in 5" :key="n">
              <td colspan="6" class="px-6 py-4">
                <div class="h-3 bg-gray-200 rounded animate-pulse"></div>
              </td>
            </tr>
          </tbody>

          <!-- Data -->
          <tbody v-else>
            <tr
              v-for="order in filteredOrders"
              :key="order.id"
              class="border-b hover:bg-slate-50"
            >
              <td class="px-6 py-4 font-medium"># {{ order.id }}</td>
              <td class="px-6 py-4">{{ order.user.name }}</td>
              <td class="px-6 py-4">{{ order.user.email }}</td>
              <td class="px-6 py-4">{{ order.address || "-" }}</td>

              <!-- Status Update -->
              <td class="px-6 py-4">
                <div class="flex items-center gap-2">
                  <!-- Status color dot -->
                  <span
                    class="w-2.5 h-2.5 rounded-full"
                    :class="statusClass(order.orderStatus)"
                  ></span>

                  <!-- White dropdown -->
                  <div
                    :title="
                      order.orderStatus === 'DELIVERED' ||
                      order.orderStatus === 'CANCELLED'
                        ? 'Delivered and cancelled ordrers cannot be updated.'
                        : ''
                    "
                  >
                    <select
                      :value="order.orderStatus"
                      @change="
                        (e) => {
                          const originalStatus = order.orderStatus;
                          order.orderStatus = e.target.value;
                          updateOrderStatus(order, originalStatus);
                        }
                      "
                      :disabled="
                        order.updating ||
                        order.orderStatus === 'CANCELLED' ||
                        order.orderStatus === 'DELIVERED'
                      "
                      class="px-3 py-1 text-sm text-gray-800 bg-white border rounded focus:outline-none focus:ring-1 focus:ring-gray-300"
                      :class="
                        order.updating ||
                        order.orderStatus === 'CANCELLED' ||
                        order.orderStatus === 'DELIVERED'
                          ? 'cursor-not-allowed'
                          : ''
                      "
                    >
                      <option value="PENDING">PENDING</option>
                      <option value="DELIVERED">DELIVERED</option>
                      <option value="CANCELLED">CANCELLED</option>
                    </select>
                  </div>
                </div>
              </td>

              <td class="px-6 py-4">
                {{ order.orderDate }}
              </td>
            </tr>

            <tr v-if="!filteredOrders.length">
              <td colspan="6" class="py-6 text-center text-red-500">
                No orders found
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "../../service/axios";
import { Icon } from "@iconify/vue";
import { formatDistanceToNow } from "date-fns";

const orders = ref([]);
const search = ref(null);
const isLoading = ref(false);

/* =============================
   Fetch Orders (Admin)
============================= */
const getOrders = async () => {
  isLoading.value = true;

  try {
    const res = await axios.get("/orders/admin");

    orders.value = res.data.map((order) => ({
      ...order,
      orderDate: formatDistanceToNow(new Date(order.orderDate), {
        addSuffix: true,
      }),
      updating: false, // 👈 per-row loading
    }));
  } catch (error) {
    console.error("Failed to fetch orders:", error);
  } finally {
    isLoading.value = false;
  }
};

/* =============================
   Update Order Status
============================= */
const updateOrderStatus = async (order, originalStatus) => {
  try {
    order.updating = true;

    // Call API with the new value
    await axios.put(`/orders/${order.id}`, {
      status: order.orderStatus,
    });
  } catch (error) {
    console.error("Failed to update order status:", error);
  } finally {
    order.updating = false;
  }
};

/* =============================
   Search Filter
============================= */
const filteredOrders = computed(() => {
  if (!search.value) return orders.value;
  return orders.value.filter((order) => order.id === search.value);
});

/* =============================
   Status Color
============================= */
const statusClass = (status) => {
  switch (status) {
    case "PENDING":
      return "bg-yellow-500";
    case "SHIPPED":
      return "bg-blue-500";
    case "DELIVERED":
      return "bg-green-600";
    case "CANCELLED":
      return "bg-red-500";
    default:
      return "bg-gray-500";
  }
};

onMounted(getOrders);
</script>
