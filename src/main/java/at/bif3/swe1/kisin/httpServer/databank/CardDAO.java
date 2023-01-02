package at.bif3.swe1.kisin.httpServer.databank;

import at.bif3.swe1.kisin.monsterTradingCards.cards.Card;
import at.bif3.swe1.kisin.monsterTradingCards.enums.ElementType;
import at.bif3.swe1.kisin.monsterTradingCards.enums.MonsterType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CardDAO implements DAO<Card>{
    @Override
    public Optional<Card> get(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Card> get(String card_id) {
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT card_id, card_name, monster_type, card_element, damage, owner, in_deck, package_id " +
                        "FROM card " +
                        "WHERE card_id = ?;")){
            statement.setString(1, card_id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        MonsterType.valueOf((resultSet.getString(3) != null) ? resultSet.getString(3) : "NULL"),
                        ElementType.valueOf(resultSet.getString(4)),
                        resultSet.getFloat(5),
                        resultSet.getString(6),
                        resultSet.getBoolean(7),
                        resultSet.getInt(8)
                ));
            }
        } catch (SQLException e){
            System.out.println("Error: get card by id. " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Card clone() throws CloneNotSupportedException {
        return (Card) super.clone();
    }
    public int getMin() {
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT MIN(package_id) " +
                        "FROM card WHERE owner IS NULL;")){
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e){
            System.out.println("Error: get min package. " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Collection<Card> getAll() {
        ArrayList<Card> result = new ArrayList<>();
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT card_id, card_name, monster_type, card_element, damage, owner, in_deck, package_id " +
                        "FROM card")){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result.add(new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        MonsterType.valueOf(resultSet.getString(3)),
                        ElementType.valueOf(resultSet.getString(4)),
                        resultSet.getFloat(5),
                        resultSet.getString(6),
                        resultSet.getBoolean(7),
                        resultSet.getInt(8)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Card> getAll(int packageId) {
        List<Card> result = new ArrayList<>();
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "SELECT card_id, card_name, monster_type, card_element, damage, owner, in_deck, package_id " +
                        "FROM card " +
                        "WHERE package_id = ? AND owner IS NULL;")){
            statement.setInt(1, packageId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result.add(new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        MonsterType.valueOf(resultSet.getString(3) != null ? resultSet.getString(3) : "NULL"),
                        ElementType.valueOf(resultSet.getString(4)),
                        resultSet.getFloat(5),
                        resultSet.getString(6),
                        resultSet.getBoolean(7),
                        resultSet.getInt(8)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void insert(Card card) {
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "INSERT INTO card (card_id, card_name, monster_type, card_element, damage, owner, package_id, in_deck) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
        )){
            statement.setString(1, card.getCardId());
            statement.setString(2, card.getName());
            statement.setString(3, (card.getMonsterType() != null ? card.getMonsterType().name() : null));
            statement.setString(4, (card.getElementType() != null ? card.getElementType().name() : null));
            statement.setFloat(5, card.getDamage());
            statement.setString(6, card.getOwner());
            statement.setInt(7, card.getPackageId());
            statement.setBoolean(8, card.getInDeck());
//            statement.setInt(8, card.getPackageId());
            statement.execute();
        } catch (SQLException e){
            System.out.println("ERROR: insert card " + e.getMessage() + " Cause: " + e.getCause());
        }
    }

    @Override
    public void update(Card card, String[] params) {
        int oldPackageId = card.getPackageId();
        card.setOwner(Objects.requireNonNull(params[0], "Owner cannot be null"));
        card.setPackageId(Integer.parseInt(Objects.requireNonNull(params[1], "New packageId")));
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE card " +
                        "SET owner = ?, package_id = ? " +
                        "WHERE package_id = ?;"
        )){
            statement.setString(1, card.getOwner());
            statement.setInt(2, card.getPackageId());
            statement.setInt(3, oldPackageId);

            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updatePackageId(Card card, String[] params) {
        int oldPackageId = card.getPackageId();
        card.setPackageId(Integer.parseInt(Objects.requireNonNull(params[0], "PackageId cannot be null")));
        try(PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "UPDATE card " +
                        "SET package_id = ? " +
                        "WHERE package_id = ?;"
        )){
            statement.setInt(1, card.getPackageId());
            statement.setInt(2, oldPackageId);

            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Card card) {
        try (PreparedStatement statement = DbConnection.getInstance().prepareStatement(
                "DELETE FROM card " +
                        "WHERE card_id = ?;"
        )){
            statement.setString(1, card.getCardId());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
