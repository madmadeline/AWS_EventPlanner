package caml.group.demo.model;

public enum RatingType {
	APPROVE(1,0), DISAPPROVE(0,1);
	
	final int approve;
	final int disapprove;
	
	RatingType(int app, int dis){
		this.approve = app;
		this.disapprove = dis;
	}
	
}
