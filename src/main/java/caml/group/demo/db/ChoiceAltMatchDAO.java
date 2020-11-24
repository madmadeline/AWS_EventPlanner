package caml.group.demo.db;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChoiceAltMatchDAO {
    LambdaLogger logger;
    java.sql.Connection conn;
    final String tblName = "ChoiceAltMatch";

    public ChoiceAltMatchDAO(LambdaLogger logger){
        this.logger = logger;
        try{
            logger.log("Connecting in ChoiceAltMatchDAO");
            conn = DatabaseUtil.connect();
            logger.log("Connection succeeded in ChoiceAltMatchDAO");
        }
        catch (Exception e){
            logger.log("Connection Failed in ChoiceAltMatchDAO");
            conn = null;
        }
    }

    public void addChoiceAltMatch(Choice choice, Alternative alternative) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ChoiceAltMatch(choiceID, altID) values (?, ?);");
        ps.setString(1, choice.getID());
        ps.setString(2, alternative.getID());
        ps.execute();
        ps.close();
    }
}
