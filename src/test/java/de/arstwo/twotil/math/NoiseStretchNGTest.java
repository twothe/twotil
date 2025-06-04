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
package de.arstwo.twotil.math;

import java.util.ArrayList;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class NoiseStretchNGTest {

    private static class StubNoise extends SimplexNoise {
            StubNoise() { super(new java.util.Random(0)); }
            List<double[]> input = new ArrayList<>();
                @Override
                public double noise(double x, double z) { input.add(new double[]{x,z}); return 0.5; }
                @Override
                public double noise(double x, double y, double z) { input.add(new double[]{x,y,z}); return 0.7; }
        }

        @Test
        public void testGetNoise2D() {
                StubNoise s = new StubNoise();
                NoiseStretch n = new NoiseStretch(s, 2.0, 4.0);
                assertEquals(n.getNoise(4.0, 8.0), 0.5);
                assertEquals(s.input.get(0)[0], 2.0);
                assertEquals(s.input.get(0)[1], 2.0);
        }

        @Test
        public void testGetNoise3D() {
                StubNoise s = new StubNoise();
                NoiseStretch n = new NoiseStretch(s, 2.0, 2.0, 2.0);
                assertEquals(n.getNoise(4.0, 6.0, 8.0), 0.7);
                assertEquals(s.input.get(0)[0], 2.0);
                assertEquals(s.input.get(0)[1], 3.0);
                assertEquals(s.input.get(0)[2], 4.0);
        }
}
