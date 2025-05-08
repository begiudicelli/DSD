package ex05;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

public class TestBancoDeDados {
    private static final int NUM_READERS = 10;
    private static final int NUM_WRITERS = 5;
    private static final int OPERATIONS_PER_THREAD = 5;
    private static final int STARVATION_THRESHOLD = 5000; // milliseconds

    // Shared resources and monitoring
    private static volatile boolean isWriting = false;
    private static final AtomicInteger activeReaders = new AtomicInteger(0);
    private static final AtomicInteger completedReaderOperations = new AtomicInteger(0);
    private static final AtomicInteger completedWriterOperations = new AtomicInteger(0);
    private static final AtomicInteger writerStarvationCount = new AtomicInteger(0);
    private static final AtomicInteger mutexViolationCount = new AtomicInteger(0);

    // Statistics
    private static final List<Long> writerWaitTimes = new ArrayList<>();
    private static final List<Long> readerWaitTimes = new ArrayList<>();

    // ANSI colors for console output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BG_RED = "\u001B[41m";
    private static final String ANSI_BG_GREEN = "\u001B[42m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public static void main(String[] args) {
        System.out.println(ANSI_BOLD + ANSI_PURPLE + "┌─────────────────────────────────────────────────┐");
        System.out.println("│ DATABASE CONCURRENCY TEST - READERS/WRITERS PROBLEM │");
        System.out.println("└─────────────────────────────────────────────────┘" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "• Testing with " + NUM_READERS + " readers and " + NUM_WRITERS + " writers");
        System.out.println("• Each thread will perform " + OPERATIONS_PER_THREAD + " operations" + ANSI_RESET);
        System.out.println();

        BancoDeDados db = new BancoDeDados();
        Thread[] readers = new Thread[NUM_READERS];
        Thread[] writers = new Thread[NUM_WRITERS];

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(NUM_READERS + NUM_WRITERS);

