package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;

import caml.group.demo.model.*;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class ChoiceDAO {
    LambdaLogger logger;
    java.sql.Connection conn;
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


    // TESTED
    /**
     * Returns a Choice object that is the requested entry from the Choice table
     * @param id, the given id of the desired choice
     * @return the Choice object
     * @throws SQLException, exception thrown on fail
     */
    public Choice getChoice(String id) throws SQLException {
        logger.log("Getting choice\n");
//        logger.log(id);
        Choice choice;
        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = conn.prepareStatement(
                    "Select c.choiceID, c.description as cDesc, altID, a.description as aDesc, dateOfCreation, " +
                            "maxTeamSize From " + tblName + " c " +
                            "join Alternative a on a.choiceID = c.choiceID WHERE c.choiceID=?");
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) { // choice doesn't exist
                logger.log("Invalid choice ID");
                return null;
            }
        } catch(SQLException e){
            throw new SQLException("Database error" + e.getMessage());
        }
        choice = generateChoice(rs);

        rs.close();
        ps.close();
//        logger.log("Returning choice");
        return choice;
    }


    /**
     * Gets every single choice in the Choice table.
     * @return The list of choices
     * @throws Exception Database error idk
     */
    public ArrayList<Choice> getAllChoices() throws Exception{
    	logger.log("Creating report");
    	ArrayList<Choice> choices;
    	
    	//what to do in here?
    	PreparedStatement ps = conn.prepareStatement("Select * From Choice");
    	ResultSet rs = ps.executeQuery();
    	logger.log("Generating report");
    	choices = generateReport(rs);
    	rs.close();
    	ps.close();
    	
    	logger.log("returning report.");
    	return choices;
    }

    // TESTED
    /**
     * Checks to see if the given choice ID can be used for a new choice.
     * @param id The given ID
     * @return true if a choice with the ID doesn't exist, false if
     * a choice with the ID already exists
     * @throws SQLException Database connection error
     */
    public boolean checkChoice(String id) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Choice where choiceID=?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            rs.close();
            return false; // id exists
        }
        return true;
    }


    // TESTED
    /**
     * Adds the given choice to the Choice table. Assume that the
     * choice has 2-5 alternatives.
     * @param choice The given choice
     * @throws Exception Couldn't add the choice
     */
    public boolean addChoice(Choice choice) throws Exception {
        logger.log("Adding Choice " + choice.getID() + ": " + choice.getDescription());
        PreparedStatement ps;

        // check if description is over character limit
        if (choice.getDescription().length() >= 60) {
            logger.log("The choice description is too long");
            return false;
        }

        // make sure that the choice isn't already in the table
        // (this will only come up in testing)
        try {
            ps = conn.prepareStatement("SELECT * FROM " + tblName + " WHERE " + tblName
                    + ".choiceID=?;");
            ps.setString(1, choice.getID());
            ResultSet resultSet = ps.executeQuery(); // cursor that points to database row

            // choice is already in the table
            if (resultSet.isBeforeFirst()) {
//                System.out.println("choice isn't in the table");
                resultSet.close();
                ps.close();
                logger.log("The choice is already in the table");
                return false;
            }
        } catch (Exception e) {
            throw new Exception ("Error " + e.getMessage());
        }

        try {
            ps = conn.prepareStatement(
                    "Insert into " + tblName + "(choiceID, description, dateOfCreation, winningAlt, maxTeamSize) values (?,?,?,null,?);"
            );
            ps.setString(1, choice.getID());
            ps.setString(2, choice.getDescription());
            ps.setTimestamp(3, choice.getTime());
            ps.setInt(4, choice.getMaxTimeSize());
            logger.log("Executing add choice statement");
            ps.execute();
            ps.close();
        } catch (Exception e) {
            throw new Exception ("Couldn't add choice " + e.getMessage());
        }


        // Inserts alts into alt table
        logger.log("In addChoice");
        if(choice.getAlternatives() != null) {
            logger.log("Adding alts");
            ArrayList<Alternative> alts = choice.getAlternatives();
            AlternativeDAO dao = new AlternativeDAO(logger);
            boolean result;
            //ChoiceAltMatchDAO dao2 = new ChoiceAltMatchDAO(logger);
            for (Alternative alt : alts) {
                logger.log("alt " + alt.getDescription());
                result = dao.addAlternative(alt, choice.getID());

                if (!result) {
                    return false;
                }
                //dao2.addChoiceAltMatch(choice, alt);
            }
        }
        logger.log("Exiting add choice");
        return true;
    }

    // TESTED
    /**
     * Deletes the given choice from the Choice table. Also deletes
     * any alternatives associated with the choice from the
     * Alternative table.
     * @param choice The given choice
     * @return True if the choice/alternatives were deleted,
     * false otherwise
     * @throws Exception The choice couldn't be deleted
     */
    public boolean deleteChoice(Choice choice) throws Exception {
        logger.log("Deleting Choice " + choice.getID() + ": " + choice.getDescription());
        PreparedStatement ps;

        // Delete users from user table
        try {
            ps = conn.prepareStatement("Delete from User where choiceID=?;");
            ps.setString(1, choice.getID());
            ps.executeUpdate();
            logger.log("Deleted all Users");
        } catch (Exception e) {
            throw new Exception ("Couldn't delete user " + e.getMessage());
        }

        // Delete alts from alt table
        AlternativeDAO dao = new AlternativeDAO(logger);
        ArrayList<Alternative> alts = dao.getAllAlternativesByChoiceID(choice.getID());

        for (Alternative alt : alts) { dao.deleteAlternative(alt); }
        logger.log("Deleted alternatives");

        // Delete the choice from the choice table
        try {
            ps = conn.prepareStatement(
                    "delete from " + tblName + " where choiceID=? and description=?;"
            );
            ps.setString(1, choice.getID());
            ps.setString(2, choice.getDescription());
            int numAffected = ps.executeUpdate();
            logger.log("Deleted choice");
            ps.close();
            return numAffected == 1;
        } catch (Exception e) {
            throw new Exception ("Couldn't delete choice " + e.getMessage());
        }
    }

    /**
     * Generates a Choice object that represents several rows of ChoiceAltMatch
     * @param rs, the cursor to the specified row in Choice
     * @return a Choice object
     * @throws SQLException failed to get Choice
     */
    private Choice generateChoice(ResultSet rs) throws SQLException {
        ArrayList<Alternative> alts = new ArrayList<>();
//        logger.log("Generating choice from result set");
        String id = "";
        String description = "";
        Timestamp time = null;
        String aID;
        String aDesc;
        int teamSize = 0;

        while(rs.next()){
            id = rs.getString("choiceID");
//            logger.log("Got cID");
            description = rs.getString("cDesc");
//            logger.log("Got cDesc");
            time = rs.getTimestamp("dateOfCreation");
//            logger.log("got time");
            aID = rs.getString("altID");
//            logger.log("got aID");
            aDesc = rs.getString("aDesc");
            logger.log("got aDesc");
            teamSize = rs.getInt("maxTeamSize");
            logger.log("got maxTeamSize");
            Alternative alt = new Alternative(aID, aDesc);
//            logger.log("made alt");
            alts.add(alt);
//            logger.log("added alt to alts");
        }

        rs.close();
        return new Choice(id, description, alts, time, teamSize);
    }

    private ArrayList<Choice> generateReport(ResultSet rs) throws Exception {
        ArrayList<Choice> choices = new ArrayList<>();

        while(rs.next()){
            logger.log("Getting choice data for choice " + rs.getString("choiceID"));
            Choice choice = new Choice(rs.getString("choiceID"), rs.getString("description"),
                    rs.getTimestamp("dateOfCreation"), rs.getInt("maxTeamSize"));
            choices.add(choice);
        }

        return choices;
    }
}
