package com.aliengame.client.controller;

import com.aliengame.client.entity.ScoreRecord;
import com.aliengame.client.helper.StagePrepareHelper;
import com.aliengame.client.service.RESTService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScoreController implements Initializable {

    @FXML
    private Button mainMenuButton;
    @FXML
    private TableView<ScoreRecord> weeklyTableView;
    @FXML
    private TableView<ScoreRecord> monthlyTableView;
    @FXML
    private TableColumn<ScoreRecord, String> weeklyUserColumn;
    @FXML
    private TableColumn<ScoreRecord, String> weeklyScoreColumn;
    @FXML
    private TableColumn<ScoreRecord, String> monthlyUserColumn;
    @FXML
    private TableColumn<ScoreRecord, String> monthlyScoreColumn;


    /**
     * This method initialize the score boards table and insert related records from database.
     * @param location  URL
     * @param resources Resource Bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        weeklyUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        weeklyScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        monthlyUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        monthlyScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        weeklyTableView.getColumns().add(weeklyUserColumn);
        weeklyTableView.getColumns().add(weeklyScoreColumn);
        monthlyTableView.getColumns().add(monthlyUserColumn);
        monthlyTableView.getColumns().add(monthlyUserColumn);
        RESTService restService = new RESTService();
        try {
            for (ScoreRecord scoreRecord : restService.getTimeDomainedScores("getWeeklyScores")) {
                weeklyTableView.getItems().add(scoreRecord);
            }
            for (ScoreRecord scoreRecord : restService.getTimeDomainedScores("getMonthlyScores")) {
                monthlyTableView.getItems().add(scoreRecord);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method prepares the Main Menu page.
     * @param event Event of button
     * @throws IOException It throws an exception if it is not load to FXML
     */
    @FXML
    protected void handleMainMenuButtonAction(ActionEvent event) throws IOException {
        StagePrepareHelper stagePrepareHelper = new StagePrepareHelper();
        stagePrepareHelper.prepareStage(event, URLs.FXML_MAIN_PAGE.getUrl(), "");
    }
}
