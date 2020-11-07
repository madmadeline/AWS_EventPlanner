package caml.group.demo.model;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Choice {
	final String id;
	final String description;
	final ArrayList<Alternative> alternatives;// = new ArrayList<Alternative>();
	LocalDateTime time;
	Alternative winner;

	public Choice(String id, String description, ArrayList<Alternative> alternatives, LocalDateTime time) {
		this.id = id;
		this.description = description;
		this.alternatives = alternatives;
		this.time = time;
	}
}

