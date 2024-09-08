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
	 * Creates a predicate for a functional context to test if a file is older than a given time frame.
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
	 * Extracts the content of a zip file to a given target directory.
	 * <p>
	 * Ignores directories in the zip, only files in the root directory are extracted.
	 */
	public static boolean unzipRootOnly(final Path source, final Path targetDir) {
		try (final ZipInputStream zis = new ZipInputStream(Files.newInputStream(source))) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()) {
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
	 * Tries to delete a given path, catches potential IOExceptions, and returns true on success, false otherwise.
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
