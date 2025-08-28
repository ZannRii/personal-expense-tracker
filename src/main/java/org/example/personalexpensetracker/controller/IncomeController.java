package org.example.personalexpensetracker.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.personalexpensetracker.common.CommonUtil;
import org.example.personalexpensetracker.dao.CategoryDao;
import org.example.personalexpensetracker.dao.IncomeDao;
import org.example.personalexpensetracker.dao.PaymentDao;
import org.example.personalexpensetracker.entity.*;

import java.util.List;

public class IncomeController {

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
    private Income income;
    private Category category;
    private CategoryDao categoryDao = new CategoryDao();
    private PaymentDao paymentDao =  new PaymentDao();
    @FXML
    public void initialize() {

        List<Category> categoryList = categoryDao.getAllCategory();
        List<String> incomeCategories = categoryList.stream()
                .filter(c -> "Income".equalsIgnoreCase(c.getType()))
                .map(Category::getCategory_name)
                .toList();
        ObservableList<String> observableList = FXCollections.observableArrayList(incomeCategories);
        categoryTextField.setItems(observableList);


        List<PaymentMethod> paymentMethodList = paymentDao.getAllMethod();
        List<String> methodNames= paymentMethodList.stream()
                .map(PaymentMethod::getMethod_name) // get only the name
                .toList();
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
        income = new Income();
        income.setUser_id(Session.getUserId());
        income.setCategory_id(categoryDao.findByName(categoryTextField.getValue()).getCategory_id());
        income.setPayment_method_id(paymentDao.findByName(paymentTextField.getValue()).getPayment_method_id());
        income.setAmount(Double.parseDouble(amountTextField.getText()));
        income.setDate(dateBtn.getValue().atStartOfDay());
        IncomeDao incomeDao = new IncomeDao();
        incomeDao.insert(income);
        HomeController homeController = CommonUtil.getHomeController();
        if (homeController != null) {
            homeController.getMonthComboBox().setValue("Total");
            homeController.loadIncomeChart();  // updates the displayed chart
        }
        System.out.println("Income saved for user " + Session.getUser().getUsername());
        ((Stage) saveBtn.getScene().getWindow()).close();

    }

}
