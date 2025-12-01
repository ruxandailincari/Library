package model.builder;

import model.Order;

import java.time.LocalDateTime;

public class OrderBuilder {
    private Order order;

    public OrderBuilder(){
        order = new Order();
    }

    public OrderBuilder setId(Long id){
        order.setId(id);
        return this;
    }

    public OrderBuilder setEmployeeId(Long employeeId){
        order.setEmployeeId(employeeId);
        return this;
    }

    public OrderBuilder setBookNumber(Integer bookNumber){
        order.setBookNumber(bookNumber);
        return this;
    }

    public OrderBuilder setTotalCost(Float totalCost){
        order.setTotalCost(totalCost);
        return this;
    }

    public OrderBuilder setOrderDate(LocalDateTime orderDate){
        order.setOrderDate(orderDate);
        return this;
    }

    public Order build(){
        return order;
    }

}
