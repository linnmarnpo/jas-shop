package com.Jas.shop_backend.api.model;

import com.Jas.shop_backend.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateOrderStatusRequest {
    private OrderStatus status;

}
