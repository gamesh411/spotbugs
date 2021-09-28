package ghIssues;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

public class Issue1338 {
    public static KeyStore loadKeyStore(final Path path) throws Exception {
        KeyStore a = null;

        try {
            InputStream inputStream = Files.newInputStream(path);
        } catch (Throwable t) {
            a = KeyStore.getInstance("asdf", "jkl;");
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            // final KeyStore keyStore = KeyStore.getInstance("JKS");
            // keyStore.load(inputStream, password.toCharArray());
            // return keyStore;
            a = KeyStore.getInstance("JKS");
        }

        return a;
    }
}
