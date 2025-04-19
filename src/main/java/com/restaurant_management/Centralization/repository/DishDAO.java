package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.Dish;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishDAO {
    private final DataSource dataSource;

    public DishDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public DishDAO() {
        this.dataSource = new DataSource();
    }

    public List<Dish> getAll() {
        List<Dish> dishes = new ArrayList<>();
        DishIngredientDAO dishIngredientDAO = new DishIngredientDAO();
        String query = "SELECT * FROM dish";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet res = statement.executeQuery()){
                while (res.next()){
                    dishes.add(new Dish(
                            res.getLong("id"),
                            res.getString("name"),
                            res.getDouble("unit_price"),
                            dishIngredientDAO.getDishIngredientByDishId(res.getLong("id"))
                    ));
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //@Override
    public Dish getById(Long id) {
        Dish dish = null;
        String query = "SELECT * FROM dish WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    dish = new Dish(
                            res.getLong("id"),
                            res.getString("name"),
                            res.getDouble("unit_price"),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dish;
    }

    public Dish findByName(String name) {
        Dish dish = null;
        String query = "SELECT * FROM dish WHERE name = ? LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    dish = new Dish(
                            res.getLong("id"),
                            res.getString("name"),
                            res.getDouble("unit_price"),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dish;
    }

    //@Override
    public List<Dish> saveAll(List<Dish> dishes) {
        DishIngredientDAO dishIngredientDAO = new DishIngredientDAO();
        List<Dish> dishList = new ArrayList<>();
        for (Dish dish: dishes){
            String insertQuery = "INSERT INTO dish (name, unit_price, id) VALUES (?, ?, ?)";
            String updateQuery = "UPDATE dish SET name = ?, unit_price = ? WHERE id = ?";
            String executeQuery;

            if (getById(dish.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)){
                statement.setString(1, dish.getName());
                statement.setDouble(2, dish.getUnitPrice());
                statement.setLong(3, dish.getId());
                statement.executeUpdate();

                dishIngredientDAO.saveAll(dish.getIngredients());

                dishList.add(dish);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return dishList;
    }
}
