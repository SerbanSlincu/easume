/** Implementation based on text input:
 *  This should be used only as a testing method.
 */

import groovy.lang.IntRange;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class TextInputProvider implements InputProvider {

    private Scanner input = new Scanner(System.in);

    // repeat the questions between begin and end
    private LinkedList<LinkedList<Question>> repeats= new LinkedList<LinkedList<Question>>();

    public TextInputProvider() {
        repeats.push(new LinkedList<Question>());
    }

    private LinkedList <Answer> previousInputs = new LinkedList<>();
    private int takeAtIndex = 0;

    // Compile the LaTeX code into PDF
    // Delete everything else
    private void toPdf(File file) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        String nameOfFile = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(fileSeparator) + 1);
        String[] command = {"xterm", "-e", "pdflatex", file.getAbsolutePath()};
        Process proc = new ProcessBuilder(command).start();
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            throw new Error("PDF file could not be created.");
        }
        System.out.println("PDF created at: " + file.getAbsolutePath() + ".pdf");

        File log = new File(file.getParentFile() + fileSeparator + nameOfFile + ".log");
        log.delete();
        System.out.println("Deleted the log file from: " + file.getParentFile() + fileSeparator + nameOfFile + ".log");

        File aux = new File(file.getParentFile() + fileSeparator + nameOfFile + ".aux");
        aux.delete();
        System.out.println("Deleted the aux file from: " + file.getParentFile() + fileSeparator + nameOfFile + ".aux");

        file.delete();
        System.out.println("Deleted the tex file from: " + file.getParentFile() + fileSeparator + nameOfFile);
    }

    // Replace the questions with the answers
    private String analyse(String input) {
        String text = "";

        for(int i = 0; i < input.length(); i ++) {
            if(i + 1 < input.length() && input.charAt(i) == '!' && input.charAt(i + 1) == '(') {
                int j = i + 1;

                while(input.charAt(j) != ')' && j < input.length()) {
                    j += 1;
                }
                if(j >= input.length()) {
                    throw new Error("The template has been corrupted");
                }

                text += this.previousInputs.get(this.takeAtIndex).getAnswer();
                this.takeAtIndex += 1;

                i = j;
            }
            else {
                text += input.charAt(i);
            }
        }

        return text;
    }

    private Answer getNextInputWithoutStoring() {
        System.out.print(" > ");
        Answer nextInput = new Answer(this.input.nextLine());

        this.previousInputs.add(nextInput);

        return nextInput;
    }

    @Override
    public Answer getNextInput() {
        Answer answer = this.getNextInputWithoutStoring();
        this.previousInputs.add(answer);
        return answer;
    }

    @Override
    public Answer getNextInput(Question question) {
        // add the current question to the current level
        // when 'end', move level up
        if(question.getQuestion().equals("begin")) {
            repeats.add(new LinkedList<Question>());
            return new Answer("");
        } else if(question.getQuestion().equals("end")) {
            while(true) {
                (new Question("Would you like a new instance of " + repeats.getLast().getFirst().getQuestion() + "?")).print();
                Answer answer = getNextInputWithoutStoring();
                if(answer.getAnswer().equals("no")) {
                    repeats.get(repeats.size() - 2).add(new Question("begin"));
                    for(Question q : repeats.getLast()) {
                        repeats.get(repeats.size() - 2).add(q);
                    }
                    repeats.removeLast();
                    repeats.get(repeats.size() - 2).add(new Question("end"));
                    return new Answer("");
                } else if (answer.getAnswer().equals("yes")) {
                    LinkedList<Question> now = repeats.getLast();
                    for(Question q : now) {
                        this.getNextInput(q);
                    }
                }
            }
        } else {
            repeats.getLast().add(question);
            question.print();
            return this.getNextInput();
        }
    }

    @Override
    public void generate(File file) throws IOException {
        this.takeAtIndex = 0;
        String text = "";

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();

        while(line != null) {
            text += analyse(line) + "\n";
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        fileReader.close();

        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(text);

        printWriter.close();
        fileWriter.close();

        System.out.println("LaTeX file created at: " + file.getAbsolutePath());

        this.toPdf(file);
    }
}
