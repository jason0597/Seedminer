import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

class Seedminer {
    private boolean isGPUbf; //false means CPU, true means GPU
    private byte[] LFCS = new byte[8];
    private String ID0_str;
    private byte[] ID0; //sha256 --> 256 / 8 / 2 --> 16

    private boolean isNew3DS;
    public boolean getNew3DS() { return isNew3DS; }

    Seedminer(Path mp1, boolean IsGPUbf) throws IOException, NumberFormatException {
        isGPUbf = IsGPUbf;
        ID0_str = FileParsing.ReadMP1(mp1, LFCS);
        isNew3DS = (LFCS[4] == 0x02);
    }

    public void DoSeedminer(byte[] nodes) throws IOException {
        short[][] parsed_nodes = FileParsing.ReadNodes(nodes);

        ByteBuffer buffer = ByteBuffer.wrap(LFCS).order(ByteOrder.LITTLE_ENDIAN);
        int LFCS_num = buffer.getInt(0);
        LFCS_num >>= 12; // pastebin.com/ujp9jLf9
        int msed3error = getMsed3Error(LFCS_num, parsed_nodes);

        System.out.println("the msed3 error estimate is " + msed3error);

        ID0 = FileParsing.parseID0(ID0_str);

        byte[] movableSed = calculateMovableSed(msed3error, isNew3DS);

        Launcher launching = new Launcher(movableSed, ID0, isNew3DS, isGPUbf);
        launching.exportFiles();
        launching.doMining();
    }

    private int getMsed3Error(int LFCS_num, short[][] nodes) {
        short[] LFCSes = nodes[0]; short[] msed3s = nodes[1];

        int distance = Math.abs(LFCSes[0] - LFCS_num);
        int idx = 0;
        for (int i = 0; i < LFCSes.length; i++) {
            int cdistance = Math.abs(LFCSes[i] - LFCS_num);
            if (cdistance < distance){
                idx = i;
                distance = cdistance;
            }
        }
        return msed3s[idx];
    }

    private byte[] calculateMovableSed(int msed3error, boolean isNew3DS) {
        byte[] movableSed = new byte[16];
        System.arraycopy(LFCS, 0, movableSed, 0, 8);

        if (isNew3DS) {
            movableSed[0x4] = 0x02;
            movableSed[0xF] = (byte)0x80;
        }

        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(LFCS, 0, 4);

        int msed3estimate = ( buffer.getInt(0) / 5) + (-1) * msed3error;

        buffer.rewind();
        buffer.putInt(msed3estimate);
        byte[] msed3estimate_bytes = buffer.array();

        System.arraycopy(msed3estimate_bytes, 0, movableSed, 12, 3);

        return movableSed;
    }
}
