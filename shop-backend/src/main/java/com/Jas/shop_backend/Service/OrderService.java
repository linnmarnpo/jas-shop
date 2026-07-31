package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.api.model.OrderRequest;
import com.Jas.shop_backend.enums.OrderStatus;
import com.Jas.shop_backend.enums.PaymentStatus;
import com.Jas.shop_backend.exception.ResourceNotFoundException;
import com.Jas.shop_backend.model.*;
import com.Jas.shop_backend.model.DAO.CartDAO;
import com.Jas.shop_backend.model.DAO.OrderDAO;
import com.Jas.shop_backend.model.DAO.OrderItemDAO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final UserDetailsService userDetailsService;
    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;

    public OrderService(UserDetailsService userDetailsService,
                        OrderDAO orderDAO,
                        OrderItemDAO orderItemDAO,
                        CartDAO cartDAO) {
        this.userDetailsService = userDetailsService;
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
    }

    @Transactional
    public void createOrder(OrderRequest orderRequest, Principal principal) throws Exception {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        List<Cart> carts = cartDAO.findByUser(user);

        if(carts.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAddress(orderRequest.getAddress());
        order.setDiscount(orderRequest.getDiscount());
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        double total = 0;

        List<OrderItem> orderItems = carts.stream().map(cart -> {

            ProductVariant variant = cart.getProductVariant();
            if(variant.getStockQuantity() < cart.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }
            variant.setStockQuantity(variant.getStockQuantity() - cart.getQuantity());

            OrderItem item = new OrderItem();
            item.setProduct(cart.getProduct());
            item.setProductVariant(cart.getProductVariant());
            item.setQuantity(cart.getQuantity());
            item.setOrder(order);

            return item;

        }).toList();

        total = carts.stream()
                .mapToDouble(c -> c.getProduct().getPrice() * c.getQuantity())
                .sum();
        order.setTotalAmount(total);
        order.setOrderItemList(orderItems);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setOrder(order);
        payment.setAmount(total);
        payment.setPaymentMethod("");

        order.setPayment(payment);

        orderDAO.save(order);

        cartDAO.deleteAllByUserId(user.getId());
    }

    public List<Order> getOrder(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        List<Order> orders = orderDAO.findByUser(user);
        if(orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders Empty");
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Transactional
    public Order updateOrder(Long id, OrderStatus status) {

        Order order = orderDAO.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id)
                );

        // Optional: prevent invalid transitions
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered order cannot be updated");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cancelled order cannot be updated");
        }

        order.setOrderStatus(status);

        return orderDAO.save(order);
    }
}
