package com.cloudok.primarkey;

import java.io.IOException;

/**
 * 
 * @author zhijian.xia@foxmail.com
 * @date 2020年6月14日 上午10:03:44
 */

public abstract class SnowflakePrimaryKeyGenerator {

	public final static SnowflakePrimaryKeyGenerator SEQUENCE = SequencePrimaryKeyGenerator.getInstance();

	public abstract Long next();
	
	public abstract void setWorkId(int workId)throws IOException ;

}

class SequencePrimaryKeyGenerator extends SnowflakePrimaryKeyGenerator {

	private static final SnowflakePrimaryKeyGenerator instance = new SequencePrimaryKeyGenerator();

	private IdWorker worker = null;
	
	private Integer workId=null;

	private SequencePrimaryKeyGenerator() {
		
	}

	public static SnowflakePrimaryKeyGenerator getInstance() {
		return instance;
	}

	@Override
	public Long next() {
		if(worker==null) {
			if(workId!=null) {
				try {
					worker = new IdWorker(workId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return worker.nextId();
	}

	@Override
	public void setWorkId(int wk) throws IOException {
		if(this.workId!=null) {
			throw new IOException("请勿重复设置！");
		}
		this.workId=wk;
	}
}

/**
 * 
 * @ClassName: IdWorker
 * @Description: 序列格式：符号位+时间戳＋5为到wordId＋17位的同时间内计算值().
 *               可以使用(2199023255552+1361753741828)/(365*24*60*60*1000)=112.
 *               91149788749年
 * @author zhijian.xia@foxmail.com
 * @date 2017年9月25日
 *
 */
class IdWorker {
	private final long workerId;
	private final static long twepoch = 9361753741828L; // 00000...-0001-0011-1101-0000-1110-1101-1001-0001-1010-0000-0100（44）
	private long sequence = 0L;
	/***
	 * 用于标示节点占多少位
	 */
	private final static long workerIdBits = 5L;

	/****
	 * 最大节点数
	 */
	public final static long maxWorkerId = -1L ^ (-1L << workerIdBits);// 000000..00011111(5)=32

	/***
	 * 序列占的数据
	 */
	private final static long sequenceBits = 17L;

	private final static long workerIdShift = sequenceBits; // 17

	private final static long timestampLeftShift = sequenceBits + workerIdBits;// 17+5=22

	public final static long sequenceMask = -1L ^ (-1L << sequenceBits); // 000....11111111111111111(17)

	private long lastTimestamp = -1L;

	public IdWorker(final long workerId) {
		super();
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {// 最后一次获取id的时间跟当前时间相等
			this.sequence = (this.sequence + 1) & sequenceMask; // 毫秒内计数加1
			if (this.sequence == 0) { // 达到最大值，则再取一次时间比当前时间大的时间（计数区间满了）
				// System.out.println("###########" + sequenceMask);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;//无并发奇偶问题
		}
		if (timestamp < this.lastTimestamp) { // 如果本次操作时间小于最后一次生成主键时间，时间被改回去，异常
			try {
				throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
						this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp; // 重设最后一次时间为本次操作时间
		long nextId = ((timestamp - twepoch << timestampLeftShift)) // 当前时间减去基础时间，向左移动22个
				| (this.workerId << workerIdShift) | (this.sequence);
		// this.workerId << this.workerIdShift 向左移动17位，占18-22这5位
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}
}