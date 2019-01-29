/** This should decide the format of the output.
 */

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BasicTemplateProvider implements TemplateProvider {

    private Question[] order;
    private int index;

    private String fileName;
    private String generatedName;
    private String fileSeparator;
    private String template;
    private String modified;
    private String genericPath;
    private String currentTime;

    public BasicTemplateProvider(String templateName) throws IOException {
        this.fileSeparator = System.getProperty("file.separator");
        this.template = System.getProperty("user.dir") + this.fileSeparator + "Template" + this.fileSeparator + "src" + this.fileSeparator + templateName + ".tex";
        this.fileName = "resume";
        this.modified = "modified-template";
        this.generatedName = "";
        this.genericPath = System.getProperty("user.dir") + this.fileSeparator;
        this.order = this.getOrderList(template);
        this.index = 0;

        Date date = new Date();
        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        this.currentTime = dateFormat.format(date);
    }

    // Get the list of questions from the template
    @NotNull
    private Question[] getOrderList(String templateName) throws IOException {
        // used for repeating details
        Scanner scanner = new Scanner(System.in);
        Stack<String> remainedLines = new Stack<>();

        // file will be here at the end
        LinkedList<String> modified = new LinkedList<>();
        // where to do additions
        int currently = 0;

        // list of details
        LinkedList<Question> questions = new LinkedList<Question>();
        InputStream inputStream = new FileInputStream(templateName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        int readLines = 0;

        String line = bufferedReader.readLine();
        modified.add(line);
        currently += 1;

        while(line != null) {
            // used for repeating details
            readLines += 1;

            if(line.length() > 0 && !line.contains("%!(END"))
            for (int i = 1; i < line.length(); i++) {
                if (line.charAt(i) == '(' && line.charAt(i - 1) == '!') {
                    String question = "";

                    int j = i + 1;
                    while (line.charAt(j) != ')' && j < line.length()) {
                        question += line.charAt(j);
                        j += 1;
                    }
                    if (j >= line.length()) {
                        throw new Error("The template has been corrupted!");
                    }

                    // This could represent a repeating detail
                    String[] parameters = question.split(",");
                    if (parameters.length == 2 && parameters[0].equals("REPEAT")) {
                        // repeat many times
                        LinkedList<String> toWrite = new LinkedList<>();

                        int last = remainedLines.size() - 1;
                        String line2;
                        if(remainedLines.size() > 0) {
                            line2 = remainedLines.get(last);
                            last --;
                        } else {
                            line2 = bufferedReader.readLine();
                        }

                        while (!line2.contains("%!(END," + parameters[1] + ")")) {
                            toWrite.add(line2 + "\n");
                            if(remainedLines.size() > 0) {
                                line2 = remainedLines.get(last);
                                last --;
                            } else {
                                line2 = bufferedReader.readLine();
                            }
                        }

                        System.out.println("How many " + parameters[1] + " would you like?");
                        int many = scanner.nextInt();
                        // hack to fix off by one logic
                        if(remainedLines.size() > 0) many --;

                        for (int times = 0; times < many; times++) {
                            modified.addAll(currently,toWrite);
                        }

                        for(int times = 0; times < many; times ++) {
                            for (int counter = toWrite.size() - 1; counter >= 0; counter --) {
                                remainedLines.push(toWrite.get(counter));
                            }
                        }
                    } else if (parameters.length == 0) {
                    } else {
                        questions.add(new Question(question));
                    }

                    i = j;
                }
            }
            // use from before, if any
            if (!remainedLines.empty()) {
                line = remainedLines.pop();
                currently += 1;
            } else {
                line = bufferedReader.readLine();
                modified.add(line + "\n");
                currently += 1;
            }
        }

        bufferedReader.close();
        inputStream.close();

        File modifiedFile = new File(this.modified);
        FileWriter fileWriter = new FileWriter(modifiedFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(String now : modified) { printWriter.print(now); }
        printWriter.close();
        fileWriter.close();

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

    // Make a copy of the template
    private File writeCurrent(String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        PrintWriter printWriter = new PrintWriter(fileWriter);

        InputStream inputStream = new FileInputStream(this.modified);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();

        while(line != null) {
            if(!line.equals("") && !line.equals("null"))
                printWriter.print(line + "\n");
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        inputStream.close();
        printWriter.close();
        fileWriter.close();

        File toDelete = new File(file.getParentFile() + fileSeparator + this.modified);
        toDelete.delete();
        System.out.println("Deleted the aux file from: " + file.getParentFile() + fileSeparator + this.modified);

        return file;
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
