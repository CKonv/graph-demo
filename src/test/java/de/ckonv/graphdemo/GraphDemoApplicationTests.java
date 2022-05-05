package de.ckonv.graphdemo;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
class GraphDemoApplicationTests {

  @Test
  void contextLoads() {}


  @Test
  void test() {

    var stopwatch = Stopwatch.createStarted();

    int NUMBER_OF_NODES = 20_000_000;
    int NUMBER_OF_VERTICES = 6_000_000;

    Runtime instance = Runtime.getRuntime();
    int mb = 1024 * 1024;

    Random random = new Random();

    Multimap<Long, Long> map = ArrayListMultimap.create();

    // Setup map with random vertices
    IntStream.range(0, NUMBER_OF_VERTICES).forEachOrdered(n -> {
      var randomNode1 = random.nextLong(NUMBER_OF_NODES);
      var randomNode2 = random.nextLong(NUMBER_OF_NODES);

      if (map.get(randomNode1).isEmpty()) {
        map.put(randomNode1, randomNode2);
      }
    });

    // Put some known vertices for testing retrieval time
    map.put(1L, 2L);
    map.put(2L, 1234L);
    map.put(1234L, 3L);
    map.put(3L, 143552L);
    map.put(143552L, 999999999L);

    log.info("Time to init map: {}", stopwatch.stop());
    log.info(
        "Current used memory: {}mb",
        ((instance.totalMemory() - instance.freeMemory()) / mb));
    log.info("----------------------------");

    stopwatch.reset();
    stopwatch.start();

    var randomStart = 1L;

    log.info("chosen node: {}", randomStart);

    while (map.containsKey(randomStart)) {
      log.info("Found mapping: {} -> {}", randomStart, map.get(randomStart));

      randomStart = map.get(randomStart).stream().findFirst().orElse(-1L);

      log.info("Trying new node: {}", randomStart);
    }
    log.info("Final mapping: {}", randomStart);
    log.info("----------------------------");

    log.info("Time to resolve mapping: {}", stopwatch.stop());
  }
}
