package repository.order;

import model.Order;
import model.validator.Notification;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    boolean add(Order order);
}
