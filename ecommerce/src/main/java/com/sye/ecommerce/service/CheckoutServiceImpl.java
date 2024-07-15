package com.sye.ecommerce.service;

import com.sye.ecommerce.dao.CustomerRepository;
import com.sye.ecommerce.dto.Purchase;
import com.sye.ecommerce.dto.PurchaseResponse;
import com.sye.ecommerce.entity.Customer;
import com.sye.ecommerce.entity.Order;
import com.sye.ecommerce.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // retrieve order info from dto
        Order order = purchase.getOrder();

        //generate tracking no
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(orderItem -> order.add(orderItem));

        //populate order with addresses
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        //populate cust with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        //save to db
        customerRepository.save(customer);

        //return response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        // generate random UUID v4
        return UUID.randomUUID().toString();
    }
}
