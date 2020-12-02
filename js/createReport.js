function handleReportClick() {
   var xhr = new XMLHttpRequest();
   xhr.open("GET", get_report_url, true);
   xhr.send();
   
   console.log("sent");

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    if (xhr.readyState == XMLHttpRequest.DONE) {
      console.log ("XHR:" + xhr.responseText);
      processReportResponse(xhr.responseText);
    } else {
      processReportResponse("N/A");
    }
  };
}

function processReportResponse(result){
	//take all of the choices and display them
	//result should be json which contains an array of choices
	console.log("res: " + result);
	var obj = JSON.parse(result);
	var reportList = document.getElementById("reportList");
	var choices = obj.choices;
	var output = "";
	
	//check the status code
	if (obj.statusCode == 200){
		//choices found, display them
		//must display the choice description, choice ID, choice date of creation, and completion status.
		console.log("Report success.");
		var isComplete = false;
		/*if (choices[i].winner == NULL){
			isComplete = false;
		}else{
			isComplete = true;
		}*/
		var output = "";
		//for loop goes through the list of choices
		for (var i = 0; i < choices.length; i++){
			var date = new Date(choices[i].time);
			//print the choice descriptions
			output += "Choice: " + choices[i].description + "<br />" +
					  "ID: " + choices[i].ID + "<br />" +
					  "Creation Date: " + date.toString() + "<br />"+
					  "Completed?: " + isComplete + "<br />" + "<br />";
		}
		reportList.innerHTML = output;
	}else{
		//choices not found, or error
		console.log("Error generating report.");
	}
}