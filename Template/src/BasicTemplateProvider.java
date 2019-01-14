/** This should decide the output.
 *  TODO: add initialisation from file
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicTemplateProvider implements TemplateProvider {

    private Question[] order;
    private int index;
    private int generatorCounter;
    private String fileName;

    // TODO: check how to reach common directory independent from platform
    private String fileSeparator;
    private String genericPath;

    public BasicTemplateProvider() {
        initialise();
    }

    private void initialise() {
        order = new Question[3];
        order[0] = new Question("First Name");
        order[1] = new Question("Last Name");
        order[2] = new Question("Section1");

        this.index = 0;
        this.generatorCounter = 0;
        this.fileName = "resume";
        this.fileSeparator = System.getProperty("file.separator");
        this.genericPath = ".." + fileSeparator + ".." + fileSeparator + ".." + fileSeparator + ".." + fileSeparator +
                ".." + fileSeparator + ".." + fileSeparator + "home" + fileSeparator + System.getProperty("user.name") + fileSeparator + "Desktop" + fileSeparator;
    }

    private int getIndexForEntry(Question entry) {
        int index = 0;

        while(index < sizeOf()) {
            if(order[index].equals(entry))
                return index;
            index += 1;
        }

        throw new Error("There is no entry " + entry.getQuestion() + " in the template.");
    }

    @Override
    public Question getNextItem() {
        if(index >= sizeOf()) {
            throw new Error("That was the end of the template.");
        }
        index += 1;
        return order[index - 1];
    }

    @Override
    public Question getNextItem(Question after) {
        int afterIndex = getIndexForEntry(after) + 1;
        if(afterIndex >= sizeOf()) {
            throw new Error("That was the end of the template.");
        }

        return order[afterIndex];
    }

    @Override
    public Question getNextItem(int index) {
        if(index >= sizeOf()) {
            throw new Error("The template does not contain an entry for the mentioned index: " + Integer.toString(index) + ".");
        }
        return order[index];
    }

    @Override
    public Integer sizeOf() {
        return order.length;
    }

    private File writeCurrent(String path) throws IOException {
        File file = new File(path);

        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(Question question : order) {
            printWriter.print(question.getQuestion() + ": \n");
        }
        printWriter.close();
        fileWriter.close();

        return file;
    }

    @Override
    public File generate() throws IOException {
        return writeCurrent(genericPath + fileName + "_v" + Integer.toString(generatorCounter));

    }

    @Override
    public File generate(String location) throws IOException {
        return writeCurrent(location + fileName + "_v" + Integer.toString(generatorCounter));
    }
}
