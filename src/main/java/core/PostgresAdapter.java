package core;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostgresAdapter {
    private static String url = "jdbc:postgresql://localhost:5432/selector";
    private static String user = "selector";
    private static String password = "selector";
    private static Connection connection;

    public PostgresAdapter() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
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

    public List<Night> getNights() throws IOException {
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

        if (nights.size() == 0) {
            throw new IOException("Could not get list of Nights.");
        }

        return nights;
    }

    public void saveNights(List<Night> nights) {

        try {
            PreparedStatement pst = connection.prepareStatement(Query.SAVENIGHT.getValue());
            for (Night night :
                nights) {
                if (night.getId() != null) {
                    pst.setInt(1,night.getId());
                }
                pst.setBoolean(1,night.isComplete());
                pst.setBoolean(2,night.hasECard());
                pst.setBoolean(3,night.involvesFood());
                pst.setBoolean(4,night.involvesProps());
                pst.setBoolean(5,night.involvesTravel());
                pst.setString(6,night.getCost().name());
                pst.setString(7,night.getPartner().name());

                pst.addBatch();
            }

            pst.executeBatch();
        } catch (SQLException se) {
            logSQLException(se);
        }
    }
//
//    public static Optional<Night> getNightById(Integer id) {
//        return source
//                .getNights()
//                .values()
//                .stream()
//                .filter(n -> n.getId().equals(id))
//                .findFirst();
//    }
//
//    public static void saveNight(Night night) {
//        source.setNight(night.getId(),night);
//    }

    private static void logSQLException(SQLException se) {
        Logger lgr = Logger.getLogger(PostgresAdapter.class.getName());
        lgr.log(Level.SEVERE, se.getMessage(), se);
    }
}
