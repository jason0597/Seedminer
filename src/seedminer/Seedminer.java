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

    private void Read_mp1(byte[] LFCS, byte[] ID0) throws IOException {
        List<String> arrlist = Files.readAllLines(mp1);
        String LFCS_str = arrlist.get(9);
        String ID0_str  = arrlist.get(15);

        for (int i = 0; i <= 9; i += 3)
            LFCS[i / 3] = (byte) ((Character.digit(LFCS_str.charAt(i), 16) << 4) | Character.digit(LFCS_str.charAt(i + 1), 16));
        for (int i = 0; i < 0x20; i += 2)
            ID0[i / 2] =  (byte)  ((Character.digit(ID0_str.charAt(i), 16) << 4) | Character.digit(ID0_str.charAt(i+1), 16));
    }

    public void DoSeedminer() throws IOException {
        byte[] LFCS = new byte[4];
        byte[] ID0  = new byte[16]; //ID0 is half the sha256, i.e. 256 / 8 / 2 == 16

        Read_mp1(LFCS, ID0);

        System.out.print("The LFCS is --> ");
        for (byte b : LFCS) System.out.printf("%02X ", b);
        System.out.print("\nThe ID0 is  --> ");
        for (byte b : ID0) System.out.printf("%02X ", b);
    }
}
