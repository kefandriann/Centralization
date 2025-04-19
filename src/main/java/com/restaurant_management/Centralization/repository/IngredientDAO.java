package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.Ingredient;
import com.restaurant_management.Centralization.model.enums.Unit;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientDAO {
    private final DataSource dataSource;

    public IngredientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public IngredientDAO() {
        this.dataSource = new DataSource();
    }

    public List<Ingredient> getAll() {
        String query = "SELECT * FROM ingredient";
        PriceDAO priceDAO = new PriceDAO();
        StockMovementDAO stockMovementDAO = new StockMovementDAO();
        List<Ingredient> ingredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet res = statement.executeQuery()){
                while (res.next()){
                    ingredients.add(new Ingredient(
                            res.getLong("id"),
                            res.getString("name"),
                            res.getTimestamp("latest_modification").toLocalDateTime(),
                            Unit.valueOf(res.getString("unit")),
                            new ArrayList<>(),
                            new ArrayList<>()
                    ));
                }
                return ingredients;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ingredient getById(Long id){
        String query = "SELECT * FROM ingredient WHERE id = ?";
        PriceDAO priceDAO = new PriceDAO();
        StockMovementDAO stockMovementDAO = new StockMovementDAO();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    return new Ingredient(
                            res.getLong("id"),
                            res.getString("name"),
                            res.getTimestamp("latest_modification").toLocalDateTime(),
                            Unit.valueOf(res.getString("unit")),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients){
        PriceDAO priceDAO = new PriceDAO();
        StockMovementDAO stockMovementDAO = new StockMovementDAO();
        for (Ingredient ingredient : ingredients){
            LocalDateTime dateTime = ingredient.getLatestModification();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            String insertQuery = "INSERT INTO ingredient (name, latest_modification, unit,  id) VALUES (?, ?, ?, ?)";
            String updateQuery = "UPDATE ingredient SET name = ?, latest_modification = ?, unit = ? WHERE id = ?";
            String executeQuery;

            if (getById(ingredient.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)){
                statement.setString(1, ingredient.getName());
                statement.setTimestamp(2, Timestamp.valueOf(ingredient.getLatestModification()));
                statement.setString(3, String.valueOf(ingredient.getUnit()));
                statement.setLong(4, ingredient.getId());
                statement.executeUpdate();

                priceDAO.saveAll(ingredient.getPrices(), ingredient.getId());
                stockMovementDAO.saveAll(ingredient.getStocksMovement(), ingredient.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ingredients;
    }
}
