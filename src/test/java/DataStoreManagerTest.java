import com.quorum.offchain.core.Data;
import com.quorum.offchain.core.DataHash;
import com.quorum.offchain.core.HashCalculator;
import com.quorum.offchain.core.datastore.DataStoreManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataStoreManagerTest {

    private DataStoreManager dataStoreManager;

    @Before
    public void before() {
        dataStoreManager = new DataStoreManager();
    }

    @Test
    public void storeAndGetData() {
        byte[] dataBytes1 = {1, 2, 3};
        DataHash dataHash1 = HashCalculator.calculateHash(dataBytes1);
        Data data1 = new Data(dataBytes1);

        byte[] dataBytes2 = {'a', 'b', 'c'};
        DataHash dataHash2 = HashCalculator.calculateHash(dataBytes2);
        Data data2 = new Data(dataBytes2);

        DataHash dataHash_1 = dataStoreManager.storeData(data1);
        DataHash dataHash_2 = dataStoreManager.storeData(data2);

        assertEquals(dataHash_1, dataHash1);
        assertEquals(dataHash_2, dataHash2);

        Data data_1 = dataStoreManager.getData(dataHash_1);
        Data data_2 = dataStoreManager.getData(dataHash_2);

        assertEquals(data_1, data1);
        assertEquals(data_2, data2);


    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDataStore(){
        DataHash dataHash1 = HashCalculator.calculateHash(new byte[]{1, 2, 3});
        dataStoreManager.getData(dataHash1);
    }
}
