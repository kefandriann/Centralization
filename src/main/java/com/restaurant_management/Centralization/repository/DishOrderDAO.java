package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.DishOrder;
import com.restaurant_management.Centralization.model.enums.Status;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishOrderDAO {
    private final DataSource dataSource;

    public DishOrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DishOrderDAO() {
        this.dataSource = new DataSource();
    }

    //@Override
    public List<DishOrder> getAll() {
        List<DishOrder> dishOrders = new ArrayList<>();
        DishDAO dishDAO = new DishDAO();
        OrderDAO orderDAO = new OrderDAO();
        String query = "SELECT * FROM dish_order";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet res = statement.executeQuery()) {
                while (res.next()) {
                    dishOrders.add(new DishOrder(
                            res.getLong("id"),
                            dishDAO.getById(res.getLong("dish_id")),
                            orderDAO.getById(res.getLong("order_id")),
                            res.getInt("dish_quantity"),
                            res.getTimestamp("dish_creation_date").toLocalDateTime(),
                            new ArrayList<>()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishOrders;
    }

    //@Override
    public DishOrder getById(Long id) {
        String query = "SELECT * FROM dish_order WHERE id = ?";
        DishDAO dishDAO = new DishDAO();
        OrderDAO orderDAO = new OrderDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    return new DishOrder(
                            res.getLong("id"),
                            dishDAO.getById(res.getLong("dish_id")),
                            orderDAO.getById(res.getLong("order_id")),
                            res.getInt("dish_quantity"),
                            res.getTimestamp("dish_creation_date").toLocalDateTime(),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DishOrder> findAllByOrderId(Long id) {
        String query = "SELECT * FROM dish_order WHERE order_id = ?";
        DishDAO dishDAO = new DishDAO();
        OrderDAO orderDAO = new OrderDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                List<DishOrder> dishOrders = new ArrayList<>();
                while (res.next()) {
                    dishOrders.add(new DishOrder(
                            res.getLong("id"),
                            dishDAO.getById(res.getLong("dish_id")),
                            orderDAO.getById(res.getLong("order_id")),
                            res.getInt("dish_quantity"),
                            res.getTimestamp("dish_creation_date").toLocalDateTime(),
                            new ArrayList<>()
                    ));
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishOrder> findAllByDishId(Long id) {
        String query = "SELECT * FROM dish_order WHERE dish_id = ?";
        DishDAO dishDAO = new DishDAO();
        OrderDAO orderDAO = new OrderDAO();
        StatusDAO statusDAO = new StatusDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()) {
                List<DishOrder> dishOrders = new ArrayList<>();
                while (res.next()) {
                    dishOrders.add(new DishOrder(
                            res.getLong("id"),
                            dishDAO.getById(res.getLong("dish_id")),
                            orderDAO.getById(res.getLong("order_id")),
                            res.getInt("dish_quantity"),
                            res.getTimestamp("dish_creation_date").toLocalDateTime(),
                            statusDAO.findAllByDishOrderId(res.getLong("id"))
                    ));
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //@Override
    public List<DishOrder> saveAll(List<DishOrder> dishOrders) {
        List<DishOrder> dishOrderList = new ArrayList<>();
        DishDAO dishDAO = new DishDAO();
        OrderDAO orderDAO = new OrderDAO();
        for (DishOrder dishOrder : dishOrders) {
            if ((orderDAO.getById(dishOrder.getOrder().getId()).getOrderStatus().isEmpty()) || (!(orderDAO.getById(dishOrder.getOrder().getId()).getOrderStatus().isEmpty()) && (orderDAO.getById(dishOrder.getOrder().getId()).getOrderStatus().getLast().getStatus().equals(Status.CREATED)))) {
                String insertQuery = "INSERT INTO dish_order (dish_id, order_id, dish_quantity, dish_creation_date, id) VALUES (?, ?, ?, ?, ?)";
                String updateQuery = "UPDATE dish_order SET dish_id = ?, order_id = ?, dish_quantity = ?, dish_creation_date = ? WHERE id = ?";
                String executeQuery;

                if (getById(dishOrder.getId()) != null) {
                    executeQuery = updateQuery;
                } else {
                    executeQuery = insertQuery;
                }

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement statement = connection.prepareStatement(executeQuery)) {
                    statement.setLong(1, dishOrder.getDish().getId());
                    statement.setLong(2, dishOrder.getOrder().getId());
                    statement.setInt(3, dishOrder.getDishQuantity());
                    statement.setTimestamp(4, Timestamp.valueOf(dishOrder.getDishOrderCreationDate()));
                    statement.setLong(5, dishOrder.getId());
                    statement.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else throw new RuntimeException("Cannot add dish_order to the specified order");
        }
        return dishOrderList;
    }

    //@Override
    public void deleteById(Long id) {
        String query = "DELETE FROM dish_order WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
