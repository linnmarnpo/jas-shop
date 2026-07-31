<script setup>
import axios from "../service/axios";
import { onMounted, ref } from "vue";
import { formatDistanceToNow } from "date-fns";

const orders = ref([]);
const getOrders = async () => {
  try {
    let res = await axios.get("/orders");
    if (res && res.data) {
      orders.value = res.data.map((data) => {
        return {
          ...data,
          orderDate: data.orderDate ? formatDistanceToNow(new Date(data.orderDate)) : 'N/A',
        };
      });
    }
  } catch (error) {
    console.log(error);
  }
};

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
onMounted(() => {
  getOrders();
});
</script>

<template>
  <div class="mt-4 overflow-x-auto sm:rounded-lg">
    <table
      class="w-full text-base text-left text-gray-700 rounded rtl:text-right"
    >
      <thead class="text-gray-900 uppercase bg-gray-100">
        <tr>
          <th scope="col" class="px-6 py-3">Order Id</th>
          <th scope="col" class="px-6 py-3">Address</th>
          <th scope="col" class="px-6 py-3">Total</th>
          <th scope="col" class="px-6 py-3">Payment</th>
          <th scope="col" class="px-6 py-3">Status</th>
          <th scope="col" class="px-6 py-3">Ordered at</th>
        </tr>
      </thead>
      <tbody v-if="orders.length">
        <tr
          v-for="order in orders"
          :key="order.id"
          class="border-b hover:bg-slate-50"
        >
          <td class="px-6 py-3"># {{ order.id }}</td>
          <td class="px-6 py-3">
            {{ order.address }}
          </td>
          <td class="px-6 py-3">{{ order.totalAmount }} $</td>
          <td class="px-6 py-3">
            {{ order.paymentMethod }}
          </td>
          <th scope="row" class="flex items-center px-6 py-3 font-medium">
            <div
              class="px-2 py-1 text-white rounded"
              :class="statusClass(order.orderStatus)"
            >
              {{ order.orderStatus }}
            </div>
          </th>
          <td class="px-6 py-3">
            {{ order.orderDate }}
          </td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr v-for="n in 3" :key="n" class="border-b">
          <td class="px-6 py-4">
            <div class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"></div>
          </td>
          <td class="px-6 py-4">
            <div class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"></div>
          </td>
          <td class="px-6 py-4">
            <div class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"></div>
          </td>
          <td class="px-6 py-4">
            <div class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"></div>
          </td>
          <td class="px-6 py-4">
            <div class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"></div>
          </td>
          <td class="px-6 py-4">
            <div class="flex space-x-3">
              <div class="w-8 h-8 bg-gray-200 rounded-full animate-pulse"></div>
              <div class="w-8 h-8 bg-gray-200 rounded-full animate-pulse"></div>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped></style>