        // Create and start reader threads
        for (int i = 0; i < NUM_READERS; i++) {
            final int readerID = i;
            readers[i] = new Thread(() -> {
                try {
                    startLatch.await(); // Wait for all threads to be ready

                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // Read operation
                        System.out.println(ANSI_BLUE + "Reader " + readerID + " trying to read" + ANSI_RESET);

                        long startWaitTime = System.currentTimeMillis();
                        db.comecarALer();
                        long waitTime = System.currentTimeMillis() - startWaitTime;
                        synchronized(readerWaitTimes) {
                            readerWaitTimes.add(waitTime);
                        }

                        // Critical section - reading
                        int newReaders = activeReaders.incrementAndGet();
                        if (isWriting) {
                            System.out.println(ANSI_BG_RED + ANSI_WHITE + "MUTEX VIOLATION: Reader " + readerID +
                                    " reading while writer is active!" + ANSI_RESET);
                            mutexViolationCount.incrementAndGet();
                        }

                        System.out.println(ANSI_GREEN + "Reader " + readerID + " is reading. Active readers: " +
                                newReaders + ANSI_RESET);
                        Thread.sleep(100 + (int)(Math.random() * 50)); // Simulate reading
                        activeReaders.decrementAndGet();

                        db.terminarDeLer();
                        completedReaderOperations.incrementAndGet();
                        System.out.println(ANSI_BLUE + "Reader " + readerID + " finished reading" + ANSI_RESET);

                        // Random pause between operations
                        Thread.sleep((long)(Math.random() * 50));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
            readers[i].start();
        }

        // Create and start writer threads
        for (int i = 0; i < NUM_WRITERS; i++) {
            final int writerID = i;
            writers[i] = new Thread(() -> {
                try {
                    startLatch.await(); // Wait for all threads to be ready

                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // Write operation
                        System.out.println(ANSI_YELLOW + "Writer " + writerID + " trying to write" + ANSI_RESET);

                        long startWaitTime = System.currentTimeMillis();
                        AtomicBoolean starved = new AtomicBoolean(false);

                        // Monitor for starvation while waiting
                        Thread starvationMonitor = new Thread(() -> {
                            try {
                                Thread.sleep(STARVATION_THRESHOLD);
                                if (!starved.get()) {
                                    writerStarvationCount.incrementAndGet();
                                    System.out.println(ANSI_BG_RED + ANSI_WHITE + "STARVATION DETECTED: Writer " +
                                            writerID + " waiting for more than " + STARVATION_THRESHOLD +
                                            "ms" + ANSI_RESET);
                                }
                            } catch (InterruptedException e) {
                                // Monitor was cancelled, writer got access
                            }
                        });
                        starvationMonitor.setDaemon(true);
                        starvationMonitor.start();

                        db.comecarAEscrever();

                        // Cancel starvation monitor
                        starved.set(true);
                        starvationMonitor.interrupt();

                        long waitTime = System.currentTimeMillis() - startWaitTime;
                        synchronized(writerWaitTimes) {
                            writerWaitTimes.add(waitTime);
                        }

                        if (waitTime > 1000) {
                            System.out.println(ANSI_YELLOW + "Writer " + writerID + " waited for " +
                                    waitTime + "ms to get access!" + ANSI_RESET);
                        }

                        // Critical section - writing
                        if (activeReaders.get() > 0) {
                            System.out.println(ANSI_BG_RED + ANSI_WHITE + "MUTEX VIOLATION: Writer " +
                                    writerID + " writing while readers (" + activeReaders.get() +
                                    ") are active!" + ANSI_RESET);
                            mutexViolationCount.incrementAndGet();
                        }

                        if (isWriting) {
                            System.out.println(ANSI_BG_RED + ANSI_WHITE + "MUTEX VIOLATION: Writer " +
                                    writerID + " writing while another writer is active!" + ANSI_RESET);
                            mutexViolationCount.incrementAndGet();
                        }

                        isWriting = true;
                        System.out.println(ANSI_PURPLE + "Writer " + writerID + " is writing" + ANSI_RESET);
                        Thread.sleep(150 + (int)(Math.random() * 50)); // Simulate writing
                        isWriting = false;

                        db.terminarDeEscrever();
                        completedWriterOperations.incrementAndGet();
                        System.out.println(ANSI_YELLOW + "Writer " + writerID + " finished writing" + ANSI_RESET);

                        // Random pause between operations
                        Thread.sleep((long)(Math.random() * 100));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
            writers[i].start();
        }

        // Start all threads simultaneously
        System.out.println(ANSI_BOLD + "Starting test execution..." + ANSI_RESET);
        startLatch.countDown();

        // Progress monitoring thread
        Thread progressMonitor = new Thread(() -> {
            try {
                int totalOps = (NUM_READERS + NUM_WRITERS) * OPERATIONS_PER_THREAD;
                while (!doneLatch.await(500, TimeUnit.MILLISECONDS)) {
                    int completedOps = completedReaderOperations.get() + completedWriterOperations.get();
                    int progressPercent = (completedOps * 100) / totalOps;

                    StringBuilder progressBar = new StringBuilder("[");
                    for (int i = 0; i < 50; i++) {
                        if (i < (progressPercent / 2)) {
                            progressBar.append("█");
                        } else {
                            progressBar.append(" ");
                        }
                    }
                    progressBar.append("] ").append(progressPercent).append("%").append("\n");

                    System.out.print("\rProgress: " + progressBar);
                }
                System.out.println();  // Final newline after progress is complete
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        progressMonitor.setDaemon(true);
        progressMonitor.start();

        // Wait for all threads to finish
        try {
            boolean completed = doneLatch.await(30, TimeUnit.SECONDS);

            // Print summary
            System.out.println("\n" + ANSI_BOLD + ANSI_PURPLE + "┌───────────────────────────────────────┐");
            System.out.println("│ TEST RESULTS AND STATISTICS SUMMARY   │");
            System.out.println("└───────────────────────────────────────┘" + ANSI_RESET);

            DecimalFormat df = new DecimalFormat("#.##");

            if (!completed) {
                System.out.println(ANSI_BG_RED + ANSI_WHITE + " TEST FAILED: Timeout - possible deadlock detected " + ANSI_RESET);
                int totalExpected = (NUM_READERS + NUM_WRITERS) * OPERATIONS_PER_THREAD;
                int totalCompleted = completedReaderOperations.get() + completedWriterOperations.get();
                System.out.println(ANSI_RED + "Operations completed: " + totalCompleted + "/" + totalExpected +
                        " (" + df.format((double)totalCompleted/totalExpected*100) + "%)" + ANSI_RESET);
            } else {
                System.out.println(ANSI_BG_GREEN + ANSI_BLACK + " ALL OPERATIONS COMPLETED SUCCESSFULLY " + ANSI_RESET);

                // Reader statistics
                int readerOpsDone = completedReaderOperations.get();
                int readerOpsExpected = NUM_READERS * OPERATIONS_PER_THREAD;
                double readerSuccessRate = (double)readerOpsDone / readerOpsExpected * 100;

                System.out.println(ANSI_BLUE + "\n◆ READER STATISTICS:");
                System.out.println("  • Operations Completed: " + readerOpsDone + "/" + readerOpsExpected +
                        " (" + df.format(readerSuccessRate) + "%)");

                if (!readerWaitTimes.isEmpty()) {
                    double avgReaderWait = readerWaitTimes.stream().mapToLong(Long::valueOf).average().orElse(0);
                    long maxReaderWait = readerWaitTimes.stream().mapToLong(Long::valueOf).max().orElse(0);
                    System.out.println("  • Average Wait Time: " + df.format(avgReaderWait) + " ms");
                    System.out.println("  • Maximum Wait Time: " + maxReaderWait + " ms" + ANSI_RESET);
                }

                // Writer statistics
                int writerOpsDone = completedWriterOperations.get();
                int writerOpsExpected = NUM_WRITERS * OPERATIONS_PER_THREAD;
                double writerSuccessRate = (double)writerOpsDone / writerOpsExpected * 100;

                System.out.println(ANSI_YELLOW + "\n◆ WRITER STATISTICS:");
                System.out.println("  • Operations Completed: " + writerOpsDone + "/" + writerOpsExpected +
                        " (" + df.format(writerSuccessRate) + "%)");

                if (!writerWaitTimes.isEmpty()) {
                    double avgWriterWait = writerWaitTimes.stream().mapToLong(Long::valueOf).average().orElse(0);
                    long maxWriterWait = writerWaitTimes.stream().mapToLong(Long::valueOf).max().orElse(0);
                    System.out.println("  • Average Wait Time: " + df.format(avgWriterWait) + " ms");
                    System.out.println("  • Maximum Wait Time: " + maxWriterWait + " ms" + ANSI_RESET);
                }

                // Concurrency violations
                System.out.println(ANSI_PURPLE + "\n◆ CONCURRENCY METRICS:");
                if (mutexViolationCount.get() > 0) {
                    System.out.println(ANSI_BG_RED + ANSI_WHITE + "  • MUTUAL EXCLUSION VIOLATIONS: " +
                            mutexViolationCount.get() + ANSI_RESET);
                } else {
                    System.out.println(ANSI_GREEN + "  • Mutual Exclusion: PRESERVED ✓" + ANSI_RESET);
                }

                if (writerStarvationCount.get() > 0) {
                    System.out.println(ANSI_BG_RED + ANSI_WHITE + "  • WRITER STARVATION INCIDENTS: " +
                            writerStarvationCount.get() + ANSI_RESET);
                } else {
                    System.out.println(ANSI_GREEN + "  • Writer Starvation: NONE DETECTED ✓" + ANSI_RESET);
                }
            }

            // Final verdict
            System.out.println("\n" + ANSI_BOLD + "FINAL VERDICT: " +
                    (completed && mutexViolationCount.get() == 0 && writerStarvationCount.get() == 0 ?
                            ANSI_GREEN + "PASSED ✓" : ANSI_RED + "FAILED ✗") + ANSI_RESET);

            // Draw a visual graph of readers vs writers activity (if relevant)
            if (completed) {
                printConcurrencyChart();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printConcurrencyChart() {
        System.out.println("\n" + ANSI_BOLD + ANSI_PURPLE + "▓▓▓ CONCURRENCY BALANCE CHART ▓▓▓" + ANSI_RESET);

        // Calculate the balance between readers and writers
        int totalReaderOps = NUM_READERS * OPERATIONS_PER_THREAD;
        int totalWriterOps = NUM_WRITERS * OPERATIONS_PER_THREAD;

        double readerSuccessRate = (double)completedReaderOperations.get() / totalReaderOps;
        double writerSuccessRate = (double)completedWriterOperations.get() / totalWriterOps;

        // Calculate average wait times
        double avgReaderWait = readerWaitTimes.stream().mapToLong(Long::valueOf).average().orElse(0);
        double avgWriterWait = writerWaitTimes.stream().mapToLong(Long::valueOf).average().orElse(0);

        // Normalize for display (0-20 range)
        int readerBarLength = (int)(readerSuccessRate * 20);
        int writerBarLength = (int)(writerSuccessRate * 20);

        // Display the chart
        System.out.println("READERS [" + ANSI_BLUE + "■".repeat(readerBarLength) +
                " ".repeat(20 - readerBarLength) + ANSI_RESET + "] " +
                String.format("%.1f%%", readerSuccessRate * 100));

        System.out.println("WRITERS [" + ANSI_YELLOW + "■".repeat(writerBarLength) +
                " ".repeat(20 - writerBarLength) + ANSI_RESET + "] " +
                String.format("%.1f%%", writerSuccessRate * 100));

        // Wait time comparison
        System.out.println("\nAVERAGE WAIT TIMES (ms):");
        int maxBarLength = 50;
        double maxWaitTime = Math.max(avgReaderWait, avgWriterWait);
        int readerWaitBar = maxWaitTime > 0 ? (int)((avgReaderWait / maxWaitTime) * maxBarLength) : 0;
        int writerWaitBar = maxWaitTime > 0 ? (int)((avgWriterWait / maxWaitTime) * maxBarLength) : 0;

        System.out.println("READERS " + String.format("%6.1f", avgReaderWait) + "ms [" +
                ANSI_BLUE + "=".repeat(readerWaitBar) + ANSI_RESET + "]");
        System.out.println("WRITERS " + String.format("%6.1f", avgWriterWait) + "ms [" +
                ANSI_YELLOW + "=".repeat(writerWaitBar) + ANSI_RESET + "]");

        // Algorithm fairness assessment
        double ratio = avgWriterWait > 0 ? avgReaderWait / avgWriterWait : 1.0;
        System.out.print("\nALGORITHM FAIRNESS: ");

        if (ratio > 2.0) {
            System.out.println(ANSI_YELLOW + "FAVORS WRITERS (Readers wait " +
                    String.format("%.1f", ratio) + "x longer)" + ANSI_RESET);
        } else if (ratio < 0.5) {
            System.out.println(ANSI_BLUE + "FAVORS READERS (Writers wait " +
                    String.format("%.1f", 1/ratio) + "x longer)" + ANSI_RESET);
        } else {
            System.out.println(ANSI_GREEN + "BALANCED (Reader:Writer wait ratio " +
                    String.format("%.1f", ratio) + ")" + ANSI_RESET);
        }
    }
}