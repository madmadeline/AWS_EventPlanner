function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);
  var obj = JSON.parse(result);
  var status = obj["statusCode"];
	
	if (status == 200){
		console.log("Choice created.");
		
		//get the choice info from the parsed json
		var choiceID = obj.choiceID;
		var description = obj.description;
		var alts = obj.alts;
		
		//update the register form
		document.createUserForm.newChoiceID.value = choiceID;
		
		//print the newly created choice
		var output = "";
		var altsOutput = "";
		
		//send the json to the invisible div for sneaky js access
		var payload = document.getElementById("payload");
		payload.innerHTML = result;
		
		//get the alternatives output
		//altsOutput = alt1 + "<br />" + alt2 + "<br />" + alt3 + "<br />" + alt4 + "<br />" + alt5 + "<br />";
		for (var i = 0; i < alts.length; i++){
			altsOutput += alts[i].description + "<br />";
		}
		output = "Choice Description: " + description + "<br />" + "Choice ID:" + choiceID + "<br />" + "Alternatives: " + "<br />" + altsOutput;
		
		displayChoice.innerHTML = output;
	}else{
		//error
		console.log("Choice failed to create.");
		displayChoice.innerHTML = "Error: Choice creation failed.";
	}
}

function handleCreateClick(e) {
  var form = document.createChoiceForm;
 
  var data = {};
  data["choiceDescription"] = form.choiceDesc.value;
  data["maxTeamSize"] = form.numParticipants.value;

  var counter = 0;
  if (form.alt1.value.length != 0){
	 data["alt1ID"] = form.alt1.value;
	 data["alt1Description"] = form.alt1.value;
	 counter++;
  }
  if (form.alt2.value.length != 0){
	 data["alt2ID"] = form.alt2.value;
     data["alt2Description"] = form.alt2.value;
	 counter++;
  }
  if (form.alt3.value.length != 0){
	data["alt3ID"] = form.alt3.value;
	data["alt3Description"] = form.alt3.value;
	counter++;
  }
  if (form.alt4.value.length != 0){
	data["alt4ID"] = form.alt4.value;
	data["alt4Description"] = form.alt4.value;
	counter++;
  }
  if (form.alt4.value.length != 0){
	data["alt5ID"] = form.alt5.value;
	data["alt5Description"] = form.alt5.value;
	counter++;
  }

  //if less than 2 alts, don't even send info. CATCH
  if (counter <= 1){
	console.log("User tried to create choice with less than 2 alternatives. Caught by front end.");
	displayChoice.innerHTML = "Please include at least 2 alternatives.";
	return;
  } 


  var js = JSON.stringify(data);
  console.log("JS:" + js);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", create_choice_url, true);

  // send the collected data as JSON
  xhr.send(js);

  // This will process results and update HTML as appropriate. 
  xhr.onloadend = function () {
    console.log(xhr);
    console.log(xhr.request);
    if (xhr.readyState == XMLHttpRequest.DONE) {
    	 if (xhr.status == 200) {
	      console.log ("XHR:" + xhr.responseText);
	      processCreateResponse(xhr.responseText);
    	 } else {
    		 console.log("actual:" + xhr.responseText)
			  var js = JSON.parse(xhr.responseText);
			  var err = js["response"];
			  alert (err);
    	 }
    } else {
      processCreateResponse("N/A");
    }
  };
}
