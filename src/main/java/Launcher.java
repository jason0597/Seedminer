import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.codec.binary.Hex;

class Launcher {
    private byte[] LFCS;
    private byte[] ID0;
    private byte[] movableSed;
    private boolean isGPUbf;
    Path tmpPath;

    Launcher(byte[] movablesed, byte[] id0, boolean isNew3DS, boolean isgpubf) {
        ID0 = id0;
        isGPUbf = isgpubf;
        movableSed = movablesed;
    }

    public void exportFiles() throws IOException {
        tmpPath = Files.createTempDirectory("Seedminer-java");
        String tmpPathStr = tmpPath.toString();
        if (isGPUbf) {
            Path cl_folder = Files.createDirectory(Paths.get(tmpPathStr, "cl"));
            String cl_folderStr = cl_folder.toString();
            Path file1 = Paths.get(cl_folderStr, "common.h");
            Path file2 = Paths.get(cl_folderStr, "kernel_msky.cl");
            Path file3 = Paths.get(cl_folderStr, "sha256_16.cl");
            Path file4 = Paths.get(tmpPathStr, "bfcl.exe");
            Files.copy(getClass().getResourceAsStream("common.h"), file1);
            Files.copy(getClass().getResourceAsStream("kernel_msky.cl"), file2);
            Files.copy(getClass().getResourceAsStream("sha256_16.cl"), file3);
            Files.copy(getClass().getResourceAsStream("bfcl.exe"), file4);
        } else {
            throw new IOException("CPU bruteforcing is not yet implemented! We're waiting for the new AVX bruteforcer...");
        }
    }

    public void doMining() throws IOException {
        String bfclexe = tmpPath.toString() + "\\bfcl.exe";
        String movableSedStr = Hex.encodeHexString(movableSed);
        String id0Str = Hex.encodeHexString(ID0);

        String[] command = {
            "cmd", "/c", "start", "cmd", "/c", bfclexe, "msky", movableSedStr, id0Str, "00000000"
        };

        ProcessBuilder procb = new ProcessBuilder(command);
        procb.directory(tmpPath.toFile());
        procb.start();
    }
}
