package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.DishOrderStatus;
import com.restaurant_management.Centralization.model.EntityStatus;
import com.restaurant_management.Centralization.model.OrderStatus;
import com.restaurant_management.Centralization.model.enums.Status;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatusDAO {
    private final DataSource dataSource;

    public StatusDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StatusDAO() {
        this.dataSource = new DataSource();
    }

    public OrderStatus findByIdForOrder(Long id){
        String query = "SELECT * FROM order_status WHERE id = ?";
        OrderStatus orderStatus = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    orderStatus = new OrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("order_status")),
                            res.getTimestamp("order_status_date").toLocalDateTime(),
                            null
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderStatus;
    }

    //ORDERS
    public OrderStatus findByOrderStatusId(Long id) {
        String query = "SELECT * FROM order_status WHERE id = ?";
        OrderDAO orderDAO = new OrderDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    return new OrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("order_status")),
                            res.getTimestamp("order_status_date").toLocalDateTime(),
                            orderDAO.getById(res.getLong("order_id"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<EntityStatus> findAllByOrderId(Long id) {
        String query = "SELECT * FROM order_status WHERE order_id = ?";
        OrderDAO orderDAO = new OrderDAO();
        List<EntityStatus> statuses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                while (res.next()) {
                    statuses.add(new OrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("order_status")),
                            res.getTimestamp("order_status_date").toLocalDateTime(),
                            null
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statuses;
    }

    public List<OrderStatus> saveAllOrderStatus(List<OrderStatus> statuses) {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        for (OrderStatus status : statuses) {
            String insertQuery = "INSERT INTO order_status (order_id, order_status, order_status_date, id) VALUES (?, ?, ?, ?)";
            String updateQuery = "UPDATE order_status SET order_id = ?, order_status = ?, order_status_date = ? WHERE id = ?";
            String executeQuery;

            if (findByIdForOrder(status.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)) {
                statement.setLong(1, status.getOrder().getId());
                statement.setObject(2, status.getStatus().name(), Types.OTHER);
                statement.setTimestamp(3, Timestamp.valueOf(status.getStatusDate()));
                statement.setLong(4, status.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return orderStatusList;
    }


    //DISH ORDERS
    public EntityStatus findByDishOrderStatusId(Long id) {
        String query = "SELECT * FROM dish_order_status WHERE id = ?";
        DishOrderDAO dishOrderDAO = new DishOrderDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    return new DishOrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("dish_order_status")),
                            res.getTimestamp("dish_order_status_date").toLocalDateTime(),
                            dishOrderDAO.getById(res.getLong("dish_order_id"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DishOrderStatus> findAllByDishOrderId(Long id) {
        String query = "SELECT * FROM dish_order_status WHERE ish_order_id = ?";
        DishOrderDAO dishOrderDAO = new DishOrderDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                List<DishOrderStatus> statuses = new ArrayList<>();
                while (res.next()) {
                    statuses.add(new DishOrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("dish_order_status")),
                            res.getTimestamp("dish_order_status_date").toLocalDateTime(),
                            null
                    ));
                }
                return statuses;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishOrderStatus> saveAllDishOrderStatus(List<DishOrderStatus> statuses) {
        for (DishOrderStatus status : statuses) {
            String insertQuery = "INSERT INTO dish_order_status (dish_order_id, dish_order_status, dish_order_status_date, id) VALUES (?, ?, ?, ?)";
            String updateQuery = "UPDATE dish_order_status SET dish_order_id = ?, status = ?, dish_order_status_date = ? WHERE id = ?";
            String executeQuery;

            if (findByIdForDishOrder(status.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)) {
                statement.setLong(1, status.getDishOrder().getId());
                statement.setObject(2, status.getStatus().name(), Types.OTHER);
                statement.setTimestamp(3, Timestamp.valueOf(status.getStatusDate()));
                statement.setLong(4, status.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return statuses;
    }

    public DishOrderStatus findByIdForDishOrder(Long id){
        String query = "SELECT * FROM dish_order_status WHERE id = ?";
        DishOrderStatus dishOrderStatus = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    dishOrderStatus = new DishOrderStatus(
                            res.getLong("id"),
                            Status.valueOf(res.getString("dish_order_status")),
                            res.getTimestamp("dish_order_status_date").toLocalDateTime(),
                            null
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishOrderStatus;
    }

    public void deleteById(Long id) {
        String query = "DELETE FROM dish_order_status WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
