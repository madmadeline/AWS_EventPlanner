//comment to try and get this to actually commit this time

//Refresh choices list from server

function refreshChoicesList(){
	var xhr = new XMLHttpRequest();
	xhr.open("GET", list_choices_url, true);
	xhr.send();
	
	console.log("sent");
	
	// This will process results and update HTML as appropriate. 
    xhr.onloadend = function () {
    	if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processChoiceResponse(xhr.responseText);
    	} else {
      		processChoiceResponse("N/A");
    }
  };
}

/**
 * Respond to server JSON object.
 *
 * Replace the contents of 'constantList' with a <br>-separated list of name,value pairs.
 */
function processListResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var choiceList = document.getElementById('displayChoice');
  
  var output = "";
  for (var i = 0; i < js.list.length; i++) {
    var choiceJson = js.list[i];
    console.log(choiceJson);
    
    var cid = choiceJson["id"];
    var cdescription = choiceJson["description"];
    var ctime = choiceJson["time"];
    output = output + "<div id=\"const" + cid + "\"><b>" + cdescription + ":</b> = " + ctime + "(<a href='javaScript:requestDelete(\"" + cid + "\")'></a>) <br></div>";
  }

  // Update computation result
  choiceList.innerHTML = output;
}