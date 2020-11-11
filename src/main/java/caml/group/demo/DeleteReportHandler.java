package caml.group.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;

import caml.group.demo.model.Admin;
import caml.group.demo.model.Choice;
import caml.group.demo.model.Model;
import caml.group.demo.model.Report;

public class DeleteReportHandler implements RequestStreamHandler {
	Model model;
	Admin admin;
	LambdaLogger logger;


	public DeleteReportHandler(Model model) {
		this.model = model;
		this.admin = model.getAdmin();
	}
	

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// create a printer
		PrintWriter pw = new PrintWriter(output);

		// create a logger
		logger = context.getLogger();
		if (context != null) { context.getLogger(); }

		// load entire input into a String (since it contains JSON)
		StringBuilder incoming = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				incoming.append(line);
			}
		}

		/* When coming in from Lambda function is pure JSON. When coming from API Gateway or the
		 * real thing, then is wrapped inside more complicated JSON and you only want the BODY
		 * in most cases. 
		 */
		JsonNode node = Jackson.fromJsonString(incoming.toString(), JsonNode.class);
		if (node.has("body")) {
			node = Jackson.fromJsonString(node.get("body").asText(), JsonNode.class);
		}


		// parse the inputed strings
		double nDays = 0;

		// TODO ensure "nDays" is correct field name
		String param = node.get("nDays").asText();
		boolean error = false;
		try {
			nDays = Double.parseDouble(param);
    	} catch (Exception e) {
    		logger.log("Unable to parse:" + param + " as nDays"); 
			error = true;
    	}


		// delete reports
		int statusCode;
		int numReportsDeleted = 0;

		if (error) {
			statusCode = 400;
		} else {
			numReportsDeleted = admin.deleteReports(nDays);
			statusCode = 200;
		}

		// Needed for CORS integration...
		String response = "{ \n" + 
				"  \"isBase64Encoded\" : false, \n" +
				"  \"statusCode\"      : " + statusCode + ", \n" +
				"  \"headers\" : { \n " +
				"     \"Access-Control-Allow-Origin\" : \"*\", \n" + 
				"     \"Access-Control-Allow-Method\"  : \"GET,POST,OPTIONS\" \n" + 
				"  }, \n" +
				"  \"body\" : \"" + "{ \\\"result\\\" : \\\"" + numReportsDeleted + "\\\" }" + "\" \n" +
				"}";

		// write out output
		pw.print(response);
		pw.close();
	}

}
