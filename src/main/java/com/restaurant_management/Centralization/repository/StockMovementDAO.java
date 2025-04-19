package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.StockMovement;
import com.restaurant_management.Centralization.model.enums.StockMovementType;
import com.restaurant_management.Centralization.model.enums.Unit;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockMovementDAO {
    private final DataSource dataSource;

    public StockMovementDAO(){
        this.dataSource = new DataSource();
    }

    public StockMovementDAO (DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<StockMovement> getAllByIngredientId (Long ingredientId) {
        List<StockMovement> stockMovements = new ArrayList<>();
        String query = "SELECT * FROM ingredient_move WHERE ingredient_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, ingredientId);
            try (ResultSet res = statement.executeQuery()){
                while (res.next()){
                    stockMovements.add (new StockMovement(
                            res.getLong("id"),
                            null,
                            StockMovementType.valueOf(res.getString("type")),
                            res.getDouble("ingredient_quantity"),
                            Unit.valueOf(res.getString("unit")),
                            res.getTimestamp("move_date").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockMovements;
    }

    public List<StockMovement> saveAll(List<StockMovement> stockMovements, Long ingredientId){
        List<StockMovement> stockMovementList = new ArrayList<>();
        for (StockMovement stockMovement : stockMovements){
            LocalDateTime dateTime = stockMovement.getMoveDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            String insertQuery = "INSERT INTO ingredient_move (ingredient_id, type, ingredient_quantity, unit, move_date, id) VALUES (?, ?, ?, '"+formattedDateTime+"', ?, ?)";
            String updateQuery = "UPDATE ingredient_move SET ingredient_id = ?, type = ?, ingredient_quantity = ?, unit = ?, move_date = '"+formattedDateTime+"' WHERE id = ?";
            String executeQuery;

            if (findById(stockMovement.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)){
                statement.setLong(1, ingredientId);
                statement.setString(2, String.valueOf(stockMovement.getMovementType()));
                statement.setDouble(3, stockMovement.getQuantity());
                statement.setString(4, String.valueOf(stockMovement.getUnit()));
                statement.setLong(5, stockMovement.getId());
                statement.executeUpdate();

                stockMovementList.add(stockMovement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return stockMovementList;
    }

    private StockMovement findById(Long id) {
        String query = "SELECT * FROM ingredient_move WHERE id = ?";
        StockMovement price = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    price = new StockMovement(
                            res.getLong("id"),
                            null,
                            StockMovementType.valueOf(res.getString("type")),
                            res.getDouble("ingredient_quantity"),
                            Unit.valueOf(res.getString("unit")),
                            res.getTimestamp("move_date").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return price;
    }
}
