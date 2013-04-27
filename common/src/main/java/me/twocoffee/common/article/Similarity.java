package me.twocoffee.common.article;

/**
 * 字串相似度
 * 
 * @author fan
 * 
 */
public class Similarity {
	/**
	 * 计算strA和strB的相似度
	 * 
	 * @param strA
	 * @param strB
	 * @return 0-1之间，越大相似度越高
	 */
	public static double compute(String strA, String strB) {
		// long start = System.currentTimeMillis();
		int a = strA.length();
		int b = strB.length();
		Similarity distance = new Similarity();
		double l = distance.LD(strA, strB);
		// long end = System.currentTimeMillis();
		/*
		 * System.out.print("a的长度："+a); System.out.println(",b的长度："+b);
		 * System.out.println("距离："+l);
		 */
		double s = 1 - (a > b ? (l / a) : (l / b));
		/*
		 * System.out.println("相似度:"+s); System.out.println();
		 * System.out.println("用时："+(end-start));
		 */
		return s;
	}

	public static void main(String args[]) {
		String strA = "";
		String strB = "";
		long start = System.currentTimeMillis();
		int a = strA.length();
		int b = strB.length();
		Similarity distance = new Similarity();
		double l = distance.LD(strA, strB);
		long end = System.currentTimeMillis();
		System.out.print("a的长度：" + a);
		System.out.println(",b的长度：" + b);
		System.out.println("距离：" + l);
		double s = 1 - (a > b ? (l / a) : (l / b));
		System.out.println("相似度:" + s);
		System.out.println();
		System.out.println("用时：" + (end - start));
	}

	// *****************************
	// 计算Levenshtein distance，即两个字符串的编辑距离
	// *****************************
	private double LD(String s, String t) {
		double d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1

		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new double[n + 1][m + 1];

		// Step 2

		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3

		for (i = 1; i <= n; i++) {

			s_i = s.charAt(i - 1);

			// Step 4

			for (j = 1; j <= m; j++) {

				t_j = t.charAt(j - 1);

				// Step 5

				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6

				d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
						d[i - 1][j - 1] + cost);

			}

		}

		// Step 7

		return d[n][m];

	}

	/**
	 * 取a，b，c的最小值
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private double Minimum(double a, double b, double c) {
		double mi;
		mi = a;
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;

	}
}
