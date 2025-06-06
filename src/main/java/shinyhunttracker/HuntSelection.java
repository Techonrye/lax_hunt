package shinyhunttracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Vector;

import static javafx.util.Duration.INDEFINITE;
import static javafx.util.Duration.ZERO;

public class HuntSelection{
    static Stage selectionPageStage = new Stage();

    //Pokemon Elements
    static Pokemon selectedPokemon = null;
    static TreeView<Pokemon> pokemonListTreeView = new TreeView<>();
    static ObservableList<TreeItem<Pokemon>> defaultPokemonList = FXCollections.observableArrayList();//List of all pokemon
    static ObservableList<TreeItem<Pokemon>> searchPokemonList = FXCollections.observableArrayList();//List of pokemon restricted by content of Search
    static TreeItem<Pokemon> pokemonListRoot = new TreeItem<>();

    //Game Elements
    static Game selectedGame = null;
    static ObservableList<Game> defaultGameList = FXCollections.observableArrayList();
    static ComboBox<Game> gameComboBox = new ComboBox<>();

    //Method Elements
    static Method selectedMethod = null;
    static ObservableList<Method> defaultMethodList = FXCollections.observableArrayList();
    static ComboBox<Method> methodComboBox = new ComboBox<>();

    /**
     * Opens a selection page for the user to select the information for the hunt they want to do
     */
    public static void createHuntSelection(){
        //makes sure to not open a new window when one is open
        if(selectionPageStage.isShowing())
            return;

        selectionPageStage.setTitle("Hunt Selection");

        //resets variables for when the window is reopened
        resetVariables();

        //setup selection window layout & initialize default info lists
        HBox selectionPageLayout = new HBox();

        AnchorPane huntInformation = new AnchorPane();
        huntInformation.setMinWidth(375);

        ImageView pokemonSprite = new ImageView(new Image("file:Images/blank.png"));
        pokemonSprite.setLayoutX(180);
        pokemonSprite.setLayoutY(275);

        gameComboBox.setPromptText("---Game---");
        gameComboBox.setMinHeight(25);
        gameComboBox.setMinWidth(200);
        gameComboBox.setLayoutY(280);
        gameComboBox.setLayoutX(87.5);

        if(defaultGameList.size() == 0) {
            try {
                JSONArray gameList = new JSONArray(new JSONTokener(new FileInputStream("GameData/game.json")));

                for (int i = 0; i < gameList.length(); i++)
                    defaultGameList.add(new Game(gameList.getJSONObject(i), i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameComboBox.getItems().setAll(defaultGameList);

        methodComboBox.setPromptText("---Method---");
        methodComboBox.setMinWidth(200);
        methodComboBox.setMinHeight(25);
        methodComboBox.setLayoutY(315);
        methodComboBox.setLayoutX(87.5);

        if(defaultMethodList.size() == 0) {
            try {
                JSONArray methodList = new JSONArray(new JSONTokener(new FileInputStream("GameData/method.json")));

                for (int i = 0; i < methodList.length(); i++)
                    defaultMethodList.add(new Method(methodList.getJSONObject(i), i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        methodComboBox.getItems().setAll(defaultMethodList);

        Button beginHunt = new Button("Start");
        beginHunt.setMinWidth(50);
        beginHunt.setLayoutY(350);
        beginHunt.setLayoutX(162.5);
        beginHunt.setDisable(true);

        Button methodHelp = new Button("?");
        methodHelp.setMinSize(25, 25);
        methodHelp.setLayoutY(315);
        methodHelp.setLayoutX(287);
        methodHelp.visibleProperty().bind(methodComboBox.valueProperty().isNotNull());

        Tooltip methodToolTip = new Tooltip();
        methodToolTip.setShowDelay(ZERO);
        methodToolTip.setShowDuration(INDEFINITE);
        Tooltip.install(methodHelp, methodToolTip);

        huntInformation.getChildren().addAll(pokemonSprite, gameComboBox, methodComboBox, methodHelp, beginHunt);

        VBox pokemonSelection = new VBox();
        pokemonSelection.setAlignment(Pos.CENTER_RIGHT);
        pokemonSelection.setMinWidth(375);

        TextField pokemonSearch = new TextField();
        pokemonSearch.setPromptText("Search...");

        pokemonListTreeView.setMinHeight(441);
        pokemonListTreeView.setRoot(pokemonListRoot);
        pokemonListTreeView.setShowRoot(false);

        if(defaultPokemonList.size() == 0) {
            try {
                JSONArray pokemonListJSON = new JSONArray(new JSONTokener(new FileInputStream("GameData/pokemon.json")));

                for (int i = 0; i < pokemonListJSON.length(); i++) {
                    defaultPokemonList.add(new TreeItem<>(new Pokemon(pokemonListJSON.getJSONObject(i), i)));
                    searchPokemonList.add(defaultPokemonList.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            searchPokemonList.clear();
            searchPokemonList.addAll(defaultPokemonList);
        }
        pokemonListRoot.getChildren().addAll(defaultPokemonList);

        pokemonSelection.getChildren().addAll(CustomWindowElements.titleBar(selectionPageStage), pokemonSearch, pokemonListTreeView);

        selectionPageLayout.getChildren().addAll(huntInformation, pokemonSelection);
        selectionPageLayout.setId("background");

        Scene selectionScene = new Scene(selectionPageLayout, 750, 500);
        selectionScene.getStylesheets().add("file:shinyTracker.css");

        if(selectionPageStage.getScene() == null)
            selectionPageStage.initStyle(StageStyle.UNDECORATED);

        selectionPageStage.setScene(selectionScene);
        CustomWindowElements.makeDraggable(selectionScene);
        selectionPageStage.show();

        ProgressIndicator progressBar = new ProgressIndicator();
        progressBar.setProgress(1);
        progressBar.managedProperty().bind(progressBar.visibleProperty());
        progressBar.visibleProperty().bind(progressBar.progressProperty().lessThan(1));

        huntInformation.getChildren().add(progressBar);

        progressBar.setLayoutX(pokemonSprite.getLayoutX() - 10);
        progressBar.setLayoutY(pokemonSprite.getLayoutY() - 100);

        pokemonSprite.managedProperty().bind(pokemonSprite.visibleProperty());
        pokemonSprite.visibleProperty().bind(progressBar.progressProperty().isEqualTo(1));

        methodHelp.setOnAction(e -> {
            Alert methodInformation = new Alert(Alert.AlertType.INFORMATION);
            methodInformation.setTitle("Hunt Information");
            methodInformation.setHeaderText(null);
            methodInformation.initStyle(StageStyle.UNDECORATED);
            methodInformation.getDialogPane().getStylesheets().add("file:shinyTracker.css");
            CustomWindowElements.makeDraggable(methodInformation.getDialogPane().getScene());

            VBox dialogLayout = new VBox();
            dialogLayout.setSpacing(10);
            dialogLayout.setPadding(new Insets(10));
            Label huntInfoLabel = new Label("Method: " + selectedMethod + "\n\n");

            Label methodInfoLabel = new Label("Method Info: \n" + selectedMethod.getMethodInfo());
            methodInfoLabel.setMaxWidth(275);
            methodInfoLabel.setWrapText(true);

            dialogLayout.getChildren().addAll(huntInfoLabel, methodInfoLabel);

            Pane masterPane = new Pane();
            masterPane.getChildren().add(dialogLayout);

            VBox resourceSection = new VBox();
            Label methodResourcesLabel = new Label("Resources:");
            resourceSection.getChildren().add(methodResourcesLabel);
            for(int i = 0; i < selectedMethod.getResources().get(0).size(); i++){
                Hyperlink link = new Hyperlink(selectedMethod.getResources().get(0).get(i));
                resourceSection.getChildren().add(link);

                String url = selectedMethod.getResources().get(1).get(i);
                link.setOnAction(f -> {
                    try {
                        Desktop.getDesktop().browse(new URL(url).toURI());
                    } catch (IOException | URISyntaxException g) {
                        g.printStackTrace();
                    }
                });
            }
            dialogLayout.getChildren().add(resourceSection);

            methodInformation.getDialogPane().setContent(masterPane);
            methodInformation.show();
            methodInformation.setHeight(dialogLayout.getHeight() + 33);
            methodInformation.setWidth(360);
        });

        //Listeners for when selection tools are changed
        pokemonListTreeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue.getValue() != selectedPokemon){
                        selectedPokemon = newValue.getValue();
                        if(pokemonSprite.getImage().getProgress() < 1)
                            pokemonSprite.getImage().cancel();
                        pokemonSprite.setImage(FetchImage.getImage(progressBar, pokemonSprite, newValue.getValue(), new Game(21)));
                        updateGameList();
                        updateMethodList();

                        if (selectedGame != null & selectedMethod != null)
                            beginHunt.setDisable(false);
                    }
                });

        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue != selectedGame) {
                        selectedGame = newValue;
                        updatePokemonList();
                        updateMethodList();

                        if(selectedPokemon != null & selectedMethod != null)
                            beginHunt.setDisable(false);
                    }
                });

        methodComboBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue != selectedMethod) {
                        selectedMethod = newValue;
                        updateGameList();
                        updatePokemonList();
                        methodToolTip.setText(selectedMethod.getMethodInfo());

                        if(selectedPokemon != null & selectedGame != null)
                            beginHunt.setDisable(false);
                    }
                });

        pokemonSearch.setOnKeyReleased(e -> {
            searchPokemonList.clear();
            for (TreeItem<Pokemon> pokemonTreeItem : defaultPokemonList)
                if (pokemonTreeItem.getValue().getName().toLowerCase().contains(pokemonSearch.getText().toLowerCase()))
                    searchPokemonList.add(pokemonTreeItem);

            updatePokemonList();
        });

        //opens new hunt and closes the selection window
        beginHunt.setOnAction(e ->{
            SaveData.saveHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, "", 0, 0, 1, -1));
            HuntController.updatePreviousSessionDat(1);
            SaveData.loadHunt(-1);
            HuntController.refreshMiscWindows();
            selectionPageStage.close();
        });
    }

