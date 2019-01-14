/** Implementation based on text input:
 *  This should be used only as a testing method.
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class TextInputProvider implements InputProvider {

    private Scanner input = new Scanner(System.in);

    private LinkedList <Answer> previousInputs = new LinkedList<>();
    private int takeAtIndex = 0;

    @Override
    public Answer getNextInput() {
        System.out.print(" > ");
        Answer nextInput = new Answer(input.next());

        previousInputs.add(nextInput);

        return nextInput;
    }

    @Override
    public Answer getNextInput(Question question) {
        question.print();
        return getNextInput();
    }

    // Compile the LaTeX code
    private void toPdf(File file) throws IOException {
        String[] command = {"xterm", "-e", "pdflatex", "-output-directory" + file.getParentFile() + "/", file.getAbsolutePath()};
        Process proc = new ProcessBuilder(command).start();

    }

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

                text += this.previousInputs.get(takeAtIndex).getAnswer();
                this.takeAtIndex += 1;

                i = j;
            }
            else {
                text += input.charAt(i);
            }
        }

        return text;
    }

    @Override
    public File generate(File file) throws IOException {
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
        System.out.println("PDF created at: " + file.getAbsolutePath() + ".pdf");

        return file;
    }
}
