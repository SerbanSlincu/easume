import java.io.File;
import java.io.IOException;

/** Always used to get the next item: (question to ask).
 */

public interface TemplateProvider {

    // getNextItem at current internal index
    public Question getNextItem();

    // getNextItem after after
    public Question getNextItem(Question after);

    // getNextItem at index index
    // eliminates need to use negative numbers
    public Question getNextItem(int index);

    // get sizeOf list of questions
    public Integer sizeOf();

    // get the name of the generated file
    public String getGeneratedName();

    // generate file in generic location
    public File generate() throws IOException;

    // generate file in given location
    public File generate(String location) throws IOException;
}