    /**
     * updates pokemonListTreeView when either the game or the method boxes are updated
     */
    private static void updatePokemonList(){
        //clears current pokemon list
        pokemonListRoot.getChildren().clear();
        ObservableList<TreeItem<Pokemon>> updatedPokemonList = FXCollections.observableArrayList();

        if(selectedGame != null){
            //Adds all pokemon from game n's pokedex and makes sure that non-breedable pokemon can be caught
            for(int i : selectedGame.getPokedex()) {
                if(!searchPokemonList.contains(defaultPokemonList.get(i)))
                    continue;

                if (!defaultPokemonList.get(i).getValue().getBreedable()) {
                    if (selectedGame.hasUnbreedable(i))
                        updatedPokemonList.add(defaultPokemonList.get(i));
                } else
                    updatedPokemonList.add(defaultPokemonList.get(i));
            }

            //All games since before Let's Go Pikachu & Eevee didn't have restricted pokedexes.
            //In this case, all pokemon with a generation lower than the game are added while removing unobtainable non-breedable pokemon
            if(updatedPokemonList.size() == 0)
                for(TreeItem<Pokemon> i : searchPokemonList)
                    if(i.getValue().getGeneration() <= selectedGame.getGeneration())
                        if(!i.getValue().getBreedable()) {
                            if(selectedGame.hasUnbreedable(i.getValue().getDexNumber()))
                                updatedPokemonList.add(i);
                        }
                        else
                            updatedPokemonList.add(i);
        }
        else if(selectedMethod != null){
            //Most methods have restricted lists attached, so just add those to the list
            for(int i : selectedMethod.getPokemon())
                if(searchPokemonList.contains(defaultPokemonList.get(i)))
                    updatedPokemonList.add(defaultPokemonList.get(i));

            if(updatedPokemonList.size() == 0){
                for(TreeItem<Pokemon> i : searchPokemonList) {
                    //Finds the newest generation and excludes all older games
                    Game newestGame = gameComboBox.getItems().get(gameComboBox.getItems().size() - 1);
                    if (i.getValue().getGeneration() <= newestGame.getGeneration())
                        if (i.getValue().getBreedable() || !selectedMethod.getBreeding())//If its a breeding method, we need to filter out non-breedable pokemon
                            updatedPokemonList.add(i);
                }
            }
        }
        else
            updatedPokemonList = searchPokemonList;

        //takes already made game list and removes the ones that aren't in the method list
        if(selectedGame != null && selectedMethod != null){
            //removes pokemon that aren't listed in the method table
            Vector<Integer> huntablePokemon = selectedGame.getMethodTable(selectedMethod.getId());
            if(huntablePokemon.size() != 0){
                for(int i = 0; i < updatedPokemonList.size(); i++){
                    if(i == huntablePokemon.size() || Collections.binarySearch(huntablePokemon, updatedPokemonList.get(i).getValue().getDexNumber()) < 0) {
                        updatedPokemonList.remove(i);
                        i--;
                    }
                }
            }
            else{
                //removes non-breedable pokemon if its a breeding method
                if(selectedMethod.getBreeding())
                    for(int i = 0; i < updatedPokemonList.size(); i++){
                        if (updatedPokemonList.get(i).getValue().getGeneration() <= selectedGame.getGeneration()) {
                            if(!updatedPokemonList.get(i).getValue().getBreedable()) {
                                updatedPokemonList.remove(i);
                                i--;
                            }
                        }
                    }
            }
        }

        pokemonListRoot.getChildren().addAll(updatedPokemonList);
        if(selectedPokemon != null) {
            pokemonListTreeView.getSelectionModel().select(defaultPokemonList.get(selectedPokemon.getDexNumber()));
            pokemonListTreeView.scrollTo(0);
        }
    }

