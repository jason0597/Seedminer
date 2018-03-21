package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class Launcher {
    private byte[] ID0;
    private byte[] movableSed;
    private boolean isGPUbf;
    private Path tmpPath;

    Launcher(byte[] movablesed, byte[] id0, boolean isgpubf) {
        movableSed = movablesed;
        ID0 = id0;
        isGPUbf = isgpubf;
    }

    public void exportFiles() throws IOException {
        tmpPath = Files.createTempDirectory("Seedminer-java-");

        if (!isGPUbf) {
            throw new IOException("CPU bruteforcing is not yet implemented!");
        } else {
            Path cl_folder = Files.createDirectory(tmpPath.resolve("cl"));
            Path file1 = cl_folder.resolve("common.h");
            Path file2 = cl_folder.resolve("kernel_msky.cl");
            Path file3 = cl_folder.resolve("sha256_16.cl");
            Path file4 = tmpPath.resolve("bfcl.exe");
            Files.copy(getClass().getResourceAsStream("/main/resources/common.h"), file1);
            Files.copy(getClass().getResourceAsStream("/main/resources/kernel_msky.cl"), file2);
            Files.copy(getClass().getResourceAsStream("/main/resources/sha256_16.cl"), file3);
            Files.copy(getClass().getResourceAsStream("/main/resources/bfcl.exe"), file4);
        }
    }

    public void doMining() throws IOException {
        String bfclexe = tmpPath.resolve("bfcl.exe").toString();
        String movableSedStr = bytesToHex(movableSed);
        String id0Str = bytesToHex(ID0);

        String[] command = {
            "cmd", "/c", "start", "cmd", "/c", bfclexe, "msky", movableSedStr, id0Str, "00000000"
        };

        System.out.println("The command being executed is --> \n\"" + String.join(" ", command) + "\"");

        ProcessBuilder procb = new ProcessBuilder(command);
        procb.directory(tmpPath.toFile());
        System.out.println("Executing...");
        procb.start();
    }

    //copy pasted without shame from stackoverflow.com/questions/9655181
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
