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
		var output = `<table id = "choicesTbl">
                <thead>
                    <tr>
                        <th colspan = "4">REPORT</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    	<td>Choice Description</td>
                        <td>Timestamp</td>
                        <td>Choice ID</td>
                        <td>Completed</td>
                    </tr>`;
		//for loop goes through the list of choices
		for (var i = 0; i < choices.length; i++){
			if (choices[i].winnerName == ""){
				isComplete = false;
			}else{
				isComplete = true;
			}
			var date = new Date(choices[i].time);
			output += `<tr><td>` + choices[i].description + `</td>
							<td>` + date.toString() + `</td>
							<td>` + choices[i].ID + `</td>
							<td>` + isComplete + `</td></tr>`;
		}
		output +=  `</tbody></table>`
		reportList.innerHTML = output;
	}else{
		//choices not found, or error
		console.log("Error generating report.");
	}
}