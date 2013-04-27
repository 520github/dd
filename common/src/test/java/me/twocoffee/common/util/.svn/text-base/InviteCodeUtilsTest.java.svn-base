package me.twocoffee.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InviteCodeUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private long revert(long invite) {
		long low = invite >> 8 << 56 >> 56;
		long middle = invite << 56 >> 48;
		long high = invite >> 16 << 16;
		return high | middle | low;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateInviteCode() {
		assertEquals("022016", InviteCodeUtils.createInviteCode(86l));
		assertEquals("000256", InviteCodeUtils.createInviteCode(1l));
		assertEquals("000000", InviteCodeUtils.createInviteCode(0l));
		assertEquals("999234", InviteCodeUtils.createInviteCode(999999l));
		assertEquals("999490", InviteCodeUtils.createInviteCode(1000000l));

		assertEquals(86l,
				revert(Long.valueOf(InviteCodeUtils.createInviteCode(86l))));
		assertEquals(1l,
				revert(Long.valueOf(InviteCodeUtils.createInviteCode(1l))));
		assertEquals(0l,
				revert(Long.valueOf(InviteCodeUtils.createInviteCode(0l))));
		assertEquals(999999l,
				revert(Long.valueOf(InviteCodeUtils.createInviteCode(999999l))));
		assertEquals(
				1000000l,
				revert(Long.valueOf(InviteCodeUtils.createInviteCode(1000000l))));
	}
}
