package trivia;

import java.util.LinkedList;
import java.util.List;

public class Questions {

    private final List<String> popQuestions = new LinkedList<>();
    private final List<String> scienceQuestions = new LinkedList<>();
    private final List<String> sportsQuestions = new LinkedList<>();
    private final List<String> rockQuestions = new LinkedList<>();

    public Questions() {
        for (int i = 0; i < 50; i++) {
            popQuestions.add("Pop Question " + i);
            scienceQuestions.add("Science Question " + i);
            sportsQuestions.add("Sports Question " + i);
            rockQuestions.add("Rock Question " + i);
        }
    }

    public String extractNextQuestion(int index) {
        return switch (currentCategory(index)) {
            case POP -> popQuestions.remove(0);
            case SCIENCE -> scienceQuestions.remove(0);
            case SPORTS -> sportsQuestions.remove(0);
            case ROCK -> rockQuestions.remove(0);
        };
    }

    public Game.QuestionCategory currentCategory(int index) {
        return switch (index % 4) {
            case 0 -> Game.QuestionCategory.POP;
            case 1 -> Game.QuestionCategory.SCIENCE;
            case 2 -> Game.QuestionCategory.SPORTS;
            default -> Game.QuestionCategory.ROCK;
        };
    }
}
