import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileParsing {
    //Give it a Path object and a byte array, and it will read in the
    //LFCS into byte array and it will return a string with the ID0
    public static String ReadMP1(Path mp1, byte[] LFCS) throws IOException, NumberFormatException {
        if (!Files.exists(mp1))
            throw new IOException("The specified file does not exist!");

        List<String> arrlist = Files.readAllLines(mp1);

        if (arrlist.size() < 17)
            throw new NumberFormatException("Invalid movable_part1.txt file!");

        String LFCS_str = arrlist.get(9);
        String ID0_str  = arrlist.get(15);
        if (LFCS_str.length() != 23 || ID0_str.length() != 32)
            throw new IOException("The LFCS/ID0 have not been filled in properly!");

        for (int i = 0; i < 24; i += 3) {
            //why i use parseInt instead of parseByte ---> stackoverflow.com/questions/6996707
            LFCS[i / 3] = (byte)Integer.parseInt(new String(new char[]{LFCS_str.charAt(i), LFCS_str.charAt(i+1)}), 16);
        }

        return ID0_str;
    }

    //Parses the node file into short array (pairs of LFCSes and msed3s)
    public static short[][] ReadNodes(byte[] nodes) {
        ByteBuffer buffer = ByteBuffer.wrap(nodes).order(ByteOrder.LITTLE_ENDIAN);

        //Each node is composed of 8 bytes each
        short[][] return_nodes = new short[2][nodes.length / 8];

        for (int i = 0; i < nodes.length; i += 8) {
            return_nodes[0][i / 8] = buffer.getShort(i);
        }
        for (int i = 4; i < nodes.length; i+= 8) {
            return_nodes[1][(i - 4) / 8] = buffer.getShort(i);
        }

        return return_nodes;
    }

    //parses the ID0 to its normal state from the below printf
    //printf("%08X%08X%08X%08X", hashword[0], hashword[1], hashword[2], hashword[3])
    //each hashword is 4 bytes long, i.e. 8 characters long
    public static byte[] parseID0(String ID0_str) throws NumberFormatException {
        String[] ID0_bytes_str = new String[16];
        for (int i = 0; i < 0x20; i += 2)
            ID0_bytes_str[i / 2]  = new String(new char[]{ID0_str.charAt(i),  ID0_str.charAt(i+1)});

        byte[] return_value = new byte[16];

        for (int i = 0; i < 16; i += 4) {
            for (int j = 0; j < 4; j++) {
                return_value[j + i] = (byte)Integer.parseInt(ID0_bytes_str[(i + 4) - 1 - j], 16);
            }
        }

        return return_value;
    }
}
