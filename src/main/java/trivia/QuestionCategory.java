package trivia;

enum QuestionCategory {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock");

    private final String value;

    QuestionCategory(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