    /**
     * updates gameComboBox when either the or the method box are updated
     */
    private static void updateGameList(){
        //resets current game box
        gameComboBox.getItems().clear();
        ObservableList<Game> updatedGameList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : defaultGameList) {
                //if its unbreedable, its makes sure its catchable in n game
                if (!selectedPokemon.getBreedable() && !i.hasUnbreedable(selectedPokemon.getDexNumber()))
                    continue;

                //for unrestricted gens, add all. for restricted add only if they are listed in the pokedex of n game
                if (i.getPokedex().size() == 0) {
                    if (i.getGeneration() >= selectedPokemon.getGeneration())
                        updatedGameList.add(i);
                } else {
                    if (Collections.binarySearch(i.getPokedex(), selectedPokemon.getDexNumber()) >= 0)
                        updatedGameList.add(i);
                }
            }
        }
        else if(selectedMethod != null){
            //the only method that is in every game is full odds, so add all listed games of each
            for(int i : selectedMethod.getGames())
                updatedGameList.add(defaultGameList.get(i));

            if(updatedGameList.size() == 0)
                updatedGameList.addAll(defaultGameList);
        }
        else
            updatedGameList = defaultGameList;

        //removes from list if they aren't listed in method's list
        if(selectedPokemon != null && selectedMethod != null){
            if(selectedMethod.getGames().size() != 0) {
                for (int i = 0; i < updatedGameList.size(); i++) {
                    if(selectedMethod.getBreeding()) {
                        if (!selectedMethod.getGames().contains(updatedGameList.get(i).getId())) { //makes sure that a breeding method is in n game
                            updatedGameList.remove(i);
                            i--;
                        }
                    }else {
                        //removes from list if it's not in the game method's list
                        Vector<Integer> huntablePokemon = updatedGameList.get(i).getMethodTable(selectedMethod.getId());
                        if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) < 0) {
                            updatedGameList.remove(i);
                            i--;
                        }
                    }
                }
            }
        }

        gameComboBox.getItems().addAll(updatedGameList);
        if(selectedGame != null)
            gameComboBox.getSelectionModel().select(selectedGame);

        //For some reason, if the box isn't opened and closed after updating, when selecting the first element
        //in a list of 1 games the combobox won't close
        gameComboBox.show();
        gameComboBox.hide();
    }

    /**
     * updates methodComboBox when either the pokemon list or the game box are updated
     */
    private static void updateMethodList(){
        //resets current method box
        methodComboBox.getItems().clear();
        ObservableList<Method> updatedMethodList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : gameComboBox.getItems()) {
                for (int j : i.getMethods()) {
                    if (!updatedMethodList.contains(defaultMethodList.get(j))) { //don't add method twice
                        if (defaultMethodList.get(j).getBreeding()) {
                            if (selectedPokemon.getBreedable()) //adds breedable methods if pokemon n can breed
                                updatedMethodList.add(defaultMethodList.get(j));
                        } else {
                            Vector<Integer> huntablePokemon = i.getMethodTable(j);
                            if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) >= 0)//full odds or method lists pokemon n
                                updatedMethodList.add(defaultMethodList.get(j));
                        }
                    }
                }
            }
        }
        else if(selectedGame != null){
            for(int i : selectedGame.getMethods())//adds every method listed in game n
                updatedMethodList.add(defaultMethodList.get(i));
        }
        else
            updatedMethodList = defaultMethodList;

        //removes if not in game list if pokemon list is already made
        if(selectedPokemon != null && selectedGame != null){
            for(int i = 0; i < updatedMethodList.size(); i++){
                if(i == selectedGame.getMethods().size() ||  !selectedGame.getMethods().contains(updatedMethodList.get(i).getId()) ||
                        (defaultMethodList.get(selectedGame.getMethods().get(i)).getBreeding() && !selectedPokemon.getBreedable()) ||
                        (selectedGame.getMethodTable(updatedMethodList.get(i).getId()).size() > 0 && Collections.binarySearch(selectedGame.getMethodTable(updatedMethodList.get(i).getId()), selectedPokemon.getDexNumber()) < 0)) {
                    updatedMethodList.remove(i);
                    i--;
                }
            }
        }

        methodComboBox.getItems().addAll(updatedMethodList);
        if(selectedMethod != null)
            methodComboBox.getSelectionModel().select(selectedMethod);

        //For some reason, if the box isn't opened and closed after updating, when selecting the first element
        //in a list of 1 methods the combobox won't close
        methodComboBox.show();
        methodComboBox.hide();
    }

    /**
     * resets all variables when opening a new window
     */
    private static void resetVariables(){
        selectedPokemon = null;
        pokemonListTreeView = new TreeView<>();
        pokemonListRoot = new TreeItem<>();

        selectedGame = null;
        gameComboBox = new ComboBox<>();

        selectedMethod = null;
        methodComboBox = new ComboBox<>();
    }
}
