package ai;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class QTableTest {

    @Test
    public void testSaveAndLoad() throws IOException, ClassNotFoundException {
        QTable qTable1 = new QTable();
        qTable1.update("state1", 0, 0.5);
        qTable1.update("state2", 1, -0.2);

        String testFilename = "test_q_table.ser";
        qTable1.save(testFilename);

        QTable qTable2 = QTable.load(testFilename);

        assertArrayEquals(qTable1.get("state1"), qTable2.get("state1"));
        assertArrayEquals(qTable1.get("state2"), qTable2.get("state2"));

        new File(testFilename).delete();
    }
}
