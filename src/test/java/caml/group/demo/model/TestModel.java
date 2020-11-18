package caml.group.demo.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import caml.group.demo.CreateReportHandler;

public class TestModel {

	Model model;


	@Before
	public void init() throws ClassNotFoundException, SQLException {
		model = new Model("admin", "admin:pass");
	}
	

	@Test
	public void testLogIn() throws IOException, SQLException {
		String name = "Jyalu";
		String pass = "awexhrcqehr";
		
		
		assertTrue(model.logIn(name, pass));
		
	}

}
