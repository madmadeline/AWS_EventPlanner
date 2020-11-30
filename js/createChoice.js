function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);

	//parse json and get the choice id
	var obj = JSON.parse(result);
	var choice = obj["choice"];
	var status = obj["statusCode"];
	
	if (status == 200){
		//update computation result
		console.log("success");
	}else{
		//error
		console.log("failure");
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
