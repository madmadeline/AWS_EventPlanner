function processCompleteResponse(result){
	console.log("result:" + result);
	var obj = JSON.parse(result);
	var status = obj["statusCode"];
	if (status == 200){
		console.log("complete successfully responded");
		//update interface to show completion?
		document.getElementById("approvalConfirm").innerHTML = "This choice has been COMPLETED."
		handleFindClick();
		
	}else{
		console.log("complete failed to respond.");
	}
}

function handleCompleteClick(index){
	
	var data = {};
	
	//TODO: figure out how to send the alternative id
	//currently: getting the index and searching through the choice
	payload = document.getElementById("payload").innerHTML;
	console.log("Payload: " + payload);
	var p = JSON.parse(payload);
	console.log("Parsed: " + p["choice"]);
	
	if (typeof p.choice !== 'undefined'){
		var alts = p.choice.alternatives;	
	}else{
		var alts = p.alternatives;
	}
	
	
	console.log(alts);
	
	console.log(alts[index].ID);
	data["choiceID"] = p.choiceID;
	data["altID"] = alts[index].ID;
	
	var js = JSON.stringify(data);
  	console.log("JS:" + js);
  	var xhr = new XMLHttpRequest();
  	xhr.open("POST", complete_choice_url, true);

  	// send the collected data as JSON
  	xhr.send(js);

  	// This will process results and update HTML as appropriate. 
  	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	 if (xhr.status == 200) {
		      console.log ("XHR:" + xhr.responseText);
		      processCompleteResponse(xhr.responseText);
	    	 } else {
	    		 console.log("actual:" + xhr.responseText)
				  var js = JSON.parse(xhr.responseText);
				  var err = js["response"];
				  alert (err);
	    	 }
	    } else {
	      processCompleteResponse("N/A");
	    }
	  };
}