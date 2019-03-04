package dinya.peter.feedmedb.controller;

import dinya.peter.feedmedb.model.Event;
import dinya.peter.feedmedb.repo.FeedMeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private final FeedMeRepository repo;

    public MainController(FeedMeRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/events")
    public List<Event> getEvents() {
        return repo.findAll();
    }
}
