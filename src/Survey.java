import java.time.LocalDate;

public class Survey {
    private final int ID;
    private final LocalDate creationDate;
    private final LocalDate expirationDate;
    private String[] questions;

    public Survey(int ID, LocalDate creationDate, LocalDate expirationDate) {
        this.ID = ID;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        questions = new String[12];
    }

    public int getID() {
        return ID;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String[] getQuestions() {
        return questions;
    }

    public void setQuestions(String[] questions) {
        this.questions = questions;
    }
}
