function processApprovalResponse(result){
	console.log("result:" + result);
	var obj = JSON.parse(result);
	var status = obj["statusCode"];
	if (status == 200){
		console.log("approval successfully responded");
		//update interface to show new approval/disapproval
		handleFindClick();
		
	}else{
		console.log("approval failed to respond.");
	}
}

function handleApprovalClick(b, index){
	//b is a boolean
	//if true, an approval was chosen
	//if false, a disapproval was chosen
	
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
	data["username"] = document.getElementById("usernameDisplay").innerHTML;
	data["userID"] = document.getElementById("userIDdisplay").innerHTML;
	data["altID"] = alts[index].ID;
	data["feedback"] = "";
	
	var confirm = document.getElementById("approvalConfirm");
	if (b == 0){
		//send approval
		confirm.innerHTML = "An approval was sent."
		data["rating"] = 'A';
	}else if (b == 1){
		//send disapproval
		confirm.innerHTML = "A disapproval was sent."
		data["rating"] = 'D';
	}else{
		//send delete
		confirm.innerHTML = "(dis)Approval removed."
		data["rating"] = 'O';
	}
	
	
	var js = JSON.stringify(data);
  	console.log("JS:" + js);
  	var xhr = new XMLHttpRequest();
  	xhr.open("POST", send_approval_url, true);

  	// send the collected data as JSON
  	xhr.send(js);

  	// This will process results and update HTML as appropriate. 
  	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	 if (xhr.status == 200) {
		      console.log ("XHR:" + xhr.responseText);
		      processApprovalResponse(xhr.responseText);
	    	 } else {
	    		 console.log("actual:" + xhr.responseText)
				  var js = JSON.parse(xhr.responseText);
				  var err = js["response"];
				  alert (err);
	    	 }
	    } else {
	      processApprovalResponse("N/A");
	    }
	  };
}