package me.twocoffee.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.entity.Repository;
import me.twocoffee.service.entity.ContentDetail;

public class BinarySearch<T extends Comparable<T>> {
	private List<T> dataList;
	private int low;
	private int high;

	public BinarySearch(List<T> dataList) {
		this.dataList = dataList;
	}

	public int search(T key) {
		int mid;

		if (dataList == null || dataList.size() <= 0)
			return -1;

		low = 0;
		high = dataList.size() - 1;

		while (low <= high) {
			mid = (low + high) / 2;
			System.out
					.println("mid " + mid + " mid value:" + dataList.get(mid));

			if (key.compareTo(dataList.get(mid)) < 0) {
				high = mid - 1;
			} else if (key.compareTo(dataList.get(mid)) > 0) {
				low = mid + 1;
			} else if (key.compareTo(dataList.get(mid)) == 0) {
				return mid;
			}
		}

		return -1;
	}

	private int doSearchRecursively(int low, int high, T key) {
		int mid;
		int result;

		if (low <= high) {
			mid = (low + high) / 2;
			result = key.compareTo(dataList.get(mid));
			System.out
					.println("mid " + mid + " mid value:" + dataList.get(mid));

			if (result < 0) {
				return doSearchRecursively(low, mid - 1, key);
			} else if (result > 0) {
				return doSearchRecursively(mid + 1, high, key);
			} else if (result == 0) {
				return mid;
			}
		}

		return -1;
	}

	public int searchRecursively(T key) {
		if (dataList == null || dataList.size() <= 0)
			return -1;

		return doSearchRecursively(0, dataList.size() - 1, key);
	}

	public static void main(String[] args) {
		ArrayList<ContentDetail> data = new ArrayList<ContentDetail>();
		ContentDetail a1 = new ContentDetail();
		Repository b1 = new Repository();
		a1.setRepository(b1);
		a1.getRepository().setLastModified(new Date(100));
		System.out.println(a1.getRepository().getLastModified().getTime());
		ContentDetail a2 = new ContentDetail();
		Repository b2 = new Repository();
		a2.setRepository(b2);
		a2.getRepository().setLastModified(new Date(10000));
		System.out.println(a2.getRepository().getLastModified().getTime());
		ContentDetail a3 = new ContentDetail();
		Repository b3 = new Repository();
		a3.setRepository(b3);
		a3.getRepository().setLastModified(new Date(1000000000));
		System.out.println(a3.getRepository().getLastModified().getTime());
		data.add(a1);
		data.add(a2);
		data.add(a3);

		ContentDetail a4 = new ContentDetail();
		Repository b4 = new Repository();
		a4.setRepository(b4);
		a4.getRepository().setLastModified(new Date(1000000000));

		BinarySearch<ContentDetail> binSearch = new BinarySearch<ContentDetail>(
				data);
		System.out.println("Key index:" + binSearch.search(a4));
		System.out.println(binSearch.getHigh());
		System.out.println(binSearch.getLow());

		// System.out.println("Key index:" + binSearch.searchRecursively(5));

		// String [] dataStr = {"A" ,"C" ,"F" ,"J" ,"L" ,"N" ,"T"};
		// BinarySearch<String> binSearch = new BinarySearch<String>(dataStr);
		// System.out.println("Key index:" + binSearch.search("A") );
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}
}
