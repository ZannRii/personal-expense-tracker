package org.example.personalexpensetracker.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.personalexpensetracker.common.CommonUtil;
import org.example.personalexpensetracker.dao.CategoryDao;
import org.example.personalexpensetracker.dao.ExpenseDao;
import org.example.personalexpensetracker.dao.IncomeDao;
import org.example.personalexpensetracker.dao.PaymentDao;
import org.example.personalexpensetracker.entity.*;

import java.util.List;

public class ExpenseController {

    @FXML
    private TextField amountTextField;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> categoryTextField;

    @FXML
    private DatePicker dateBtn;

    @FXML
    private ComboBox<String> paymentTextField;

    @FXML
    private Button saveBtn;

    private User user;
    private Expense expense;
    private Category category;
    private CategoryDao categoryDao = new CategoryDao();
    private PaymentDao paymentDao =  new PaymentDao();
    private IncomeDao incomeDao = new IncomeDao();
    private ExpenseDao expenseDao = new ExpenseDao();
    @FXML
    public void initialize() {
        
        List<Category> categoryList = categoryDao.getAllCategory();
        List<String> expenseCategories = categoryList.stream()
                .filter(c -> "Expense".equalsIgnoreCase(c.getType()))
                .map(Category::getCategory_name)
                .toList();
        ObservableList<String> observableList = FXCollections.observableArrayList(expenseCategories);
        categoryTextField.setItems(observableList);


        List<PaymentMethod> paymentMethodList = paymentDao.getAllMethod();
        List<String> methodNames= paymentMethodList.stream()
                .map(PaymentMethod::getMethod_name) // get only the name
                .toList(); // collect as list
        ObservableList<String> paymentobservableList = FXCollections.observableArrayList(methodNames);
        paymentTextField.setItems(paymentobservableList);
    }

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    @FXML
    void categoryTextFieldClicked(ActionEvent event) {

    }

    @FXML
    void dateBtnClicked(ActionEvent event) {

    }

    @FXML
    void paymentTextFieldClicked(ActionEvent event) {

    }

    @FXML
    void saveBtnClicked(ActionEvent event) {

        double amount = Double.parseDouble(amountTextField.getText()); // example
        int userId = Session.getUserId();

        double totalIncome = incomeDao.getTotalIncome(userId);
        double totalExpense = expenseDao.getTotalExpense(userId);
        double currentBalance = totalIncome - totalExpense;

        if (amount > currentBalance) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Expense");
            alert.setHeaderText(null);
            alert.setContentText("You cannot add an expense that exceeds your current balance!");
            alert.showAndWait();
            return;
        }

        expense = new Expense();
        expense.setUser_id(Session.getUserId());
        expense.setCategory_id(categoryDao.findByName(categoryTextField.getValue()).getCategory_id());
        expense.setPayment_method_id(paymentDao.findByName(paymentTextField.getValue()).getPayment_method_id());
        expense.setAmount(Double.parseDouble(amountTextField.getText()));
        expense.setDate(dateBtn.getValue().atStartOfDay());
        ExpenseDao expenseDao = new ExpenseDao();
        expenseDao.insert(expense);
        HomeController homeController = CommonUtil.getHomeController();
        if (homeController != null) {
            homeController.getMonthComboBox().setValue("Total");
            homeController.loadExpenseChart();  // updates the displayed chart
        }

        System.out.println("Expense saved for user " + Session.getUser().getUsername());
        ((Stage) saveBtn.getScene().getWindow()).close();

    }

}
