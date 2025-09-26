package com.ShopingCart.api.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

	@PostMapping("/updateItem/{id}")
    public String updateCartItems(HttpSession session, @PathVariable String id) {
		
		String cartItems = (String) session.getAttribute("cartItems");
		if (cartItems == null) {
			cartItems = id;	
		}
		else
		{
			cartItems = session.getAttribute("cartItems") + "," + id;
		}
		
        session.setAttribute("cartItems", cartItems);
        return cartItems;
    }
    @GetMapping("/create")
    public String createSession(HttpSession session) {
        session.setAttribute("username", "JohnDoe");

        String sessionId = session.getId();
        return "Session created with ID: " + sessionId;
    }

    @GetMapping("/get")
    public String getSession(HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "No session found!";
        }

        return "Session found with username: " + username;
    }

    @GetMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "Session invalidated!";
    }
}

