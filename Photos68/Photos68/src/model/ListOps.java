package model;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class contains a number of methods for dealing with lists
 * @author Joel Kurian
 * @author Jinseong Lee
 *
 */
public abstract class ListOps {
	
	/**
	 * Adds an item to the list in order
	 * @param list the list to be added to
	 * @param item the item to add
	 * @param <T> type of list
	 * @return true if added, false if not
	 */
	public static <T extends Comparable<T>> boolean addToList(List<T> list, T item) {
		ListIterator<T> it = list.listIterator();
		
		while(true) {
			if (!it.hasNext()) {
				it.add(item);
				return true;
			} else {
				T element = it.next();
				if (element.compareTo(item) == 0) {
					return false;
				} else if (element.compareTo(item) > 0) {
					it.previous();
					it.add(item);
					return true;
				}
			}
		}
	}

	/**
	 * deletes an item from a list
	 * @param list the list to be traversed
	 * @param item object to test for the item
	 * @param pred predicate for choosing the item to delete
	 * @param <T> type of list
	 * @param <U> type of item
	 * @return the item deleted from the list
	 */
	public static <T extends Comparable<T>, U> T deleteItem(List<T> list, U item, BiPredicate<T,U> pred) {
		ListIterator<T> it = list.listIterator();
		while (it.hasNext()) {
			T curr = it.next();
			if (pred.test(curr, item)) {
				it.remove();
				return curr;
			}
		}
		return null;
	}
	
	/**
	 * filters a list to elements matching the predicate
	 * @param col list to be filtered
	 * @param item element to test against
	 * @param pred predicate to filter by
	 * @param <T> type of list
	 * @param <U> type of item
	 * @return list after being filtered
	 */
	public static <T,U> List<T> filterList(Collection<T> col, U item, BiPredicate<T,U> pred) {
		return col.stream().filter(t -> pred.test(t, item)).collect(Collectors.toList());
	}
	
	/**
	 * Finds an element matching to the predicate in list
	 * @param list the list to be searched through
	 * @param item the item to be tested against
	 * @param pred the predicate to test
	 * @param <T> type of list
	 * @param <U> type of item
	 * @return the first element to match against the predicate
	 */
	public static <T extends Comparable<T>, U> T getItem(List<T> list, U item, BiPredicate<T,U> pred) {
		return list.stream().filter(t -> pred.test(t, item)).findFirst().orElse(null);
	}
	
	/**
	 * tests if all items in a list match with the comparisons
	 * @param list the list of items
	 * @param comparison list of items to compare against
	 * @param pred predicate to test comparisons
	 * @param <T> type of list
	 * @return true if all comparisons are matched
	 */
	public static <T extends Comparable<T>> boolean search(List<T> list, List<T> comparison, BiPredicate<T,T> pred) {
		for (T item: comparison) {
			boolean match = list.stream().anyMatch(t -> pred.test(t, item));
			if (!match) {
				return false;
			}
		}
		return true;
	}
}
