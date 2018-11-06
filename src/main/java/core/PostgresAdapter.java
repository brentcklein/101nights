package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresAdapter extends DataAdapter {
    private static String url = "jdbc:postgresql://localhost:5432/";
    private static String prodDb = "selector";
    private static String devDb = "dev";
    private static String testDb = "test";
    private static String user = "selector";
    private static String password = "selector";
    private Connection connection;

    public PostgresAdapter(Mode mode) throws DataException {
        super(mode);
        String db;
        switch (mode.name()) {
            case "PROD":
                db = prodDb;
                break;
            case "TEST":
                db = testDb;
                break;
            default:
                db = devDb;
                break;
        }

        try {
            connection = DriverManager.getConnection(url+db, user, password);
        } catch (SQLException se) {
            throw new DataException("Could not connect to database: " + se.getMessage());
        }
    }

    public PostgresAdapter() throws DataException {
        this(Mode.DEV);
    }

    public String getVersion() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException se) {
            logSQLException(se);
        }

        return "Could not get version number.";
    }

    public List<Night> getNights() {
        List<Night> nights = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(Query.GETALLNIGHTS.getValue());

            while (rs.next()) {
                nights.add(
                    new Night(
                        rs.getInt(1),
                        rs.getBoolean(2),
                        rs.getBoolean(3),
                        rs.getBoolean(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6),
                        Cost.valueOf(rs.getString(7)),
                        Partner.valueOf(rs.getString(8))
                    )
                );
            }
        } catch (SQLException se) {
            logSQLException(se);
        }

        return nights;
    }

    public void saveNights(List<Night> nights) {

        try {
            PreparedStatement pst = connection.prepareStatement(Query.SAVENIGHT.getValue());
            for (Night night :
                nights) {

//                TODO: Figure out how to avoid making a separate db call for the next id for each iteration
                ResultSet id = connection
                        .createStatement()
                        .executeQuery(
                                Query.NEXTID.getValue()
                        );
                id.next();
                pst.setInt(
                        1,
                        night.getId() == null ? id.getInt(1) : night.getId()
                );
                pst.setBoolean(2,night.isComplete());
                pst.setBoolean(3,night.hasECard());
                pst.setBoolean(4,night.involvesFood());
                pst.setBoolean(5,night.involvesProps());
                pst.setBoolean(6,night.involvesTravel());
                pst.setString(7,night.getCost().name());
                pst.setString(8,night.getPartner().name());

                pst.addBatch();
            }

            pst.executeBatch();
        } catch (SQLException se) {
            logSQLException(se);
        }
    }

    public Optional<Night> getNightById(Integer id) {
        try {
            PreparedStatement st = connection.prepareStatement(Query.GETNIGHTBYID.getValue());
            st.setInt(1,id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return Optional.of(new Night(
                        rs.getInt(1),
                        rs.getBoolean(2),
                        rs.getBoolean(3),
                        rs.getBoolean(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6),
                        Cost.valueOf(rs.getString(7)),
                        Partner.valueOf(rs.getString(8))
                ));
            }
        } catch (SQLException se) {
            logSQLException(se);
        }

        return Optional.empty();
    }

    public void saveNight(Night night) {
        saveNights(Collections.singletonList(night));
    }

    public void clearNights() throws SQLException {
        if (!mode.equals(Mode.TEST)) {
//            Don't let the nights be cleared if we're not running tests
            throw new SQLException("Can't truncate nights when not in test mode!!");
        }
        String query = "TRUNCATE nights;";
        Statement st = connection.createStatement();
        st.executeUpdate(query);
    }

    private static void logSQLException(SQLException se) {
        Logger lgr = Logger.getLogger(PostgresAdapter.class.getName());
        lgr.log(Level.SEVERE, se.getMessage(), se);
    }
}
