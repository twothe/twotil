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
 * Collection of various utility functions that fit nowhere else.
 */
public class Util {

        /**
         * Returns the first element matching the validator.
         *
         * @param validator predicate to test each value
         * @param values    values to examine
         * @return first matching value or {@code null} if none match
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
         * Returns the first element matching the validator or throws the supplied exception if none match.
         *
         * @param validator predicate to test each value
         * @param throwable supplier for the exception to throw when no match is found
         * @param values    values to examine
         * @return first matching value
         * @throws E when no value matches
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
         * Returns the first non-{@code null} element.
         */
        public static <T> T firstNonNull(final T... values) {
                return first(Objects::nonNull, values);
        }

        /**
         * Returns the first element considered not empty via {@link #isNotEmpty(Object)}.
         */
        public static <T> T firstNonEmpty(final T... values) {
                return first(Util::isNotEmpty, values);
        }

        /**
         * Returns the first element considered not blank via {@link #isNotBlank(Object)}.
         */
        public static <T> T firstNonBlank(final T... values) {
                return first(Util::isNotBlank, values);
        }

        /**
         * Returns {@code true} if none of the values satisfy the validator.
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
         * Returns {@code true} if all values satisfy the validator.
         */
        public static <T> boolean allMatch(final Predicate<T> validator, final T... values) {
                return noneMatch(validator.negate(), values);
        }

        /**
         * Returns {@code true} if at least one value satisfies the validator.
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
         * Returns {@code true} if at least one value does not satisfy the validator.
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

        /**
         * Negation of {@link #isEmpty(Object)}.
         *
         * @param t value to check
         * @return {@code true} if {@code t} is not empty
         */
        public static <T> boolean isNotEmpty(final T t) {
                return !isEmpty(t);
        }

	/**
	 * Tests whether or not something is either null or contains no relevant content.
	 * <p>
	 * A String is blank if it contains nothing or only whitespace.<br>
	 * A Collection is blank if it contains nothing or only blank elements.<br>
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

        /**
         * Negation of {@link #isBlank(Object)}.
         *
         * @param t value to check
         * @return {@code true} if {@code t} is not blank
         */
        public static <T> boolean isNotBlank(final T t) {
                return !isBlank(t);
        }

        /**
         * Builds a predicate that returns {@code true} only when all predicates evaluate to {@code true}.
         *
         * @param predicates predicates to combine
         * @return conjunction of the predicates
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
         * Builds a predicate that returns {@code true} when any predicate evaluates to {@code true}.
         *
         * @param predicates predicates to combine
         * @return disjunction of the predicates
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
         * Returns {@code optionalValue} or generates a fallback when it is {@code null}.
         *
         * @param optionalValue value to check
         * @param generator     supplier for the default value
         * @return {@code optionalValue} or the generated default
         */
        public static <T> T getOrDefault(final T optionalValue, final Supplier<T> generator) {
                return optionalValue == null ? generator.get() : optionalValue;
        }

        /**
         * Variant of {@link #getOrDefault(Object, Supplier)} for suppliers that may throw an exception.
         *
         * @param optionalValue value to check
         * @param generator     supplier for the default value
         * @param <T>           result type
         * @param <E>           exception type
         * @return {@code optionalValue} or the generated default
         * @throws E if the generator throws
         */
        public static <T, E extends Throwable> T getOrDefaultExceptional(final T optionalValue, final ThrowingSupplier<T, E> generator) throws E {
                return optionalValue == null ? generator.get() : optionalValue;
        }

        /**
         * Applies the mapper when the value is present.
         *
         * @param optionalValue nullable input value
         * @param mapper        mapping function
         * @return mapped result or {@code null} if value was {@code null}
         */
        public static <T, R> R mapIfPresent(final T optionalValue, final Function<T, R> mapper) {
                return optionalValue == null ? null : mapper.apply(optionalValue);
        }

        /**
         * Variant of {@link #mapIfPresent(Object, Function)} for mappers that may throw an exception.
         *
         * @param optionalValue nullable input value
         * @param mapper        mapping function
         * @param <T>           input type
         * @param <R>           result type
         * @param <E>           exception type
         * @return mapped result or {@code null} if value was {@code null}
         * @throws E if the mapper throws
         */
        public static <T, R, E extends Throwable> R mapIfPresentExceptional(final T optionalValue, final ThrowingFunction<T, R, E> mapper) throws E {
                return optionalValue == null ? null : mapper.apply(optionalValue);
        }

        /**
         * Concatenates all non-blank strings using the given delimiter.
         *
         * @param delimiter delimiter string
         * @param parts     strings to join
         * @return concatenated result
         */
        public static String joinNonBlank(final String delimiter, final String... parts) {
                return Arrays.stream(parts)
                                                .filter(Util::isNotBlank)
                                                .collect(Collectors.joining(delimiter));
        }

