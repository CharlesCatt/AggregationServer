
import java.util.concurrent.*;
public interface FileRequest extends Callable {
    public int eventNo = 0;
    public int getEventNo();
}