import java.io.IOException;

public class ETHMonitor extends Monitor {

    public ETHMonitor() throws IOException {
        super();
        setSmsRetrievalBehavior(new AfricastalkingSMS());
    }
}
