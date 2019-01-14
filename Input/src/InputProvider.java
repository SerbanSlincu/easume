/** Main interaction point between user and app.
 *  Always used to get the next input.
 */
public interface Provider {

    public String getNextInput();

    public String getNextInput(String question);

}
