import java.io.File;
import java.io.IOException;

/** Way to run main part of the Text version.
 *  Should also be used for testing.
 */

public class app {

    static InputProvider inputProvider;
    static TemplateProvider templateProvider;

    public static void main(String[] args) throws IOException {
        inputProvider = new TextInputProvider();
        templateProvider = new BasicTemplateProvider();

        for(int index = 0; index < templateProvider.sizeOf(); index ++) {
            Question question = templateProvider.getNextItem();
            Answer answer = inputProvider.getNextInput(question);
        }

        File output = templateProvider.generate();
        output = inputProvider.generate(output);
        System.exit(0);
    }
}
