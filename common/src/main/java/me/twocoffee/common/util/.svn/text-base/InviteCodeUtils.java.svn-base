/**
 * 
 */
package me.twocoffee.common.util;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Random;
import java.util.Stack;

/**
 * @author momo
 * 
 */
public class InviteCodeUtils {

	public static final long HEX = 62;

	// public static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u',
	// 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
	// 'x', 'c', 'v', 'b', 'n', 'm' };

	public static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u',
			'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
			'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
			'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V',
			'B', 'N', 'M' };

	private static int _n_value(Character c) {

		for (int i = 0; i < array.length; i++) {

			if (c.equals(array[i])) {
				return i;
			}
		}
		throw new IllegalArgumentException();
	}

	public static String _10_to_n(long number) {
		BigDecimal rest = new BigDecimal(number);
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);

		while (rest.longValue() != 0l) {
			stack.add(array[rest.remainder(new BigDecimal(HEX)).intValueExact()]);
			rest = rest.divideToIntegralValue(new BigDecimal(HEX));
		}

		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();
	}

	public static long _n_to_10(final String sixty_str) {
		long multiple = 1;
		long result = 0;
		Character c;

		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += _n_value(c) * multiple;
			multiple = multiple * HEX;
		}
		return result;
	}

	// 将id的1-8位与9-16位互换生成新的id作为验证码。如果id不足6位则前端补0凑足6位
	public static String createInviteCode(Long id) {
		Long low = ((id >> 8) << 56) >> 56;
		Long middle = (id << 56) >> 48;
		Long high = (id >> 16) << 16;
		String res = String.valueOf(high | middle | low);

		if (res.length() >= 6) {
			return res;

		} else {
			StringBuilder b = new StringBuilder();
			int size = 6;

			while (size - res.length() > 0) {
				b.append("0");
				size--;
			}
			b.append(res);
			return b.toString();
		}
	}

	public static String createRandom(int num, int n) {

		if (num <= 0 || n <= 0) {
			throw new InvalidParameterException();
		}
		Random random = new Random();
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < num; i++) {
			b.append(random.nextInt(n));
		}
		return b.toString();
	}

	public static String createRandom(String base, int num, int n) {

		if (num <= 0 || n <= 0) {
			throw new InvalidParameterException();
		}
		Random random = new Random();
		StringBuilder b = new StringBuilder(base);

		for (int i = 0; i < num; i++) {
			b.append(random.nextInt(n));
		}
		return b.toString();
	}
}
