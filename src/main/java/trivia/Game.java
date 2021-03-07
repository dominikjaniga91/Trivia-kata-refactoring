package trivia;

import java.util.ArrayList;
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
    private final Questions questions = new Questions();
    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public Game() {

    }

    public void add(String playerName) {

        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer).getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.printf("%s is getting out of the penalty box%n", players.get(currentPlayer).getName());
                currentPlayer().advancePlayer(roll);

                System.out.printf("%s's new location is %d%n", players.get(currentPlayer).getName(), currentPlayer().place());
                System.out.printf("The category is %s%n", questions.currentCategory(currentPlayer().place()));
                askQuestion();
            } else {
                System.out.printf("%s is not getting out of the penalty box%n", players.get(currentPlayer).getName());
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            currentPlayer().advancePlayer(roll);

            System.out.printf("%s's new location is %d%n", players.get(currentPlayer).getName(), currentPlayer().place());
            System.out.printf("The category is %s%n", questions.currentCategory(currentPlayer().place()));
            askQuestion();
        }

    }

    private void askQuestion() {
        String question = questions.extractNextQuestion(currentPlayer().place());
        System.out.println(question);
    }


    private Player currentPlayer() {
        return players.get(currentPlayer);
    }

    public boolean wasCorrectlyAnswered() {
        if (currentPlayer().isInPenaltyBox()) {
            if (isGettingOutOfPenaltyBox) {
                System.out.println("Answer was correct!!!!");
                currentPlayer().addCoin();
                System.out.printf("%s now has %d Gold Coins.%n", players.get(currentPlayer).getName(), currentPlayer().coins());

                boolean winner = didPlayerWin();
                moveToNextPlayer();

                return winner;
            } else {
                moveToNextPlayer();
                return true;
            }


        } else {

            System.out.println("Answer was corrent!!!!");
            currentPlayer().addCoin();
            System.out.printf("%s now has %d Gold Coins.%n", players.get(currentPlayer).getName(), currentPlayer().coins());

            boolean winner = didPlayerWin();
            moveToNextPlayer();

            return winner;
        }
    }

    private void moveToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.printf("%s was sent to the penalty box%n", players.get(currentPlayer).getName());
        currentPlayer().moveToPenaltyBox();

        moveToNextPlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return !(currentPlayer().coins() == 6);
    }
}
