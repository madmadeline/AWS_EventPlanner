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
		
		//get the alternatives output
		var len = Object.keys(obj.alts[0]).length;
		len = len - 1;
		for (var i = 0; i < len; i++){
			altsOutput += ("Alternative " + i + "ID: " + alts[i].ID + " Description: " + alts[i].description + "\"><b>");
		}
		output = "Choice Description: " + description + " Choice ID: " + choiceID + " Alternatives: " + "\"><b>" + altsOutput;
		
		displayChoice.innerHTML = output;
	}else{
		//error
		console.log("Choice failed to create.");
	}
	
	
	/* OLD JS=======================================================================
	var id = obj.choiceID; //the choice id
	
	//change the innerHTML of the register choice thing
	var head = document.getElementById("registerHeader");
	head.innerHTML = "Register with Choice: " + id;
	
		var displayChoice = document.getElementById('displayChoice');

	var len = Object.keys(obj.alts[0]).length;

	var output = "";
	//print the info about the alternatives
	
	var altOutput = "";
	for(var i = 0; i < len - 1; i++){
		console.log("GET ALTS: " + obj.alts[i].description);
		altOutput += ("Alternative " + i +  " ID: " + obj.alts[i].ID +  " Description: " + obj.alts[i].description + "\n");
	}
		
    output = "Choice Description: " + obj.description + " Choice ID: " + obj.choiceID + " Alternatives:\n" + altOutput;


  // Update computation result
  displayChoice.innerHTML = output;

	console.log("ID: " + obj.choiceID);

  refreshChoicesList();*/
}

function handleCreateClick(e) {
  var form = document.createChoiceForm;
 
  var data = {};
  data["choiceDescription"] = form.choiceDesc.value;
  data["maxTeamSize"] = form.numParticipants.value;
  data["alternatives"] = [form.alt1.value, form.alt2.value, form.alt3.value, form.alt4.value, form.alt5.value];

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
