package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

final class Player {
    private final String name;
    private int place = 0;
    private int coin = 0;
    private boolean inPenaltyBox = false;

    Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int place() {
        return place;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Player) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, place);
    }

    @Override
    public String toString() {
        return "Player[" +
                "name=" + name + ", " +
                "place=" + place + ']';
    }

    void advancePlayer(int roll) {
        place += roll;
        if (place >= 12) {
            place -= 12;
        }
    }

    public void addCoin() {
        coin++;
    }

    public int coins() {
        return coin;
    }

    public void moveToPenaltyBox() {
        inPenaltyBox = true;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }
}


public class Game implements IGame {
    private final List<Player> players = new ArrayList<>();
    private final List<String> popQuestions = new LinkedList<>();
    private final List<String> scienceQuestions = new LinkedList<>();
    private final List<String> sportsQuestions = new LinkedList<>();
    private final List<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.add("Pop Question " + i);
            scienceQuestions.add("Science Question " + i);
            sportsQuestions.add("Sports Question " + i);
            rockQuestions.add(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean add(String playerName) {

        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer).getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer).getName() + " is getting out of the penalty box");
                advancePlayer(roll);

                System.out.println(players.get(currentPlayer).getName()
                        + "'s new location is "
                        + currentPlayer().place());
                System.out.println("The category is " + currentCategory());
                askQuestion();
            } else {
                System.out.println(players.get(currentPlayer).getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            advancePlayer(roll);

            System.out.println(players.get(currentPlayer).getName()
                    + "'s new location is "
                    + currentPlayer().place());
            System.out.println("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void advancePlayer(int roll) {
        currentPlayer().advancePlayer(roll);
    }

    private void askQuestion() {
        String question = extractNextQuestion();
        System.out.println(question);
    }

    private String extractNextQuestion() {
        return switch (currentCategory()) {
            case "Pop" -> popQuestions.remove(0);
            case "Science" -> scienceQuestions.remove(0);
            case "Sports" -> sportsQuestions.remove(0);
            case "Rock" -> rockQuestions.remove(0);
            default -> throw new IllegalStateException("Unexpected value: " + currentCategory());
        };
    }


    private String currentCategory() {
        if (currentPlayer().place() == 0) return "Pop";
        if (currentPlayer().place() == 4) return "Pop";
        if (currentPlayer().place() == 8) return "Pop";
        if (currentPlayer().place() == 1) return "Science";
        if (currentPlayer().place() == 5) return "Science";
        if (currentPlayer().place() == 9) return "Science";
        if (currentPlayer().place() == 2) return "Sports";
        if (currentPlayer().place() == 6) return "Sports";
        if (currentPlayer().place() == 10) return "Sports";
        return "Rock";
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                currentPlayer().addCoin();
                System.out.println(players.get(currentPlayer).getName()
                        + " now has "
                        + currentPlayer().coins()
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }


        } else {

            System.out.println("Answer was corrent!!!!");
            currentPlayer().addCoin();
            System.out.println(players.get(currentPlayer).getName()
                    + " now has "
                    + currentPlayer().coins()
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer).getName() + " was sent to the penalty box");
        currentPlayer().moveToPenaltyBox();

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(currentPlayer().coins() == 6);
    }
}
