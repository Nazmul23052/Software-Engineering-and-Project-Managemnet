package com.quiz;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.*;
import java.util.*;

public class QuizController {

    @FXML private Label questionLabel, timerLabel, resultLabel;
    @FXML private RadioButton opt1, opt2, opt3, opt4;
    @FXML private TextField nameField;

    private ToggleGroup group;
    private List<Question> questionList;
    private int currentIndex = 0;
    private int score = 0;
    private int timeSeconds = 15;
    private Timeline timeline;

    public void initialize() {
        group = new ToggleGroup();
        opt1.setToggleGroup(group);
        opt2.setToggleGroup(group);
        opt3.setToggleGroup(group);
        opt4.setToggleGroup(group);

        loadQuestions();
        showQuestion();
        startTimer();
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 5";
            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                questionList.add(new Question(
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getString("correct_answer")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuestion() {
        if (currentIndex >= questionList.size()) {
            timeline.stop();
            saveScore();
            resultLabel.setText("Quiz Finished! Score: " + score);
            return;
        }

        Question q = questionList.get(currentIndex);
        questionLabel.setText(q.question);
        opt1.setText(q.opt1);
        opt2.setText(q.opt2);
        opt3.setText(q.opt3);
        opt4.setText(q.opt4);
    }

    @FXML
    private void nextQuestion() {
        RadioButton selected = (RadioButton) group.getSelectedToggle();

        if (selected != null &&
                selected.getText().equals(questionList.get(currentIndex).correct)) {
            score++;
        }

        group.selectToggle(null);
        currentIndex++;
        timeSeconds = 15;
        showQuestion();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds--;
            timerLabel.setText("Time: " + timeSeconds);

            if (timeSeconds <= 0) {
                nextQuestion();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void saveScore() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO scores(player_name, score) VALUES (?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setInt(2, score);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void restartGame() {
        score = 0;
        currentIndex = 0;
        loadQuestions();
        showQuestion();
        timeline.play();
        resultLabel.setText("");
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }

    class Question {
        String question, opt1, opt2, opt3, opt4, correct;

        Question(String q, String o1, String o2, String o3, String o4, String c) {
            question = q;
            opt1 = o1;
            opt2 = o2;
            opt3 = o3;
            opt4 = o4;
            correct = c;
        }
    }
}
