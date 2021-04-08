
// Simple monitor factory.
public class MonitorFactory {

    public MonitorFactory() { }

    public Monitor createMonitor(String type) {
        if(type.compareTo("USA") == 0) {
            return new USAMonitor();
        } else if(type.compareTo("ETH") == 0) {
            // return new ethiopia monitor
        } else if(type.compareTo("UZB") == 0) {
            // return new uzbekistan monitor
        } else {
            throw new IllegalArgumentException("Monitor of type \"" + type + "\" is not supported.");
        }
        // this will never execute because an exception will be thrown.
        return null;
    }
}
