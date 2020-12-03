package caml.group.demo.model;



public class Rating {
	Boolean rating; // true approve and false disapprove
	int userID;
	
	public Rating(Boolean rating, int userID) {
		this.rating = rating;
		this.userID = userID;
	}
	
	public int getUserID() { return userID;}
	
	public boolean getIsSameUserID(int id) {
		if(helpEquals(userID, id)) { return true;}
		else { return false; }
	}
	
	public boolean getIsApproval() {
		if(helpEquals(rating.approve, 1)) {
			return true;
		}
		else { return false;}		
	}
	
	public boolean getIsDisapproval() {
		if(helpEquals(rating.disapprove, 1)) {
			return true;
		}
		else { return false;}		
	}
	
	public boolean helpEquals(int a, int b) {
		Integer var1 = Integer.valueOf(a);
		Integer var2 = Integer.valueOf(b);

		if(var2.intValue() == var1.intValue()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
