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
		var alt1 = obj.alt1;
		var alt2 = obj.alt2;
		var alt3 = obj.alt3;
		var alt4 = obj.alt4;
		var alt5 = obj.alt5;
		
		//update the register form
		document.createUserForm.newChoiceID.value = choiceID;
		
		//print the newly created choice
		var output = "";
		var altsOutput = "";
		
		//get the alternatives output
		altsOutput = alt1 + "<br />" + alt2 + "<br />" + alt3 + "<br />" + alt4 + "<br />" + alt5 + "<br />";
		output = "Choice Description: " + description + "<br />" + "Choice ID:" + choiceID + "<br />" + "Alternatives: " + "<br />" + altsOutput;
		
		displayChoice.innerHTML = output;
	}else{
		//error
		console.log("Choice failed to create.");
	}
}

function handleCreateClick(e) {
  var form = document.createChoiceForm;
 
  var data = {};
  data["choiceDescription"] = form.choiceDesc.value;
  data["maxTeamSize"] = form.numParticipants.value;
  data["alt1ID"] = form.alt1.value;
  data["alt2ID"] = form.alt2.value;
  data["alt3ID"] = form.alt3.value;
  data["alt4ID"] = form.alt4.value;
  data["alt5ID"] = form.alt5.value;


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
