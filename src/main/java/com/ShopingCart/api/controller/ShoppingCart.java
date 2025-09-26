package com.ShopingCart.api.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShopingCart.api.model.request.Item;
import com.ShopingCart.api.service.OrderService;
import com.ShopingCart.api.utility.ProductHelper;
import com.ShopingCart.api.utility.Utils;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class ShoppingCart {

	@Autowired
	HttpSession session;
	
	@Autowired
	Utils utils;
	
	@Autowired
    private OrderService orderService;
	
	@Value("${razorpay.key.id}")
	private	String keyId;
	
	@GetMapping("/error")
	public String getErrorPath() {
		return "error";
	}
	
	@GetMapping({"/", "/index"})
	public String indexPage(Model model) {
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");	
		String name = (String) session.getAttribute("name");
		
		model.addAttribute("name", name);
		model.addAttribute("totalItemscount", totalItemscount);
		model.addAttribute("isActive", "true");
		model.addAttribute("page", "index");
		
		return "index"; 
	}
	
	@GetMapping("/products")
	public String productListingPage(Model model) {
		List<Item> itemList = ProductHelper.getItemList();
		
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");
		String name = (String) session.getAttribute("name");
		
		model.addAttribute("name", name);
		model.addAttribute("totalItemscount", totalItemscount);
		model.addAttribute("isActive", "true");
		model.addAttribute("page", "products");
		model.addAttribute("itemList", itemList);
	    return "product"; 
	}
	
	@GetMapping("/productDetails/{id}")
	public String productDetailPage(@PathVariable("id") int id, Model model) {
		List<Item> itemList = ProductHelper.getItemList();
		Item itemDetail = itemList.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
		
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");
		String name = (String) session.getAttribute("name");
		
		model.addAttribute("name", name);
		model.addAttribute("totalItemscount", totalItemscount);
		model.addAttribute("isActive", "true");
		model.addAttribute("page", "productDetails");
		model.addAttribute("itemDetail", itemDetail);
	    return "productDetail"; 
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/viewCart")
	public String viewCart(Model model) throws Exception {
		if( session.getAttribute("totalItemscount") == null ){
			return "redirect:http://localhost:8080/index";
		}
		else {
			int totalPrice = 0;
			int totalItemscount = 0;
			int itemsCount;
			int price;
			final Map<Integer, Integer> cartItemsMap = (Map<Integer, Integer>) session.getAttribute("cartItem");
			
			List<Item> itemList = ProductHelper.getItemList();
			List<Item> cartItemList = itemList.stream().filter(item -> cartItemsMap.containsKey(item.getId())).toList();
			
			for( Entry<Integer, Integer> entry : cartItemsMap.entrySet() )
			{
				itemsCount = entry.getValue();
				price = itemList.stream().filter(item -> item.getId() == entry.getKey()).findFirst().orElse(null).getPrice();
				
				totalPrice = totalPrice + ( itemsCount * price );
				totalItemscount = totalItemscount + itemsCount;
				
			}
			session.setAttribute("totalItemscount", totalItemscount);
			String name = (String) session.getAttribute("name");
			
			model.addAttribute("name", name);
			model.addAttribute("totalItemscount", totalItemscount);
			model.addAttribute("isActive", "true");
			model.addAttribute("page", "viewCart");
			model.addAttribute("cartItem", cartItemsMap);
			model.addAttribute("totalPrice", totalPrice);
			model.addAttribute("itemList", itemList);
			model.addAttribute("cartItemList", cartItemList);
		    return "viewCart"; 
		}
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");	
		
		model.addAttribute("totalItemscount", totalItemscount);
		model.addAttribute("isActive", "true");
		model.addAttribute("page", "login");
	    return "login"; 
	}
	
	@PostMapping("/address")
	public String address(Model model, @RequestParam Map<String, Object> data) {
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");
		int amount = Integer.parseInt( (String) data.get("amount") );
		String name = (String) session.getAttribute("name");
		
		model.addAttribute("name", name);
		model.addAttribute("data", data);
		model.addAttribute("amount", amount);
		model.addAttribute("totalItemscount", totalItemscount);
		model.addAttribute("page", "address");
	    return "address"; 
	}
	/*
	@PostMapping("/payment")
	public String payment(Model model) {
		
		model.addAttribute("page", "payment");
	    return "payment"; 
	}
	
	
	@PostMapping("/payment/success")
	public String paymentSuccess(Model model) {
		
		model.addAttribute("page", "paymentSuccess");
	    return "success"; 
	}*/
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/setCartItems/{id}")
	public ResponseEntity<Void> setCartItems(@PathVariable int id) {
		Map<Integer, Integer> cartItems = new HashMap<>();
		
		if( session.getAttribute("cartItem") != null ){
			cartItems = (Map<Integer, Integer>) session.getAttribute("cartItem");
			if( cartItems.containsKey(id) )
			{
				cartItems.put(id, cartItems.get(id)+1);
			}
			else
			{
				cartItems.put(id, 1);
			}
		}
		else{
			cartItems.put(id, 1);
		}
		
		int totalItemscount =  session.getAttribute("totalItemscount") == null ? 0 : (int) session.getAttribute("totalItemscount");
		session.setAttribute("cartItem", cartItems);
		session.setAttribute("totalItemscount", totalItemscount+1);
		
	    return ResponseEntity.ok().build();
		
	}
	
	@PostMapping("/setUserSession")
	public String setUserSession( @RequestParam Map<String, String> formData) {
		session.setAttribute("name", formData.get("name"));
		
		if( session.getAttribute("totalItemscount") != null ){
			return "redirect:http://localhost:8080/viewCart";
		}
		
		return "redirect:http://localhost:8080/index";
	}
	
	@PostMapping("/createOrder")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> data) {
        try {
            Long amount = Long.parseLong(data.get("amount").toString());
            String currency = data.get("currency").toString();
            Order order = orderService.createRazorpayOrder(amount, currency);
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId", keyId); // Send keyId to frontend
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
	
	@PostMapping("/verifyPayment")
		public ResponseEntity<Map<String, String>> verifyPayment(@RequestBody Map<String, String> data) {
		String razorpayOrderId = data.get("razorpay_order_id");
		String razorpayPaymentId = data.get("razorpay_payment_id");
		String razorpaySignature = data.get("razorpay_signature");

		boolean isValid = orderService.verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
		if (isValid) {
			return ResponseEntity.ok(Collections.singletonMap("status", "Payment verified successfully"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid payment signature"));
		}
	}

	@GetMapping("/emptyCart")
	public String emptyCart() {
		session.removeAttribute("totalItemscount");
		session.removeAttribute("cartItem");
		
		return "redirect:/index";
	}
	
	@GetMapping("/logout")
	public String signOut() {
		session.removeAttribute("totalItemscount");
		session.removeAttribute("cartItem");
		session.removeAttribute("name");
		
		return "redirect:/index";
	}
	
	
}

