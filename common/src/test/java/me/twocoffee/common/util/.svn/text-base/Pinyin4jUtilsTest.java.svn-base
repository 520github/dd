/**
 * 
 */
package me.twocoffee.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author momo
 * 
 */
public class Pinyin4jUtilsTest {

    /**
     * Test method for
     * {@link me.twocoffee.common.util.Pinyin4jUtils#getPinYin(java.lang.String)}
     * .
     */
    @Test
    public void testGetPinYin() {
	assertEquals("kaijiongshuishuohuaixueshenglaijiufashipindanger",
		Pinyin4jUtils.getPinYin("嘅囧誰說壞學生來勼髮視頻裆児"));

	assertEquals("nvabc",
		Pinyin4jUtils.getPinYin("女abc"));

	assertEquals("ab123nvabc",
		Pinyin4jUtils.getPinYin("ab123女abc"));

	assertEquals("guodiduo",
		Pinyin4jUtils.getPinYin("鎺國啲埖哚"));
    }

}
