package repository.order;

import model.Order;
import model.builder.OrderBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ORDER;

public class OrderRepositoryMySQL implements OrderRepository{
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Order> findAll() {
        String sql = "Select * from `" + ORDER + "` ;";

        List<Order> orders = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{
        return new OrderBuilder()
                .setId(resultSet.getLong("id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setBookNumber(resultSet.getInt("book_number"))
                .setTotalCost(resultSet.getFloat("total_cost"))
                .setOrderDate(resultSet.getTimestamp("order_date").toLocalDateTime())
                .build();
    }

    @Override
    public boolean add(Order order) {
        String newSql = "INSERT INTO `order`(id, employee_id, book_number, total_cost, order_date) VALUES(null, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setLong(1, order.getEmployeeId());
            preparedStatement.setInt(2, order.getBookNumber());
            preparedStatement.setFloat(3, order.getTotalCost());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
