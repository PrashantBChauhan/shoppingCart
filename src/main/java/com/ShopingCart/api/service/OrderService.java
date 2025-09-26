package com.ShopingCart.api.service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface OrderService {

	public Order createRazorpayOrder(Long amount, String currency) throws RazorpayException;

	public boolean verifyPaymentSignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);
	
}