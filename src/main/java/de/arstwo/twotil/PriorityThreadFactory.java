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

import java.util.concurrent.ThreadFactory;

/**
 * {@link ThreadFactory} that creates threads within a dedicated {@link ThreadGroup} and assigns a maximum priority.
 */
public class PriorityThreadFactory implements ThreadFactory {

	final ThreadGroup threadGroup;

        /**
         * Creates a new factory with a separate thread group and maximum priority.
         *
         * @param name        name of the thread group
         * @param maxPriority maximum priority of created threads
         */
        public PriorityThreadFactory(final String name, final int maxPriority) {
                this.threadGroup = new ThreadGroup(name);
                this.threadGroup.setMaxPriority(maxPriority);
        }

        /**
         * Creates a new thread in the configured thread group.
         *
         * @param r task to execute
         * @return the created thread
         */
        @Override
        public Thread newThread(final Runnable r) {
                return new Thread(threadGroup, r);
        }

}
