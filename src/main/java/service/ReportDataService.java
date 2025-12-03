package service;

import model.Order;
import service.order.OrderService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportDataService {
    private OrderService orderService;
    private Map<Long, Integer> booksEmployee;
    private Map<Long, Float> priceEmployee;

    public ReportDataService(OrderService orderService){
        this.orderService = orderService;
        this.booksEmployee = new HashMap<>();
        this.priceEmployee = new HashMap<>();
        generateData();
    }

    private void generateData(){
        List<Order> orders = orderService.findAll();
        for(Order order : orders) {
            if(order.getOrderDate().isBefore(LocalDateTime.now().minusDays(30))){
                continue;
            }
            boolean exists = false;
            Set<Long> employeeIds = booksEmployee.keySet();
            if (!employeeIds.isEmpty()) {
                for (Long emp : employeeIds) {
                    if (emp.equals(order.getEmployeeId())) {
                        booksEmployee.put(order.getEmployeeId(), order.getBookNumber() + booksEmployee.get(emp));
                        priceEmployee.put(order.getEmployeeId(), order.getTotalCost() + priceEmployee.get(emp));
                        exists = true;
                    }
                }
            }
            if(!exists) {
                booksEmployee.put(order.getEmployeeId(), order.getBookNumber());
                priceEmployee.put(order.getEmployeeId(), order.getTotalCost());
            }
        }
    }

    public Map<Long, Integer> getBooksEmployee(){
        return this.booksEmployee;
    }

    public Map<Long, Float> getPriceEmployee(){
        return this.priceEmployee;
    }
}
