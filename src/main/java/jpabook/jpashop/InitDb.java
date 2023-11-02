package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.doInit1(100);
        initService.doInit2(100);
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void doInit1(int stockQuantity) {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "1", "1111"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(stockQuantity);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book1.setPrice(20000);
            book2.setStockQuantity(stockQuantity);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void doInit2(int stockQuantity) {
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("진주", "1", "2222"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("SPTRING1 BOOK");
            book1.setPrice(20000);
            book1.setStockQuantity(stockQuantity);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("SPTRING1 BOOK");
            book1.setPrice(40000);
            book2.setStockQuantity(stockQuantity);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
    }
}

