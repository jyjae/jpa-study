package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class OrderRepository {
    @PersistenceContext
    private EntityManager em;
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDeliveryItemCollection() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(0)
                .setMaxResults(100)
                .getResultList();
    }

    public List<OrderSimpleDto> findAllWithDto() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleDto.class
        ).getResultList();

    }

    public List<Order> findAllWithItem() {
        return em.createQuery("select o from Order o " +
                "join fetch o.member m " +
                "join fetch o.delivery d " +
                "join fetch o.orderItems oi " +
                "join fetch oi.item i ", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }
}
