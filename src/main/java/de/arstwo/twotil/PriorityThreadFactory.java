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
 * A ThreadFactory to create threads with a given priority.
 *
 * @author Stefan Feldbinder <sfeldbin@googlemail.com>
 */
public class PriorityThreadFactory implements ThreadFactory {

	final ThreadGroup threadGroup;

	public PriorityThreadFactory(final String name, final int maxPriority) {
		this.threadGroup = new ThreadGroup(name);
		this.threadGroup.setMaxPriority(maxPriority);
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(threadGroup, r);
	}

}
