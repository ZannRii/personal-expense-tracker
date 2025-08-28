package org.example.personalexpensetracker.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.personalexpensetracker.common.CommonUtil;
import org.example.personalexpensetracker.dao.CategoryDao;
import org.example.personalexpensetracker.dao.TransactionDao;
import org.example.personalexpensetracker.entity.Category;
import org.example.personalexpensetracker.entity.Session;
import org.example.personalexpensetracker.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionController {

    @FXML
    private Button allBtn;

    @FXML
    private TableColumn<Transaction, Double> amountCol;

    @FXML
    private TableColumn<Transaction, String> categoryCol;

    @FXML
    private TableColumn<Transaction, LocalDate> dateCol;


    @FXML
    private Button expenseBtn;

    @FXML
    private Button filterBtn;

    @FXML
    private Button backBtn;

    @FXML
    private DatePicker fromDate;

    @FXML
    private Button incomeBtn;

    @FXML
    private DatePicker toDate;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeCol;

    @FXML
    private TableColumn<Transaction, Void> actionCol;

    private TransactionDao transactionDao = new TransactionDao();
    private CategoryDao categoryDao =  new CategoryDao();
    private ObservableList<Transaction> transactionList;
    // In TransactionController
    @Setter
    private Runnable onTransactionsChanged;

    @FXML
    public void initialize() {
        int userId = Session.getUserId();
        // Initialize table columns
        typeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType())
        );
        amountCol.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject()
        );
        categoryCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory_name())
        );
        dateCol.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate().toLocalDate())
        );


        // Load all transactions
        List<Transaction> allTransactions = transactionDao.getAllTransactions(userId);
        transactionList = FXCollections.observableArrayList(allTransactions);
        transactionTable.setItems(transactionList);

        // Button actions
        allBtn.setOnAction(e -> {transactionTable.setItems(transactionList);addActionButtons();});
        incomeBtn.setOnAction(e -> {filterByType("Income");addActionButtons();});
        expenseBtn.setOnAction(e -> {filterByType("Expense"); addActionButtons();});
        filterBtn.setOnAction(e -> {filterByDate();addActionButtons();});
        addActionButtons();
        refreshData();

    }

    private void filterByType(String type) {
        List<Transaction> filtered = transactionList.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        transactionTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void filterByDate() {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        List<Transaction> filtered = transactionList.stream()
                .filter(t -> (from == null || !t.getDate().toLocalDate().isBefore(from)) &&
                        (to == null || !t.getDate().toLocalDate().isAfter(to)))
                .collect(Collectors.toList());
        transactionTable.setItems(FXCollections.observableArrayList(filtered));
    }
    private void addActionButtons() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(10, editBtn, deleteBtn);

            {
                // Edit button action
                editBtn.setOnAction(e -> {
                    Transaction txn = getTableView().getItems().get(getIndex());
                    handleEdit(txn);
                });

                // Delete button action
                deleteBtn.setOnAction(e -> {
                    Transaction txn = getTableView().getItems().get(getIndex());
                    handleDelete(txn);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

private void handleEdit(Transaction txn) {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Edit Transaction");

    // Buttons
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    // Fields
    TextField amountField = new TextField(String.valueOf(txn.getAmount()));
    DatePicker datePicker = new DatePicker(txn.getDate().toLocalDate());

    // Category ComboBox
    ComboBox<String> categoryBox = new ComboBox<>();
    String txnType = txn.getType(); // "Income" or "Expense"
    categoryBox.setItems(FXCollections.observableArrayList(
            categoryDao.getCategoryNamesByType(txnType)
    ));
    categoryBox.setValue(txn.getCategory_name());

    // Layout
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Amount:"), 0, 0);
    grid.add(amountField, 1, 0);
    grid.add(new Label("Date:"), 0, 1);
    grid.add(datePicker, 1, 1);
    grid.add(new Label("Category:"), 0, 2);
    grid.add(categoryBox, 1, 2);

    dialog.getDialogPane().setContent(grid);

    // Handle result
    dialog.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                double newAmount = Double.parseDouble(amountField.getText().trim());
                LocalDate newDate = datePicker.getValue();
                String newCategory = categoryBox.getValue();

                // Validation
                if (newAmount <= 0) {
                    new Alert(Alert.AlertType.WARNING, "Amount must be greater than zero!").showAndWait();
                    return;
                }
                if (newDate == null || newCategory == null) {
                    new Alert(Alert.AlertType.WARNING, "Date and category are required!").showAndWait();
                    return;
                }

                // Check balance if Expense
                if ("Expense".equalsIgnoreCase(txnType)) {
                    double currentBalance = transactionDao.getCurrentBalance(Session.getUserId());
                    // If new amount is more than available balance + old expense amount
                    if (newAmount > currentBalance + txn.getAmount()) {
                        new Alert(Alert.AlertType.WARNING,
                                "New expense amount exceeds your available balance!").showAndWait();
                        return;
                    }
                }

                // Update transaction
                txn.setAmount(newAmount);
                txn.setDate(newDate.atStartOfDay()); // if LocalDateTime
                txn.setCategory_name(newCategory);

                transactionDao.updateTransaction(txn);
                refreshData();

                // Show success alert
                new Alert(Alert.AlertType.INFORMATION, "Transaction updated successfully!").showAndWait();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid amount!").showAndWait();
            }
        }
    });
}


    private void handleDelete(Transaction txn) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this transaction?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                transactionDao.deleteTransaction(txn.getId(), txn.getType());

                // Refresh after delete
                refreshData();
            }
        });
    }

    // New method to reload all data
    private void refreshData() {
        transactionList.setAll(transactionDao.getAllTransactions(Session.getUserId()));
        transactionTable.setItems(transactionList);
        transactionTable.refresh();

        // Notify homepage/dashboard controller if it exists
        if (onTransactionsChanged != null) {
            onTransactionsChanged.run();
        }
    }

    @FXML
    void backBtnClicked(ActionEvent event) {
        ((Stage) backBtn.getScene().getWindow()).close();
        HomeController homeController = CommonUtil.getHomeController();
        if (homeController != null) {
            homeController.getMonthComboBox().setValue("Total");
            homeController.loadExpenseChart();
            homeController.loadIncomeChart();// updates the displayed chart
        }

    }
}
