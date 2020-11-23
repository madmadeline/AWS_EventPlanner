function processCreateResponse(result) {
  // Can grab any DIV or SPAN HTML element and can then manipulate its
  // contents dynamically via javascript
  console.log("result:" + result);

  refreshChoicesList();
}

function handleCreateClick(e) {
  var form = document.createChoiceForm;
 
  var data = {};
  data["name"] = form.choiceName.value;
  data["numParticipants"] = form.numParticipants.value;
  data["alt1"] = form.alt1.value;
  data["alt2"] = form.alt2.value;
  data["alt3"] = form.alt3.value;
  data["alt4"] = form.alt4.value;
  data["alt5"] = form.alt5.value;

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
