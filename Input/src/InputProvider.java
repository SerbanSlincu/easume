import java.io.File;
import java.io.IOException;

/** Main interaction point between user and app.
 *  Always used to get the next input.
 */

public interface InputProvider {

    public Answer getNextInput();

    public Answer getNextInput(Question question);

    // Get a copy of the template
    // and replace the questions with the answers
    public void generate(File file) throws IOException;


}
