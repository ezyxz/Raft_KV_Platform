import com.cuhk.raft.core.RaftServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestRaftServer {
    @Test
    void test01 () throws IOException {
        RaftServer server = new RaftServer(50001);
        server.build(null);
    }

}
