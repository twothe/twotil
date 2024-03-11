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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Separates a given list into smaller chunks (partitions).
 * <p>
 * Useful if you want to bulk-process large amounts of data, but want to interrupt for a different task every X items.
 * <p>
 * Usage example:
 * <pre>{@code
 *   Partition<String> lines = Partition.of(myHugeTextFileData, 500);
 *   for (List<String> linesChunk : lines) { // gives you a list with up to 500 items
 *     for (String line : linesChunk) {
 *       // processing
 *     }
 *     updateUI(linesChunk.size());
 *   }
 * }
 * </pre>
 */
public final class Partition<T> extends AbstractList<List<T>> {

	/**
	 * Creates a partitioned view of a given list with a given chunk size.
	 *
	 * @param <T> any
	 * @param list the source data
	 * @param chunkSize how many items are in each partition.
	 * @return a partitioned view of the source data.
	 */
	public static <T> Partition<T> of(final List<T> list, final int chunkSize) {
		return new Partition<>(list, chunkSize);
	}

	private final List<T> list;
	private final int chunkSize;

	private Partition(final List<T> list, final int chunkSize) {
		this.list = new ArrayList<>(list);
		this.chunkSize = chunkSize;
	}

	/**
	 * Returns a chunk of the partitioned content with the given index.
	 *
	 * @param index the chunk index of the underlaying data.
	 * @return a list of all elements in the requested chunk.
	 */
	@Override
	public List<T> get(int index) {
		final int start = index * chunkSize;
		final int end = Math.min(start + chunkSize, list.size());

		if (start > end) {
			throw new IndexOutOfBoundsException("Index " + index + " is out of the partition's range <0," + (size() - 1) + ">");
		}

		return new ArrayList<>(list.subList(start, end));
	}

	@Override
	public int size() {
		return (int) Math.ceil((double) list.size() / (double) chunkSize);
	}
}
