package de.ckonv.graphdemo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GraphController {


  int NUMBER_OF_NODES = 20_000_000;
  int NUMBER_OF_VERTICES = 6_000_000;

  Random random = new Random();

  Multimap<Long, Long> map = ArrayListMultimap.create();

  GraphController() {
    IntStream.range(0, NUMBER_OF_VERTICES).forEachOrdered(n -> {
      var randomNode1 = random.nextLong(NUMBER_OF_NODES);
      var randomNode2 = random.nextLong(NUMBER_OF_NODES);

      if (map.get(randomNode1).isEmpty()) {
        map.put(randomNode1, randomNode2);
      }
    });

    map.put(1L, 2L);
    map.put(2L, 1234L);
    map.put(1234L, 3L);
  }

  @GetMapping("graph/{node}")
  public Long hello(@PathVariable Long node){

    log.info("chosen node: {}", node);

    while (map.containsKey(node)) {
      log.info("Found mapping: {} -> {}", node, map.get(node));
      node = map.get(node).stream().findFirst().orElse(-1L);
      log.info("Trying new node: {}", node);
    }
    log.info("Final mapping: {}", node);

    return node;
  }

}
