import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.input.KeyEvent;

/**
 * @author ii79
 * @version 1.1
 * Class that creates a "Jordle" game.
 */
public class Jordle1 extends Application {
    public Label[][] arr = new Label[6][5];
    public Rectangle[][] rec = new Rectangle[6][5];
    public static ArrayList<String> list =  new ArrayList<>();
    public static String word;
    public int rowCounter = 0;
    public int columnCounter = 0;
    public boolean won = false;
    public Label messageDisplay = new Label("Try guessing a word!");
    public int greenCounter = 0;


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.setTranslateY(20);
        Text title = new Text("Wordle");
        title.setFont(Font.font("Impact", 40));
        pane.getChildren().add(title);
        grid.setHgap(5);
        grid.setVgap(5);
        root.setCenter(grid);
        root.setTop(pane);
        root.setPrefSize(900, 900);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                rec[i][j] = new Rectangle(100, 100, null);
                rec[i][j].setStroke(Color.BLACK);
                rec[i][j].setTranslateX(j + 180);
                rec[i][j].setTranslateY(i + 40);
                arr[i][j] = new Label();
                arr[i][j].setTranslateX(j + 215);
                arr[i][j].setTranslateY(i + 40);
                arr[i][j].setFont(new Font("Helvetica", 50));
                grid.add(rec[i][j], j, i, 1, 1);
                grid.add(arr[i][j], j, i);
            }
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage errorStage = new Stage();
        Text errorMessage = new Text("Error. Please enter a 5-letter word!");
        errorMessage.setFont(new Font("Lucidia Sans Unicode", 20));
        StackPane errorPane = new StackPane();
        errorPane.getChildren().add(errorMessage);
        Scene errorScene = new Scene(errorPane, 300, 300);
        errorStage.setScene(errorScene);
        messageDisplay.setFont(new Font("Lucidia Sans Unicode", 15));

        Stage instruction = new Stage();
        StackPane jordleInstructions = new StackPane();
        Text directions = new Text("Guess the Jordle (a 5-letter word) in 6 tries or less. \n" + "\n"
                + "If the guess a letter in the word in the correct spot, the box turns green. \n" + "\n"
                + "If the letter is in the word, but in the wrong spot, the box turns yellow. \n" + "\n"
                + "Else, the letter is not in the word, and the box turns grey. \n" + "\n"
                + "No letters wil be repeated in a word.");
        directions.setFont(Font.font("Lucida Sans Unicode", 20));
        jordleInstructions.setAlignment(Pos.CENTER);
        jordleInstructions.setTranslateY(10);
        jordleInstructions.getChildren().add(directions);
        Scene instructionScene = new Scene(jordleInstructions, 600, 600);
        instruction.setScene(instructionScene);
        HBox instructionPane = new HBox();
        instructionPane.setTranslateY(-10);
        instructionPane.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(instructionPane);



        Button instructions = new Button("Instructions");
        Button restart = new Button("Restart");
        instructions.setOnAction(e -> instruction.show());
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        arr[i][j].setText("");
                        rec[i][j].setFill(null);
                        rowCounter = 0;
                        columnCounter = 0;
                        messageDisplay.setText("Try guessing a word!");


                    }
                }
               word = list.get(generateRandom());
            }
        });

        instructionPane.getChildren().addAll(messageDisplay, restart, instructions);
        instructionPane.setSpacing(10);

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().toString().equals("ENTER")) {
                    if (columnCounter < 5) {
                        errorStage.show();
                    } else {
                        evaluateWord();
                        evaluateWin();
                        greenCounter = 0;
                    }
                }
                if (keyEvent.getCode().toString().equals("BACK_SPACE")) {
                    if (columnCounter == 0) {
                        arr[rowCounter][0].setText("");
                    } else if (columnCounter > 0) {
                        arr[rowCounter][columnCounter - 1].setText("");
                        columnCounter -= 1;
                    }
                }

                if (keyEvent.getCode().isLetterKey() && rowCounter < 6 && columnCounter < 5) {
                    arr[rowCounter][columnCounter].setText(keyEvent.getCode().toString());
                    columnCounter += 1;
                }
            }
        });


    }


    /**
     * Method that generates a random index number to select a random word.
     * @return int representing a random number index
     */

    public static int generateRandom() {
        Random rand = new Random();
        int wordIndex = rand.nextInt(list.size());
        return wordIndex;
    }

    /**
     * Method that compares the user's guess to the correct word.
     */

    public void evaluateWord() {
        for (int i = 0; i < 5; i++) {
            if ((arr[rowCounter][i].getText()).equals((word.charAt(i) + "").toUpperCase(Locale.ROOT))) {
                updateColorGreen(i);
            } else if ((word.toUpperCase(Locale.ROOT)).contains((arr[rowCounter][i].getText()))) {
                updateColorYellow(i);
            } else {
                updateColorGray(i);
            }
        }


        for(int i = 0; i < 5; i++) {
            if (rec[rowCounter][i].getFill() == Color.GREEN) {
                greenCounter += 1;
            } else {
                won = false;
            }
        }
        rowCounter += 1;
        columnCounter = 0;
        if (greenCounter == 5) {
            won = true;
        }
    }

    /**
     * Method that updates the color of the "Jordle" rectangle to green.
     * @param i int representing the index of a column
     */
    public void updateColorGreen(int i) {
        rec[rowCounter][i].setFill(Color.GREEN);
    }

    /**
     * Method that updates the color of the "Jordle" rectangle to yellow.
     * @param i int representing the index of a column
     */
    public void updateColorYellow(int i) {
        rec[rowCounter][i].setFill(Color.YELLOW);
    }

    /**
     * Method that updates the color of the "Jordle" rectangle to gray.
     *
     * @param i int representing the index of a column
     */
    public void updateColorGray(int i) {
        rec[rowCounter][i].setFill(Color.GRAY);
    }

    /**
     * Method that determines whether the user won the game.
     */
    public void evaluateWin() {
        if (won) {
            messageDisplay.setText("Congratulations! You've guessed the word!");
        } else if (!(won) && rowCounter == 6 && !(messageDisplay.getText().equals("Congratulations! "
                + "You've guessed the word!"))) {
            messageDisplay.setText("Game over. The word was " + word);
        }
    }




    /**public static void readFile() throws IOException {} **/


    public static ArrayList<String> createList() throws IOException {
        try {
            File file = new File("/yourlocalpath/words.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().length() == 5) {
                    list.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }


    /**
     * Main method.
     * @param args command line arguments
     */

    public static void main(String[] args) throws IOException {
        list = createList();
        word = list.get(generateRandom());
        launch(args);

    }

}

