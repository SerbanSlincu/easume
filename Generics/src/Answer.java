import java.util.Objects;

public class Answer {

    private String answer;

    // transform into type-safe input
    private String check(String input) {
        String answer = "";
        for(int i = 0; i < input.length(); i ++) {
            if(input.charAt(i) == '\\') {
                answer += " \\textbackslash ";
            }
            else if(input.charAt(i) == '^') {
                answer += " \\textasciicircum ";
            }
            else if(input.charAt(i) == '~') {
                answer += " \\textasciitilde ";
            }
            else if("%${}_#&".indexOf(input.charAt(i)) >= 0){
                answer += " \\" + input.charAt(i) + " ";
            }
            else if("<>".indexOf(input.charAt(i)) >= 0){
                answer += " $" + input.charAt(i) + "$ ";
            }
            else {
                answer += input.charAt(i);
            }
        }
        return answer;
    }

    public Answer(String answer) {
        this.answer = check(answer);
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void print() {
        System.out.print(this.answer);
    }

    public void println() {
        System.out.println(this.answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return this.answer.equals(answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.answer);
    }
}
