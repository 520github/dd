package me.twocoffee.rest.generic;

import java.util.List;

public class PartialList<E> {

	private final boolean lastPage;
	private final List<E> result;
	private long total;

	public PartialList(List<E> result, boolean lastPage) {
		this.result = result;
		this.lastPage = lastPage;
	}

	public PartialList(List<E> result, boolean lastPage, long total) {
		this.result = result;
		this.lastPage = lastPage;
		this.total = total;
	}

	public List<E> getResult() {
		return result;
	}

	public long getTotal() {
		return total;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public PartialList<E> setTotal(long total) {
		this.total = total;
		return this;
	}

}
