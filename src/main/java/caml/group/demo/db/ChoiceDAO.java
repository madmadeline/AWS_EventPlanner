package caml.group.demo.db;

import java.sql.*;
import java.util.ArrayList;

//import caml.group.demo.db.AlternativeDAO;

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
    public Choice getChoice(String id) throws Exception {
    	
        logger.log("Getting choice\n");
//        logger.log(id);
        Choice choice;
        String description = "";
        Timestamp time = null;
        int teamSize = 0;
        ArrayList<Alternative> alts = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        int numLikes;
        String altID;

        ps = conn.prepareStatement("SELECT c.choiceID, c.description as CDescription, a.description as ADescription," +
                " dateOfCreation, maxTeamSize, altID, numLikes, numDislikes" +
                " From Choice c join Alternative a on c.choiceID=a.choiceID " +
                "where c.choiceID=?");
        ps.setString(1,id);
        rs = ps.executeQuery();

        while(rs.next()){
            description = rs.getString("CDescription");
            time = rs.getTimestamp("dateOfCreation");
            teamSize = rs.getInt("maxTeamSize");
            numLikes = rs.getInt("numLikes");
            altID = rs.getString("altID");
            logger.log(String.valueOf(numLikes));

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT altID, f.userID as FuserID, approved, " +
                    "username FROM Feedback f join User u on f.userID = u.userID where altID=?;");
            preparedStatement.setString(1,altID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> aUsers = new ArrayList<>();
            ArrayList<String> dUsers = new ArrayList<>();
            String approved;

            while(resultSet.next()){
                approved = resultSet.getString("approved");
                if(approved.equals("A")){
                    aUsers.add(resultSet.getString("username"));
                }
                else if(approved.equals("D")){
                    dUsers.add(resultSet.getString("username"));
                }
                else logger.log("Failed to get approval");
            }

            Alternative alt = new Alternative(altID, rs.getString("ADescription"),
                    numLikes, rs.getInt("numDislikes"), aUsers, dUsers);
            //logger.log(String.valueOf(alt.getTotalApprovals()));
            alts.add(alt);
        }
        choice = new Choice(id, description, alts, time, teamSize);
        /*System.out.println(id);

        try {
            ps = conn.prepareStatement(
                    "Select * From " + tblName + " WHERE choiceID=?");
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) { // choice doesn't exist
                logger.log("Invalid choice ID");
                //return null;
            }
        } catch(SQLException e){
            throw new SQLException("Database error" + e.getMessage());
        }
        choice = generateChoice(rs);*/

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
            ps.setInt(4, choice.getMaxTeamSize());
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
    public ArrayList<Choice> deleteChoice(Timestamp timestamp) throws Exception {
        logger.log("Deleting choices from before " + timestamp);

        PreparedStatement ps = conn.prepareStatement("SELECT * from Choice Where dateOfCreation<=?");
        ps.setTimestamp(1,timestamp);
        ResultSet rs = ps.executeQuery();
        ArrayList<Choice> choices = generateReport(rs);
        ps.close();

        PreparedStatement ps2 = conn.prepareStatement("DELETE From Choice where dateOfCreation<=?");
        ps2.setTimestamp(1, timestamp);
        ps2.execute();
        ps2.close();

        return  choices;
    }

    /**
     * Generates a Choice object that represents several rows of ChoiceAltMatch
     * @param rs, the cursor to the specified row in Choice
     * @return a Choice object
     * @throws SQLException failed to get Choice
     */
    private Choice generateChoice(ResultSet rs) throws Exception {
        ArrayList<Alternative> alts;
        logger.log("Generating choice from result set");
        String id;
        String description;
        Timestamp time;
        int teamSize;
        AlternativeDAO alternativeDAO = new AlternativeDAO(logger);

        id = rs.getString("choiceID");
            logger.log("Got cID");
        description = rs.getString("description");
            logger.log("Got cDesc");
        time = rs.getTimestamp("dateOfCreation");
        teamSize = rs.getInt("maxTeamSize");
        logger.log("got maxTeamSize");

        logger.log("about to get alts");
        alts = alternativeDAO.getAllAlternativesByChoiceID(id);

        rs.close();
        return new Choice(id, description, alts, time, teamSize);
    }
    /**
     * Generates a Choice object that represents several rows of ChoiceAltMatch
     * @param rs, the cursor to the specified row in Choice
     * @return a Choice object
     * @throws SQLException failed to get Choice
     */
    public Choice generateChoiceByAltID(String altID) throws Exception {
        Choice choice = null;
        
        PreparedStatement ps = conn.prepareStatement("SELECT * from Choice");
        ResultSet rs = ps.executeQuery();
        ArrayList<Choice> choices = generateReport(rs);
        ps.close();
        
        for(Choice achoice: choices) {
        	if(achoice.getChoiceIDbyAltID(altID)) {
        		choice = achoice;
        	}
//        	ArrayList<Alternative> alternatives = achoice.getAlternatives();
//        	for(Alternative alt: alternatives) {
//        		if(alt.getID().equals(altID))
//        			choice = achoice;
//        	}
        }   
        return choice;
    }
    
//    public ArrayList<Alternative> getAllAlternativesByChoiceID(String choiceID) throws Exception {
//		ArrayList<Alternative> alts = new ArrayList<Alternative>();
//		logger.log("getting the alts");
//
//		try {
//			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + tblName +
//					" WHERE choiceID=?;");
//			ps.setString(1,  choiceID);
//			ResultSet resultSet = ps.executeQuery(); // cursor that points to database row
//
//			while (resultSet.next()) {
//				alts.add(generateAlternative(resultSet));
//			}
//			resultSet.close();
//			ps.close();
//
//			logger.log("Retrieved all alternatives");
//			return alts;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception("Failed in getting alternatives: " + e.getMessage());
//		}
//	}  
    
    
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
