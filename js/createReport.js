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
	var output = "";
	
	//check the status code
	if (obj.statusCode == 200){
		//choices found, display them
		//must display the choice description, choice ID, choice date of creation, and completion status.
		console.log("Report success.");
		reportList.innerHTML = "Status code success. edit the js now!";
	}else{
		//choices not found, or error
		console.log("Error generating report.");
	}
}