package org.example.personalexpensetracker.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.personalexpensetracker.common.CommonUtil;
import org.example.personalexpensetracker.dao.ExpenseDao;
import org.example.personalexpensetracker.dao.IncomeDao;
import org.example.personalexpensetracker.entity.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController {

    @FXML
    private TableColumn<Summary, Double> totalBalanceColumn;

    @FXML
    private TableColumn<Expense, Double> totalExpenseColumn;

    @FXML
    private MenuItem expenseBtn;

    @FXML
    private MenuItem incomeBtn;

    @FXML
    private MenuItem profileBtn;
    @FXML
    private TableView<Summary> tableView;
    @FXML
    private TableColumn<Income, Double> totalIncomeColumn;

    @FXML
    private MenuItem transactionBtn;

    @FXML
    private MenuItem signOutBtn;

    @Getter
    @FXML
    private ComboBox<String> monthComboBox;

    private IncomeDao incomeDao = new IncomeDao();
    private ExpenseDao expenseDao = new ExpenseDao();

    @FXML
    void expenseBtnClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/expense.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add Income");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(CommonUtil.getPrimaryStage());
        stage.showAndWait();
        loadSummaryData();
    }

    @FXML
    void incomeBtnClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/income.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add Income");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(CommonUtil.getPrimaryStage());
        stage.showAndWait();
        loadSummaryData();
    }

    @FXML
    void profileBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/profile.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("User Profile");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(CommonUtil.getPrimaryStage());
        stage.showAndWait();
    }


    @FXML
    void transactionBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/transaction.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Transaction");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(CommonUtil.getPrimaryStage());
        stage.showAndWait();
        loadSummaryData();

    }

    @FXML
    void signOutBtnClicked(ActionEvent event) throws IOException {
        // Clear session
        Session.clear();  // you need a clear() method in your Session class to reset user info

        // Close current window
        Stage stage = CommonUtil.getPrimaryStage();
        stage.close();

        // Open login window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/personalexpensetracker/common/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = CommonUtil.getPrimaryStage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void monthComboBoxClicked(ActionEvent event) {
        int selectedMonth = monthComboBox.getSelectionModel().getSelectedIndex() + 1;
        loadChartsForMonth(selectedMonth);
    }

    @FXML
    private PieChart expensePieChart;

    @FXML
    private PieChart incomePieChart;


public void loadExpenseChart() {
    int userId = Session.getUserId();
    Map<String, Double> expenseByCategory = expenseDao.getExpenseByCategory(userId);
    setPieChartData(expensePieChart, expenseByCategory);
}

    public void loadExpenseChart(int month) {
        int userId = Session.getUserId();
        Map<String, Double> expenseByCategory = expenseDao.getExpenseByCategoryByMonth(userId, month);
        setPieChartData(expensePieChart, expenseByCategory);
    }

    public void loadIncomeChart() {
        int userId = Session.getUserId();
        Map<String, Double> incomeByCategory = incomeDao.getIncomeByCategory(userId);
        setPieChartData(incomePieChart, incomeByCategory);
    }

    public void loadIncomeChart(int month) {
        int userId = Session.getUserId();
        Map<String, Double> incomeByCategory = incomeDao.getIncomeByCategoryByMonth(userId, month);
        setPieChartData(incomePieChart, incomeByCategory);
    }


    @FXML
    public void initialize() {
        // bind table columns
        totalIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        totalExpenseColumn.setCellValueFactory(new PropertyValueFactory<>("totalExpense"));
        totalBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        ObservableList<String> months = FXCollections.observableArrayList(
                "Total","January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setItems(months);
        monthComboBox.setValue("Total");
        loadIncomeChart();
        loadExpenseChart();
        // Add listener when user changes month
        monthComboBox.setOnAction(e -> {
            String selected = monthComboBox.getValue();
            if ("Total".equals(selected)) {
                // Load all-time charts
                loadIncomeChart();
                loadExpenseChart();
                loadSummaryData();
            } else {
                // Load monthly charts
                int month = monthComboBox.getSelectionModel().getSelectedIndex(); // January = 1, etc.
                loadChartsForMonth(month);
                loadSummaryData(month);
            }
        });

        loadSummaryData();

    }
    public void loadChartsForMonth(int month) {

        loadIncomeChart(month);
        loadExpenseChart(month);

    }

    public void loadSummaryData() {

        int userId = Session.getUserId();
        double totalIncome = incomeDao.getTotalIncome(userId);
        double totalExpense = expenseDao.getTotalExpense(userId);
        double balance = totalIncome - totalExpense;
        Summary summary = new Summary(totalIncome, totalExpense, balance);
        tableView.getItems().clear();
        tableView.getItems().add(summary);

    }

    public void loadSummaryData(int month) {
        int userId = Session.getUserId();
        double totalIncome = incomeDao.getTotalIncomeByMonth(userId, month);
        double totalExpense = expenseDao.getTotalExpenseByMonth(userId, month);
        double balance = totalIncome - totalExpense;

        Summary summary = new Summary(totalIncome, totalExpense, balance);
        tableView.getItems().clear();
        tableView.getItems().add(summary);
    }

    private void setPieChartData(PieChart chart, Map<String, Double> categoryData) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double total = categoryData.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
            // Label: "Category : Amount (Percentage%)"
            String label = entry.getKey() + " : " + String.format("%.2f", entry.getValue()) +
                    " (" + String.format("%.1f", (entry.getValue() / total * 100)) + "%)";
            pieChartData.add(new PieChart.Data(label, entry.getValue()));
        }

        chart.setData(pieChartData);
    }

}
