package caml.group.demo.db;

import caml.group.demo.model.Alternative;
import caml.group.demo.model.TestContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Test;

public class AlternativeDAOTest {
    AlternativeDAO altDAO;
    java.sql.Connection conn;

    @Before
    public void init() throws Exception {
        conn = DatabaseUtil.connect();
        TestContext ctx = new TestContext();
        ctx.setFunctionName("post");
        LambdaLogger logger = ctx.getLogger();
        altDAO = new AlternativeDAO(logger);
    }

    @Test
    public void testAddAlternative() throws Exception {
        init();
        String choiceID = "4510";
        String altDesc = "FuzzyBrownBlanket";
        Alternative alt = new Alternative(altDesc+"_"+choiceID,altDesc);
        altDAO.addAlternative(alt,choiceID);
    }



}
