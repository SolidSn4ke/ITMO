package module;

import com.example.demo.util.Hex;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class HexTest {
    @Test
    public void testBytesToHexEmpty() {
        byte[] bytes = new byte[]{};
        String hexString = Hex.bytesToHex(bytes);
        assertEquals("", hexString);
    }

    @Test
    public void testBytesToHexSingleByte() {
        String hexString = Hex.bytesToHex(new byte[]{0x41});
        assertEquals("41", hexString);
    }

    @Test
    public void textBytesToHexWord() {
        String hexString = Hex.bytesToHex(new byte[]{0x1f, 0x45, 0x0b, 0x32});
        assertEquals("1f450b32", hexString);
    }
}
