package caml.group.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import caml.group.demo.model.Admin;
import caml.group.demo.model.Model;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateReportHandlerTest {

	private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
	private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";
	CreateReportHandler handler;
	Model model;

/*
	@Before
	public void init() throws ClassNotFoundException, SQLException {
		model = new Model("admin", "admin:pass");
		handler = new CreateReportHandler(model);
	}
	

	@Test
	public void testCreateReportHandler() throws IOException, ClassNotFoundException, SQLException {
		init();
		
		InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
		OutputStream output = new ByteArrayOutputStream();

		handler.handleRequest(input, output, null);

		// TODO: validate output here if needed.
		String sampleOutputString = output.toString();
		System.out.println(sampleOutputString);
		//Assert.assertEquals(EXPECTED_OUTPUT_STRING, sampleOutputString);
	}

 */
}
