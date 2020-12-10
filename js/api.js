//comment to try and get this to actually commit this time

//base aws url, all other urls are derived from this.
//MUST end in a SLASH
var base_url = "https://24afvsw7sh.execute-api.us-east-1.amazonaws.com/alpha/";

var find_choice_url = base_url + "find"; //POST
var create_choice_url = base_url + "choice"; //POST
var get_report_url = base_url + "report"; //GET
var create_user_url = base_url + "user"; //POST
var send_approval_url = base_url + "approve"; //POST
var delete_choice_url = base_url + "delete"; //POST
var send_feedback_url = base_url + "feedback"; //POST
var complete_choice_url = base_url + "complete"; //POST