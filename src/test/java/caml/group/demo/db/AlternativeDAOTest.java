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
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

// ALL TESTS PASSED 12/11 4:20PM
public class AlternativeDAOTest {
    AlternativeDAO altDAO;
    ChoiceDAO choiceDAO;
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

        // adding new choice
        choice = new Choice(newID, "Best torture device", alts,
                Timestamp.from(Instant.now()), 5);
        choiceDAO.addChoice(choice);
    }

    @After
//	@Test
    public void deleteChoice() throws Exception {
        System.out.println("deleting mock choice");
        choice = choiceDAO.getChoice(choice.getID());
//		choice = choiceDAO.getChoice("3f2e5e56-de9a-4cd1-8474-4528edf2f4e9");
        choiceDAO.deleteSpecificChoice(choice.getID());
    }

    @Test
    public void testAddAlternative() throws Exception {
        Alternative alt = new Alternative(UUID.randomUUID().toString(), "tickling");

        Assert.assertEquals(choice.getAlternatives().size(), 0);
        altDAO.addAlternative(alt, choice.getID());

        choice = choiceDAO.getChoice(choice.getID());
        Assert.assertEquals(choice.getAlternatives().size(), 1);
    }

}
