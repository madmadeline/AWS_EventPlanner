//comment to try and get this to actually commit this time

//Refresh choices list from server

function refreshUserList(){
	var xhr = new XMLHttpRequest();
	xhr.open("GET", list_users_url, true);
	xhr.send();
	
	console.log("sent");
	
	// This will process results and update HTML as appropriate. 
    xhr.onloadend = function () {
    	if (xhr.readyState == XMLHttpRequest.DONE) {
      		console.log ("XHR:" + xhr.responseText);
      		processUserResponse(xhr.responseText);
    	} else {
      		processUserResponse("N/A");
    }
  };
}

/**
 * Respond to server JSON object.
 *
 * Replace the contents of 'constantList' with a <br>-separated list of name,value pairs.
 */
function processUserResponse(result) {
  console.log("res:" + result);
  // Can grab any DIV or SPAN HTML element and can then manipulate its contents dynamically via javascript
  var js = JSON.parse(result);
  var userList = document.getElementById('userList');
  
  var output = "";
  for (var i = 0; i < js.list.length; i++) {
    var userJson = js.list[i];
    console.log(userJson);
    
    var uname = userJson["username"];
    var pword = userJson["password"];

    output = output + "<div id=\"const" + uname + "\"><b>" + pword + ":</b> = "+ "(<a href='javaScript:requestDelete(\"" + uname + "\")'></a>) <br></div>";
  }

  // Update computation result
  userList.innerHTML = output;
}