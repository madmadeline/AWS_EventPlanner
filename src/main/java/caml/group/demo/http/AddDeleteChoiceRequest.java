package caml.group.demo.http;

public class AddDeleteChoiceRequest {
    double days;

    public double getDays() {
        return days;
    }

    public void setDays(double days) {
        this.days = days;
    }

    public String toString() { return "Add(" + days + ")"; }
}
