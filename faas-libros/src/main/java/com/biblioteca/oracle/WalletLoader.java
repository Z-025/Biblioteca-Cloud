package com.biblioteca.oracle;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public final class WalletLoader {

    private static volatile Path walletDir;

    private WalletLoader() {}

    public static Path ensureWalletExtracted() throws IOException {
        if (walletDir != null && Files.exists(walletDir.resolve("tnsnames.ora"))) {
            return walletDir;
        }

        synchronized (WalletLoader.class) {
            if (walletDir != null && Files.exists(walletDir.resolve("tnsnames.ora"))) {
                return walletDir;
            }

            // Usamos la variable exacta que tienes en Azure
            String walletBase64 = System.getenv("DB_WALLET_BASE64");
            if (walletBase64 == null || walletBase64.isBlank()) {
                throw new IllegalStateException("Falta la variable DB_WALLET_BASE64 en Azure");
            }

            byte[] zipBytes = Base64.getDecoder().decode(walletBase64.trim());

            Path tempRoot = Paths.get(System.getProperty("java.io.tmpdir"), "oci-wallet-" + UUID.randomUUID());
            Files.createDirectories(tempRoot);

            unzip(zipBytes, tempRoot);

            if (!Files.exists(tempRoot.resolve("tnsnames.ora"))) {
                throw new IllegalStateException("No se encontró tnsnames.ora dentro del wallet");
            }

            walletDir = tempRoot;
            return walletDir;
        }
    }

    private static void unzip(byte[] zipBytes, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path out = targetDir.resolve(entry.getName()).normalize();
                if (!out.startsWith(targetDir)) {
                    throw new SecurityException("ZIP inválido: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(out);
                } else {
                    Files.createDirectories(out.getParent());
                    try (OutputStream os = Files.newOutputStream(out)) {
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
        }
    }
}