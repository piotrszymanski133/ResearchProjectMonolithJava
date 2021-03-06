package studies.research.project.monolithjava.controller;


import io.vavr.control.Try;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import studies.research.project.monolithjava.model.Graph;
import studies.research.project.monolithjava.service.EdmondsKarpService;
import studies.research.project.monolithjava.service.GraphService;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class GraphController {

    private final GraphService graphService;
    private final EdmondsKarpService edmondsKarpService;

    public GraphController(GraphService graphService, EdmondsKarpService edmondsKarpService) {
        this.graphService = graphService;
        this.edmondsKarpService = edmondsKarpService;
    }

    @GetMapping("/graphs/{id}")
    public ResponseEntity<Graph> getGraph(@PathVariable("id") String id) {
        return Try.of(() -> Integer.parseInt(id))
                .map(graphService::getGraph)
                .map(graph -> new ResponseEntity<>(graph, OK))
                .onFailure(System.err::println)
                .getOrElseGet(e -> new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/graphs/maxFlow/{id}")
    public ResponseEntity<Integer> getMaxGraphFlow(@PathVariable("id") String id, @RequestParam String source, @RequestParam String destination) {
        return Try.of(() -> Integer.parseInt(id))
                .map(graphService::getGraph)
                .map(graph -> {
                    int s = Integer.parseInt(source);
                    int d = Integer.parseInt(destination);
                    return new ResponseEntity<>(edmondsKarpService.calculateMaxFlow(graph, s, d), OK);
                })
                .onFailure(System.err::println)
                .getOrElseGet(e -> new ResponseEntity<>(BAD_REQUEST));
    }
}
