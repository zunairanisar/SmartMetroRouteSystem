package com.example.demo1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class SmartMetroGUI extends Application {

    Vehicle bus1 = new Vehicle("BUS-1001", MetroStations.stations, "5 minutes", 20);
    Vehicle bus2 = new Vehicle("BUS-2001", MetroStations.stations, "10 minutes", 25);
    Vehicle bus3 = new Vehicle("BUS-3001", MetroStations.stations, "15 minutes", 30);
    Vehicle[] buses = {bus1, bus2, bus3};

    Stage window;
    Scene homeScene, bookingScene, paymentScene, receiptScene;

    String passengerName, passengerCNIC;
    String startStation, destStation;
    String selectedBusID, arrivalTime, crowdLevel;
    int travelTime;
    String bookingTime, paidAmount;

    ArrayList<Ticket> passengerTickets = new ArrayList<>();

    TextField nameField, cnicField;
    ComboBox<String> start, dest;
    Label timeLabel;
    Label rec = new Label();

    @Override
    public void start(Stage stage) {
        window = stage;
        window.setTitle("Smart Metro Route System");
        window.setResizable(true);

        loadTickets();
        buildHome();
        buildBooking();
        buildPayment();
        buildReceipt();

        window.setOnCloseRequest(e -> {
            saveTickets();
            System.out.println("Data saved before exit.");
        });

        window.setScene(homeScene);
        window.show();
    }

    void buildHome() {

        Label title = new Label("Smart Metro Route System");
        title.setFont(new Font("Arial Bold", 32));
        title.setTextFill(Color.WHITE);


        Button bookBtn = new Button("Book Ticket");
        Button stationsBtn = new Button("View Stations");
        Button busBtn = new Button("Bus Information");
        Button viewTicketsBtn = new Button("View Past Bookings");
        Button exit = new Button("Exit");

        bookBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-font-size:16;");
        stationsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-font-size:16;");
        busBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-font-size:16;");
        viewTicketsBtn.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-font-size:16;");
        exit.setStyle("-fx-background-color: rgba(255,0,0,0.7); -fx-text-fill: white; -fx-font-size:16;");

        bookBtn.setPrefWidth(220);
        stationsBtn.setPrefWidth(220);
        busBtn.setPrefWidth(220);
        viewTicketsBtn.setPrefWidth(220);
        exit.setPrefWidth(220);

        bookBtn.setOnAction(e -> {
            resetBookingFields();
            window.setScene(bookingScene);
        });
        stationsBtn.setOnAction(e -> showStations());
        busBtn.setOnAction(e -> showBusInfo());
        viewTicketsBtn.setOnAction(e -> {
            buildReceipt();
            window.setScene(receiptScene);
        });
        exit.setOnAction(e -> window.close());

        VBox layout = new VBox(20, title, bookBtn, stationsBtn, busBtn, viewTicketsBtn, exit);
        layout.setAlignment(Pos.CENTER);


        try {
            Image img = new Image("file:///C:/Users/HP/Desktop/Zunaira.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(1100);
            imgView.setFitHeight(600);
            imgView.setPreserveRatio(false);

           StackPane root = new StackPane(imgView, layout);
           homeScene = new Scene(root, 600, 600);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Image not found. Check your path!");
            homeScene = new Scene(layout, 600, 600);
        }
    }
    void buildBooking() {

        Label head = new Label("Book Ticket");
        head.setFont(new Font("Arial Bold", 30));
        head.setTextFill(Color.YELLOW);

        String fieldStyle = "-fx-background-color: white; -fx-font-size: 14px; -fx-background-radius: 5;";

        nameField = new TextField();
        nameField.setPromptText("Enter Name");
        nameField.setStyle(fieldStyle);

        cnicField = new TextField();
        cnicField.setPromptText("13-digit CNIC");
        cnicField.setStyle(fieldStyle);

        cnicField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 13) {
                cnicField.setText(oldValue);
            }
        });
        cnicField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        }));

        start = new ComboBox<>();
        dest = new ComboBox<>();
        start.getItems().addAll(MetroStations.stations);
        dest.getItems().addAll(MetroStations.stations);
        start.setStyle(fieldStyle);
        dest.setStyle(fieldStyle);

        timeLabel = new Label("--");
        timeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button calc = new Button("Calculate Time");
        calc.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Button proceed = new Button("Proceed to Payment");
        proceed.setStyle("-fx-background-color: green; -fx-text-fill: black; -fx-font-weight: bold; -fx-cursor: hand;");

        Button back = new Button("Back");
        back.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        calc.setMaxWidth(Double.MAX_VALUE);
        proceed.setMaxWidth(Double.MAX_VALUE);
        back.setMaxWidth(Double.MAX_VALUE);

        calc.setOnAction(e -> {
            String s = start.getValue();
            String d = dest.getValue();
            if (s == null || d == null) {
                timeLabel.setText("Select both stations");
                return;
            }
            travelTime = RouteManager.calculateTravelTime(s, d, MetroStations.stations);
            timeLabel.setText(travelTime + " minutes");
        });

        proceed.setOnAction(e -> {
            if (nameField.getText().isEmpty() || cnicField.getText().isEmpty() ||
                    start.getValue() == null || dest.getValue() == null) {
                alert("Error", "Please fill all fields.");
                return;
            }

            if (cnicField.getText().length() != 13) {
                alert("Error", "Invalid CNIC! (Must be exactly 13 digits)");
                return;
            }

            passengerName = nameField.getText();
            passengerCNIC = cnicField.getText();
            startStation = start.getValue();
            destStation = dest.getValue();
            bookingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            travelTime = RouteManager.calculateTravelTime(startStation, destStation, MetroStations.stations);
            window.setScene(paymentScene);
        });

        back.setOnAction(e -> window.setScene(homeScene));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(30));
        grid.setVgap(15);
        grid.setHgap(15);

        grid.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-background-radius: 20; -fx-border-color: white; -fx-border-radius: 20; -fx-border-width: 2;");
        grid.setMaxSize(400, 580);

        String labelStyle = "-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: white;";

        Label lblName = new Label("Name:");
        lblName.setStyle(labelStyle);
        Label lblCNIC = new Label("CNIC:");
        lblCNIC.setStyle(labelStyle);
        Label lblStart = new Label("Start Station:");
        lblStart.setStyle(labelStyle);
        Label lblDest = new Label("Destination:");
        lblDest.setStyle(labelStyle);
        Label lblTime = new Label("Est. Time:");
        lblTime.setStyle(labelStyle);

        grid.add(head, 0, 0, 2, 1);
        GridPane.setHalignment(head, javafx.geometry.HPos.CENTER);
        grid.add(lblName, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(lblCNIC, 0, 2);
        grid.add(cnicField, 1, 2);
        grid.add(lblStart, 0, 3);
        grid.add(start, 1, 3);
        grid.add(lblDest, 0, 4);
        grid.add(dest, 1, 4);
        grid.add(lblTime, 0, 5);
        grid.add(timeLabel, 1, 5);
        grid.add(calc, 1, 6);
        grid.add(proceed, 1, 7);
        grid.add(back, 1, 8);

        try {
            Image bg = new Image("file:///C:/Users/HP/Desktop/Lahore.png");
            ImageView bgView = new ImageView(bg);
            bgView.setFitWidth(1100);
            bgView.setFitHeight(600);
            bgView.setPreserveRatio(false);

            StackPane stack = new StackPane(bgView, grid);
            bookingScene = new Scene(stack, 600, 600);

        } catch (Exception e) {
            e.printStackTrace();
            bookingScene = new Scene(grid, 600, 600);
        }
    }

    void buildPayment() {
        Label head = new Label("Payment Options");
        head.setFont(new Font("Arial Bold", 30));
        head.setTextFill(Color.BLACK);

        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");

        Button cashBtn = new Button("Cash Payment");
        cashBtn.setStyle("-fx-background-color:green ; -fx-text-fill: white;");
        Button easyBtn = new Button("EasyPaisa");
        easyBtn.setStyle("-fx-background-color:blue; -fx-text-fill: white;");
        Button jazzBtn = new Button("JazzCash");
        jazzBtn.setStyle("-fx-background-color:orange; -fx-text-fill: white;");
        Button cardBtn = new Button("Card Payment");
        cardBtn.setStyle("-fx-background-color:purple; -fx-text-fill: white;");

        Button back = new Button("Back");
        back.setStyle("-fx-background-color:red; -fx-text-fill: white;");
        back.setOnAction(e -> window.setScene(bookingScene));

        cashBtn.setOnAction(e -> {
            if (!amountField.getText().matches("\\d+")) {
                alert("Error", "Enter valid amount!");
                return;
            }
            paidAmount = amountField.getText() + " PKR (Cash)";
            confirmPayment();
        });

        easyBtn.setOnAction(e -> mobilePayment("EasyPaisa", amountField));
        jazzBtn.setOnAction(e -> mobilePayment("JazzCash", amountField));
        cardBtn.setOnAction(e -> cardPayment(amountField));

        VBox layout = new VBox(20, head, amountField, cashBtn, easyBtn, jazzBtn, cardBtn, back);
        layout.setPadding(new Insets(25));
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(Color.OLDLACE, CornerRadii.EMPTY, Insets.EMPTY)));

        try {
            Image b = new Image("file:///C:/Users/HP/Desktop/Lahore.png");
            ImageView bView = new ImageView(b);
            bView.setFitWidth(600);
            bView.setFitHeight(600);
            bView.setPreserveRatio(false);

            StackPane stack = new StackPane(bView, layout);
            paymentScene = new Scene(stack, 600, 600);

        } catch (Exception e) {
            e.printStackTrace();
            paymentScene = new Scene(layout, 600, 600);
        }
    }

    void mobilePayment(String provider, TextField amountField) {
        if (!amountField.getText().matches("\\d+")) {
            alert("Error", "Enter valid amount!");
            return;
        }

        TextInputDialog d = new TextInputDialog();
        d.setHeaderText("Enter " + provider + " Mobile Number (11 digits)");

        d.showAndWait().ifPresent(num -> {
            if (!num.matches("03\\d{9}")) {
                alert("Error", "Invalid Number!");
                return;
            }
            paidAmount = amountField.getText() + " PKR (" + provider + ")";
            confirmPayment();
        });
    }

    void cardPayment(TextField amountField) {

        if (!amountField.getText().matches("\\d+")) {
            alert("Error", "Enter valid amount!");
            return;
        }

        Dialog<ButtonType> d = new Dialog<>();
        d.setTitle("Card Payment");

        GridPane g = new GridPane();
        g.setVgap(10);
        g.setHgap(10);

        TextField card = new TextField();
        TextField exp = new TextField();
        TextField cvv = new TextField();

        card.setPromptText("Card Number 16 digits");
        exp.setPromptText("MM/YY");
        cvv.setPromptText("CVV 3 digits");

        g.add(new Label("Card Number:"), 0, 0);
        g.add(card, 1, 0);
        g.add(new Label("Expiry:"), 0, 1);
        g.add(exp, 1, 1);
        g.add(new Label("CVV:"), 0, 2);
        g.add(cvv, 1, 2);

        d.getDialogPane().setContent(g);
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        d.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {

                if (!card.getText().matches("\\d{16}")) { alert("Error","Invalid card number!"); return; }
                if (!exp.getText().matches("(0[1-9]|1[0-2])/\\d{2}")) { alert("Error","Invalid expiry!"); return; }
                if (!cvv.getText().matches("\\d{3}")) { alert("Error","Invalid CVV!"); return; }

                paidAmount = amountField.getText() + " PKR (Card)";
                confirmPayment();
            }
        });
    }
    void confirmPayment() {
        Vehicle selected = null;

        for (Vehicle v : buses) {
            if (v.bookSeat()) {
                selected = v;
                break;
            }
        }
        if (selected == null) {
            alert("Full", "All buses are FULL.");
            return;
        }
        selectedBusID = selected.getVehicleID();
        arrivalTime = selected.getArrivalTime();
        crowdLevel = selected.getCrowdLevel();

        Ticket t = new Ticket(passengerName, passengerCNIC, startStation, destStation,
                travelTime, selectedBusID, arrivalTime, crowdLevel,
                paidAmount, bookingTime);
        passengerTickets.add(t);

        buildReceipt();
        window.setScene(receiptScene);
    }
    void buildReceipt() {

        Label title = new Label("Ticket Receipt");
        title.setFont(new Font("Arial Bold", 26));
        title.setTextFill(Color.WHITE);

        StringBuilder txt = new StringBuilder();
        int count = 1;
        for (Ticket t : passengerTickets) {
            txt.append("----- Ticket ").append(count).append(" -----\n")
                    .append("Passenger: ").append(t.passengerName).append("\n")
                    .append("CNIC: ").append(t.passengerCNIC).append("\n")
                    .append("From: ").append(t.startStation).append("\n")
                    .append("To: ").append(t.destStation).append("\n")
                    .append("Travel Time: ").append(t.travelTime).append(" minutes\n")
                    .append("Booking Time: ").append(t.bookingTime).append("\n")
                    .append("Paid Amount: ").append(t.paidAmount).append("\n")
                    .append("--- Bus Details ---\n")
                    .append("Bus ID: ").append(t.busID).append("\n")
                    .append("Arrival Time: ").append(t.arrivalTime).append("\n")
                    .append("Crowd Level: ").append(t.crowdLevel).append("\n\n");
            count++;
        }

        rec.setText(txt.toString());
        rec.setFont(new Font(16));
        rec.setTextFill(Color.WHITE);
        Button bookAnother = new Button("Book Another Ticket");
        bookAnother.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        bookAnother.setOnAction(e -> {
            resetBookingFields();
            window.setScene(bookingScene);
        });
        Button backToHome = new Button("Back to Home");
        backToHome.setStyle("-fx-background-color:blue; -fx-text-fill: white;");
        backToHome.setOnAction(e -> window.setScene(homeScene));

        Button exit = new Button("Exit");
        exit.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        exit.setOnAction(e -> window.close());

        HBox buttons = new HBox(20, bookAnother, backToHome, exit);
        buttons.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(rec);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: black; -fx-background: black;");

        if(receiptScene == null) {
            VBox box = new VBox(20, title, scrollPane, buttons);
            box.setPadding(new Insets(25));
            box.setAlignment(Pos.TOP_LEFT);
            box.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            receiptScene = new Scene(box, 600, 600);
        }
    }

    void showStations() {
        StringBuilder s = new StringBuilder("Metro Stations:\n\n");
        for (String st : MetroStations.stations) s.append("- ").append(st).append("\n");
        alert("Stations", s.toString());
    }

    void showBusInfo() {
        String info =
                bus1.getVehicleID() + " | Arrival: " + bus1.getArrivalTime() + " | Crowd: " + bus1.getCrowdLevel() + "\n" +
                        bus2.getVehicleID() + " | Arrival: " + bus2.getArrivalTime() + " | Crowd: " + bus2.getCrowdLevel() + "\n" +
                        bus3.getVehicleID() + " | Arrival: " + bus3.getArrivalTime() + " | Crowd: " + bus3.getCrowdLevel();
        alert("Bus Info", info);
    }

    void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    public void saveTickets() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("tickets.dat"))) {
            oos.writeObject(passengerTickets);
            System.out.println("Tickets saved successfully.");
        } catch (Exception e) {
            System.out.println("Could not save tickets: " + e.getMessage());
        }
    }
    public void loadTickets() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("tickets.dat"))) {

            ArrayList<Ticket> loadedList = (ArrayList<Ticket>) ois.readObject();
            passengerTickets.addAll(loadedList);
            System.out.println("Tickets loaded successfully. Total: " + passengerTickets.size());

        } catch (FileNotFoundException e) {
            System.out.println("No previous ticket data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading tickets: " + e.getMessage());
        }
    }
    void resetBookingFields() {
        if(nameField != null) {
            nameField.clear();
            cnicField.clear();
            start.getSelectionModel().clearSelection();
            dest.getSelectionModel().clearSelection();
            timeLabel.setText("--");
        }
    }
    public static void main(String[] args)
    {
        launch();
    }
}