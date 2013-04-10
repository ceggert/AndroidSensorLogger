package com.guseggert.sensorlogger.feature;

import java.util.Iterator;
import java.util.NoSuchElementException;

import android.util.SparseArray;

// SparseArray has no iterator, so this is a bare bones implementation
public class SparseArrayIterable<T> implements Iterable<T> {
	private SparseArray<T> mSparseArray;
	private int mIndex = 0;
	
	public SparseArrayIterable() {
		mSparseArray = new SparseArray<T>();
	}
	
	public SparseArrayIterable(SparseArray<T> sa) {
		mSparseArray = sa;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return mIndex < mSparseArray.size();
			}

			@Override
			public T next() {
				if (mIndex == mSparseArray.size()) throw new NoSuchElementException();
				int key = mSparseArray.keyAt(++mIndex);
				return mSparseArray.get(key);
			}

			@Override
			public void remove() {
				mSparseArray.delete(mSparseArray.keyAt(mIndex-1));
			}
			
		};
	}
	
	public void append(int key, T value) {
		mSparseArray.append(key, value);
	}
	
	public T get(int key) {
		return mSparseArray.get(key);
	}
	
	public int size() {
		return mSparseArray.size();
	}
}
