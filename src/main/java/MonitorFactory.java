import java.io.IOException;

// Simple monitor factory.
public class MonitorFactory {

    public MonitorFactory() { }

    public Monitor createMonitor(String type) throws IOException {

        if(type.length() == 0) {
            throw new IllegalArgumentException("Monitor type not specified.");
        }

        if(type.compareTo("USA") == 0) {
            return new USAMonitor();
        } else if(type.compareTo("ETH") == 0) {
            // return new ethiopia monitor
            return new ETHMonitor();
        } else {
            throw new IllegalArgumentException("Monitor of type \"" + type + "\" is not supported.");
        }
    }
}
