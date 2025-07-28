import com.cuhk.raft.core.RaftServerInr;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestRaftServer {
    @Test
    void test01 () throws IOException {
        RaftServerInr server = new RaftServerInr(50001);
        server.build(null);
    }

}
