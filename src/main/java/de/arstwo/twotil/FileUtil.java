/*
 * Copyright 2024 Stefan Feldbinder <sfeldbin@googlemail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.arstwo.twotil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Various file-related utility functions.
 */
public class FileUtil {

        /**
         * Creates a predicate that checks whether a file was modified before the specified duration.
         *
         * @param amountToSubtract amount of {@code unit} to subtract from now
         * @param unit             time unit of the duration
         * @return predicate returning {@code true} if the file is older than the computed timestamp
         */
        public static Predicate<Path> fileOlderThan(final long amountToSubtract, final TemporalUnit unit) {
                final Instant compareInstant = Instant.now().minus(amountToSubtract, unit);
                return (final Path p) -> {
                        try {
                                return Files.getLastModifiedTime(p).toInstant().isBefore(compareInstant);
                        } catch (IOException e) {
                                return false;
                        }
                };
        }

        /**
         * Extracts all files in the root of the given ZIP into the target directory.
         *
         * @param source     zip archive
         * @param targetDir  directory to write to
         * @return {@code true} on success, otherwise {@code false}
         */
    public static boolean unzipRootOnly(final Path source, final Path targetDir) {
        try (final ZipInputStream zis = new ZipInputStream(Files.newInputStream(source))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()
                        && !zipEntry.getName().contains("/")
                        && !zipEntry.getName().contains("\\")) {
                    final Path target = targetDir.resolve(zipEntry.getName());
                    Files.copy(zis, target, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

        /**
         * Attempts to delete the given path.
         *
         * @param p path to delete
         * @return {@code true} if deletion succeeded, otherwise {@code false}
         */
        public static boolean tryDeletePath(final Path p) {
                try {
                        Files.deleteIfExists(p);
                        return true;
                } catch (IOException e) {
			return false;
		}
	}

	/**
	 * Do not instantiate.
	 */
	private FileUtil() {
	}
}
