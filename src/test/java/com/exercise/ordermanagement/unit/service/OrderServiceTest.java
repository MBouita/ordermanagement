package com.exercise.ordermanagement.unit.service;

import com.exercise.ordermanagement.dto.NewOrderRequest;
import com.exercise.ordermanagement.dto.OrderStatusRequest;
import com.exercise.ordermanagement.entity.Order;
import com.exercise.ordermanagement.enums.OrderStatus;
import com.exercise.ordermanagement.exception.ValidationException;
import com.exercise.ordermanagement.repository.OrderRepository;
import com.exercise.ordermanagement.service.IDistanceCalculator;
import com.exercise.ordermanagement.service.OrderService;
import com.exercise.ordermanagement.service.validation.NewOrderRequestValidator;
import com.exercise.ordermanagement.service.validation.OrderStatusRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NewOrderRequestValidator newOrderValidator;
    @Mock
    private OrderStatusRequestValidator orderStatusValidator;

    @Mock
    private IDistanceCalculator distanceCalculator;
    @InjectMocks
    private OrderService orderService;

    @Test
    public void orderCreationSuccessfullWhenOrderRequestSubmitted() throws Exception {
        String[] origin = new String[]{"START_LATITUDE", "START_LONGITUDE"};
        String[] destination = new String[]{"END_LATITUDE", "END_LONGITUDE"};
        NewOrderRequest orderRequest = new NewOrderRequest(origin, destination);

        Order expectedResult = new Order();
        expectedResult.setStatus(OrderStatus.UNASSIGNED);
        expectedResult.setDistance(10);

        doNothing().when(newOrderValidator).validate(orderRequest);
        when(distanceCalculator.calculateDistance(orderRequest.getOrigin(), orderRequest.getDestination())).thenReturn(10);
        when(orderRepository.save(expectedResult)).thenReturn(expectedResult);


        Order order = orderService.createOrder(orderRequest);

        assertEquals(10, order.getDistance());
        assertEquals(OrderStatus.UNASSIGNED, order.getStatus());
    }

    @Test
    public void orderCreationThrowExceptionWhenOrderRequestIsInvalid() throws ValidationException {
        NewOrderRequest invalidRequest = new NewOrderRequest(new String[]{"START_LATITUDE", "START_LONGITUDE"}, new String[]{"START_LATITUDE", "START_LONGITUDE", ""});
        doThrow(ValidationException.class).when(newOrderValidator).validate(invalidRequest);
        assertThrows(Exception.class, () -> orderService.createOrder(invalidRequest));
    }

    @Test
    public void takeOrderSuccessfully() throws ValidationException {
        Integer orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.TAKEN);
        Order order = new Order();
        order.setStatus(OrderStatus.UNASSIGNED);

        doNothing().when(orderStatusValidator).validate(request);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.takeOrder(orderId, request);

        assertEquals(OrderStatus.TAKEN, order.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void takeOrderThrowExceptionIfOrderByIdNotFound() throws ValidationException {
        Integer orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.TAKEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        doNothing().when(orderStatusValidator).validate(request);

        assertThrows(ValidationException.class, () -> orderService.takeOrder(orderId, request));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void takeOrderThrowExceptionIfOrderAlreadyTaken() throws ValidationException {
        Integer orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.TAKEN);
        Order order = new Order();
        order.setStatus(OrderStatus.TAKEN);

        doNothing().when(orderStatusValidator).validate(request);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.takeOrder(orderId, request));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void getOrdersSuccessfully() throws ValidationException {
        int page = 1;
        int limit = 10;

        PageRequest pageable = PageRequest.of(0, limit);
        List<Order> orders = Arrays.asList(new Order(), new Order());

        when(orderRepository.findAll(pageable)).thenReturn(new PageImpl<>(orders));

        List<Order> result = orderService.getOrders(page, limit);

        assertEquals(orders.size(), result.size());
        assertEquals(orders.get(0), result.get(0));
        assertEquals(orders.get(1), result.get(1));
        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getOrdersThrowExceptionWhenInvalidPageOrLimit() {
        int page = 0;
        int limit = 10;

        assertThrows(ValidationException.class, () -> orderService.getOrders(page, limit));
    }

}