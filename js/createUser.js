function processCreateUserResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);
  var obj = JSON.parse(result);
  var status = obj.statusCode;
  var choice = obj.choice;

  //check the status code
  if (status == 200){
	console.log("User created.")
	//get the choice info from the parsed json
	//var choiceID = choice.choiceID;
	var maxTeamSize = choice.maxTeamSize;
	var description = choice.description;
	var alts = choice.alternatives;
	
	//update the UI
	document.findChoiceForm.findChoice.value = choice.ID;
	document.createChoiceForm.choiceDesc.value = description;
	document.createChoiceForm.numParticipants.value = maxTeamSize;
	document.createChoiceForm.alt1.value = alts[0].description;
	document.createChoiceForm.alt2.value = alts[1].description;
	//all other alts are optional, so check length
	if (alts.length > 2){
		document.createChoiceForm.alt3.value = alts[2].description;
	}
	if (alts.length > 3){
		document.createChoiceForm.alt4.value = alts[3].description;
	}
	if (alts.length > 4){
		document.createChoiceForm.alt5.value = alts[4].description;
	}
	
		
	//send the json to the invisible div for sneaky js access
	var payload = document.getElementById("payload");
	payload.innerHTML = result;
	
	
	//display the alternative approval UI and list the alternatives (with buttons!)
	document.getElementById("alternativeApproval").style.visibility = "visible";
	//refreshChoice(result);
	var uname = document.getElementById("usernameDisplay");
	var uid = document.getElementById("userIDdisplay");
	var display = document.getElementById("altList");
	var altList = "";
	
	for (var i = 0; i < alts.length; i++){
		altList += `<div> <br>` + alts[i].description + `<br>` + alts[i].totalApprovals + ` users approved` + `<br>` + alts[i].totalDisapprovals + ` users disapproved` + `<br><button id = "approveBtn" onclick = "Javascript:handleApprovalClick(true,` + i + `)">
                    <i class="fa fa-thumbs-up"></i>
                </button>
                <button id = "disapproveBtn" onclick = "Javascript:handleApprovalClick(false,` + i + `)">
                    <i class="fa fa-thumbs-down"></i>
                </button> </div>`;
	}
	
	uname.innerHTML = obj.username;
	display.innerHTML = altList;
	document.getElementById("userList").innerHTML = "Successfully registered with choice: " + description;
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
