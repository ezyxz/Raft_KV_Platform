package raft.cuhk;

public class ConnConfig {
    public String ip_address;
    public int rport;

    public ConnConfig() {
    }

    public ConnConfig(String ip_address, int rport) {
        this.ip_address = ip_address;
        this.rport = rport;
    }
}
