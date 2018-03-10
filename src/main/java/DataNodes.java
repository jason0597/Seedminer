import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;

public class DataNodes {
    private URL nodes_old_url;
    private URL nodes_new_url;

    DataNodes() {
        try {
            nodes_old_url = new URL("https://github.com/zoogie/seedminer/raw/master/seedminer/saves/lfcs.dat");
            nodes_new_url = new URL("https://github.com/zoogie/seedminer/raw/master/seedminer/saves/lfcs_new.dat");
        } catch (Exception e) {}
    }

    public byte[][] GetDataNodes() throws IOException {
        byte[] nodes_old_arr = null;
        byte[] nodes_new_arr = null;

        try {
            nodes_old_arr = IOUtils.toByteArray(nodes_old_url);
            nodes_new_arr = IOUtils.toByteArray(nodes_new_url);
        }
        catch (IOException e1) {
            System.out.println("Failed to get the latest data nodes! Falling back to pre-bundled nodes...");

            try (InputStream old_stream = getClass().getResourceAsStream("lfcs.dat");
                 InputStream new_stream = getClass().getResourceAsStream("lfcs_new.dat"))
            {
                nodes_old_arr = IOUtils.toByteArray(old_stream);
                nodes_new_arr = IOUtils.toByteArray(new_stream);
            }
        }
        return (new byte[][] {nodes_old_arr, nodes_new_arr});
    }
}
