package application;

import com.victorkzk.furniture.Deserializer;
import com.victorkzk.furniture.Furniture;
import com.victorkzk.furniture.Serializer;
import furniture.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    @FXML
    private TextField lengthField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private ChoiceBox<Object> typeBox;
    @FXML
    private ChoiceBox<Object> materialBox;
    @FXML
    private ListView listView;
    @FXML
    private Pane pane;
    @FXML
    private CheckBox pluginSerializerCheckBox;
    @FXML
    private CheckBox pluginDeserializerCheckBox;
    @FXML
    private CheckBox pluginArchiverCheckBox;


    private ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();
    private static ArrayList<Class> plugins = new ArrayList<>();
    private Class pluginSerializerClass = null;
    private Class pluginDeserializerClass = null;
    private Serializer serializer = new JSONSerializer();
    private Deserializer deserializer = new JSONDeserializer();
    private Furniture furniture = null;
    private boolean createNew = false;

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

    private void loadClassesToDeserializer() {
        deserializer.addClass(Bed.class);
        deserializer.addClass(Cabinetry.class);
        deserializer.addClass(Chair.class);
        deserializer.addClass(Desk.class);
        deserializer.addClass(Table.class);
        for (Class c : plugins) {
            deserializer.addClass(c);
        }
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
        materialBox.setItems(FXCollections.observableArrayList(Furniture.getMaterialList()));
    }

    @FXML
    private void serializeClick() throws FileNotFoundException, InstantiationException, IllegalAccessException {
        List<Object> objectList = new ArrayList<>(furnitureList.size());
        for (Furniture furniture : furnitureList) {
            objectList.add(furniture);
        }
        serializer.serialize("FurnitureList" + serializer.getFileExtension(), objectList);
    }

    @FXML
    private void deserializeClick() throws IOException, InstantiationException, IllegalAccessException {
        loadClassesToDeserializer();
        List<Object> objectList = deserializer.deserialize("FurnitureList" + deserializer.getFileExtension());
        furnitureList.clear();
        for (Object object : objectList) {
            furnitureList.add((Furniture)object);
        }
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

    @FXML
    private void loadSerializer() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        PluginLoader pluginLoader = new PluginLoader(file);
        pluginSerializerClass = pluginLoader.loadSerializer();
        pluginSerializerCheckBox.setDisable(false);
    }

    @FXML
    private void loadDeserializer() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        PluginLoader pluginLoader = new PluginLoader(file);
        pluginDeserializerClass = pluginLoader.loadDeserializer();
        pluginDeserializerCheckBox.setDisable(false);
    }

    @FXML
    void enableSerializerPlugin() throws IllegalAccessException, InstantiationException {
        if (serializer.getClass() == pluginSerializerClass) {
            serializer = new JSONSerializer();
        }
        else {
            serializer = (Serializer)pluginSerializerClass.newInstance();
        }
    }

    @FXML
    void enableDeserializerPlugin() throws IllegalAccessException, InstantiationException {
        if (deserializer.getClass() == pluginDeserializerClass) {
            deserializer = new JSONDeserializer();
        }
        else {
            deserializer = (Deserializer)pluginDeserializerClass.newInstance();
        }
    }

    private void createButton(int index) {
        Furniture newFurniture = createFurniture(index);
        Button button = new Button();
        button.setText(newFurniture.toString());
        button.setOnAction(e -> {
            clearFields();
            createNew = true;
            furniture = createFurniture(index);
            updateTypeList();
        });
        button.setLayoutY(index * 31);
        button.setPrefWidth(117);
        pane.getChildren().add(button);
    }

    private Furniture createFurniture (int index) {
        try {
            Class[] cArg = new Class[3];
            cArg[0] = cArg[1] = cArg[2] = int.class;
            return (Furniture)plugins.get(index).getDeclaredConstructor(cArg).newInstance(0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
