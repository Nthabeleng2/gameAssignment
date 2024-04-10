package com.example.gameassignment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloApplication extends Application {
    private final String[][] allQuestions = {
            {"How many districts does Lesotho have?", "A. 4", "B. 10", "C. 8", "D. 5"},
            {"What is the capital city of Lesotho?", "A. Cape Town", "B. Harare", "C. Maseru", "D. Eswatini"},
            {"Who is the current king of Lesotho?", "A. King Letsie", "B. King Moswati", "C. King Akhenaten", "D. King Shaka"},
            {"What is the official language of Lesotho?", "A. English", "B. French", "C. Spanish", "D. Sesotho"},
            {"What is the highest fall in Lesotho?", "A. Tugela Falls", "B. Victoria Falls", "C. Maletsunyane Falls", "D. Kongou Falls"}
    };
    private int total = 0;
    private int questionIndex = 0;

    private Label questionLabel;
    private VBox box;
    private VBox mediaBox;
    private Label timerLabel;

    private Image[] images = {
            new Image(getClass().getResourceAsStream("/com/example/gameassignment/images/1.jpg")),
            new Image(getClass().getResourceAsStream("/com/example/gameassignment/images/2.jpg")),
            new Image(getClass().getResourceAsStream("/com/example/gameassignment/images/3.jpeg")),
            new Image(getClass().getResourceAsStream("/com/example/gameassignment/images/4.jpg")),
            new Image(getClass().getResourceAsStream("/com/example/gameassignment/images/5.jpg"))
    };

    private Media endVideo = new Media(getClass().getResource("/com/example/gameassignment/videos/5.mp4").toString());
    private MediaPlayer endMediaPlayer = new MediaPlayer(endVideo);
    private MediaView endMediaView = new MediaView(endMediaPlayer);

    private Timeline timer;

    @Override
    public void start(Stage stage) {
        questionLabel = new Label();
        box = new VBox(10);
        box.setPadding(new Insets(20));

        mediaBox = new VBox(10);
        mediaBox.setPadding(new Insets(20));

        timerLabel = new Label();
        timerLabel.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(questionLabel);
        root.setCenter(mediaBox);
        root.setRight(timerLabel);

        HBox controlButtons = new HBox(10);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBack());
        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> goNext());
        Button replayButton = new Button("Replay");
        replayButton.setOnAction(e -> replay());
        controlButtons.getChildren().addAll(backButton, nextButton, replayButton);

        VBox questionAndButtons = new VBox(10);
        questionAndButtons.getChildren().addAll(box, controlButtons);
        root.setBottom(new StackPane(questionAndButtons));

        QuestionsAndAnswers();

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private void QuestionsAndAnswers() {
        if (questionIndex < allQuestions.length) {
            questionLabel.setText(allQuestions[questionIndex][0]);
            box.getChildren().clear();
            mediaBox.getChildren().clear();

            ImageView imageView = new ImageView(images[questionIndex]);
            imageView.setFitWidth(300);
            imageView.setFitHeight(250);
            imageView.setPreserveRatio(true);
            mediaBox.getChildren().add(imageView);

            for (int i = 1; i < allQuestions[questionIndex].length; i++) {
                final int optionIndex = i - 1;
                Label label = new Label(allQuestions[questionIndex][i]);
                label.setOnMouseClicked(e -> Feedback(optionIndex));
                label.setOnMouseEntered(e -> label.setUnderline(true));
                label.setOnMouseExited(e -> label.setUnderline(false));
                box.getChildren().add(label);
            }

            timer = new Timeline(new KeyFrame(Duration.seconds(10), e -> goNext()));
            timer.setCycleCount(1);
            timer.play();

            timerLabel.setText("Time remaining: 10 seconds");

            Timeline countdown = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                int remainingTime = (int) timer.getCurrentTime().toSeconds();
                timerLabel.setText("Time remaining: " + (10 - remainingTime) + " seconds");
            }));
            countdown.setCycleCount(Timeline.INDEFINITE);
            countdown.play();
        } else {
            end();
        }
    }

    private void Feedback(int optionIndex) {
        String selectedAnswer = allQuestions[questionIndex][optionIndex + 1];
        String correctAnswer = getCorrectAnswer(questionIndex);
        if (selectedAnswer.equals(correctAnswer)) {
            total++;
        }
        timer.stop();
        questionIndex++;
        QuestionsAndAnswers();
    }

    private String getCorrectAnswer(int questionIndex) {
        switch (questionIndex) {
            case 0:
                return "B. 10";
            case 1:
                return "C. Maseru";
            case 2:
                return "A. King Letsie";
            case 3:
                return "D. Sesotho";
            case 4:
                return "C. Maletsunyane Falls";
            default:
                return "";
        }
    }

    private void end() {
        questionLabel.setText("You have obtained: " + total + "/" + allQuestions.length);
        box.getChildren().clear();
        mediaBox.getChildren().clear();
        endMediaView.setFitWidth(450);
        endMediaView.setFitHeight(400);
        endMediaView.setPreserveRatio(true);
        mediaBox.getChildren().add(endMediaView);
        endMediaPlayer.play();
    }

    private void goNext() {
        questionIndex++;
        QuestionsAndAnswers();
    }

    private void goBack() {
        if (questionIndex > 0) {
            questionIndex--;
            QuestionsAndAnswers();
        }
    }

    private void replay() {
        questionIndex = 0;
        total = 0;
        QuestionsAndAnswers();
    }

    public static void main(String[] args) {
        launch();
    }
}
