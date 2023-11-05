package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.query.OrderQueryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * xxxToMany
 */
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    public OrderApiController(OrderRepository orderRepository, OrderQueryRepository orderQueryRepository) {
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
    }

    @GetMapping("/api/v1/orders")
    public List<OrderDto> ordersV1() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAllWithMemberDeliveryItemCollection().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderQueryDto> odersV3() {
        return orderQueryRepository.findAllDtos();
    }



    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;//상품 명
        private int orderPrice; //주문 가격
        private int count;      //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
