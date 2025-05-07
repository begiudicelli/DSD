package geral;

public class PortAddr {
    private final String hostname;
    private final int portnum;
    public PortAddr(String s, int i) {
        hostname = s;
        portnum = i;
    }
    public String getHostName() {
        return hostname;
    }
    public int getPort() {
        return portnum;
    }
}
