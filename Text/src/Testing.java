/** Should be a way to test every component
 *  Left like this until unit tests(?)
 */

import java.io.IOException;

public class Testing {

    static InputProvider inputProvider;
    static TemplateProvider templateProvider;

    private static void testingInput() {
        System.out.println("Beginning the input testing phase!");

        Answer answer1 = inputProvider.getNextInput();
        answer1.println();

        Question question2 = new Question("What is your name?");
        Answer answer2 = inputProvider.getNextInput(question2);
        answer2.println();

        System.out.println();
    }

    private static void testingTemplate() {
        System.out.println("Beginning the template testing phase!");

        for(int index = 0; index < templateProvider.sizeOf(); index ++) {
            Question nextEntry = templateProvider.getNextItem();
            nextEntry.println();
        }
        try {
            Question nextEntry = templateProvider.getNextItem();
        } catch (Error e) {
            if(e.getMessage().equals("That was the end of the template.")) {
                System.out.println("After last one: passed!");
            }
        }

        try {
            Question nextEntry = templateProvider.getNextItem(new Question("coconut"));
        } catch (Error e) {
            if(e.getMessage().equals("That was the end of the template.") ||
                e.getMessage().equals("There is no entry coconut in the template.")) {
                System.out.println("After inexistent: passed!");
            }
        }

        try {
            Question nextEntry = templateProvider.getNextItem(100000);
        } catch (Error e) {
            if(e.getMessage().equals("The template does not contain an entry for the mentioned index: 100000.")) {
                System.out.println("At large index: passed!");
            }
        }

        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("....................................");
        System.out.println("Beginning the initial testing phase!");
        System.out.println("....................................");

        inputProvider = new TextInputProvider();
        templateProvider = new BasicTemplateProvider("BasicTemplate");

        testingInput();
        testingTemplate();

        System.out.println("....................................");
        System.out.println("Ending the testing phase!");
        System.out.println("....................................");
    }
}
