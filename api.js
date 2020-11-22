//comment to try and get this to actually commit this time

//base aws url, all other urls are derived from this.
//MUST end in a SLASH
var base_url = "something/";

var list_choices_url = base_url + "choice"; //GET
var create_choice_url = base_url + "choice"; //POST
var participate_url = base_url + "participate"; //POST
var feedback_url = base_url + "feedback"; //POST
var approve_url = base_url + "approve"; //POST
var disapprove_url = base_url + "disapprove"; //POST
var unselect_url = base_url + "unselect"; //POST
var complete_url = base_url + "complete"; //POST
var delete_url = base_url + "delete"; //POST
