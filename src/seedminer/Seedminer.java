package seedminer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Seedminer {
    private char bfmode = 'u'; //u for undeclared
    private Path mp1;

    Seedminer(Path mp1_txt, char bf_mode_char) {
        bfmode = bf_mode_char;
        mp1 = mp1_txt;
    }

    private void Read_mp1(byte[] LFCS, byte[] ID0) throws IOException, NumberFormatException {
        List<String> arrlist = Files.readAllLines(mp1);
        String LFCS_str = arrlist.get(9);
        String ID0_str  = arrlist.get(15);
        if (LFCS_str.length() == 0 || ID0_str.length() == 0)
            throw new IOException("The LFCS or ID0 have not been filled in!");

        String[] LFCS_bytes = new String[8];
        String[] ID0_bytes  = new String[16];

        for (int i = 0; i <= 21; i += 3)
            LFCS_bytes[i / 3] = new String(new char[]{LFCS_str.charAt(i), LFCS_str.charAt(i+1)});
        for (int i = 0; i < 0x20; i += 2)
            ID0_bytes[i / 2]  = new String(new char[]{ID0_str.charAt(i),  ID0_str.charAt(i+1)});

        for (int i = 0; i < 8; i++)
            LFCS[i] = (byte) Integer.parseInt(LFCS_bytes[i], 16);
        for (int i = 0; i < 16; i++)
            ID0[i]  = (byte) Integer.parseInt(ID0_bytes[i], 16);
    }

    public void DoSeedminer() throws IOException, NumberFormatException {
        byte[] LFCS = new byte[8];
        byte[] ID0  = new byte[16]; //ID0 is half the sha256, i.e. 256 / 8 / 2 == 16
        Read_mp1(LFCS, ID0);
    }
}
