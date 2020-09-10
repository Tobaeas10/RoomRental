package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rental.Booking;
import rental.Building;
import rental.Database;
import rental.Floor;
import rental.Room;

public class Gui extends Application {
	
	Controller ctrl;
	Database db;
	Stage window;
	private ComboBox<Building> buildingComboBox;
	private Label roomsInBuildingLabel;
	private Label vacantRoomsInBuildingLabel;
	private ComboBox<Floor> floorComboBox;
	private Label vacantRoomsInFloorLabel;
	private Label roomsInFloorLabel;
	private ComboBox<Room> roomComboBox;
	private CheckBox useBuildingComboBoxAsFilter;
	private boolean useBuildingComboBoxAsFilterBoolean = false;
	private CheckBox useFloorComboBoxAsFilter;
	private boolean useFloorComboBoxAsFilterBoolean = false;
	private TableView<Room> roomsTable;
	private Button deleteBuildingButton;
	private Button editBuildingButton;
	private Button addFloorButton;
	private Button deleteFloorButton;
	private Button editFloorButton;
	private Button addRoomButton;
	private Button deleteRoomButton;
	private Button editRoomButton;
	private Label tenantLabel;
	private GridPane buildingGridPane;
	private BorderedTitledPane buildingTitledPane;
	private GridPane addBuildingGridPane;
	private TextField addBuildingTextField;
	private Button confirmAddBuildingButton;
	
