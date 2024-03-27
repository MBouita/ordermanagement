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

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.takeOrder(orderId, request);

        assertEquals(OrderStatus.TAKEN, order.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void takeOrderThrowExceptionIfOrderByIdNotFound() {
        Integer orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.TAKEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> orderService.takeOrder(orderId, request));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void takeOrderThrowExceptionIfOrderAlreadyTaken() {
        // Arrange
        Integer orderId = 1;
        OrderStatusRequest request = new OrderStatusRequest();
        request.setStatus(OrderStatus.TAKEN);
        Order order = new Order();
        order.setStatus(OrderStatus.TAKEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.takeOrder(orderId, request));
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

}