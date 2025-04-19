package com.restaurant_management.Centralization.repository;

import com.restaurant_management.Centralization.config.DataSource;
import com.restaurant_management.Centralization.model.Price;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriceDAO {
    private final DataSource dataSource;

    public PriceDAO(){
        this.dataSource = new DataSource();
    }

    public PriceDAO (DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<Price> getAllByIngredientId (Long ingredientId) {
        List<Price> prices = new ArrayList<>();
        String query = "SELECT * price WHERE ingredient_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, ingredientId);
            try (ResultSet res = statement.executeQuery()){
                while (res.next()){
                    prices.add (new Price(
                            res.getLong("id"),
                            null,
                            res.getDouble("amount"),
                            res.getTimestamp("begin_date").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prices;
    }

    public List<Price> saveAll(List<Price> prices, Long ingredientId){
        List<Price> priceList = new ArrayList<>();
        for (Price price : prices){
            LocalDateTime dateTime = price.getBeginDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);

            String insertQuery = "INSERT INTO price (ingredient_id, amount, begin_date, id) VALUES (?, ?, '"+formattedDateTime+"', ?)";
            String updateQuery = "UPDATE price SET ingredient_id = ?, amount = ?, begin_date = '"+formattedDateTime+"' WHERE id = ?";
            String executeQuery;

            if (getById(price.getId()) != null){
                executeQuery = updateQuery;
            } else {
                executeQuery = insertQuery;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(executeQuery)){
                statement.setLong(1, price.getIngredient().getId());
                statement.setDouble(2, price.getAmount());
                statement.setLong(3, price.getId());
                statement.executeUpdate();

                priceList.add(price);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return priceList;
    }

    public Price getById(Long id) {
        String query = "SELECT * FROM price WHERE id = ?";
        Price price = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet res = statement.executeQuery()){
                if (res.next()){
                    price = new Price(
                            res.getLong("id"),
                            null,
                            res.getDouble("amount"),
                            res.getTimestamp("begin_date").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return price;
    }
}