	private boolean newBuilding;
	private ComboBox<Floor> addBuildingFloorsComboBox;
	private Label floorsOfBuildingLabel;
	
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage window) throws Exception {
		try {
		setUpController();
		loadDataBase();
		this.window = window;
		window.setTitle("Room Rental");
		window.setOnCloseRequest(e -> ctrl.exitProgram(e));
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1280, 720);
		scene.getStylesheets().add(getClass().getResource("design.css").toExternalForm());
		window.setScene(scene);
		
		//Top Menu Bar
		MenuBar menuBar = new MenuBar();
		root.setTop(menuBar);
		
		Menu fileMenu = new Menu("File");
		menuBar.getMenus().add(fileMenu);
		MenuItem newMenuItem = new MenuItem("New");
		fileMenu.getItems().add(newMenuItem);
		MenuItem openMenuItem = new MenuItem("Open");
		fileMenu.getItems().add(openMenuItem);
		MenuItem openRecentMenuItem = new MenuItem("Open Recent");
		fileMenu.getItems().add(openRecentMenuItem);
		MenuItem saveMenuItem = new MenuItem("Save");
		fileMenu.getItems().add(saveMenuItem);
		
		Menu helpMenu = new Menu("Help");
		menuBar.getMenus().add(helpMenu);
		MenuItem tutorialMenuItem = new MenuItem("Tutorial");
		fileMenu.getItems().add(tutorialMenuItem);
		
		
		//Main View
		TabPane mainTabPane = new TabPane();
		mainTabPane.setSide(Side.LEFT);
		root.setCenter(mainTabPane);
		
		Tab bookingsTab = new Tab("  Bookings  ");
		bookingsTab.setClosable(false);
		mainTabPane.getTabs().add(bookingsTab);
		
		Tab roomsTab = new Tab("  Rooms  ");
		roomsTab.setClosable(false);
		mainTabPane.getTabs().add(roomsTab);
		
		BorderPane roomsPane = new BorderPane();
		roomsTab.setContent(roomsPane);
		
		VBox roomNavigationPane = new VBox();
		roomNavigationPane.setPrefWidth(300);
		roomNavigationPane.setPadding(new Insets(10, 10, 10, 10));
		roomNavigationPane.setSpacing(10);
		roomsPane.setLeft(roomNavigationPane);
		
		
		//Building	
		buildingGridPane = new GridPane();
		buildingGridPane.setHgap(5);
		buildingGridPane.setVgap(10);
		
		buildingComboBox = new ComboBox<Building>();
		buildingComboBox.setOnAction(e -> onBuildingSelectionChange());
		buildingComboBox.setPromptText("Select a Building");
		buildBuildingComboBoxItems();
		buildingGridPane.add(buildingComboBox, 0, 0, 3, 1);

		roomsInBuildingLabel = new Label("");
		buildingGridPane.add(roomsInBuildingLabel, 0, 1, 3, 1);
		
		vacantRoomsInBuildingLabel = new Label("");
		buildingGridPane.add(vacantRoomsInBuildingLabel, 0, 2, 3, 1);
		
		Button addBuildingButton = makeButtonWithImage25Height("gui/add.png", "Add Building");
		addBuildingButton.setOnAction(e -> onAddBuildingButton());
		buildingGridPane.add(addBuildingButton, 0, 3);
		
		addBuildingGridPane = new GridPane();
		addBuildingGridPane.setHgap(5);
		addBuildingGridPane.setVgap(10);
		
		addBuildingTextField = new TextField();
		addBuildingTextField.textProperty().addListener(e -> addBuildingTextFieldTextChanged());
		addBuildingTextField.setOnAction((e) -> onAddBuildingTextField(e));
		addBuildingTextField.setPromptText("Building Name");
		addBuildingTextField.setTooltip(new Tooltip("Building Name"));
		addBuildingGridPane.add(addBuildingTextField, 0, 0, 2, 1);
		
		addBuildingFloorsComboBox = new ComboBox<Floor>();
		addBuildingFloorsComboBox.setOnAction(e -> onAddBuildingFloorsComboBox());
		addBuildingGridPane.add(addBuildingFloorsComboBox, 0, 1, 2, 1);
		
		floorsOfBuildingLabel = new Label();
		floorsOfBuildingLabel.setWrapText(true);
		addBuildingGridPane.add(floorsOfBuildingLabel, 0, 2, 2, 1);

		confirmAddBuildingButton = makeButtonWithImage25Height("gui/check.png", "Confirm");
		confirmAddBuildingButton.setDisable(true);
		confirmAddBuildingButton.setOnAction(e -> onConfirmAddBuildingButton());
		addBuildingGridPane.add(confirmAddBuildingButton, 1, 3);
		
		Button cancelAddBuildingButton = makeButtonWithImage25Height("gui/cancel.png", "Cancel");
		cancelAddBuildingButton.setOnAction(e -> onCancelAddBuildingButton());
		addBuildingGridPane.add(cancelAddBuildingButton, 0, 3);
		
		
		deleteBuildingButton = makeButtonWithImage25Height("gui/delete.png", "Delete Building");
		deleteBuildingButton.setOnAction(e -> onDeleteBuildingButton());
		buildingGridPane.add(deleteBuildingButton, 1, 3);
		
		
		editBuildingButton = makeButtonWithImage25Height("gui/edit.png", "Edit Building");
		editBuildingButton.setOnAction(e -> onEditBuildingButton());
		buildingGridPane.add(editBuildingButton, 2, 3);
		
		
		useBuildingComboBoxAsFilter = new CheckBox("Use as Filter");
		useBuildingComboBoxAsFilter.setSelected(false);
		useBuildingComboBoxAsFilter.setOnAction(e -> onUseComboBoxAsFilterChange());
		buildingGridPane.add(useBuildingComboBoxAsFilter, 0, 4, 3, 1);
		
		buildingTitledPane = new BorderedTitledPane("Building", buildingGridPane);
		roomNavigationPane.getChildren().add(buildingTitledPane);
		VBox.setVgrow(buildingTitledPane, Priority.ALWAYS);
		
		
		//Floor
		GridPane floorGridPane = new GridPane();
		floorGridPane.setGridLinesVisible(false);
		floorGridPane.setHgap(5);
		floorGridPane.setVgap(10);
		
		floorComboBox = new ComboBox<Floor>();
		floorComboBox.setOnAction(e -> onFloorSelectionChange());
		floorComboBox.setPromptText("Select a Floor");
		
		floorGridPane.add(floorComboBox, 0, 0, 3, 1);
		
		addFloorButton = makeButtonWithImage25Height("gui/add.png", "Add Floor");
		floorGridPane.add(addFloorButton, 0, 3);
		
		deleteFloorButton = makeButtonWithImage25Height("gui/delete.png", "Delete Floor");
		floorGridPane.add(deleteFloorButton, 1, 3);
		
		editFloorButton = makeButtonWithImage25Height("gui/edit.png", "Edit Floor");
		floorGridPane.add(editFloorButton, 2, 3);
		
		roomsInFloorLabel = new Label("");
		floorGridPane.add(roomsInFloorLabel, 0, 1, 3, 1);
		
		vacantRoomsInFloorLabel = new Label("");
		floorGridPane.add(vacantRoomsInFloorLabel, 0, 2, 3, 1);
		
		useFloorComboBoxAsFilter = new CheckBox("Use as Filter");
		useFloorComboBoxAsFilter.setSelected(false);
		useFloorComboBoxAsFilter.setOnAction(e -> onUseComboBoxAsFilterChange());
		floorGridPane.add(useFloorComboBoxAsFilter, 0, 4, 3, 1);
		
		BorderedTitledPane floorTitledPane = new BorderedTitledPane("Floor", floorGridPane);
		roomNavigationPane.getChildren().add(floorTitledPane);
		VBox.setVgrow(floorTitledPane, Priority.ALWAYS);
		
		
		//Room
		GridPane roomGridPane = new GridPane();
		
		roomGridPane.setGridLinesVisible(false);
		roomGridPane.setHgap(5);
		roomGridPane.setVgap(10);
		
		roomComboBox = new ComboBox<Room>();
		roomComboBox.setOnAction(e -> onRoomSelectionChange());
		roomComboBox.setPromptText("Select a Room");
		
		roomGridPane.add(roomComboBox, 0, 0, 3, 1);
		
		addRoomButton = makeButtonWithImage25Height("gui/add.png", "Add Room");
		roomGridPane.add(addRoomButton, 0, 1);
		
		deleteRoomButton = makeButtonWithImage25Height("gui/delete.png", "Delete Room");
		roomGridPane.add(deleteRoomButton, 1, 1);
		
		editRoomButton = makeButtonWithImage25Height("gui/edit.png", "Edit Room");
		roomGridPane.add(editRoomButton, 2, 1);
		
		tenantLabel = new Label("");
		roomGridPane.add(tenantLabel, 0, 2, 3, 1);
		
		BorderedTitledPane roomTitledPane = new BorderedTitledPane("Room", roomGridPane);
		roomNavigationPane.getChildren().add(roomTitledPane);
		VBox.setVgrow(roomTitledPane, Priority.ALWAYS);
		
		
		//Column Room name
		TableColumn<Room, String> roomNameColumn = new TableColumn<>("Room");
		roomNameColumn.setMinWidth(140);
		roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Room, String> roomFloorColumn = new TableColumn<>("Floor");
		roomFloorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
		
		TableColumn<Room, String> roomBuildingColumn = new TableColumn<>("Building");
		roomBuildingColumn.setCellValueFactory(new PropertyValueFactory<>("building"));
		
		TableColumn<Room, String> bookingColumn = new TableColumn<>("Booked");
		
		TableColumn<Room, String> bookedByColumn = new TableColumn<>("by");
		bookedByColumn.setCellValueFactory(new PropertyValueFactory<>("tenantName"));

		TableColumn<Room, String> bookedFromColumn = new TableColumn<>("from");
		bookedFromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));

		TableColumn<Room, String> bookedUntilColumn = new TableColumn<>("until");
		bookedUntilColumn.setCellValueFactory(new PropertyValueFactory<>("until"));

		bookingColumn.getColumns().addAll(bookedByColumn, bookedFromColumn, bookedUntilColumn);
		
		roomsTable = new TableView<Room>();
		onUseComboBoxAsFilterChange(); //does roomsTable.setItems(getRoomsToBeDisplayedInRoomsTable());
		roomsTable.getColumns().addAll(roomNameColumn, roomFloorColumn, roomBuildingColumn, bookingColumn);
		roomsTable.getSelectionModel().selectedItemProperty().addListener(
					(obs, oldSelection, newSelection) -> onRoomsTableSelectionChange(obs, oldSelection, newSelection)
		);
		roomsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		roomsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		roomsPane.setCenter(roomsTable);
		
		//Finishing
		updateDisableDeleteEditButtons();
		window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private Object onAddBuildingFloorsComboBox() {
		// TODO Auto-generated method stub
		return null;
	}


	private void onEditBuildingButton() {
		newBuilding = false;
		
		//filling available floors
		for(Building b : db()) {
			for(Floor f : b.floors) {
				if(f.building == null) {
					addBuildingFloorsComboBox.getItems().add(f);
				}
			}
		}
		
		updateFloorsOfBuildingLabelText();
		
		buildingTitledPane.setContent(addBuildingGridPane);
		addBuildingTextField.requestFocus();
	}


	private void updateFloorsOfBuildingLabelText() {
		String text = "Floors in " + buildingComboBox.getValue().toString() + ":\n";
		for(Floor f : buildingComboBox.getValue().floors) {
			if(buildingComboBox.getValue().floors.indexOf(f) == buildingComboBox.getValue().floors.size() - 1) {
				//If last index
				text += f.toString();
			} else {
				text += f.toString() + ", ";				
			}
		}
		floorsOfBuildingLabel.setText(text);
	}


	private void onDeleteBuildingButton() {
		boolean delete = PopupWindows.confirmOrCancel("Delete Building Dialogue",
					"Delete " + buildingComboBox.getValue().name + " and its floors, rooms?");
		
		if(delete) {
			db().remove(buildingComboBox.getValue());
			roomsTable.setItems(getRoomsToBeDisplayedInRoomsTable());
			buildBuildingComboBoxItems();
		}
	}


	private void buildBuildingComboBoxItems() {
		buildingComboBox.getItems().clear();
		for(Building b : db()) {
			buildingComboBox.getItems().add(b);
		}
	}


	private void onAddBuildingTextField(ActionEvent e) {
		if(!addBuildingTextField.getText().trim().equalsIgnoreCase("")) onConfirmAddBuildingButton();
	}


	private void onCancelAddBuildingButton() {
		addBuildingTextField.setText("");
		buildingTitledPane.setContent(buildingGridPane);
	}


	private void onConfirmAddBuildingButton() {
		if(newBuilding) {
			Building building = new Building(addBuildingTextField.getText());
			db().add(building);
			buildingComboBox.setValue(building);
		} else {
			buildingComboBox.getValue().name = addBuildingTextField.getText();
		}
		buildBuildingComboBoxItems();
		addBuildingTextField.setText("");
		buildingTitledPane.setContent(buildingGridPane);
	}


	private void addBuildingTextFieldTextChanged() {
		if(!addBuildingTextField.getText().trim().equalsIgnoreCase("")) {
			//Valid
			confirmAddBuildingButton.setDisable(false);
		} else {
			//Invalid
			confirmAddBuildingButton.setDisable(true);
		}
		updateFloorsOfBuildingLabelText();
	}


	private void onAddBuildingButton() {
		newBuilding = true;
		buildingTitledPane.setContent(addBuildingGridPane);
		addBuildingTextField.requestFocus();
	}


	private void onRoomsTableSelectionChange(ObservableValue<? extends Room> obs, Room oldSelection, Room newSelection) {
		if(newSelection == null) return;
		buildingComboBox.setValue(newSelection.floor.building);
		floorComboBox.setValue(newSelection.floor);
		roomComboBox.setValue(newSelection);
	}


	private void updateDisableDeleteEditButtons() {
		if(buildingComboBox.getValue() == null) {
			editBuildingButton.setDisable(true);
			deleteBuildingButton.setDisable(true);
			
			editFloorButton.setDisable(true);
			deleteFloorButton.setDisable(true);
			
			editRoomButton.setDisable(true);
			deleteRoomButton.setDisable(true);
			
		} else if(floorComboBox.getValue() == null) {
			editBuildingButton.setDisable(false);
			deleteBuildingButton.setDisable(false);
			
			editFloorButton.setDisable(true);
			deleteFloorButton.setDisable(true);
			
			editRoomButton.setDisable(true);
			deleteRoomButton.setDisable(true);
				
		} else if(roomComboBox.getValue() == null) {
			editBuildingButton.setDisable(false);
			deleteBuildingButton.setDisable(false);
			
			editFloorButton.setDisable(false);
			deleteFloorButton.setDisable(false);
			
			editRoomButton.setDisable(true);
			deleteRoomButton.setDisable(true);
		} else {
			editBuildingButton.setDisable(false);
			deleteBuildingButton.setDisable(false);
			
			editFloorButton.setDisable(false);
			deleteFloorButton.setDisable(false);
			
			editRoomButton.setDisable(false);
			deleteRoomButton.setDisable(false);
		}
	}
	
	private void manageUseAsFilter() {
		if(buildingComboBox.getValue() == null) {
			useBuildingComboBoxAsFilter.setDisable(true);
			useFloorComboBoxAsFilter.setDisable(true);
			
		} else {
			useBuildingComboBoxAsFilter.setDisable(false);
			
			if(floorComboBox.getValue() == null) {
				useFloorComboBoxAsFilter.setDisable(true);
			} else {
				useFloorComboBoxAsFilter.setDisable(false);
			}
		}
		
		if(!useBuildingComboBoxAsFilter.isSelected()) {
			useFloorComboBoxAsFilter.setDisable(true);
			useFloorComboBoxAsFilterBoolean = false;
		} else if(useFloorComboBoxAsFilter.isSelected() && useFloorComboBoxAsFilter.isDisable()) {
			useFloorComboBoxAsFilterBoolean = true;
		}
		
		if(useBuildingComboBoxAsFilter.isSelected() && !useBuildingComboBoxAsFilter.isDisable()) {
			useBuildingComboBoxAsFilterBoolean = true;
		} else {
			useBuildingComboBoxAsFilterBoolean = false;
		}
		
		if(useFloorComboBoxAsFilter.isSelected() && !useFloorComboBoxAsFilter.isDisable()) {
			useFloorComboBoxAsFilterBoolean = true;
		} else {
			useFloorComboBoxAsFilterBoolean = false;
		}
	}
	
	private void onUseComboBoxAsFilterChange() {
		manageUseAsFilter();
		roomsTable.setItems(getRoomsToBeDisplayedInRoomsTable());
	}

	private ObservableList<Room> getRoomsToBeDisplayedInRoomsTable() {
		ObservableList<Room> rooms = FXCollections.observableArrayList();
		
		if(buildingComboBox.getValue() == null || !useBuildingComboBoxAsFilterBoolean) {
			for(Building b : db()) {
				for(Floor f : b.floors) {
					for(Room r : f.rooms) {
						rooms.add(r);
					}
				}
			}
		} else {
			if(floorComboBox.getValue() == null || !useFloorComboBoxAsFilterBoolean) {
				for(Floor f : buildingComboBox.getValue().floors) {
					for(Room r : f.rooms) {
						rooms.add(r);
					}
				}
			} else {
				for(Room r : floorComboBox.getValue().rooms) {
					rooms.add(r);
				}
			}
		}
		
		return rooms;
	}
	
	private void onBuildingSelectionChange() {
		//Update Room Labels
		if(buildingComboBox.getValue() != null) {
			roomsInBuildingLabel.setText("Rooms in " + buildingComboBox.getValue().name + ": " + 
					buildingComboBox.getValue().totalRooms);
			vacantRoomsInBuildingLabel.setText("Vacant Rooms in " + buildingComboBox.getValue().name + ": " + 
					buildingComboBox.getValue().vacantRooms);
			
			//Fill Floor ComboBox
			floorComboBox.getItems().clear();
			roomComboBox.getItems().clear();
			for(Floor f : buildingComboBox.getValue().floors) {
				floorComboBox.getItems().add(f);
			}
			manageUseAsFilter();
			
		} else {
			roomsInBuildingLabel.setText("");
			vacantRoomsInBuildingLabel.setText("");
		}
		updateDisableDeleteEditButtons();
		if(useBuildingComboBoxAsFilterBoolean) roomsTable.setItems(getRoomsToBeDisplayedInRoomsTable());
	}
	
	private void onFloorSelectionChange() {
		if(floorComboBox.getValue() != null) {
			roomsInFloorLabel.setText("Rooms in " + floorComboBox.getValue().name + ": " + 
					floorComboBox.getValue().rooms.size());
			vacantRoomsInFloorLabel.setText("Vacant rooms in " + floorComboBox.getValue().name + ": " + 
					floorComboBox.getValue().vacantRooms);
			
			//Fill Room ComboBox
			roomComboBox.getItems().clear();
			for(Room r : floorComboBox.getValue().rooms) {
				roomComboBox.getItems().add(r);
			}
			
			manageUseAsFilter();

		} else {
			roomsInFloorLabel.setText("");
			vacantRoomsInFloorLabel.setText("");
		}
		updateDisableDeleteEditButtons();
		if(useFloorComboBoxAsFilterBoolean) roomsTable.setItems(getRoomsToBeDisplayedInRoomsTable());
	}
	
	private void onRoomSelectionChange() {
		updateDisableDeleteEditButtons();
		if(roomComboBox.getValue() != null) {
			roomsTable.getSelectionModel().select(roomComboBox.getValue());
			String tenantLabelText = roomComboBox.getValue().getTenantName();
			if(!tenantLabelText.equalsIgnoreCase("")) tenantLabelText = "Tenant: " + tenantLabelText;
			tenantLabel.setText(tenantLabelText);
		}
	}

	private Button makeButtonWithImage25Height(String path, String toolTip) {
		Button b = new Button();
		Image img = new Image(path);
	    ImageView view = new ImageView(img);
	    view.setSmooth(true);
	    view.setFitHeight(25);
	    view.setPreserveRatio(true);
	    b.setGraphic(view);
	    b.setTooltip(new Tooltip(toolTip));
	    return b;
	}

	private void loadDataBase() {
		db = new Database();
		db.setBuildings(getPresetDatabase());
	}

	private ArrayList<Building> getPresetDatabase() {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		Building b1 = new Building("Katharina v. Sienna Schule");
		buildings.add(b1);
		
		Floor f1 = new Floor(b1, "Ground Level");
		b1.addFloor(f1);
		
		Room r1 = new Room(f1, "Raum 001");
		Room r2 = new Room(f1, "Raum 002");
		f1.addRoom(r1);
		f1.addRoom(r2);
		
		f1.addRoom(new Room(f1, "Raum 003"));
		f1.addRoom(new Room(f1, "Raum 004"));
		f1.addRoom(new Room(f1, "Raum 005"));
		f1.addRoom(new Room(f1, "Raum 006"));
		f1.addRoom(new Room(f1, "Raum 007"));
		f1.addRoom(new Room(f1, "Raum 008"));

		Floor f2 = new Floor(b1, "First Floor");
		b1.addFloor(f2);
		
		f2.addRoom(new Room(f2, "Raum 101"));
		f2.addRoom(new Room(f2, "Raum 102"));
		f2.addRoom(new Room(f2, "Raum 103"));
		f2.addRoom(new Room(f2, "Raum 104"));


		Building b2 = new Building("GymHum H-Gebäude");
		buildings.add(b2);
		
		Floor f3 = new Floor(b2, "Basement");
		b2.addFloor(f3);
		
		Room r3 = new Room(f3, "Raum in dem Trampe die Kinder gefangen hält");
		f3.addRoom(r3);
		
		Floor f4 = new Floor(b2, "Ground Floor");
		b2.addFloor(f4);
		Floor f5 = new Floor(b2, "First Floor");
		b2.addFloor(f5);
		Floor f6 = new Floor(b2, "Second Floor");
		b2.addFloor(f6);
		
		Building b3 = new Building("Albert Schweizer Gymnasium");
		buildings.add(b3);
		
		ArrayList<Room> bo1r = new ArrayList<Room>();
		Collections.addAll(bo1r, new Room[] {r1, r2, r3});
		db.bookings.add(new Booking(bo1r, new Date(), new Date(), "Penis"));

		return buildings;
	}


	//shortcut method call
	private ArrayList<Building> db() {
		return db.getBuildings();
	}
	
	private void setUpController() {
		ctrl = new Controller(this);
	}
	
	
	
	public static void main(String[] args) {
		Gui.launch(args);
	}
}
