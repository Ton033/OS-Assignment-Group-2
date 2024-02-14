package org.ntnu.testing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ntnu.client.ClientSimulator;
import org.ntnu.server.MultiThreadedServer;
import org.ntnu.server.SingleThreadedServer;

public class PerformanceTester {

  private static List<Long> allRoundsResponseTimes = Collections.synchronizedList(
    new ArrayList<>()
  );
  private static int totalClientsTested = 0;

  public static synchronized void recordResponseTime(long responseTimeMs) {
    allRoundsResponseTimes.add(responseTimeMs);
  }

  public static void runTests(
    int numberOfClients,
    boolean isMultiThreaded,
    int testRounds
  ) {
    totalClientsTested = 0;
    for (int round = 1; round <= testRounds; round++) {
      System.out.println("Running test round: " + round + " of " + testRounds);
      testServer(numberOfClients, isMultiThreaded);
      totalClientsTested += numberOfClients;
      displayFinalResults(isMultiThreaded, round);
    }
    try {
      ProcessBuilder pb = new ProcessBuilder("python", "generate_graphs.py");
      Process p = pb.start();
      p.waitFor();
      System.out.println("Graphs generated.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testServer(int numberOfClients, boolean isMultiThreaded) {
    startServer(isMultiThreaded);
    waitForServerStartup();
    ClientSimulator.simulateClients(numberOfClients);
    waitForClientCompletion();
    stopServer(isMultiThreaded);
  }

  private static void startServer(boolean isMultiThreaded) {
    if (isMultiThreaded) {
      MultiThreadedServer.startServer();
    } else {
      SingleThreadedServer.startServer();
    }
  }

  private static void waitForServerStartup() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void waitForClientCompletion() {}

  private static void stopServer(boolean isMultiThreaded) {
    if (isMultiThreaded) {
      MultiThreadedServer.stopServer();
    } else {
      SingleThreadedServer.stopServer();
    }
  }

  private static void displayFinalResults(boolean isMultiThreaded, int round) {
    if (allRoundsResponseTimes.isEmpty()) {
      System.out.println("No results to display.");
      return;
    }

    Collections.sort(allRoundsResponseTimes);
    double averageResponseTimeMs =
      allRoundsResponseTimes
        .stream()
        .mapToDouble(Long::doubleValue)
        .average()
        .orElse(0.0) /
      1_000_000;
    long medianResponseTimeMs =
      calculateMedian(allRoundsResponseTimes) / 1_000_000;
    long responseTime95thPercentileMs =
      calculatePercentile(allRoundsResponseTimes, 95) / 1_000_000;

    System.out.println("Aggregated results after all test rounds:");
    System.out.println("Total clients tested: " + totalClientsTested);
    System.out.println(
      "Average response time: " + averageResponseTimeMs + " ms"
    );
    System.out.println("Median response time: " + medianResponseTimeMs + " ms");
    System.out.println(
      "95th percentile response time: " + responseTime95thPercentileMs + " ms"
    );

    exportResultsToCsv(
      "aggregated_test_results.csv",
      isMultiThreaded,
      averageResponseTimeMs,
      medianResponseTimeMs,
      responseTime95thPercentileMs,
      round
    );
  }

  private static long calculateMedian(List<Long> times) {
    int size = times.size();
    if (size % 2 == 0) {
      return (times.get(size / 2 - 1) + times.get(size / 2)) / 2;
    } else {
      return times.get(size / 2);
    }
  }

  private static long calculatePercentile(List<Long> times, double percentile) {
    int index = (int) Math.ceil(percentile / 100.0 * times.size()) - 1;
    return times.get(index);
  }

  public static void main(String[] args) {}

  private static void exportResultsToCsv(
    String filename,
    boolean isMultiThreaded,
    double averageResponseTimeMs,
    long medianResponseTimeMs,
    long responseTime95thPercentileMs,
    int round
  ) {
    File csvFile = new File(filename);
    boolean exists = csvFile.exists();

    try (PrintWriter out = new PrintWriter(new FileWriter(csvFile, true))) {
      if (!exists) {
        out.println(
          "ServerType,NumberOfClients,AverageResponseTimeMs,MedianResponseTimeMs,95thPercentileResponseTimeMs,TestRound"
        );
      }

      String serverType = isMultiThreaded ? "MultiThreaded" : "SingleThreaded";
      out.printf(
        "%s,%d,%.2f,%d,%d,%d\n",
        serverType,
        totalClientsTested,
        averageResponseTimeMs,
        medianResponseTimeMs,
        responseTime95thPercentileMs,
        round
      );
    } catch (IOException e) {
      System.err.println(
        "An error occurred while writing to CSV: " + e.getMessage()
      );
    }
  }
}
