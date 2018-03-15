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

    public byte[] GetDataNodes(boolean isNew3DS) throws IOException {
        byte[] nodes;

        try {
            nodes = IOUtils.toByteArray(isNew3DS ? nodes_new_url : nodes_old_url);
        } catch (IOException e1) {
            //Failed to get the latest data nodes! Falling back to pre-bundled nodes...
            try (InputStream stream = getClass().getResourceAsStream(isNew3DS ? "lfcs_new.dat" : "lfcs.dat")) {
                nodes = IOUtils.toByteArray(stream);
            }
        }

        return nodes;
    }
}
