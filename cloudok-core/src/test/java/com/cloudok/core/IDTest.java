package com.cloudok.core;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.cloudok.primarkey.SnowflakePrimaryKeyGenerator;

public class IDTest {

    final static SnowflakePrimaryKeyGenerator pkg = SnowflakePrimaryKeyGenerator.SEQUENCE;

    static {
        try {
            pkg.setWorkId(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        testIdGen(2560, 5000);
        System.out.println("\n");
        testIdGen(2560, 5000);
    }

    private static void testIdGen(int threadNum, int perThreadIdNum) {
        final Thread[] threads = new Thread[threadNum];
        final long[][] idsArr = new long[threadNum][perThreadIdNum];
        final AtomicInteger index = new AtomicInteger(0);
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        final int expectNum = threadNum * perThreadIdNum;
        int actuallyNum = 0;
        final HashSet<Long> set = new HashSet<>(expectNum);
        long t1 = 0;
        long t2 = 0;
        for (int l1 = 0; l1 < threads.length; l1++) {
            threads[l1] = new Thread(() -> {
                try {
                    final int l2 = index.getAndIncrement();
                    for (int l3 = 0; l3 < perThreadIdNum; l3++) idsArr[l2][l3] = pkg.next();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        t1 = System.nanoTime();
        for (Thread t : threads) t.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        t2 = System.nanoTime();
        System.out.println("run over using time " + (t2 - t1) / 1000000000.0 + " seconds");
        for (long[] arr : idsArr) for (long item : arr) set.add(item);
        actuallyNum = set.size();
        System.out.println("expect " + expectNum + " ids, actually is " + actuallyNum + ",and collision rate is " + String.format("%.8f", (expectNum - actuallyNum) * 1.0 / expectNum * 100) + "%");
    }
}
