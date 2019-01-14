import java.io.File;
import java.io.IOException;

/** Main interaction point between user and app.
 *  Always used to get the next input.
 */

public interface InputProvider {

    public Answer getNextInput();

    public Answer getNextInput(Question question);

    public File generate(File file) throws IOException;


}
