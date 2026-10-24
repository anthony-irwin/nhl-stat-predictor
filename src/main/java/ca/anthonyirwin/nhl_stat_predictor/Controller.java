package ca.anthonyirwin.nhl_stat_predictor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
public class Controller {
    
    private final RestClient restClient = RestClient.create();

    // Record games
    @JsonIgnoreProperties(ignoreUnknown = true)
    record ScoreResponse(List<Game> games) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Game(String gameState, Team homeTeam, Team awayTeam, List<Goal> goals) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Team(String abbrev, Integer score, Integer sog) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Goal(Integer playerId, Name name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Name(@JsonProperty("default") String name) {}

    @GetMapping("/api/nhle")
    public List<Game> getGameData() {
        // Java calls the nhle API
        ScoreResponse response = restClient.get()
                .uri("https://nhle.com")
                .retrieve()
                .body(ScoreResponse.class);

        List<Game> results = new ArrayList<>();

        for (Game g : response.games) {
            results.add(g);
        }

        return results;
    }
}
