package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.Order;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAO {
    private final DataSource dataSource;

    public OrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public OrderDAO() {
        this.dataSource = new DataSource();
    }

    //@Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT id, reference, date_time FROM \"order\"";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet res = statement.executeQuery()) {
                while (res.next()) {
                    orders.add(new Order(
                            res.getLong("id"),
                            res.getTimestamp("order_date").toLocalDateTime(),
                            res.getString("reference"),
                            new ArrayList<>(),
                            new ArrayList<>()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    //@Override
    public Order getById(Long id) {
        String query = "SELECT * FROM \"order\" WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    return new Order(
                            res.getLong("id"),
                            res.getTimestamp("order_date").toLocalDateTime(),
                            res.getString("reference"),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Order> saveAll(List<Order> orders) {
        List<Order> orderList = new ArrayList<>();
        StatusDAO statusDAO = new StatusDAO();
        DishOrderDAO dishOrderDAO = new DishOrderDAO();
        for (Order order : orders) {
            String insertQuery = "INSERT INTO \"order\" (reference, order_date, id) VALUES (?, ?, ?)";
            String updateQuery = "UPDATE \"order\" SET reference = ?, order_date = ? WHERE id = ?";
            String executeQuery;

            if (getById(order.getId()) != null) {
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)) {
                statement.setString(1, order.getReference());
                statement.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                statement.setLong(3, order.getId());
                statement.executeUpdate();

                statusDAO.saveAllOrderStatus(order.getOrderStatus());
                dishOrderDAO.saveAll(order.getDishOrders());

                orderList.add(order);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return orderList;
    }

    public Order findByReference(String reference){
        String query = "SELECT * FROM \"order\" WHERE reference = ?";
        Order order = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reference);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    order = new Order(
                            res.getLong("id"),
                            res.getTimestamp("order_date").toLocalDateTime(),
                            res.getString("reference"),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    //@Override
    public void deleteById(Long id) {
        String query = "DELETE FROM \"order\" WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
