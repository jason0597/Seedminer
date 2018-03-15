import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileParsing {
    public static void ReadMP1(Path mp1, byte[] LFCS, String ID0) throws IOException, NumberFormatException {
        if (!Files.exists(mp1))
            throw new IOException("The specified file does not exist!");

        List<String> arrlist = Files.readAllLines(mp1);

        if (arrlist.size() < 17)
            throw new NumberFormatException("Invalid movable_part1.txt file!");

        String LFCS_str = arrlist.get(9);
        String ID0_str  = arrlist.get(15);
        if (LFCS_str.length() != 23 || ID0_str.length() != 32)
            throw new IOException("The LFCS/ID0 have not been filled in properly!");

        //Send the ID0 back to the caller thanks to pass-by-reference
        ID0 = ID0_str;

        for (int i = 0; i < 24; i += 3) {
            //why i use parseInt instead of parseByte ---> stackoverflow.com/questions/6996707
            LFCS[i / 3] = (byte)Integer.parseInt(new String(new char[]{LFCS_str.charAt(i), LFCS_str.charAt(i+1)}), 16);
        }
    }

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
}
