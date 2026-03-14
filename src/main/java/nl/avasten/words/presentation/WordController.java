package nl.avasten.words.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import nl.avasten.words.application.WordService;
import nl.avasten.words.domain.exception.WordLengthNotSupportedException;

import java.util.List;

@Path("/api/words")
public class WordController {

    @Inject
    WordService service;

    @GET
    public List<WordResult> getWords() {
        return service.getWords().stream().map(WordResult::new).toList();
    }

    @GET
    @Path("/random")
    public WordResult getRandomWord(@QueryParam("length") Integer length) {
        try {
            String word = service.provideRandomWord(length);
            return new WordResult(word);
        } catch (WordLengthNotSupportedException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @DELETE
    @Path("/{word}")
    public void deleteWord(@PathParam("word") String word) {
        service.deleteWord(word);
    }
}
