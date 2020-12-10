function processFeedbackResponse(result){
	console.log("result:" + result);
	var obj = JSON.parse(result);
	var status = obj["statusCode"];
	if (status == 200){
		console.log("feedback successfully responded");
		//update interface to show new feedback
		handleFindClick();
		
	}else{
		console.log("feedback failed to respond.");
	}
}

function handleFeedbackClick(index){
	//b is a boolean
	//if true, an approval was chosen
	//if false, a disapproval was chosen
	
	var data = {};
	
	//TODO: figure out how to send the alternative id
	//currently: getting the index and searching through the choice
	payload = document.getElementById("payload").innerHTML;
	//console.log("Payload: " + payload);
	var p = JSON.parse(payload);
	//console.log("Parsed: " + p["choice"]);
	
	if (typeof p.choice !== 'undefined'){
		var alts = p.choice.alternatives;	
	}else{
		var alts = p.alternatives;
	}
	
	
	//console.log(alts);
	
	console.log(alts[index].ID);
	data["username"] = document.getElementById("usernameDisplay").innerHTML;
	data["userID"] = document.getElementById("userIDdisplay").innerHTML;
	data["altID"] = alts[index].ID;
	
	var feed;
	//get the correct feedback form
	switch (index){
		case 0:
		//first alternative
		feed = document.feedbackForm0.feedbackInput0.value;
		console.log("Feedback received: " + feed);
		break;
		case 1:
		//second alternative
		feed = document.feedbackForm1.feedbackInput1.value;
		console.log("Feedback received: " + feed);
		break;
		case 2:
		//third alternative
		feed = document.feedbackForm2.feedbackInput2.value;
		console.log("Feedback received: " + feed);
		break;
		case 3:
		//fourth alternative
		feed = document.feedbackForm3.feedbackInput3.value;
		console.log("Feedback received: " + feed);
		break;
		case 4:
		//fifth alternative
		feed = document.feedbackForm4.feedbackInput4.value;
		console.log("Feedback received: " + feed);
		break;
	}
	
	data["message"] = feed;
	
	var js = JSON.stringify(data);
  	console.log("JS:" + js);
  	var xhr = new XMLHttpRequest();
  	xhr.open("POST", send_feedback_url, true);

  	// send the collected data as JSON
  	xhr.send(js);

  	// This will process results and update HTML as appropriate. 
  	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	 if (xhr.status == 200) {
		      console.log ("XHR:" + xhr.responseText);
		      processFeedbackResponse(xhr.responseText);
	    	 } else {
	    		 console.log("actual:" + xhr.responseText)
				  var js = JSON.parse(xhr.responseText);
				  var err = js["response"];
				  alert (err);
	    	 }
	    } else {
	      processFeedbackResponse("N/A");
	    }
	  };
}