package com.sye.ecommerce.service;

import com.sye.ecommerce.dto.Purchase;
import com.sye.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);
}
