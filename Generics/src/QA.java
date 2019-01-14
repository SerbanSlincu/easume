public class QA {

    private Question question;
    private Answer answer;

    public QA(Question question, Answer answer) {
        this.question = question;
        this.answer = answer;
    }

    public QA(Question question) {
        this.question = question;
        this.answer = new Answer("");
    }

    public QA(Answer answer) {
        this.question = new Question("");
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }
}
