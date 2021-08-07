package util;

import org.junit.Test;

public class FunctionWrapperTest {

    @Test
    public void getInvokerTest() {
        try {
            System.out.println(FunctionWrapper.getInvoker());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
