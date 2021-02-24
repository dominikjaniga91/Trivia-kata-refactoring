package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

final class Player {
    private final String name;
    private int place = 0;

    Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int place() {
        return place;
    }

    public void place(int place) {
        this.place = place;
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

}


public class Game implements IGame {
   List<Player> players = new ArrayList<>();
   int[] purses = new int[6];
   boolean[] inPenaltyBox = new boolean[6];

  List<String> popQuestions = new LinkedList<>();
  List<String> scienceQuestions = new LinkedList<>();
  List<String> sportsQuestions = new LinkedList<>();
  List<String> rockQuestions = new LinkedList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.add("Pop Question " + i);
         scienceQuestions.add(("Science Question " + i));
         sportsQuestions.add(("Sports Question " + i));
         rockQuestions.add(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

    public boolean add(String playerName) {

      players.add(new Player(playerName));
      purses[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;

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

      if (inPenaltyBox[currentPlayer]) {
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
        players.get(currentPlayer).place(currentPlayer().place() + roll);
        if (currentPlayer().place() >= 12) {
            players.get(currentPlayer).place(currentPlayer().place() - 12);
        }
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
      if (inPenaltyBox[currentPlayer]) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            purses[currentPlayer]++;
            System.out.println(players.get(currentPlayer).getName()
                               + " now has "
                               + purses[currentPlayer]
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
         purses[currentPlayer]++;
         System.out.println(players.get(currentPlayer).getName()
                            + " now has "
                            + purses[currentPlayer]
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
      inPenaltyBox[currentPlayer] = true;

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean didPlayerWin() {
      return !(purses[currentPlayer] == 6);
   }
}
