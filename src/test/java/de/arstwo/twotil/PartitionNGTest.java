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

import java.util.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class PartitionNGTest {

        @Test
        public void testPartitionSizes() {
                Partition<Integer> p = Partition.of(Arrays.asList(0,1,2,3,4), 2);
                assertEquals(p.size(), 3);
                assertEquals(p.get(0), Arrays.asList(0,1));
                assertEquals(p.get(1), Arrays.asList(2,3));
                assertEquals(p.get(2), Arrays.asList(4));
        }

        @Test
        public void testEmptyList() {
                Partition<Integer> p = Partition.of(Collections.emptyList(), 3);
                assertEquals(p.size(), 0);
        }

        @Test
        public void testIndexOutOfBounds() {
                Partition<Integer> p = Partition.of(Arrays.asList(1,2), 1);
                try {
                        p.get(3);
                        fail("no throw");
                } catch (IndexOutOfBoundsException e) {
                }
        }
}
