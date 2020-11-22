package caml.group.demo.db;

import java.awt.image.DataBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import caml.group.demo.model.*;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class ChoiceDAO {
    LambdaLogger logger;
    java.sql.Connection conn;
    Model model;
    final String tblName = "Choice";

    public ChoiceDAO(LambdaLogger logger){
        this.logger = logger;
        try{
            logger.log("Connecting in ChoiceDAO");
            conn = DatabaseUtil.connect();
            logger.log("Connection succeeded in ChoiceDAO");
        }
        catch (Exception e){
            logger.log("Connection Failed in ChoiceDAO");
            conn = null;
        }
    }

    /**
     * Returns a Choice object that is the requested entry from the Choice table
     * @param id, the given id of the desired choice
     * @return the Choice object
     * @throws SQLException, exception thrown on fail
     */
    public Choice getChoice(String id) throws Exception {
        Choice choice = null;
        PreparedStatement ps = conn.prepareStatement("Select choiceID, description, dateOfCreation, " +
                "altID From " + tblName + " c Join ChoiceAltMatch a on c.id = a.choiceID WHERE id=" + id);
        ResultSet rs = ps.executeQuery();

        choice = generateChoice(rs);

        rs.close();
        ps.close();

        return choice;
    }

    /**
     * Generates a Choice object that represents several rows of ChoiceAltMatch
     * @param rs, the cursor to the specified row in Choice
     * @return a Choice object
     * @throws Exception failed to get Choice
     */
    private Choice generateChoice(ResultSet rs) throws Exception {
        int id = Integer.parseInt(rs.getString("id"));
        String description = rs.getString("description");
        ArrayList<Alternative> alts = new ArrayList<>();
        AlternativeDAO altDAO = new AlternativeDAO(logger);
        Timestamp time = rs.getTimestamp("dateOfCreation");

        while(rs.next()){
            alts.add(altDAO.getAlternativeByID(rs.getString("altID")));
        }

        rs.close();

        return new Choice(id, description, alts, time);
    }
}
