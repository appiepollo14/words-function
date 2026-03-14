package nl.avasten.words.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import nl.avasten.words.domain.Word;
import nl.avasten.words.domain.exception.WordLengthNotSupportedException;

import java.util.List;

@ApplicationScoped
@Transactional
public class WordService {

    public String provideRandomWord(Integer length) {
        return Word.findRandomWordByLength(length).orElseThrow(() -> new WordLengthNotSupportedException(length)).getValue();
    }

    public boolean wordExists(String word) {
        return Word.findByIdOptional(word).isPresent();
    }

    public List<String> getWords() {
        List<Word> all = Word.listAll();
        return all.stream().map(Word::getValue).toList();
    }

    public void deleteWord(String word) {
        Word.deleteById(word);
    }
}
