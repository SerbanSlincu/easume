/** Implementation based on text input:
 *  This should be used only as a testing method.
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class TextInputProvider implements InputProvider {

    private Scanner input = new Scanner(System.in);

    private LinkedList <Answer> previousInputs = new LinkedList<>();

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

    @Override
    public File generate(File file) throws IOException {
        String text = "";

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        for(Answer answer : previousInputs) {
            if((line = bufferedReader.readLine()) != null) {
                text += line + answer.getAnswer() + "\n";
            }
        }

        bufferedReader.close();
        fileReader.close();

        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(text);

        printWriter.close();
        fileWriter.close();

        System.out.println("File created at: " + file.getAbsolutePath());
        return file;
    }
}
