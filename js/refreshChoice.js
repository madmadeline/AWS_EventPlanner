function refreshChoice(payload){
	var obj = JSON.parse(payload);
	//display the alternative approval UI and list the alternatives (with buttons!)
	//document.getElementById("alternativeApproval").style.visibility = "visible";
	///var choice = obj.choice;
	var alts = obj.alternatives;
	var uname = document.getElementById("usernameDisplay");
	//var choiceID = document.getElementById("currentChoice");
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
	//choiceID.innerHTML = choice.ID;
	display.innerHTML = altList;
}