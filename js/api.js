//comment to try and get this to actually commit this time

//base aws url, all other urls are derived from this.
//MUST end in a SLASH
var base_url = "https://24afvsw7sh.execute-api.us-east-1.amazonaws.com/alpha/";

var find_choice_url = base_url + "choice"; //GET
var create_choice_url = base_url + "choice"; //POST
var get_report_url = base_url + "report"; //GET
var participate_url = base_url + "participate"; //POST
var feedback_url = base_url + "feedback"; //POST
var approve_url = base_url + "approve"; //POST
var disapprove_url = base_url + "disapprove"; //POST
var unselect_url = base_url + "unselect"; //POST
var complete_url = base_url + "complete"; //POST
var delete_url = base_url + "delete"; //POST

var create_user_url = base_url + "user"; //post
var list_users_url = base_url + "user"; //get
