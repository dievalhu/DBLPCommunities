package com.dblp.communities.datastructure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMaxPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	private int numKeys;
	private int[] pq;
	private int[] qp;
	private Key[] keys;
	
	/**
	 * Initializes an empty indexed priority queue with indices between 0 and max-1.
	 * 
	 * @param max
	 */
	@SuppressWarnings("unchecked")
	public IndexMaxPQ(int max) {
		keys = (Key[]) new Comparable[max+1];
		pq = new int[max + 1];
		qp = new int[max + 1];
		for (int i = 0; i <= max; ++i)
			qp[i] = -1;
	}
	
	/**
	 * Is the priority queue empty?
	 * 
	 * @return true if and only if the priority queue is empty.
	 */
	public boolean isEmpty() {
		return numKeys == 0;
	}
	
	/**
	 * Is i an index on the priority queue?
	 * 
	 * Running time: O(1)
	 * 
	 * @param i an index
	 * @return true if and only if i is an index on the priority queue.
	 */
	public boolean contains(int i) {
		return qp[i] != -1;
	}
	
	/**
	 * Returns the number of keys on the priority queue.
	 * 
	 * Running time: O(1)
	 * 
	 * @return
	 */
	public int size() {
		return numKeys;
	}
	
	/**
	 * Associate key with index i. If index i is already associated with
	 * a key, then 'key' will replace the old key.
	 * 
	 * Running time: O(log n)
	 * 
	 * @param i an index
	 * @param key the key to associate with index i
	 */
	public void insert(int i, Key key) {
		if (contains(i)) {			
			if (key.compareTo(keys[i]) > 0)
				increaseKey(i, key);
			else if (key.compareTo(keys[i]) < 0)
				decreaseKey(i, key);
			
			return;
		}
		
		++numKeys;
		qp[i] = numKeys;
		pq[numKeys] = i;
		keys[i] = key;
		swim(numKeys);
	}
	
	/**
	 * Returns an index associated with a maximum key.
	 * 
	 * Running time: O(1)
	 * 
	 * @return an index associated with a maximum key.
	 * @throws NoSuchElementException if the priority queue is empty.
	 */
	public int maxIndex() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		return pq[1];
	}
	
	/**
	 * Return a maximum key.
	 * 
	 * Running time: O(1)
	 * 
	 * @return a maximum key.
	 * @throws NoSuchElementException if the priority queue is empty
	 */
	public Key maxKey() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		return keys[pq[1]];
	}
	
	/**
	 * Removes a maximum key and returns its associated index.
	 * 
	 * Running time: O(log n)
	 * 
	 * @return an index associated with a maximum key.
	 * @throws NoSuchElementException if the priority queue is empty
	 */
	public Pair<Key> deleteMax() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		int maxIndex = pq[1];
		Key maxKey = keys[maxIndex];
		Pair<Key> max = new Pair<Key>(maxKey,maxIndex);
		
		exchange(1, numKeys--);
		sink(1);
		qp[maxIndex] = -1; // delete
		keys[pq[numKeys+1]] = null; // to help with garbage collection
		pq[numKeys + 1] = -1; // not needed
		
		return max;
	}
	
	/**
	 * Returns the key associated with index i.
	 * 
	 * Running time: O(1)
	 * 
	 * @param i the index of the key to return
	 * @return the key associated with index i
	 * @throws NoSuchElementException if no key is associated with index i 
	 */
	public Key keyOf(int i) {
		if (!contains(i)) {
			return null;
		}
		
		return keys[i];
	}
	
	/**
	 * Change the key associated with index i to the specified value.
	 * 
	 * Running time: 2 * O(log n)
	 * 
	 * @param i the index of the key to change.
	 * @param key change the key associated with index i to this key.
	 * @throws NoSuchElementException if no key is associated with index i 
	 */
	public void changeKey(int i, Key key) {
		if (!contains(i)) {
			throw new NoSuchElementException("index is not in the priority queue");
		}
		
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}
	
	/**
	 * Increase the key associated with index i to the specified value.
	 * 
	 * Running time: O(log n)
	 * 
	 * @param i the index of the key to increase
	 * @param key increase the key associated with index i to this key
	 * @throws NoSuchElementException if no key is associated with index i
	 * @throws IllegalArgumentException if key is less than or equal to the existing key associated with index i.
	 */
	public void increaseKey(int i, Key key) {
		if (!contains(i)) {
			throw new NoSuchElementException("index is not in the priority queue");
		}
		
		if (keys[i].compareTo(key) >= 0) {
			return;
		}
		
		keys[i] = key;
		swim(qp[i]);
	}
	
	/**
	 * Decrease the key associated with index i to the specified value.
	 * 
	 * Running time: O(log n)
	 * 
	 * @param i the index of the key to increase.
	 * @param key decrease the key associated with index i to this key.
	 * @throws NoSuchElementException if no key is associated with index i
	 * @throws IllegalArgumentException if key is greater than or equal to the existing key associated with index i.
	 */
	public void decreaseKey(int i, Key key) {
		if (!contains(i)) {
			throw new NoSuchElementException("index is not in the priority queue");
		}
		
		if (keys[i].compareTo(key) <= 0) {
			System.out.println("decreaseKey");
			System.out.println("  prev: " + keys[i].toString());
			System.out.println("  new: " + key.toString());
			throw new IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key");
		}
		
		keys[i] = key;
		sink(qp[i]);
	}
	
	/**
	 * Remove the key associated with index i.
	 * 
	 * Running time: 2 * O(log n)
	 * 
	 * @param i the index of the key to remove.
	 * @throws NoSuchElementException if no key is associated with index i
	 */
	public boolean delete(int i) {
		if (!contains(i))
			return false;
		
		int index = qp[i];
		exchange(index, numKeys--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
		
		return true;
	}
	
	private boolean less(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
	}
	
	/**
	 * Exchange places of elements i and j.
	 * 
	 * Running time: O(1)
	 * 
	 * @param i
	 * @param j
	 */
	private void exchange(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}
	
	/**
	 * 
	 * Running time: O(log n)
	 * 
	 * @param k
	 */
	private void swim(int k) {
		while (k > 1 && less(k/2,k)) {
			exchange(k, k/2);
			k = k/2;
		}
	}
	
	/**
	 * 
	 * Running time: O(log n)
	 * 
	 * @param k
	 */
	private void sink(int k) {
		while (2*k <= numKeys) {
			int j = 2*k;
			
			if (j < numKeys && less(j, j+1))
				j++;
			
			if (!less(k,j))
				break;
			
			exchange(k,j);
			
			k = j;
		}
	}
	
	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}
	
	private class HeapIterator implements Iterator<Integer> {
		private IndexMaxPQ<Key> copy; // create a new priority queue
		
		/**
		 * Copy the heap in linear time since it is already in heap order.
		 */
		public HeapIterator() {
			copy = new IndexMaxPQ<Key>(pq.length -1);
			for (int i = 1; i <= numKeys; ++i) {
				copy.insert(pq[i], keys[pq[i]]);
			}
		}

		public boolean hasNext() {
			return !copy.isEmpty();
		}

		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			return copy.deleteMax().index();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
