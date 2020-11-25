package caml.group.demo.http;

import java.sql.Timestamp;

public class AddFindChoiceRequest {
    String choiceID;
    String choiceDescription;
    Timestamp dateOfCreation;
    String alt1ID;
    String alt1Description;
    String alt2ID;
    String alt2Description;
    String alt3ID;
    String alt3Description;
    String alt4ID;
    String alt4Description;
    String alt5ID;
    String alt5Description;
    String desiredChoice;

    public String getChoiceID() { return choiceID; }
    public void setChoiceID(String id) { this.choiceID = id; }

    public String getChoiceDescription() { return choiceDescription; }
    public void setChoiceDescription(String description) { this.choiceDescription = description; }

    public String getAlt1ID() { return alt1ID; }
    public void setAlt1ID(String id) { this.alt1ID = id; }

    public String getAlt1Description() { return alt1Description; }
    public void setAlt1Description(String description) { this.alt1Description = description; }

    public String getAlt2ID(){return alt2ID;};
    public void setAlt2ID(String id){this.alt2ID = id;}

    public String getAlt2Description() { return alt2Description; }
    public void setAlt2Description(String description) { this.alt2Description = description; }

    public String getAlt3ID(){return alt3ID;};
    public void setAlt3ID(String id){this.alt3ID = id;}

    public String getAlt3Description() { return alt3Description; }
    public void setAlt3Description(String description) { this.alt3Description = description; }

    public String getAlt4ID(){return alt4ID;};
    public void setAlt4ID(String id){this.alt4ID = id;}

    public String getAlt4Description() { return alt4Description; }
    public void setAlt4Description(String description) { this.alt4Description = description; }

    public String getAlt5ID(){return alt5ID;};
    public void setAlt5ID(String id){this.alt5ID = id;}

    public String getAlt5Description() { return alt5Description; }
    public void setAlt5Description(String description) { this.alt5Description = description; }

    public Timestamp getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(Timestamp timestamp) { this.dateOfCreation = timestamp; }

    public String getDesiredChoice() { return desiredChoice; }

    public String toString() { return "Add(" + choiceID + ", " + dateOfCreation + ")"; }
}
