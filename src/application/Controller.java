package application;

import com.google.gson.GsonBuilder;
import com.victorkzk.furniture.Furniture;
import furniture.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;


public class Controller implements Initializable {
    @FXML
    private TextField lengthField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private ChoiceBox typeBox;
    @FXML
    private ChoiceBox materialBox;
    @FXML
    private ListView listView;
    @FXML
    private Pane pane;

    private ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();
    private static ArrayList<Class> plugins = new ArrayList<>();

    Furniture furniture = null;
    boolean createNew = false;

    @FXML
    private void tableBtnClick() {
        clearFields();
        createNew = true;
        furniture = new Table(0, 0, 0);
        updateTypeList();
    }

    @FXML
    private void chairBtnClick() {
        clearFields();
        createNew = true;
        furniture = new Chair(0, 0, 0);
        updateTypeList();
    }

    @FXML
    private void bedBtnClick() {
        clearFields();
        createNew = true;
        furniture = new Bed(0, 0, 0);
        updateTypeList();
    }

    @FXML
    private void cabinetryBtnClick() {
        clearFields();
        createNew = true;
        furniture = new Cabinetry(0, 0, 0);
        updateTypeList();
    }

    @FXML
    private void deskBtnClick() {
        clearFields();
        createNew = true;
        furniture = new Desk(0, 0, 0);
        updateTypeList();
    }

    @FXML
    private void deleteClick() {
        furnitureList.remove(furniture);
        clearFields();
    }

    @FXML
    private void addToList() {
        furniture.setType(typeBox.getValue());
        furniture.setMaterial(materialBox.getValue());
        furniture.setLength(Integer.parseInt(lengthField.getText()));
        furniture.setWidth(Integer.parseInt(widthField.getText()));
        furniture.setHeight(Integer.parseInt(heightField.getText()));
        if (createNew)
            furnitureList.add(furniture);
        listView.setItems(furnitureList);
    }

    private void clearFields() {
        lengthField.clear();
        widthField.clear();
        heightField.clear();
    }

    private void updateTypeList() {
        if (furniture != null)
            typeBox.setItems(FXCollections.observableArrayList(furniture.getTypesList()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMaterialList();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    furniture = (Furniture) listView.getSelectionModel().getSelectedItem();
                    createNew = false;
                    updateTypeList();
                    setFields();
                }
        );
    }

    private void setFields() {
        if (furniture == null)
            return;
        lengthField.setText(String.valueOf(furniture.getLength()));
        widthField.setText(String.valueOf(furniture.getWidth()));
        heightField.setText(String.valueOf(furniture.getHeight()));
        typeBox.getSelectionModel().select(furniture.getType());
        materialBox.getSelectionModel().select(furniture.getMaterial());
    }

    private void initMaterialList() {
        materialBox.setItems(FXCollections.observableArrayList(furniture.getMaterialList()));
    }

    @FXML
    private void serializeClick() throws FileNotFoundException {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Furniture.class, new AbstractElementAdapter());
        String json = gson.setPrettyPrinting().create().toJson(furnitureList.toArray(), Furniture[].class);
        PrintWriter writer = new PrintWriter("FurnitureList.json");
        writer.write(json);
        writer.close();
    }

    @FXML
    private void deserializeClick() throws IOException {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Furniture.class, new AbstractElementAdapter());
        Furniture[] furnitureArray= gson.create().fromJson((new String(readAllBytes(get("FurnitureList.json")))),
                Furniture[].class);
        furnitureList = FXCollections.observableArrayList(Arrays.asList(furnitureArray));
        listView.setItems(furnitureList);
    }

    @FXML
    private void loadPluginClick(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        PluginLoader pluginLoader = new PluginLoader(file);
        plugins.add(pluginLoader.getFurnitureClass());
        createButton(plugins.size() - 1);
    }



    private void createButton(int index) {
        Furniture newFurniture = createFurniture(index);
        Button button = new Button();
        button.setText(newFurniture.toString());
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                clearFields();
                createNew = true;
                furniture = createFurniture(index);
                updateTypeList();
            }
        });
        button.setLayoutY(index * 31);
        button.setPrefWidth(117);
        pane.getChildren().add(button);
    }

    private Furniture createFurniture (int index) {
        try {
            Class[] cArg = new Class[3];
            cArg[0] = cArg[1] = cArg[2] = int.class;
            Furniture res = (Furniture)plugins.get(index).getDeclaredConstructor(cArg).newInstance(0, 0, 0);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Class> getLoadedPlugins() {
        return plugins;
    }
}
