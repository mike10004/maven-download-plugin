package com.googlecode.download.maven.plugin.internal.cache;

import org.apache.commons.codec.binary.Base64;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;

public class FileBackedIndexDeserializationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void deserializeIndexFromPreviousVersion() throws Exception {
        File cacheDir = temporaryFolder.newFolder();
        // This index.ser file was created with plugin version 1.3.0 and contains
        // one entry for URL "https://example.com/index.html"
        String version130IndexSerBase64 =
                "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVz\n" +
                        "aG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAAeaHR0cHM6Ly9leGFtcGxlLmNvbS9pbmRleC5odG1s\n" +
                        "c3IAPWNvbS5nb29nbGVjb2RlLmRvd25sb2FkLm1hdmVuLnBsdWdpbi5pbnRlcm5hbC5DYWNoZWRG\n" +
                        "aWxlRW50cnkEeE9qOnYk/wIAAUwACGZpbGVOYW1ldAASTGphdmEvbGFuZy9TdHJpbmc7eHB0ACtp\n" +
                        "bmRleC5odG1sXzVkZjRlMTQxNGVjN2RlY2IwNWJlZjY4ZjkyZmQ2NzQxeA==";
        URI url = URI.create("https://example.com/index.html");
        byte[] version130IndexSerBytes = new Base64().decode(version130IndexSerBase64.getBytes(StandardCharsets.US_ASCII));
        Files.write(cacheDir.toPath().resolve("index.ser"), version130IndexSerBytes);
        FileBackedIndex index = new FileBackedIndex(cacheDir);
        assertFalse("expect miss due to cache format mismatch", index.contains(url));
    }
}
