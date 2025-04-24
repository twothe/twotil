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

import de.arstwo.twotil.functional.ThrowingFunction;
import de.arstwo.twotil.functional.ThrowingSupplier;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Colletion of various utility functions that fit nowhere else.
 */
public class Util {

	/**
	 * Returns the first element that passes the given validator.
	 */
	public static <T> T first(final Predicate<T> validator, final T... values) {
		for (final T t : values) {
			if (validator.test(t)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Returns the first element that passes the given validator, otherwise throw the supplied throwable.
	 */
	public static <T, E extends Throwable> T firstOrThrow(final Predicate<T> validator, final Supplier<E> throwable, final T... values) throws E {
		for (final T t : values) {
			if (validator.test(t)) {
				return t;
			}
		}
		throw throwable.get();
	}

	/**
	 * Returns the first non-null element.
	 */
	public static <T> T firstNonNull(final T... values) {
		return first(Objects::nonNull, values);
	}

	/**
	 * Returns the first non-empty element.
	 */
	public static <T> T firstNonEmpty(final T... values) {
		return first(Util::isNotEmpty, values);
	}

	/**
	 * Returns the first non-blank element.
	 */
	public static <T> T firstNonBlank(final T... values) {
		return first(Util::isNotBlank, values);
	}

	/**
	 * Returns true if no value passes the given validator.
	 */
	public static <T> boolean noneMatch(final Predicate<T> validator, final T... values) {
		for (final T t : values) {
			if (validator.test(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if all values pass the given validator.
	 */
	public static <T> boolean allMatch(final Predicate<T> validator, final T... values) {
		return noneMatch(validator.negate(), values);
	}

	/**
	 * Returns true if at least one value passes the given validator.
	 */
	public static <T> boolean anyMatch(final Predicate<T> validator, final T... values) {
		for (final T t : values) {
			if (validator.test(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if at least one value fails the given validator.
	 */
	public static <T> boolean anyFail(final Predicate<T> validator, final T... values) {
		return anyMatch(validator.negate(), values);
	}

	/**
	 * Tests whether or not something is either null or contains no content at all.
	 */
	public static <T> boolean isEmpty(final T t) {
		if (t == null) {
			return true;
		} else if (t instanceof String) {
			return ((String) t).isEmpty();
		} else if (t instanceof Collection) {
			return ((Collection) t).isEmpty();
		} else if (t.getClass().isArray()) {
			return Array.getLength(t) == 0;
		}
		return false; // anything unknown but not null is not empty
	}

	public static <T> boolean isNotEmpty(final T t) {
		return !isEmpty(t);
	}

	/**
	 * Tests whether or not something is either null or contains no relevant content.
	 * <p>
	 * A String is blank if it contains nothing or only whitespace.<br>
	 * A Colection is blank if it contains nothing or only blank elements.<br>
	 * An Array is blank if it contains nothing, or - in case of an array of Objects - all objects contained are blank. Arrays of primitive data types do not
	 * check individual elements, as there is no definition what a blank value is for e.g. a float.
	 */
	public static <T> boolean isBlank(final T t) {
		if (t == null) {
			return true;
		} else if (t instanceof String) {
			final String s = ((String) t);
			return s.isEmpty() || s.chars().allMatch(Character::isWhitespace);
		} else if (t instanceof Collection) {
			final Collection c = (Collection) t;
			return c.isEmpty() || c.stream().allMatch(Util::isBlank);
		} else if (t.getClass().isArray()) {
			if (t instanceof Object[]) {
				final Object[] a = (Object[]) t;
				return (a.length == 0) || Arrays.stream(a).allMatch(Util::isBlank);
			} else {
				return Array.getLength(t) == 0;
			}
		}
		return false; // anything unknown but not null is not empty
	}

	public static <T> boolean isNotBlank(final T t) {
		return !isBlank(t);
	}

	/**
	 * Creates a new predicate that is equivalent to AND together the given predicates.
	 */
	public static <T> Predicate<T> AND(final Predicate<T>... predicates) {
		return (T t) -> {
			for (final Predicate<T> p : predicates) {
				if (!p.test(t)) {
					return false;
				}
			}
			return true;
		};
	}

	/**
	 * Creates a new predicate that is equivalent to OR together the given predicates.
	 */
	public static <T> Predicate<T> OR(final Predicate<T>... predicates) {
		return (T t) -> {
			for (final Predicate<T> p : predicates) {
				if (p.test(t)) {
					return true;
				}
			}
			return false;
		};
	}

	/**
	 * Functional convenience function to return the given value, or a default value if it is null.
	 */
	public static <T> T getOrDefault(final T optionalValue, final Supplier<T> generator) {
		return optionalValue == null ? generator.get() : optionalValue;
	}

	/**
	 * Functional convenience function to return the given value, or a default value if it is null. Compatible with methods that might throw an exception.
	 */
	public static <T, E extends Throwable> T getOrDefaultExceptional(final T optionalValue, final ThrowingSupplier<T, E> generator) throws E {
		return optionalValue == null ? generator.get() : optionalValue;
	}

	/**
	 * Functional convenience function to map the given value if present, or otherwise return null.
	 */
	public static <T, R> R mapIfPresent(final T optionalValue, final Function<T, R> mapper) {
		return optionalValue == null ? null : mapper.apply(optionalValue);
	}

	/**
	 * Functional convenience function to map the given value if present, or otherwise return null. Compatible with methods that might throw an exception.
	 */
	public static <T, R, E extends Throwable> R mapIfPresentExceptional(final T optionalValue, final ThrowingFunction<T, R, E> mapper) throws E {
		return optionalValue == null ? null : mapper.apply(optionalValue);
	}

	/**
	 * Joins all non-blank items together.
	 */
	public static String joinNonBlank(final String delimiter, final String... parts) {
		return Arrays.stream(parts)
						.filter(Util::isNotBlank)
						.collect(Collectors.joining(delimiter));
	}

	/**
	 * Gets the current stack trace as a string for debugging.
	 */
	public static String getStackTrace() {
		return "\tat " + Arrays.stream(Thread.currentThread().getStackTrace())
						.skip(2) // this and getStackTrace()
						.map(StackTraceElement::toString)
						.collect(Collectors.joining("\n\tat "));
	}

	/**
	 * Convenience function to map a whole collection with a given mapper and returns the result in a new list.
	 */
	public static <T, R> List<R> mapList(final Collection<T> collection, final Function<T, R> mapper) {
		return collection.stream().map(mapper).collect(Collectors.toList());
	}

	/**
	 * Convenience function to map a whole collection with a given mapper and returns the result in a new set.
	 */
	public static <T, R> Set<R> mapSet(final Collection<T> collection, final Function<T, R> mapper) {
		return collection.stream().map(mapper).collect(Collectors.toSet());
	}

	/**
	 * Converts a LocalDate into a Date by applying the systems default timezone.
	 */
	public static Date toDate(final LocalDate ld) {
		return Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts a Date into a LocalDate by applying the systems default timezone.
	 * <p>
	 * Safely detects sql Dates that somehow forgot to implement toInstant.
	 */
	public static LocalDate toLocalDate(final Date d) {
		if (d instanceof java.sql.Date) {
			return ((java.sql.Date) d).toLocalDate(); // toInstant is not defined on SQL Date
		} else {
			return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
	}

	/**
	 * Returns the 1st day at 0:00 of the month of a given date.
	 */
	public static Date startOfMonth(final Date inputDate) {
		return Date.from(toLocalDate(inputDate)
						.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Creates the inverse of a Map, as in: returns a map where all values point to the corresponding keys that link to them in the original map.
	 */
	public static <V, K> Map<V, List<K>> inverseMap(final Map<K, V> map) {
		return map.entrySet().stream()
						.collect(Collectors.groupingBy(
										Map.Entry::getValue,
										Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
	}

	/**
	 * Convenience function to group a list of items by a specific function and return a map.
	 *
	 * @param <K> any
	 * @param <V> any
	 * @param list list of items to group
	 * @param keyMapper a method that determines which key shall be associated with a given list item
	 * @return the grouped map
	 */
	public static <K, V> Map<K, List<V>> groupList(final List<V> list, final Function<V, K> keyMapper) {
		return list.stream().collect(Collectors.groupingBy(keyMapper, Collectors.toList()));
	}

	/**
	 * Convenience function to group a list of items by a specific function and return a map. In addition the individual items in each list are sorted by the
	 * given comparator.
	 *
	 * @param <K> any
	 * @param <V> any
	 * @param list list of items to group
	 * @param keyMapper a method that determines which key shall be associated with a given list item
	 * @param comparator comparator for sorting
	 * @return the grouped map with sorted list members
	 */
	public static <K, V> Map<K, NavigableSet<V>> groupListSorted(final List<V> list, final Function<V, K> keyMapper,
					final Comparator<V> comparator) {
		return list.stream().collect(
						Collectors.groupingBy(keyMapper,
										Collectors.toCollection(() -> new TreeSet<V>(comparator))));
	}

	/**
	 * Creates a comparator that sorts data based on their associated dates ascending.
	 *
	 * @param <T> any
	 * @param dateGetFunc function that retrieves the dates associated with a given data element
	 * @return a comparator that sorts the data by date ascending.
	 */
	public static <T> Comparator<T> compareDatesASC(final Function<T, Date> dateGetFunc) {
		return Comparator.comparing(dateGetFunc, (d1, d2) -> {
			return d1.before(d2) ? -1 : (d1.equals(d2) ? 0 : 1);
		});
	}

	/**
	 * Creates a comparator that sorts data based on their associated dates descending.
	 *
	 * @param <T> any
	 * @param dateGetFunc function that retrieves the dates associated with a given data element
	 * @return a comparator that sorts the data by date descending.
	 */
	public static <T> Comparator<T> compareDatesDESC(final Function<T, Date> dateGetFunc) {
		return Comparator.comparing(dateGetFunc, (d1, d2) -> {
			return d2.before(d1) ? -1 : (d1.equals(d2) ? 0 : 1);
		});
	}

	/**
	 * Creates a list of all classes in a given package using the current ClassLoader.
	 */
	public static List<Class<?>> getAllClassesFromPackage(final String sourcePackage) throws IOException {
		final String SUFFIX_CLASS = ".class";

		try {
			final List<Class<?>> result = new ArrayList<>();
			final String packageName = sourcePackage.replace(".", "/");

			for (final Enumeration<URL> resources = Thread.currentThread().getContextClassLoader()
							.getResources(packageName); resources.hasMoreElements();) {
				final List<Class<?>> newClasses = Files.walk(Paths.get(resources.nextElement().toURI()))
								.filter(f -> Files.isRegularFile(f) && f.toString().endsWith(SUFFIX_CLASS))
								.map(f -> {
									final String filename = f.getFileName().toString();
									final String fullClassName = sourcePackage + '.'
													+ filename.substring(0, filename.length() - SUFFIX_CLASS.length());
									try {
										return Class.forName(fullClassName);
									} catch (ClassNotFoundException e) {
										return null;
									}
								})
								.filter(Objects::nonNull)
								.collect(Collectors.toList());
				result.addAll(newClasses);
			}
			return result;
		} catch (final URISyntaxException e) {
			throw new IllegalStateException(e); // impossible to reach
		}
	}

	/**
	 * Do not instantiate.
	 */
	private Util() {
	}
}
