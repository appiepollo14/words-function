package nl.avasten;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import nl.avasten.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class WordControllerTest {

    @BeforeEach
    @Transactional
    void resetWords() {
        Word.deleteAll();
        new Word("cat").persist();
        new Word("house").persist();
        new Word("plane").persist();
    }

    @Test
    void getWordsReturnsSeededWords() {
        given()
                .when().get("/api/words")
                .then()
                .statusCode(200)
                .body("word", hasItems("cat", "house", "plane"));
    }

    @Test
    void getRandomWordByLengthReturnsMatchingWord() {
        given()
                .queryParam("length", 5)
                .when().get("/api/words/random")
                .then()
                .statusCode(200)
                .body("word", anyOf(equalTo("house"), equalTo("plane")));
    }

    @Test
    void getRandomWordByUnsupportedLengthReturnsBadRequest() {
        given()
                .queryParam("length", 99)
                .when().get("/api/words/random")
                .then()
                .statusCode(400);
    }

    @Test
    void deleteWordRemovesItFromResultList() {
        given()
                .when().delete("/api/words/cat")
                .then()
                .statusCode(anyOf(is(200), is(204)));

        given()
                .when().get("/api/words")
                .then()
                .statusCode(200)
                .body("word", not(hasItems("cat")));
    }
}

