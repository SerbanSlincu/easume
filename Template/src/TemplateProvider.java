/** Always used to get the next item: (question to ask).
 */
public interface SequenceProvider {

    void initialise();

    public String getNextItem();

    public String getNextItem(String after);
}