        /**
         * Returns the current stack trace as a single string.
         */
        public static String getStackTrace() {
                return "\tat " + Arrays.stream(Thread.currentThread().getStackTrace())
                                                .skip(2) // this and getStackTrace()
                                                .map(StackTraceElement::toString)
                                                .collect(Collectors.joining("\n\tat "));
        }

        /**
         * Maps each element of the collection and returns the results in a new list.
         *
         * @param collection source elements
         * @param mapper     mapping function
         * @return list of mapped results
         */
        public static <T, R> List<R> mapList(final Collection<T> collection, final Function<T, R> mapper) {
                return collection.stream().map(mapper).collect(Collectors.toList());
        }

        /**
         * Maps each element of the collection and returns the results in a new set.
         *
         * @param collection source elements
         * @param mapper     mapping function
         * @return set of mapped results
         */
        public static <T, R> Set<R> mapSet(final Collection<T> collection, final Function<T, R> mapper) {
                return collection.stream().map(mapper).collect(Collectors.toSet());
        }

        /**
         * Converts a {@link LocalDate} to a {@link Date} using the system time zone.
         *
         * @param ld local date value
         * @return corresponding {@link Date}
         */
        public static Date toDate(final LocalDate ld) {
                return Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }

        /**
         * Converts a {@link Date} to a {@link LocalDate} using the system time zone.
         * Handles {@link java.sql.Date} which lacks {@code toInstant()}.
         *
         * @param d date value
         * @return corresponding {@link LocalDate}
         */
        public static LocalDate toLocalDate(final Date d) {
                if (d instanceof java.sql.Date) {
                        return ((java.sql.Date) d).toLocalDate(); // toInstant is not defined on SQL Date
                } else {
                        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }
        }

        /**
         * Returns the first day of the month at midnight for the given date.
         *
         * @param inputDate source date
         * @return start of month as {@link Date}
         */
        public static Date startOfMonth(final Date inputDate) {
                return Date.from(toLocalDate(inputDate)
                                                .withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        /**
         * Returns a map grouping keys by their values from the input map.
         *
         * @param map source map
         * @param <V> value type
         * @param <K> key type
         * @return inverse multi-map view
         */
        public static <V, K> Map<V, List<K>> inverseMap(final Map<K, V> map) {
                return map.entrySet().stream()
                                                .collect(Collectors.groupingBy(
                                                                               Map.Entry::getValue,
                                                                               Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        }

        /**
         * Groups elements of a list by the supplied key mapper.
         *
         * @param list      items to group
         * @param keyMapper function deriving the group key
         * @param <K>       key type
         * @param <V>       value type
         * @return map of grouped elements
         */
        public static <K, V> Map<K, List<V>> groupList(final List<V> list, final Function<V, K> keyMapper) {
                return list.stream().collect(Collectors.groupingBy(keyMapper, Collectors.toList()));
        }

        /**
         * Groups elements of a list and sorts each group using the provided comparator.
         *
         * @param list       items to group
         * @param keyMapper  function deriving the group key
         * @param comparator comparator for sorting each group
         * @param <K>        key type
         * @param <V>        value type
         * @return map with sorted group members
         */
        public static <K, V> Map<K, NavigableSet<V>> groupListSorted(final List<V> list, final Function<V, K> keyMapper,
                                        final Comparator<V> comparator) {
                return list.stream().collect(
                                                Collectors.groupingBy(keyMapper,
                                                                               Collectors.toCollection(() -> new TreeSet<V>(comparator))));
        }

        /**
         * Returns a comparator that orders elements by the extracted date in ascending order.
         *
         * @param dateGetFunc extractor for the element's date
         * @param <T>        element type
         * @return comparator for ascending dates
         */
        public static <T> Comparator<T> compareDatesASC(final Function<T, Date> dateGetFunc) {
                return Comparator.comparing(dateGetFunc, (d1, d2) -> {
                        return d1.before(d2) ? -1 : (d1.equals(d2) ? 0 : 1);
                });
        }

        /**
         * Returns a comparator that orders elements by the extracted date in descending order.
         *
         * @param dateGetFunc extractor for the element's date
         * @param <T>        element type
         * @return comparator for descending dates
         */
        public static <T> Comparator<T> compareDatesDESC(final Function<T, Date> dateGetFunc) {
                return Comparator.comparing(dateGetFunc, (d1, d2) -> {
                        return d2.before(d1) ? -1 : (d1.equals(d2) ? 0 : 1);
                });
        }

        /**
         * Lists all classes in the specified package using the context class loader.
         *
         * @param sourcePackage dot-separated package name
         * @return list of discovered classes
         * @throws IOException on resource access errors
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
