package caml.group.demo.db;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.Choice;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.Timestamp;


import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class ChoiceDAOTest {
    ChoiceDAO choiceDAO;
    AlternativeDAO altDAO;
    LambdaLogger logger;
    Choice choice;

    @Before
    public void init() throws Exception {
        System.out.println("creating mock choice");

        DatabaseUtil.connect();
        TestContext ctx = new TestContext();
        ctx.setFunctionName("post");
        logger = ctx.getLogger();

        choiceDAO = new ChoiceDAO(logger);
        altDAO = new AlternativeDAO(logger);

        ArrayList<Alternative> alts = new ArrayList<>();
        String newID = UUID.randomUUID().toString();

        alts.add(new Alternative(UUID.randomUUID().toString(), "minor bugs"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "zoom classes"));
        alts.add(new Alternative(UUID.randomUUID().toString(), "tickling"));

        // adding new choice
        choice = new Choice(newID, "Best torture device", alts,
                Timestamp.from(Instant.now()), 5);
        choiceDAO.addChoice(choice);
    }

    @After
//	@Test
    public void clean() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
//		choice = choiceDAO.getChoice("b6cc394e-b3d0-490e-bfd1-0fd387c3ad26");
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testGetChoice() throws Exception {
//        String choiceID = "3572";
//        String desc = "work dammit";
//        int maxSize = 3;

        // check valid choice id
        Choice heyChoice = choiceDAO.getChoice(choice.getID());
        Assert.assertEquals(heyChoice.getID(), choice.getID());
        Assert.assertEquals(heyChoice.getDescription(), choice.getDescription());
        Assert.assertEquals(heyChoice.getMaxTeamSize(), choice.getMaxTeamSize());

        // check invalid choice id --> should return null
        String choiceID = "0009";
        heyChoice = choiceDAO.getChoice(choiceID);
        Assert.assertNull(heyChoice);
    }
}
