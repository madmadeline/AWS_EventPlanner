package caml.group.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import caml.group.demo.SubmitFeedbackHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SubmitFeedbackHandlerTest {

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";
    SubmitFeedbackHandler handler;
/*

	@Before
	public void init() throws ClassNotFoundException, SQLException {
		model = new Model("admin", "admin:pass");
		handler = new SubmitFeedbackHandler(model);
	}

    @Test
    public void testSubmitFeedbackHandler() throws IOException, ClassNotFoundException, SQLException {
        init();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, null);

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        System.out.println(sampleOutputString);
        Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
    }

 */
}
