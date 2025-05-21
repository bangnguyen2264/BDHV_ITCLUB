package com.example.bdhv_itclub.controller;
import com.example.bdhv_itclub.dto.reponse.OrderResponse;
import com.example.bdhv_itclub.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get-all/user/{id}")
    public ResponseEntity<?> getAllByUser(@PathVariable(value = "id") Integer userId) {
        List<OrderResponse> listOrder = orderService.getAllByUser(userId);
        if (listOrder.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listOrder);
    }

    @GetMapping("/list-all")
    public ResponseEntity<?> listAll(){
        List<OrderResponse> listOrder = orderService.getAll();
        if (listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listOrder);
    }
}
