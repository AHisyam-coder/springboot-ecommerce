package com.sye.ecommerce.dto;

import com.sye.ecommerce.entity.Address;
import com.sye.ecommerce.entity.Customer;
import com.sye.ecommerce.entity.Order;
import com.sye.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
