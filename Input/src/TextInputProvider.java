/** Implementation based on text input:
 *  This should be used only as a testing method.
 */

import java.util.Scanner;

public class TextProvider implements Provider {
    private Scanner input = new Scanner(System.in);

    @Override
    public String getNextInput() {
        System.out.print(" > ");
        String nextInput = input.next();
        return nextInput;
    }

    @Override
    public String getNextInput(String question) {
        System.out.print(question);
        return getNextInput();
    }
}
