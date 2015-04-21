package com.dblp.communities.datastructure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMinPQ <Key extends Comparable<Key>> implements Iterable<Integer> {

	private int numKeys;
	private int[] pq;
	private int[] qp;
	private Key[] keys;
	private Comparator<Key> comparator;
	
	/**
	 * Initializes an empty indexed priority queue with indices between 0 and max-1.
	 * 
	 * @param max
	 */
	@SuppressWarnings("unchecked")
	public IndexMinPQ(int max) {
		keys = (Key[]) new Comparable[max+1];
		pq = new int[max + 1];
		qp = new int[max + 1];
		for (int i = 0; i <= max; ++i)
			qp[i] = -1;
	}
	
	/**
	 * Initializes an empty indexed priority queue with indices between 0 and max-1.
	 * 
	 * @param max
	 */
	@SuppressWarnings("unchecked")
	public IndexMinPQ(int max, Comparator<Key> comparator) {
		keys = (Key[]) new Comparable[max+1];
		pq = new int[max + 1];
		qp = new int[max + 1];
		for (int i = 0; i <= max; ++i)
			qp[i] = -1;
		this.comparator = comparator;
	}
	
	public void print() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < pq.length; ++i) {
			b.append("pq[" + i + "] = " + pq[i] + "\n");
		}
		for (int i = 0; i < qp.length; ++i) {
			b.append("qp[" + i + "] = " + qp[i] + "\n");
		}
		for (int i = 0; i < qp.length; ++i) {
			b.append("keys[" + i + "] = " + keys[i] + "\n");
		}
		System.out.println(b);
	}
	
	/**
	 * Is the priority queue empty.
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
	 * Returns an index associated with a minimum key.
	 * 
	 * Running time: O(1)
	 * 
	 * @return an index associated with a minimum key.
	 * @throws NoSuchElementException if the priority queue is empty.
	 */
	public int minIndex() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		return pq[1];
	}
	
	/**
	 * Return a minimum key.
	 * 
	 * Running time: O(1)
	 * 
	 * @return a minimum key.
	 * @throws NoSuchElementException if the priority queue is empty
	 */
	public Key minKey() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		return keys[pq[1]];
	}
	
	/**
	 * Removes a minimum key and returns its associated index.
	 * 
	 * Running time: O(log n)
	 * 
	 * @return an index associated with a minimum key.
	 * @throws NoSuchElementException if the priority queue is empty
	 */
	public Pair<Key> deleteMin() {
		if (numKeys == 0) {
			throw new NoSuchElementException("Priority queue underflow");
		}
		
		int minIndex = pq[1];
		Key minKey = keys[minIndex];
        Pair<Key> min = new Pair<Key>(minKey,minIndex);
        
        exchange(1, numKeys--);
        sink(1);
        
        keys[pq[numKeys+1]] = null;
        pq[numKeys+1] = -1;       // avoid loitering and help with garbage collection
		qp[minIndex] = -1;
		
		return min;
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
			System.out.println("no index = " + i);
			return;
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
			return;
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
	
	private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exchange(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= numKeys) {
            int j = 2*k;
            if (j < numKeys && greater(j, j+1)) 
            	j++;
            if (!greater(k, j)) 
            	break;
            exchange(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
    	return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
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

    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..N] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > numKeys) 
        	return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= numKeys && greater(k, left))  
        	return false;
        if (right <= numKeys && greater(k, right)) 
        	return false;
        
        return isMinHeap(left) && isMinHeap(right);
    }
	
    public Iterator<Integer> iterator() { 
    	return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private IndexMinPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) 
            	copy = new IndexMinPQ<Key>(size());
            else                   
            	copy = new IndexMinPQ<Key>(size(), comparator);
            
            for (int i = 1; i <= numKeys; ++i) {
				copy.insert(pq[i], keys[pq[i]]);
			}
        }

        public boolean hasNext()  { 
        	return !copy.isEmpty();
        }
        
        public void remove() { 
        	throw new UnsupportedOperationException();  
        }

        public Integer next() {
            if (!hasNext()) 
            	throw new NoSuchElementException();
            return copy.deleteMin().index();
        }
    }

}