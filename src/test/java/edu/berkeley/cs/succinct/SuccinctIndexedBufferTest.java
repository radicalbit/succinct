package edu.berkeley.cs.succinct;

import junit.framework.TestCase;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SuccinctIndexedBufferTest extends TestCase {

    private SuccinctIndexedBuffer sIBuf;
    private long[] offsets;
    private byte[] fileData;

    /**
     * Set up test.
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();

        File inputFile = new File("data/test_file");

        fileData = new byte[(int) inputFile.length()];
        DataInputStream dis = new DataInputStream(
                new FileInputStream(inputFile));
        dis.readFully(fileData);
        ArrayList<Long> positions = new ArrayList<Long>();
        positions.add(0L);
        for(int i = 0; i < fileData.length; i++) {
            if(fileData[i] == '\n') {
                positions.add(Long.valueOf(i + 1));
            }
        }
        offsets = new long[positions.size()];
        for(int i = 0; i < offsets.length; i++) {
            offsets[i] = positions.get(i);
        }
        sIBuf = new SuccinctIndexedBuffer(fileData, offsets);
    }

    /**
     * Test method: byte getRecordDelim()
     *
     * @throws Exception
     */
    public void testGetRecordDelim() throws Exception {
        System.out.println("getRecordDelim");
        
        assertEquals(SuccinctIndexedBuffer.getRecordDelim(), '\n');
    }

    /**
     * Test method: Long[] recordSearchOffsets(byte[] query)
     *
     * @throws Exception
     */
    public void testRecordSearchOffsets() throws Exception {
        System.out.println("recordSearchOffsets");

        /*
        Long[] searchOffsets = sIBuf.recordSearchOffsets("int".getBytes());
        for(int i = 0; i < searchOffsets.length; i++) {
            byte[] buf = sIBuf.extractUntil(searchOffsets[i].intValue(), (byte)'\n');
            assertTrue(new String(buf).contains("int"));
        }
        */
    }

    /**
     * Test method: byte[][] recordSearch(byte[] query)
     *
     * @throws Exception
     */
    public void testRecordSearch() throws Exception {
        System.out.println("recordSearch");

        /*
        byte[][] records = sIBuf.recordSearch("int".getBytes());
        for(int i = 0; i < records.length; i++) {
            assertTrue(new String(records[i]).contains("int"));
        }
        */
    }

    /**
     * Test method: long recordCount(byte[] query)
     *  
     * @throws Exception
     */
    public void testRecordCount() throws Exception {
        System.out.println("recordCount");

        long count = sIBuf.recordCount("int".getBytes());
        assertEquals(count, 28L);
    }

    /**
     * Test method: byte[][] extractRecords(int offset, int len)
     *  
     * @throws Exception
     */
    public void testExtractRecords() throws Exception {
        System.out.println("extractRecords");

        /*
        byte[][] records = sIBuf.extractRecords(0, 5);
        for(int i = 0; i < records.length; i++) {
            for(int j = 0; j < records[i].length; j++) {
                assertEquals(records[i][j], fileData[((int) (offsets[i] + j))]);
            }
        }
        */
    }

    /**
     * Test method: byte[][] recordSearchRegex(String query)
     *
     * @throws Exception
     */
    public void testRegexSearchRecords() throws Exception {
        System.out.println("regexSearchRecords");

        /*
        // TODO: Add more tests
        byte[][] records = sIBuf.recordSearchRegex("int");
        for(int i = 0; i < records.length; i++) {
            assertTrue(new String(records[i]).contains("int"));
        }
        */
    }

    /**
     * Test method: void readObject(ObjectInputStream ois)
     * Test method: void writeObject(ObjectOutputStream oos)
     *
     * @throws Exception
     */
    public void testSerializeDeserialize() throws Exception {
        System.out.println("serializeDeserialize");

        // Serialize data
        FileOutputStream fOut = new FileOutputStream("data/test_file.succinct");
        ObjectOutputStream oos = new ObjectOutputStream(fOut);
        oos.writeObject(sIBuf);
        oos.close();

        // Deserialize data
        FileInputStream fIn = new FileInputStream("data/test_file.succinct");
        ObjectInputStream ois = new ObjectInputStream(fIn);
        SuccinctIndexedBuffer sIBufRead = (SuccinctIndexedBuffer) ois.readObject();
        ois.close();

        assertNotNull(sIBufRead);
        assertEquals(sIBufRead.getOriginalSize(), sIBuf.getOriginalSize());
        assertTrue(Arrays.equals(sIBufRead.extract(0, sIBufRead.getOriginalSize()),
                                sIBuf.extract(0, sIBuf.getOriginalSize())));
    }
}