function refreshChoice(payload){
	var obj = JSON.parse(payload);
	
	
	//only display the functionality if the choice has not been completed.
	//otherwise, show the completion with the winning alternative.
	
	
	//display the alternative approval UI and list the alternatives (with buttons!)

	var alts = obj.alternatives;

	var display = document.getElementById("altList");
	var altList = "";
	
	if (obj.winner == ""){			
		for (var i = 0; i < alts.length; i++){
			altList += `<div><hr> <br><p>` + alts[i].description + `</p><br>` 
			+ `Feedback Given: <br>`;
			
			for (var j = 0; j < alts[i].feedback.length; j++){
				if (alts[i].feedback[j].message != ""){
					var time = new Date(alts[i].feedback[j].timeStamp);
					console.log(alts[i].feedback[j].message);
					altList += alts[i].feedback[j].message + " - " + alts[i].feedback[j].username + " : " + time + `<br>`;
				}
			}
			
			var totA = 0;
			for (var k = 0; k < alts[i].totalApprovalUsers.length; k++){
				totA++;
			}
			var totD = 0;
			for (var k = 0; k < alts[i].totalDisapprovalUsers.length; k++){
				totD++;
			}
			
			altList += totA + ` users approved:` + `<br>` 
			+ alts[i].totalApprovalUsers + `<br>` 
			+ totD + ` users disapproved: ` + `<br>`
			+ alts[i].totalDisapprovalUsers + `<br>` + 
			`<br><button class = "approveBtn" onclick = "Javascript:handleApprovalClick(0,` + i + `)"><i class="fa fa-thumbs-up"></i></button>
	        <button class = "disapproveBtn" onclick = "Javascript:handleApprovalClick(1,` + i + `)"><i class="fa fa-thumbs-down"></i></button>
			<button class = "deleteAppBtn" onclick = "Javascript:handleApprovalClick(2,`+ i +`)">Remove Approval</button></div> 
			<br><form name = 'feedbackForm`+i+`' method = "post"> Submit Feedback:<br><input name = "feedbackInput`+i+`" type = "text" id = "feedbackInput`+i+`"></form>
			<input type = "button" value = "Submit" onclick = "Javascript:handleFeedbackClick(`+i+`)">` +
			`<br> Complete choice with this alternative: <input type = "button" value = "complete" onclick = "Javascript:handleCompleteClick(`+i+`)">`;
		}
		
		altList += `<hr>`;
		
		display.innerHTML = altList;	
	}else{
		display.innerHTML = "This choice has been completed. Winning alternative: " + obj.winnerName;
	}

}