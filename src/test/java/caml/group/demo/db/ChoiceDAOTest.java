package caml.group.demo.db;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.Timestamp;


import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

public class ChoiceDAOTest {
    ChoiceDAO choiceDAO;

    @Before
    public void init() throws Exception {
        DatabaseUtil.connect();
        TestContext ctx = new TestContext();
        ctx.setFunctionName("post");
        LambdaLogger logger = ctx.getLogger();
        choiceDAO = new ChoiceDAO(logger);
    }

//    @Test
//    public void testGetChoice() throws SQLException {
//        String choiceID = "3572";
//        String desc = "work dammit";
//        int maxSize = 3;
//
//        // check valid choice id
//        Choice choice = choiceDAO.getChoice(choiceID);
//        Assert.assertEquals(choice.getID(), choiceID);
//        Assert.assertEquals(choice.getDescription(), desc);
//        Assert.assertEquals(choice.getMaxTimeSize(), maxSize);
//
//        // check invalid choice id --> should return null
//        choiceID = "0009";
//        choice = choiceDAO.getChoice(choiceID);
//        Assert.assertNull(choice);
//    }
//
//    @Test
//    public void testCheckChoice() throws SQLException {
//        boolean allGood;
//
//        allGood = choiceDAO.checkChoice("0009");
//        Assert.assertTrue(allGood);
//
//        allGood = choiceDAO.checkChoice("3572");
//        Assert.assertFalse(allGood);
//    }
//
//    @Test
//    public void testDeleteChoice() throws Exception {
//        Choice choice = choiceDAO.getChoice("0001");
//        boolean result = choiceDAO.deleteChoice(choice);
//
//        Assert.assertTrue(result);
//    }
//
//    @Test
//    public void testAddChoice() throws Exception {
//        Choice choice;
//        boolean result;
//        ArrayList<Alternative> alts = new ArrayList<Alternative>();
//        String newID = "0001";
//
//        // tested with 2, 3, 4, 5 alternatives
//        alts.add(new Alternative("blueberry_"+newID, "blueberry"));
//        alts.add(new Alternative("strawberry_"+newID, "strawberry"));
//        alts.add(new Alternative("raspberry_"+newID, "raspberry"));
//        alts.add(new Alternative("cloud berry_"+newID, "cloud berry"));
//        alts.add(new Alternative("cranberry_"+newID, "cranberry"));
//
//        // adding new choice
//        choice = new Choice(newID, "Best Berry", alts,
//                Timestamp.from(Instant.now()), 5);
//        choiceDAO.deleteChoice(choice); // make sure that there isn't a duplicate
//        result = choiceDAO.addChoice(choice);
//        Assert.assertTrue(result);
//
//        // can't add same choice a second time
//        result = choiceDAO.addChoice(choice);
//        Assert.assertFalse(result); // "The choice is already in the table"
//    }
//
//    @Test
//    public void testAddChoiceDescTooLong() throws Exception {
//        Choice choice;
//        boolean result;
//        ArrayList<Alternative> alts = new ArrayList<Alternative>();
//        String newID = "0002";
//
//        // tested with 2, 3, 4, 5 alternatives
//        alts.add(new Alternative("blueberry_"+newID, "blueberry"));
//        alts.add(new Alternative("strawberry_"+newID, "strawberry"));
//        alts.add(new Alternative("raspberry_"+newID, "raspberry"));
//        alts.add(new Alternative("cloud berry_"+newID, "cloud berry"));
//        alts.add(new Alternative("cranberry_"+newID, "cranberry"));
//
//        // adding new choice, desc is 1 char too long
//        choice = new Choice(newID,
//                "Please pick the berry you think is da best in the whole world",
//                alts, Timestamp.from(Instant.now()), 5);
//        choiceDAO.deleteChoice(choice); // make sure that there isn't a duplicate
//        result = choiceDAO.addChoice(choice);
//        Assert.assertFalse(result);
//    }
//
//    @Test
//    public void testGetAllChoices() throws Exception {
//        ArrayList<Choice> choices = choiceDAO.getAllChoices();
//    }
}
