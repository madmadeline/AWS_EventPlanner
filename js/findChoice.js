//find a specific choice from the db


function processFindResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);
  var obj = JSON.parse(result);
  var status = obj.statusCode;

  //check the status code
  if (status == 200){
	console.log("Choice found.")
	//get the choice info from the parsed json
	var choiceID = obj.choiceID;
	var maxTeamSize = obj.maxTeamSize;
	var description = obj.description;
	var alts = obj.alts;
	
	//update the register form
	document.createUserForm.newChoiceID.value = choiceID;
	
	//send the json to the invisible div for sneaky js access
	var payload = document.getElementById("payload");
	payload.innerHTML = result;
	
	//update the UI
	document.createChoiceForm.choiceDesc.value = description;
	document.createChoiceForm.numParticipants.value = maxTeamSize;
	document.createChoiceForm.alt1.value = alts[0];
	document.createChoiceForm.alt2.value = alts[1];
	//all other alts are optional, so check length
	if (alts.length > 2){
		document.createChoiceForm.alt3.value = alts[2];
	}
	if (alts.length > 3){
		document.createChoiceForm.alt4.value = alts[3];
	}
	if (alts.length > 4){
		document.createChoiceForm.alt5.value = alts[4];
	}
	
	refreshChoice(result);
	
  }else{
	//error
	console.log("Choice could not be found or does not exist.");
	document.getElementById("findChoiceResult").innerHTML = "Choice does not exist.";
  }

}

function handleFindClick() {
  var form = document.findChoiceForm;
 
  var data = {};
  data["choiceID"] = form.findChoice.value;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("GET", find_choice_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processFindResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processFindResponse("N/A");
    }
  };
}