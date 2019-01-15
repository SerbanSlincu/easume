import java.util.Objects;

public class Question {

    private String question;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void print() {
        System.out.print(this.question);
    }

    public void println() {
        System.out.println(this.question);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return this.question.equals(question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.question);
    }
}
