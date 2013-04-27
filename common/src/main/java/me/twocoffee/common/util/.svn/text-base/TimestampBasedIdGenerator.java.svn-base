package me.twocoffee.common.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于时间戳的ID生成器。每个ID的结构是： 高42位： 时间戳， 毫秒书； 低12位： 同一时间戳内的序列号； 中间10位：worker号
 *
 * 也就是说，不考虑硬件的性能，在一毫秒内，这个产生器最多可以生成4096个ID。
 * 也就是说， worker 号不可以大于4096 
 * 
 * 这个生成器的生成的ID基本上是增长的。如果在分布式环境中，可以使用不同的worker号来保证所有实例产生的ID是唯一的
 * 
 * @author Leon
 * 
 */
public class TimestampBasedIdGenerator implements IdGenerator {
	private final long workerId;
	private final AtomicLong sequence;
	private long lastTimeStamp;
	private static final int SEQUENCE_LENGTH = 12;
	private static final int WORKER_ID_LENGTH = 10;
	private final long maxSequence;
	private final long maxWorkerId;
	
	/**
	 * 使用当前的时间戳作为种子生成workerId
	 */
	public TimestampBasedIdGenerator(){
		this(System.currentTimeMillis() % 4095);
	}
	
	/**
	 * 使用指定的WorkerID来实例化生成器
	 * @param workerId 指定的workerId， 需要小于4096
	 * @throws IllegalArgumentException workerID不小于4096
	 */
	public TimestampBasedIdGenerator(long workerId) {
		this.workerId = workerId;
		sequence = new AtomicLong(0);
		lastTimeStamp = 0;
		maxSequence = -1L ^ SEQUENCE_LENGTH;
		maxWorkerId = -1L ^ WORKER_ID_LENGTH;
		
		if(workerId > WORKER_ID_LENGTH){
			throw new IllegalArgumentException("Worker Id must less than " + maxWorkerId);
		}
	}

	// TODO 处理时钟突然往回走诡异的情况
	// TODO 干掉野蛮的synchronized
	@Override
	public synchronized long nextId() {
		long now = System.currentTimeMillis();
		if (sequence.get() >= maxSequence) {
			// 序列号用完了。等到下一毫秒再说吧
			try {
				Thread.sleep(1L);
			} catch (InterruptedException ignore) {
			}
		}		
		
		if (now != lastTimeStamp) {
			lastTimeStamp = now;
			sequence.set(0);
		}		

		return (lastTimeStamp << (SEQUENCE_LENGTH + WORKER_ID_LENGTH))
				| (workerId << SEQUENCE_LENGTH) | sequence.getAndIncrement();
	}
}