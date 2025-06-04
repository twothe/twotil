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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class FileUtilNGTest {

        @Test
        public void testFileOlderThan() throws Exception {
                Path f = Files.createTempFile("old", "txt");
                Files.setLastModifiedTime(f, FileTime.from(Instant.now().minus(2, ChronoUnit.DAYS)));
                Predicate<Path> p = FileUtil.fileOlderThan(1, ChronoUnit.DAYS);
                assertTrue(p.test(f));
                Files.setLastModifiedTime(f, FileTime.from(Instant.now()));
                assertFalse(p.test(f));
                Files.deleteIfExists(f);
        }

        @Test
        public void testUnzipRootOnly() throws Exception {
                Path zip = Files.createTempFile("test", ".zip");
                try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zip))) {
                        zos.putNextEntry(new ZipEntry("a.txt"));
                        zos.write("x".getBytes());
                        zos.closeEntry();
                        zos.putNextEntry(new ZipEntry("sub/b.txt"));
                        zos.write("y".getBytes());
                        zos.closeEntry();
                }
                Path target = Files.createTempDirectory("unz");
                assertTrue(FileUtil.unzipRootOnly(zip, target));
                assertTrue(Files.exists(target.resolve("a.txt")));
                assertFalse(Files.exists(target.resolve("sub/b.txt")));
                FileUtil.tryDeletePath(zip);
                FileUtil.tryDeletePath(target.resolve("a.txt"));
                FileUtil.tryDeletePath(target);
        }

        @Test
        public void testTryDeletePath() throws Exception {
                Path f = Files.createTempFile("del", "txt");
                assertTrue(FileUtil.tryDeletePath(f));
                assertFalse(Files.exists(f));
                assertTrue(FileUtil.tryDeletePath(f));
        }
}
