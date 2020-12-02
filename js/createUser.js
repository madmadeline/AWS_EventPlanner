function processCreateUserResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);
  var obj = JSON.parse(result);
  var status = obj.statusCode;

  //check the status code
  if (status == 200){
	console.log("User created.")
	//get the choice info from the parsed json
	var choiceID = obj.choiceID;
	var maxTeamSize = obj.maxTeamSize;
	var description = obj.description;
	//var alts = obj.alts;
	
	//update the UI
	document.createChoiceForm.choiceDesc.value = description;
	document.createChoiceForm.numParticipants.value = maxTeamSize;
	/*document.createChoiceForm.alt1.value = alts[0];
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
	}*/
	
	//display the alternative approval UI and list the alternatives (with buttons!)
	document.getElementById("alternativeApproval").style.visibility = "visible";
	var display = document.getElementById("altList");
	var altList = "";
	
	
	
	display.innerHTML = altList;
  }else{
	//error
	console.log("User could not be created.");
  }

}

function handleUserClick(e) {
  var form = document.createUserForm;
 
  var data = {};
  data["username"] = form.newUsername.value;
  data["password"] = form.newPassword.value;
  data["choiceID"] = form.newChoiceID.value;

  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", create_user_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processCreateUserResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processCreateUserResponse("N/A");
    }
  };
}
