package jpabook.jpashop.repository.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findAllOrderDtos() {
        return em.createQuery(" select new jpabook.jpashop.repository.query.OrderQueryDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o " +
                " join o.member m " +
                " join o.delivery d ", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllDtos() {
        List<OrderQueryDto> result = findAllOrderDtos();
        result.stream()
                .forEach(o -> {
                    List<OrderItemDto> orderItems = findOrderItems(o.getOrderId());
                    o.setOrderItems(orderItems);
                });
        return result;
    }

    private List<OrderItemDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.repository.query.OrderItemDto(" +
                "oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findAllDtos_optimization() {
        List<OrderQueryDto> result = findAllOrderDtos();
        List<Long> orderIds = toOrderIds(result);

        List<OrderItemDto> orderItemDtos = em.createQuery("select new jpabook.jpashop.repository.query.OrderItemDto(" +
                        "oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        Map<Long, List<OrderItemDto>> orderItemMap = orderItemDtos.stream()
                .collect(Collectors.groupingBy(o -> o.getId()));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return  result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(toList());
    }
}
