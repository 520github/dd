package me.twocoffee.common.search;

import java.util.List;

/**
 * 搜索结果
 * 
 * @author drizzt
 * 
 */
public class PagedResult<T> {
	// 是否最后一页
	private boolean lastPage = false;

	/**
	 * 查询到的结果
	 */
	private List<T> result;

	/**
	 * 查询到的总记录数
	 */
	private long total;

	public List<T> getResult() {
		return result;
	}

	public long getTotal() {
		return total;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public void setLastPage(int limit, int offset) {
		if (result == null || result.size() < 1) {
			this.lastPage = true;
		} else if (limit > result.size()) {
			this.lastPage = true;
		} else if (limit + offset >= total) {
			this.lastPage = true;
		}

	}

	public void setLastPage(int limit, int offset, long total) {
		lastPage = result == null || result.size() < limit
				|| total <= limit + offset;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
