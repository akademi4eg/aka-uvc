package net.dtkanov.blocks.circuit;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.dtkanov.blocks.logic.AddrPair;

public class Clock {
	public static long DEFAULT_FREQUENCY = 3;
	
	private long freq;
	private long step;
	private boolean state;
	private List<AddrPair> dst = new LinkedList<AddrPair>();
	private boolean alive;
	
	public Clock() {
		this(DEFAULT_FREQUENCY);
	}
	
	public Clock(long frequency) {
		freq = frequency;
		step = 1000000/freq;
		state = false;
	}
	
	public Clock addSink(AddrPair sink) {
		dst.add(sink);
		return this;
	}
	
	public void start() {
		alive = true;
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
				new Runnable() {
					public void run() {
						if (alive) {
							System.out.println("---Tick---");
							state = !state;
							for (AddrPair sink : dst) {
								sink.node.in(sink.port, state);
								sink.node.propagate();
							}
						}
					}
				}, 0, step, TimeUnit.MICROSECONDS);
	}
	
	public void stop() {
		alive = false;
	}
}
