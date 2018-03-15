import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

public class Seedminer {
    private boolean bfmode; //false means CPU, true means GPU
    private byte[] LFCS = new byte[8];

    //You don't need to parse the ID0 to into a byte array as you will be sending it to
    //the bruteforce program as a parameter later on (as a string)
    private String ID0;

    private boolean isNew3DS;
    public boolean getNew3DS() { return isNew3DS; }

    Seedminer(Path mp1, boolean isGPUbf) throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException {
        bfmode = isGPUbf;
        FileParsing.ReadMP1(mp1, LFCS, ID0);
        isNew3DS = (LFCS[4] == 0x02);
    }

    public void DoSeedminer(byte[] nodes) {
        short[][] parsed_nodes = FileParsing.ReadNodes(nodes);

        ByteBuffer buffer = ByteBuffer.wrap(LFCS).order(ByteOrder.LITTLE_ENDIAN);
        int LFCS_num = buffer.getInt(0);
        LFCS_num >>= 12; //pastebin.com/ujp9jLf9

        int msed3estimate = getMsed3Estimate(parsed_nodes, LFCS_num);

        //TODO: Parse and flip the ID0 correctly
    }

    private int getMsed3Estimate(short[][] nodes, int LFCS_num) {
        short[] LFCSes = nodes[0];
        short[] msed3s = nodes[1];

        int distance = Math.abs(LFCSes[0] - LFCS_num);
        int idx = 0;
        for (int i = 0; i < LFCSes.length; i++){
            int cdistance = Math.abs(LFCSes[i] - LFCS_num);
            if (cdistance < distance){
                idx = i;
                distance = cdistance;
            }
        }

        System.out.printf("The LFCS is %d (0x%X), the closest match to the LFCS was %d (0x%X) %n", LFCS_num, LFCS_num, LFCSes[idx], LFCSes[idx]);
        System.out.printf("The error estimate corresponding to that is %d (0x%X)", msed3s[idx], msed3s[idx]);

        return msed3s[idx];
    }
}
