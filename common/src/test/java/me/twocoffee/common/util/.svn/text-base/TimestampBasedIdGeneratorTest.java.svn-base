package me.twocoffee.common.util;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TimestampBasedIdGeneratorTest {

		
	@Test
	public void testNextId() {
		TimestampBasedIdGenerator ig = new TimestampBasedIdGenerator(2L);		
		
		Set<Long> result = new HashSet<Long>();
		long[] buf = new long[4096];
		for(int i = 0; i < 4096 ; i++){
			buf[i] = ig.nextId();
		}
		
		for(long id : buf){
			result.add(id);
		}
		
		assertEquals(4096 , result.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void workerId太大的时候(){
		TimestampBasedIdGenerator ig = new TimestampBasedIdGenerator(4097L);		
	}

}
