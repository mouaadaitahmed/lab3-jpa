package com.example.lab3.service;

import com.example.lab3.dto.OrderDto;
import com.example.lab3.dto.OrderItemDto;
import com.example.lab3.model.Order;
import com.example.lab3.model.OrderItem;
import com.example.lab3.model.Product;
import com.example.lab3.repository.OrderItemRepository;
import com.example.lab3.repository.OrderRepository;
import com.example.lab3.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductRepository productRepo;

    @Transactional
    public OrderDto create(OrderDto dto) {
        Order order = orderRepo.save(new Order());

        for (OrderItemDto itemDto : dto.items()) {
            Product product = productRepo.findById(itemDto.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemDto.productId()));
            OrderItem item = new OrderItem(null, product, itemDto.quantity(), order);
            order.getItems().add(orderItemRepo.save(item));
        }

        return toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return toDto(order);
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getItems()
                        .stream()
                        .map(i -> new OrderItemDto(i.getId(), i.getProduct().getId(), i.getQuantity()))
                        .toList()
        );
    }
}
