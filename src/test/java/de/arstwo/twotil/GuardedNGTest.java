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

import java.util.concurrent.atomic.AtomicBoolean;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class GuardedNGTest {

        @Test
        public void testProcessAndAccess() {
                Guarded<Integer> g = new Guarded<>(1);
                assertEquals(g.process(v -> v + 1), Integer.valueOf(2));
                AtomicBoolean called = new AtomicBoolean();
                g.access(v -> called.set(true));
                assertTrue(called.get());
        }

        @Test
        public void testTryAccessWhenLocked() throws Exception {
                Guarded<Integer> g = new Guarded<>(1);
                Thread t = new Thread(() -> g.access(v -> {
                        try {
                                Thread.sleep(50);
                        } catch (InterruptedException e) {
                        }
                }));
                t.start();
                Thread.sleep(10);
                AtomicBoolean called = new AtomicBoolean();
                assertFalse(g.tryAccess(v -> called.set(true)));
                assertFalse(called.get());
                t.join();
                assertTrue(g.tryAccess(v -> called.set(true)));
                assertTrue(called.get());
        }
}
