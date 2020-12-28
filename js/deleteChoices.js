function processDeleteResponse(result){
	console.log("result:" + result);
	var obj = JSON.parse(result);
	var status = obj["statusCode"];
	var deleteResult = document.getElementById("deleteResult");
	if (status == 200){
		console.log("Deletion successful!");
		deleteResult.innerHTML = "Choices successfully deleted.";
	}else{
		console.log("Deletion unsuccessful.");
		deleteResult.innerHTML = "Could not delete choices, or there were no choices to delete.";
	}
}

function handleDeleteClick(){
	//b is a boolean
	//if true, an approval was chosen
	//if false, a disapproval was chosen
	
	var data = {};
	
	days = document.deleteForm.deleteDays.value;
	
	data["days"] = days;
	
	var js = JSON.stringify(data);
  	console.log("JS:" + js);
  	var xhr = new XMLHttpRequest();
  	xhr.open("POST", delete_choice_url, true);

  	// send the collected data as JSON
  	xhr.send(js);

  	// This will process results and update HTML as appropriate. 
  	xhr.onloadend = function () {
	    console.log(xhr);
	    console.log(xhr.request);
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	 if (xhr.status == 200) {
		      console.log ("XHR:" + xhr.responseText);
		      processDeleteResponse(xhr.responseText);
	    	 } else {
	    		 console.log("actual:" + xhr.responseText)
				  var js = JSON.parse(xhr.responseText);
				  var err = js["response"];
				  alert (err);
	    	 }
	    } else {
	      processDeleteResponse("N/A");
	    }
	  };
}