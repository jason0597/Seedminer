import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class DataNodes {
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
            try (InputStream stream = (isNew3DS ? nodes_new_url : nodes_old_url).openStream()) {
                nodes = toByteArray(stream);
            }
        } catch (IOException e) {
            System.out.println("Failed to get the latest data nodes! Falling back to pre-bundled nodes...");
            try (InputStream stream = getClass().getResourceAsStream(isNew3DS ? "lfcs_new.dat" : "lfcs.dat")) {
                nodes = toByteArray(stream);
            }
        }

        return nodes;
    }

    private byte[] toByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = stream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();

        return baos.toByteArray();
    }
}
