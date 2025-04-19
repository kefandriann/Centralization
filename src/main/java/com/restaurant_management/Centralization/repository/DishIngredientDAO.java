package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.DishIngredient;
import com.restaurant_management.Centralization.model.enums.Unit;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishIngredientDAO {
    private final DataSource dataSource;

    public DishIngredientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public DishIngredientDAO() {
        this.dataSource = new DataSource();
    }

    public List<DishIngredient> getDishIngredientByDishId(Long id){
        List<DishIngredient> ingredients = new ArrayList<>();
        IngredientDAO ingredientDAO = new IngredientDAO();
        DishDAO dishDAO = new DishDAO();
        String query = "SELECT * FROM dish_ingredient WHERE dish_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                while(res.next()){
                    ingredients.add( new DishIngredient(
                            res.getLong("id"),
                            Unit.valueOf(res.getString("unit")),
                            dishDAO.getById(res.getLong("dish_id")),
                            ingredientDAO.getById(res.getLong("ingredient_id")),
                            res.getDouble("ingredient_quantity")
                    ));
                }

                return ingredients;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishIngredient> saveAll(List<DishIngredient> dishIngredients){
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        IngredientDAO ingredientDAO = new IngredientDAO();
        for (DishIngredient dishIngredient : dishIngredients){
            String insertQuery = "INSERT INTO dish_ingredient (unit, dish_id, ingredient_id, ingredient_quantity, id) VALUES (?, ?, ?, ?, ?)";
            String updateQuery = "UPDATE dish_ingredient SET unit = ?, dish_id = ?, ingredient_id = ?, ingredient_quantity = ? WHERE id = ?";
            String executeQuery;

            if (getById(dishIngredient.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)){
                statement.setString(1, String.valueOf(dishIngredient.getUnit()));
                statement.setLong(2, dishIngredient.getDish().getId());
                statement.setLong(3, dishIngredient.getIngredient().getId());
                statement.setDouble(4, dishIngredient.getIngredientQuantity());
                statement.setLong(5, dishIngredient.getId());
                statement.executeUpdate();

                ingredientDAO.saveAll(List.of(dishIngredient.getIngredient()));

                dishIngredientList.add(dishIngredient);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return dishIngredientList;
    }

    public DishIngredient getById(Long id){
        DishDAO dishDAO = new DishDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        DishIngredient dishIngredient = null;
        String query = "SELECT * FROM dish_ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    dishIngredient = new DishIngredient(
                            res.getLong("id"),
                            Unit.valueOf(res.getString("unit")),
                            dishDAO.getById(res.getLong("dish_id")),
                            ingredientDAO.getById(res.getLong("ingredient_id")),
                            res.getDouble("ingredient_quantity")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredient;
    }
}
