/** This should decide the format of the output.
 */

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class BasicTemplateProvider implements TemplateProvider {

    private Question[] order;
    private int index;

    private String fileName;
    private String generatedName;
    private String fileSeparator;
    private String template;
    private String genericPath;
    private String currentTime;

    public BasicTemplateProvider(String templateName) throws IOException {
        initialise(templateName);
    }

    private void initialise(String templateName) throws IOException {
        this.fileSeparator = System.getProperty("file.separator");
        this.template = System.getProperty("user.dir") + this.fileSeparator + "Template" + this.fileSeparator + "src" + this.fileSeparator + templateName + ".tex";
        this.order = this.getOrderList(this.template);
        this.index = 0;
        this.fileName = "resume";
        this.generatedName = "";
        this.genericPath = System.getProperty("user.dir") + this.fileSeparator;

        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        this.currentTime = dateFormat.format(date);
    }

    // Get the list of questions from the template
    @NotNull
    private Question[] getOrderList(String templateName) throws IOException {
        LinkedList<Question> questions = new LinkedList<Question>();

        InputStream inputStream = new FileInputStream(templateName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = bufferedReader.readLine();

        while(line != null) {
            for(int i = 1; i < line.length(); i ++) {
                if(line.charAt(i) == '(' && line.charAt(i - 1) == '!') {
                    String question = "";

                    int j = i + 1;
                    while(line.charAt(j) != ')' && j < line.length()) {
                        question += line.charAt(j);
                        j += 1;
                    }
                    if(j >= line.length()) {
                        throw new Error("The template has been corrupted!");
                    }

                    questions.add(new Question(question));
                    i = j;
                }
            }
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        inputStream.close();

        return questions.toArray(new Question[0]);
    }

    private int getIndexForEntry(Question entry) {
        int index = 0;

        while(index < sizeOf()) {
            if(this.order[index].equals(entry))
                return index;
            index += 1;
        }

        throw new Error("There is no entry " + entry.getQuestion() + " in the template.");
    }

    @Override
    public Question getNextItem() {
        if(this.index >= sizeOf()) {
            throw new Error("That was the end of the template.");
        }
        this.index += 1;
        return this.order[this.index - 1];
    }

    @Override
    public Question getNextItem(Question after) {
        int afterIndex = this.getIndexForEntry(after) + 1;
        if(afterIndex >= this.sizeOf()) {
            throw new Error("That was the end of the template.");
        }

        return this.order[afterIndex];
    }

    @Override
    public Question getNextItem(int index) {
        if(index >= this.sizeOf()) {
            throw new Error("The template does not contain an entry for the mentioned index: " + Integer.toString(index) + ".");
        }
        return this.order[index];
    }

    @Override
    public Integer sizeOf() {
        return this.order.length;
    }

    @Override
    public String getGeneratedName() {
        if(this.generatedName.equals("")) {
            throw new Error("The file has not been generated yet!");
        }
        return this.generatedName;
    }

    // Make a copy of the template
    private File writeCurrent(String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        PrintWriter printWriter = new PrintWriter(fileWriter);

        InputStream inputStream = new FileInputStream(this.template);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();

        while(line != null) {
            printWriter.print(line + "\n");
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        inputStream.close();
        printWriter.close();
        fileWriter.close();

        return file;
    }

    @Override
    public File generate() throws IOException {
        this.generatedName = this.genericPath + this.fileName + "_" + this.currentTime;
        return writeCurrent(this.genericPath + this.fileName + "_" + this.currentTime);

    }

    @Override
    public File generate(String location) throws IOException {
        this.generatedName = location + this.fileName + "_" + this.currentTime;
        return writeCurrent(location + this.fileName + "_" + this.currentTime);
    }
}
