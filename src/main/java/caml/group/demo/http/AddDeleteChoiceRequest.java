package caml.group.demo.http;

public class AddDeleteChoiceRequest {
    int days;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String toString() { return "Add(" + days + ")"; }
}
