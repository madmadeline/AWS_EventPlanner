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
        logger.log("getting choice");
        logger.log(id);
        Choice choice = null;
        //PreparedStatement ps = conn.prepareStatement("Select choiceID, description, dateOfCreation, " +
        //        "altID From " + tblName + " c Join ChoiceAltMatch m on c.id = m.choiceID " +
        //        "WHERE id=?");
        PreparedStatement ps = conn.prepareStatement(
                "Select choiceID, c.description as cDesc, altID, a.description as aDesc, dateOfCreation" +
                        " From " + tblName + " c Join ChoiceAltMatch m on c.id = m.choiceID " +
                "join Alternative a on a.id = m.altID WHERE choiceID=?");
        //PreparedStatement ps = conn.prepareStatement("Select c.id as cID, c.description as cDesc From Choice");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        logger.log("Generating choice");
        choice = generateChoice(rs);

        rs.close();
        ps.close();
        logger.log("Returning choice");
        return choice;
    }
    
    public ArrayList<Choice> getReport() throws Exception{
    	logger.log("creating report");
    	ArrayList<Choice> choices = null;
    	
    	//what to do in here?
    	PreparedStatement ps = conn.prepareStatement("");
    	ps.close();
    	
    	logger.log("returning report.");
    	return choices;
    }

    public Boolean checkChoice(String id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Choice where id=?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            rs.close();
            return false; // id exists
        }
        return true;
    }

    public void addChoice(Choice choice) throws Exception {
        // Adds choice to choice table
        logger.log("Creating add choice statement");
        logger.log(String.valueOf(choice.getID()));
        //PreparedStatement ps = conn.prepareStatement(
        //        "Insert into Choice(id, description, dateOfCreation, winningAlt) values (1234, 'test 1', 20201123, null);"
        //);
        PreparedStatement ps = conn.prepareStatement(
                "Insert into " + tblName + "(id, description, dateOfCreation, winningAlt) values (?,?,?,null);"
        );
        ps.setString(1, choice.getID());
        ps.setString(2, choice.getDescription());
        ps.setTimestamp(3, choice.getTime());
        logger.log("Executing add choice statement");
        ps.execute();
        ps.close();

        // Inserts alts into alt table
        logger.log("In addChoice");
        if(choice.getAlternatives() != null) {
            logger.log("Adding alts");
            ArrayList<Alternative> alts = choice.getAlternatives();
            AlternativeDAO dao = new AlternativeDAO(logger);
            ChoiceAltMatchDAO dao2 = new ChoiceAltMatchDAO(logger);
            for (Alternative alt : alts) {
                dao.addAlternative(alt);
                dao2.addChoiceAltMatch(choice, alt);
            }
        }
    }

    /**
     * Generates a Choice object that represents several rows of ChoiceAltMatch
     * @param rs, the cursor to the specified row in Choice
     * @return a Choice object
     * @throws Exception failed to get Choice
     */
    private Choice generateChoice(ResultSet rs) throws Exception {
        ArrayList<Alternative> alts = new ArrayList<>();
        logger.log("In gen choice");
        String id = "";
        String description = "";
        Timestamp time = null;
        String aID = "";
        String aDesc = "";

        while(rs.next()){
            id = rs.getString("choiceID");
            logger.log("Got cID");
            description = rs.getString("cDesc");
            logger.log("Got cDesc");
            time = rs.getTimestamp("dateOfCreation");
            logger.log("got time");
            aID = rs.getString("altID");
            logger.log("got aID");
            aDesc = rs.getString("aDesc");
            logger.log("got aDesc");
            Alternative alt = new Alternative(aID, aDesc);
            logger.log("made alt");
            alts.add(alt);
            logger.log("added alt to alts");
        }

        rs.close();

        return new Choice(id, description, alts, time);
    }
}
