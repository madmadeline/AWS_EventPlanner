package caml.group.demo.db;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseUtilTest {
    Connection conn;

    @Test
    public void testConnect() throws Exception {
        conn = DatabaseUtil.connect();
        Assert.assertNotNull(conn);
    }
}
