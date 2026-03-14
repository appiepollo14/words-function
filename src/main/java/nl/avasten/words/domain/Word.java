package nl.avasten.words.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.util.Map;
import java.util.Optional;

@Entity(name = "words")
@NamedQuery(name = "Word.getRandomByLength", query = "FROM words w WHERE w.length = :length ORDER BY random() LIMIT 1")
public class Word extends PanacheEntityBase {

    @Id
    @Column(name = "word")
    private String value;
    private Integer length;

    public Word() {
    }

    public Word(String word) {
        this.value = word;
        this.length = word.length();
    }

    public String getValue() {
        return value;
    }

    public Integer getLength() {
        return length;
    }

    public static Optional<Word> findRandomWordByLength(Integer length) {
        return find("#Word.getRandomByLength", Map.of("length", length)).firstResultOptional();
    }
}
