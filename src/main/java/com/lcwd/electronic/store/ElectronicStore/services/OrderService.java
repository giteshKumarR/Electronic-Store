package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.OrderRequest;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestAdmin;
import com.lcwd.electronic.store.ElectronicStore.payload.orderpayload.UpdateOrderRequestUser;

import java.util.List;

public interface OrderService {

    // Create Order
    OrderDto createOrderFromCart(String userId, OrderRequest orderRequest);
    PagableResponse<OrderDto> getAllOrders(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );
    List<OrderDto> getAllOrdersOfUser(String userId);

    void deleteOrder(String userId, String orderId);
    OrderDto updateOrderUser(String userId, UpdateOrderRequestUser updateOrderRequestUser);

    // For admin uses
    OrderDto searchByOrderNumber(String orderNumber);

    OrderDto updateOrderAdmin(String userId, UpdateOrderRequestAdmin updateOrderRequestAdmin);



}
