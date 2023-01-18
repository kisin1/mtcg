package at.bif3.swe1.kisin.httpServer.database;

import at.bif3.swe1.kisin.monsterTradingCards.trade.Trade;
import at.bif3.swe1.kisin.monsterTradingCards.enums.CardType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TradingsDAO implements DAO<Trade>{
    @Override
    public Optional<Trade> get(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Trade> get(String trading_id) {
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT trading_id, card_id, card_type, min_damage, owner " +
                        "FROM trading " +
                        "WHERE trading_id = ?;")){
            statement.setString(1, String.valueOf(trading_id));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return Optional.of(new Trade(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        CardType.valueOf(resultSet.getString(3)),
                        resultSet.getFloat(4),
                        resultSet.getString(5)
                        )
                );
            }
        } catch (SQLException e){
            System.out.println("Error: get tradings by id. " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<Trade> getAll() {
        ArrayList<Trade> result = new ArrayList<>();
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT trading_id, card_id, card_type, min_damage, owner " +
                        "FROM trading;")){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result.add(new Trade(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        CardType.valueOf(resultSet.getString(3)),
                        resultSet.getFloat(4),
                        resultSet.getString(5)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void insert(Trade trade) {
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "INSERT INTO trading (trading_id, card_id, card_type, min_damage, owner) " +
                        "VALUES (?, ?, ?, ?, ?);"
        )){
            statement.setString(1, trade.getTradingId());
            statement.setString(2, trade.getCardId());
            statement.setString(3, (trade.getCardType() != null ? trade.getCardType().name() : null));
            statement.setFloat(4, trade.getMinDamage());
            statement.setString(5, trade.getOwner());

            statement.execute();
        } catch (SQLException e){
            System.out.println("ERROR: insert tradings " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Trade trade, String[] params) {

    }

    @Override
    public void delete(Trade trade) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "DELETE FROM trading " +
                        "WHERE trading_id = ?;"
        )){
            statement.setString(1, trade.getTradingId());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(String trading_id) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "DELETE FROM trading " +
                        "WHERE trading_id = ?;"
        )){
            statement.setString(1, trading_id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
