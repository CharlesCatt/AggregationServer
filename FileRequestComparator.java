import java.util.concurrent.*;
import java.util.Comparator;

public class FileRequestComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        FileRequest fr1 = (FileRequest) o1;
        FileRequest fr2 = (FileRequest) o2;

        if (fr1.getEventNo() == fr2.getEventNo()) {
            return 0;
        } else if (fr1.getEventNo() > fr2.getEventNo()) {
            return 1;
        }
        return -1;
    }
}
