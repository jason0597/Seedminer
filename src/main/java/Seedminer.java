package main.java;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

class Seedminer {
    private boolean isGPUbf; //false means CPU, true means GPU
    private byte[] LFCS = new byte[8];
    private String ID0_str;
    private int msed3errorestimate;

    private boolean isNew3DS;
    public boolean getNew3DS() { return isNew3DS; }

    Seedminer(Path mp1, boolean IsGPUbf) throws IOException, NumberFormatException {
        isGPUbf = IsGPUbf;
        ID0_str = FileParsing.ReadMP1(mp1, LFCS);
        isNew3DS = (LFCS[4] == 0x02);
    }

    public void parseNodes(byte[] nodes) {
        short[][] parsed_nodes = FileParsing.ReadNodes(nodes);

        ByteBuffer buffer = ByteBuffer.wrap(LFCS).order(ByteOrder.LITTLE_ENDIAN);
        int LFCS_num = buffer.getInt(0);
        LFCS_num >>= 12; // pastebin.com/ujp9jLf9
        msed3errorestimate = getMsed3Error(LFCS_num, parsed_nodes);
        System.out.println("the msed3 error estimate is " + msed3errorestimate);
    }

    public void DoSeedminer() throws IOException {
        byte[] ID0 = FileParsing.parseID0(ID0_str);

        byte[] movableSed = calculateMovableSed();

        Launcher launching = new Launcher(movableSed, ID0, isGPUbf);
        launching.exportFiles();
        launching.doMining();
    }

    //This is an algorithm which finds the closest match to a value
    //What I do here is I find the closest match to the LFCS_num that I possibly can
    //Then, I retrieve the index to which the match I have found belongs, and I use
    //that index to retrieve the corresponding msed3 error estimate
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

    private byte[] calculateMovableSed() {
        byte[] movableSed = new byte[16];
        System.arraycopy(LFCS, 0, movableSed, 0, 8);

        if (isNew3DS) {
            movableSed[0x4] = 0x02;
            movableSed[0xF] = (byte)0x80;
        }

        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(LFCS, 0, 4);

        int msed3estimate = ( buffer.getInt(0) / 5) + (-1) * msed3errorestimate;

        buffer.rewind();
        buffer.putInt(msed3estimate);
        byte[] msed3estimate_bytes = buffer.array();

        System.arraycopy(msed3estimate_bytes, 0, movableSed, 12, 3);

        return movableSed;
    }
}
