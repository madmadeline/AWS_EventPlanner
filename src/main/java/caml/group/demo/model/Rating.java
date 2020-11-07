package caml.group.demo.model;

public class Rating {
	RatingType rating;
	int userID;
	
	public Rating(RatingType rating, int userID) {
		this.rating = rating;
		this.userID = userID;
	}
}
