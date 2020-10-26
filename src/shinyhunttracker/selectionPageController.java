package shinyhunttracker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.util.Duration.INDEFINITE;
import static javafx.util.Duration.ZERO;

public class selectionPageController implements Initializable {
    //selection page elements
    public BorderPane shinyTrackerScene;
    public TreeView<String> PokemonList, GameList, MethodList;
    public Label pokemonLabel = new Label(), gameLabel = new Label(), methodLabel = new Label();
    public CheckBox alolanCheckBox, galarianCheckBox, shinyCharmCheckBox, lureCheckBox;
    public Button beginHuntButton, helpButton;
    Tooltip methodToolTip = new Tooltip();

    //tree view elements
    public TreeItem<String> pokemonRoot, Gen1, Gen2, Gen3, Gen4, Gen5, Gen6, Gen7, Gen8;
    public TreeItem<String> gameRoot, treeGamesGen1, treeGamesGen2, treeGamesGen3, treeGamesGen4, treeGamesGen5, treeGamesGen6, treeGamesGen7, treeGamesGen8;
    public TreeItem<String> methodRoot, evolution0, evolution1, evolution2;
    int index = 0;
    Tooltip searchField = new Tooltip("Type to search for Pokemon");
    StringProperty pokemonSearch = new SimpleStringProperty();

    //selected objects
    Game selectedGame = new Game();
    Pokemon selectedPokemon = new Pokemon();
    Pokemon Stage0 = new Pokemon();
    Pokemon Stage1 = new Pokemon();
    Method selectedMethod = new Method();

    //hunt page after pokemon is caught variables
    huntController controller;
    String currentLayout;

    //misc variables
    int oldSelectionGeneration, oldSelectionGameGeneration = 0;
    int evolutionStage = 0;

    //array with every pokemon
    String[][] Pokedex = {{"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"},
                        {"Chikorita", "Bayleef", "Meganium", "Cyndaquil", "Quilava", "Typhlosion", "Totodile", "Croconaw", "Feraligatr", "Sentret", "Furret", "Hoothoot", "Noctowl", "Ledyba", "Ledian", "Spinarak", "Ariados", "Crobat", "Chinchou", "Lanturn", "Pichu", "Cleffa", "Igglybuff", "Togepi", "Togetic", "Natu", "Xatu", "Mareep", "Flaaffy", "Ampharos", "Bellossom", "Marill", "Azumarill", "Sudowoodo", "Politoed", "Hoppip", "Skiploom", "Jumpluff", "Aipom", "Sunkern", "Sunflora", "Yanma", "Wooper", "Quagsire", "Espeon", "Umbreon", "Murkrow", "Slowking", "Misdreavus", "Unown", "Wobbuffet", "Girafarig", "Pineco", "Forretress", "Dunsparce", "Gligar", "Steelix", "Snubbull", "Granbull", "Qwilfish", "Scizor", "Shuckle", "Heracross", "Sneasel", "Teddiursa", "Ursaring", "Slugma", "Magcargo", "Swinub", "Piloswine", "Corsola", "Remoraid", "Octillery", "Delibird", "Mantine", "Skarmory", "Houndour", "Houndoom", "Kingdra", "Phanpy", "Donphan", "Porygon2", "Stantler", "Smeargle", "Tyrogue", "Hitmontop", "Smoochum", "Elekid", "Magby", "Miltank", "Blissey", "Raikou", "Entei", "Suicune", "Larvitar", "Pupitar", "Tyranitar", "Lugia", "Ho-Oh", "Celebi"},
                        {"Treecko", "Grovyle", "Sceptile", "Torchic", "Combusken", "Blaziken", "Mudkip", "Marshtomp", "Swampert", "Poochyena", "Mightyena", "Zigzagoon", "Linoone", "Wurmple", "Silcoon", "Beautifly", "Cascoon", "Dustox", "Lotad", "Lombre", "Ludicolo", "Seedot", "Nuzleaf", "Shiftry", "Taillow", "Swellow", "Wingull", "Pelipper", "Ralts", "Kirlia", "Gardevoir", "Surskit", "Masquerain", "Shroomish", "Breloom", "Slakoth", "Vigoroth", "Slaking", "Nincada", "Ninjask", "Shedinja", "Whismur", "Loudred", "Exploud", "Makuhita", "Hariyama", "Azurill", "Nosepass", "Skitty", "Delcatty", "Sableye", "Mawile", "Aron", "Lairon", "Aggron", "Meditite", "Medicham", "Electrike", "Manectric", "Plusle", "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Swalot", "Carvanha", "Sharpedo", "Wailmer", "Wailord", "Numel", "Camerupt", "Torkoal", "Spoink", "Grumpig", "Spinda", "Trapinch", "Vibrava", "Flygon", "Cacnea", "Cacturne", "Swablu", "Altaria", "Zangoose", "Seviper", "Lunatone", "Solrock", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Baltoy", "Claydol", "Lileep", "Cradily", "Anorith", "Armaldo", "Feebas", "Milotic", "Castform", "Kecleon", "Shuppet", "Banette", "Duskull", "Dusclops", "Tropius", "Chimecho", "Absol", "Wynaut", "Snorunt", "Glalie", "Spheal", "Sealeo", "Walrein", "Clamperl", "Huntail", "Gorebyss", "Relicanth", "Luvdisc", "Bagon", "Shelgon", "Salamence", "Beldum", "Metang", "Metagross", "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Jirachi", "Deoxys"},
                        {"Turtwig", "Grotle", "Torterra", "Chimchar", "Monferno", "Infernape", "Piplup", "Prinplup", "Empoleon", "Starly", "Staravia", "Staraptor", "Bidoof", "Bibarel", "Kricketot", "Kricketune", "Shinx", "Luxio", "Luxray", "Budew", "Roserade", "Cranidos", "Rampardos", "Shieldon", "Bastiodon", "Burmy", "Wormadam", "Mothim", "Combee", "Vespiquen", "Pachirisu", "Buizel", "Floatzel", "Cherubi", "Cherrim", "Shellos", "Gastrodon", "Ambipom", "Drifloon", "Drifblim", "Buneary", "Lopunny", "Mismagius", "Honchkrow", "Glameow", "Purugly", "Chingling", "Stunky", "Skuntank", "Bronzor", "Bronzong", "Bonsly", "Mime Jr.", "Happiny", "Chatot", "Spiritomb", "Gible", "Gabite", "Garchomp", "Munchlax", "Riolu", "Lucario", "Hippopotas", "Hippowdon", "Skorupi", "Drapion", "Croagunk", "Toxicroak", "Carnivine", "Finneon", "Lumineon", "Mantyke", "Snover", "Abomasnow", "Weavile", "Magnezone", "Lickilicky", "Rhyperior", "Tangrowth", "Electivire", "Magmortar", "Togekiss", "Yanmega", "Leafeon", "Glaceon", "Gliscor", "Mamoswine", "Porygon-Z", "Gallade", "Probopass", "Dusknoir", "Froslass", "Rotom", "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus"},
                        {"Victini", "Snivy", "Servine", "Serperior", "Tepig", "Pignite", "Emboar", "Oshawott", "Dewott", "Samurott", "Patrat", "Watchog", "Lillipup", "Herdier", "Stoutland", "Purrloin", "Liepard", "Pansage", "Simisage", "Pansear", "Simisear", "Panpour", "Simipour", "Munna", "Musharna", "Pidove", "Tranquill", "Unfezant", "Blitzle", "Zebstrika", "Roggenrola", "Boldore", "Gigalith", "Woobat", "Swoobat", "Drilbur", "Excadrill", "Audino", "Timburr", "Gurdurr", "Conkeldurr", "Tympole", "Palpitoad", "Seismitoad", "Throh", "Sawk", "Sewaddle", "Swadloon", "Leavanny", "Venipede", "Whirlipede", "Scolipede", "Cottonee", "Whimsicott", "Petilil", "Lilligant", "Basculin", "Sandile", "Krokorok", "Krookodile", "Darumaka", "Darmanitan", "Maractus", "Dwebble", "Crustle", "Scraggy", "Scrafty", "Sigilyph", "Yamask", "Cofagrigus", "Tirtouga", "Carracosta", "Archen", "Archeops", "Trubbish", "Garbodor", "Zorua", "Zoroark", "Minccino", "Cinccino", "Gothita", "Gothorita", "Gothitelle", "Solosis", "Duosion", "Reuniclus", "Ducklett", "Swanna", "Vanillite", "Vanillish", "Vanilluxe", "Deerling", "Sawsbuck", "Emolga", "Karrablast", "Escavalier", "Foongus", "Amoonguss", "Frillish", "Jellicent", "Alomomola", "Joltik", "Galvantula", "Ferroseed", "Ferrothorn", "Klink", "Klang", "Klinklang", "Tynamo", "Eelektrik", "Eelektross", "Elgyem", "Beheeyem", "Litwick", "Lampent", "Chandelure", "Axew", "Fraxure", "Haxorus", "Cubchoo", "Beartic", "Cryogonal", "Shelmet", "Accelgor", "Stunfisk", "Mienfoo", "Mienshao", "Druddigon", "Golett", "Golurk", "Pawniard", "Bisharp", "Bouffalant", "Rufflet", "Braviary", "Vullaby", "Mandibuzz", "Heatmor", "Durant", "Deino", "Zweilous", "Hydreigon", "Larvesta", "Volcarona", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom", "Landorus", "Kyurem", "Keldeo", "Meloetta", "Genesect"},
                        {"Chespin", "Quilladin", "Chesnaught", "Fennekin", "Braixen", "Delphox", "Froakie", "Frogadier", "Greninja", "Bunnelby", "Diggersby", "Fletchling", "Fletchinder", "Talonflame", "Scatterbug", "Spewpa", "Vivillon", "Litleo", "Pyroar", "Flabébé", "Floette", "Florges", "Skiddo", "Gogoat", "Pancham", "Pangoro", "Furfrou", "Espurr", "Meowstic", "Honedge", "Doublade", "Aegislash", "Spritzee", "Aromatisse", "Swirlix", "Slurpuff", "Inkay", "Malamar", "Binacle", "Barbaracle", "Skrelp", "Dragalge", "Clauncher", "Clawitzer", "Helioptile", "Heliolisk", "Tyrunt", "Tyrantrum", "Amaura", "Aurorus", "Sylveon", "Hawlucha", "Dedenne", "Carbink", "Goomy", "Sliggoo", "Goodra", "Klefki", "Phantump", "Trevenant", "Pumpkaboo", "Gourgeist", "Bergmite", "Avalugg", "Noibat", "Noivern", "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion"},
                        {"Rowlet", "Dartrix", "Decidueye", "Litten", "Torracat", "Incineroar", "Popplio", "Brionne", "Primarina", "Pikipek", "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Grubbin", "Charjabug", "Vikavolt", "Crabrawler", "Crabominable", "Oricorio", "Cutiefly", "Ribombee", "Rockruff", "Lycanroc", "Wishiwashi", "Mareanie", "Toxapex", "Mudbray", "Mudsdale", "Dewpider", "Araquanid", "Fomantis", "Lurantis", "Morelull", "Shiinotic", "Salandit", "Salazzle", "Stufful", "Bewear", "Bounsweet", "Steenee", "Tsareena", "Comfey", "Oranguru", "Passimian", "Wimpod", "Golisopod", "Sandygast", "Palossand", "Pyukumuku", "Type: Null", "Silvally", "Minior", "Komala", "Turtonator", "Togedemaru", "Mimikyu", "Bruxish", "Drampa", "Dhelmise", "Jangmo-o", "Hakamo-o", "Kommo-o", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Necrozma", "Magearna", "Marshadow", "Poipole", "Naganadel", "Stakataka", "Blacephalon", "Zeraora", "Meltan", "Melmetal"},
                        {"Grookey", "Thwackey", "Rillaboom", "Scorbunny", "Raboot", "Cinderace", "Sobble", "Drizzile", "Inteleon", "Skwovet", "Greedent", "Rookidee", "Corvisquire", "Corviknight", "Blipbug", "Dottler", "Orbeetle", "Nickit", "Thievul", "Gossifleur", "Eldegoss", "Wooloo", "Dubwool", "Chewtle", "Drednaw", "Yamper", "Boltund", "Rolycoly", "Carkol", "Coalossal", "Applin", "Flapple", "Appletun", "Silicobra", "Sandaconda", "Cramorant", "Arrokuda", "Barraskewda", "Toxel", "Toxtricity", "Sizzlipede", "Centiskorch", "Clobbopus", "Grapploct", "Sinistea", "Polteaseist", "Hatenna", "Hattrem", "Hatterene", "Impidimp", "Morgrem", "Grimmsnarl", "Obstagoon", "Perrserker", "Cursola", "Sirfetch'd", "Mr. Rime", "Runerigus", "Milcery", "Alcremie", "Falinks", "Pincurchin", "Snom", "Frosmoth", "Stonjourner", "Eiscue", "Indeedee", "Morpeko", "Cufant", "Copperajah", "Dracozolt", "Arctozolt", "Dracovish", "Arctovish", "Duraludon", "Dreepy", "Drakloak", "Dragapult", "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude"}};

    //array with pokemon avaliable in SWSH
    String[] SWSHPokedex = {"Caterpie", "Metapod", "Butterfree", "Grubbin", "Charjabug", "Vikavolt", "Hoothoot", "Noctowl", "Pidove", "Tranquill", "Unfezant", "Zigzagoon", "Linoone", "Lotad", "Lombre", "Ludicolo", "Seedot", "Nuzleaf", "Shiftry", "Purrloin", "Liepard", "Bunnelby", "Diggersby", "Minccino", "Cinccino", "Bounsweet", "Steenee", "Tsareena", "Oddish", "Gloom", "Vileplume", "Bellossom", "Budew", "Roselia", "Roserade", "Wingull", "Pelipper", "Joltik", "Galvantula", "Electrike", "Manectric", "Vulpix", "Ninetales", "Growlithe", "Arcanine", "Vanillite", "Vanillish", "Vaniluxe", "Swinub", "Piloswine", "Mamoswine", "Snorunt", "Glalie", "Froslass", "Baltoy", "Claydol", "Mudbray", "Mudsdale", "Dwebble", "Crustle", "Golett", "Golurk", "Munna", "Musharna", "Natu", "Xatu", "Stufful", "Bewear", "Snover", "Abomasnow", "Krabby", "Kingler", "Wooper", "Quagsire", "Corphish", "Crawdaunt", "Nincada", "Ninjask", "Shedinja", "Tyrogue", "Hitmonlee", "Hitmonchan", "Hitmontop", "Pancham", "Pangoro", "Klink", "Klinklang", "Combee", "Vespiquen", "Bronzor", "Bronzong", "Ralts", "Kirlia", "Gardevoir", "Gallade", "Drifblim", "Gossifleur", "Eldegoss","Cherubi", "Cherrim", "Stunky", "Skuntank", "Tympole", "Palpitoad", "Seismitoad", "Duskull", "Dusclops","Dusknoir", "Machop", "Machoke", "Machamp", "Gastly", "Haunter", "Gengar", "Magikarp", "Gyarados","Goldeen", "Seaking", "Remoraid", "Octillery", "Shellder", "Cloyster", "Feebas", "Milotic", "Basculin", "Wishiwashi", "Pyukumuku", "Trubbish", "Garbodor", "Diglett", "Dugtrio", "Drilbur", "Excadrill", "Roggenrola", "Boldore", "Gigalith", "Timburr", "Gurdurr", "Conkeldurr", "Woobat", "Swoobat", "Noibat", "Noivern", "Onix", "Steelix", "Meowth", "Persian", "Cutiefly", "Ribombee", "Ferroseed", "Ferrothorn", "Pumpkaboo", "Gourgeist", "Pichu", "Pikachu", "Raichu", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Espeon", "Umbreon", "Leafeon", "Glaceon", "Sylveon", "Espurr", "Meowstic", "Swirlix", "Slurpuff", "Spritzee", "Aromatisse", "Dewpider", "Araquanid", "Wynaut", "Wobbuffet", "Farfetch'd", "Chinchou", "Croagunk", "Toxicroak", "Scraggy", "Scrafty", "Stunfisk", "Shuckle", "Barboach", "Whiscash", "Shellos", "Gastrodon", "Wimpod", "Golisopod", "Binacle", "Barbaracle", "Corsola", "Salandit", "Salazzle", "Pawniard", "Bisharp", "Throh", "Sawk", "Koffing", "Weezing", "Bonsly", "Sudowoodo", "Cleffa", "Clefairy", "Clefable", "Togepi", "Togetic", "Togekiss", "Munchlax", "Snorlax", "Cottonee", "Whimsicott", "Rhyhorn", "Rhydon", "Rhyperior", "Gothita", "Gothorita", "Gothitelle", "Solosis", "Duosion", "Reuniclus", "Karrablast", "Escavalier", "Shelmet", "Accelgor", "Elgyem", "Beheeyem", "Cubchoo", "Beartic", "Rufflet", "Braviary", "Vullaby", "Mandibuzz", "Skorupi", "Drapion", "Litwick", "Lampent", "Chandelure", "Inkay", "Malamar", "Sneasel", "Weavile", "Sableye", "Mawile", "Maractus", "Sigilyph", "Riolu", "Lucario", "Torkoal", "Mimikyu", "Qwilfish", "Frillish", "Jellicent", "Mareanie", "Toxapex", "Hippopotas", "Hippowdon", "Durant", "Heatmor", "Helioptile", "Heliolisk", "Hawlucha", "Trapinch", "Vibrava","Flygon", "Axew", "Fraxure", "Haxorus", "Yamask", "Cofagrigus", "Honedge", "Doublade", "Aegislash", "Ponyta", "Rapidash", "Phantump", "Trevenant", "Morelull", "Shiinotic", "Oranguru", "Passimian", "Drampa", "Turtonator", "Togedemaru", "Mantyke", "Mantine", "Wailmer", "Wailord", "Bergmite", "Avalugg", "Dehlmise", "Lapras", "Lunatone", "Solrock", "Mime Jr.", "Mr. Mime", "Mr. Rime", "Darumaka", "Darmanitan", "Rotom", "Ditto", "Charmander", "Charmeleon", "Charizard", "Type: Null", "Silvally", "Larvitar", "Pupitar", "Tyranitar", "Deino", "Zweilous", "Hydreigon", "Goomy", "Sliggoo", "Goodra", "Jangmo-o", "Hakamo-o", "Kommo-o", "Bulbasaur", "Ivysaur", "Venusaur", "Squirtle", "Wartortle", "Blastoise", "Mewtwo", "Mew", "Celebi", "Jirachi", "Cobalion", "Terrakion", "Virizion", "Reshiram", "Zekrom", "Kyurem", "Keldeo", "Rowlet", "Dartrix", "Decidueye", "Litten", "Torracat", "Incineroar", "Popplio", "Brionne", "Primarina", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Necrozma", "Marshadow", "Zeraora", "Meltan", "Melmetal", "Kubfu", "Urshifu", "Zarude", "Slowpoke", "Slowbro", "Sandshrew", "Sandslash", "Jigglypuff", "Wigglytuff", "Psyduck", "Golduck", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Tentacool", "Tentacruel", "Magnemite", "Magneton", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Lickitung", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Staryu", "Starmie", "Scyther", "Pinsir", "Tauros", "Porygon", "Igglybuff", "Marill", "Azumarill", "Politoed", "Slowking", "Dunsparce", "Scizor", "Heracross", "Skarmory", "Kingdra", "Porygon2", "Miltank", "Blissey", "Whismur", "Loudred", "Exploud", "Azurill", "Carvanha", "Sharpedo", "Shinx", "Luxio", "Luxray", "Buneary", "Lopunny", "Happiny", "Magnezone", "Lickilicky", "Tangwoth", "Porygon-Z", "Lillipup", "Herdier", "Stoutland", "Venipede", "Whirlipede", "Scolipede", "Petilil", "Lilligant", "Sandile", "Krokorok", "Krookodile", "Zorua", "Zoroark", "Emolga", "Foongus", "Amoonguss", "Mienfoo", "Mienshao", "Druddigon", "Bouffalant", "Larvesta", "Volcarona", "Fletchling", "Fletchinder", "Talonflame", "Skrelp", "Dragalge", "Clauncher", "Clawitzer", "Dedenne", "Klefki", "Rockruff", "Lycanroc", "Fomantis", "Lurantis", "Comfey", "Sandygast", "Palossand", "Magearna", "Alolan Raichu", "Alolan Sandshrew", "Alolan Sandslash", "Alolan Vulpix", "Alolan Ninetales", "Alolan Diglett", "Alolan Dugtrio", "Alolan Meowth", "Alolan Persian", "Alolan Exeggutor", "Alolan Marowak"};

    //array with every main line pokemon game
    String[][] Games= {{"Red", "Green", "Blue", "Yellow"},
                    {"Gold", "Silver", "Crystal"},
                    {"Ruby", "Sapphire", "FireRed", "LeafGreen", "Emerald"},
                    {"Diamond", "Pearl", "Platinum", "HeartGold", "SoulSilver"},
                    {"Black", "White","Black 2", "White 2"},
                    {"X", "Y", "Omega Ruby", "Alpha Sapphire"},
                    {"Sun", "Moon", "Ultra Sun", "Ultra Moon", "Let's Go Pikachu", "Let's Go Eevee"},
                    {"Sword", "Shield"}};

    @Override
    public void initialize(URL url, ResourceBundle rb){
        InitializePokemonList();//creates pokemon list
        searchField.setShowDuration(INDEFINITE);
        searchField.setShowDelay(new Duration(500));
        PokemonList.setTooltip(searchField);

        searchField.textProperty().bind(pokemonSearch);

        PokemonList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        String newSelectionPokemon, oldSelectionPokemon;

                        newSelectionPokemon = newValue.toString().substring(18, newValue.toString().length()-2);
                        if(findGenerationPokemon(newSelectionPokemon) != 0) {//throws away input if selection is Generation 1, Generation 2, etc.
                            if (oldValue != null) {//sets old value to old selected pokemon name if it wasn't one of the generation tree views
                                oldSelectionPokemon = oldValue.toString().substring(18, oldValue.toString().length() - 2);
                                if (findGenerationPokemon(oldSelectionPokemon) != 0) {
                                    oldSelectionGeneration = findGenerationPokemon(oldSelectionPokemon);
                                }
                            }
                            selectedPokemon = new Pokemon(newSelectionPokemon, findGenerationPokemon(newSelectionPokemon));//creates Pokemon object
                            pokemonLabel.textProperty().bind(selectedPokemon.getNameProperty());

                            //checks if pokemon has regional variant, and resets checkbox
                            alolanCheckBox.setDisable(!selectedPokemon.isAlolan());
                            alolanCheckBox.setSelected(false);
                            galarianCheckBox.setDisable(!selectedPokemon.isGalarian());
                            galarianCheckBox.setSelected(false);

                            //captures old selected game generation
                            if(!selectedGame.getName().equals(""))
                                oldSelectionGameGeneration = selectedGame.getGeneration();

                            InitializeGameList(selectedPokemon.getGeneration());//creates game list
                            collapseGeneration(oldSelectionGameGeneration);//opens game generation tree view based on previously selected game
                        }
                    }
                });

        GameList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        helpButton.setVisible(false);

                        //disables shiny charm and lure checkboxes
                        shinyCharmCheckBox.setDisable(true);
                        lureCheckBox.setDisable(true);

                        String newSelectionGame = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(findGenerationGame(newSelectionGame) != 0) {//throws away input if selection is Generation 1, Generation 2, etc.
                            selectedGame = new Game(newSelectionGame, findGenerationGame(newSelectionGame));//create Game object
                            gameLabel.textProperty().bind(selectedGame.getNameProperty());

                            //resets shiny charm and lure checkboxes
                            shinyCharmCheckBox.setSelected(false);
                            lureCheckBox.setSelected(false);

                            //Enables Shiny Charm if it is avaliable in the selected Game
                            //Shiny Charm is available from Black 2, White 2 up to the present games
                            if(selectedGame.generation >= 5) {
                                if (!(selectedGame.getName().compareTo("Black") == 0 || selectedGame.getName().compareTo("White") == 0))
                                    shinyCharmCheckBox.setDisable(false);
                            }else
                                shinyCharmCheckBox.setDisable(true);

                            //Enables Lure if the selected game is one of the let's go games
                            if(selectedGame.getName().length() >= 3)
                                lureCheckBox.setDisable(!(selectedGame.getName().substring(0,3).compareTo("Let") == 0));

                            InitializeMethodList();//creates method list
                        }
                    }
                });

        MethodList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        String newSelectionMethod = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(findGenerationPokemon(newSelectionMethod) == 0) {
                            selectedMethod = new Method(newSelectionMethod, selectedGame.getGeneration());//creates Method object
                            methodLabel.textProperty().bind(selectedMethod.getNameProperty());
                            beginHuntButton.setDisable(selectedMethod.getName() == null);//disables begin hunt button if the selected method is a pokemon name
                            helpButton.setVisible(selectedMethod.getName() != null);//displays tooltip button
                            setToolTip(selectedMethod.getName());//creates the tool tip that appears when method is selected
                            methodToolTip.setShowDelay(ZERO);
                            methodToolTip.setShowDuration(INDEFINITE);
                        }
                    }
                });
    }

    //creates pokemon list
    public void InitializePokemonList(){
        pokemonRoot = new TreeItem<>();

        //create tree views by pokemon generation
        Gen1 = makeBranch("Generation 1", pokemonRoot);
        Gen2 = makeBranch("Generation 2", pokemonRoot);
        Gen3 = makeBranch("Generation 3", pokemonRoot);
        Gen4 = makeBranch("Generation 4", pokemonRoot);
        Gen5 = makeBranch("Generation 5", pokemonRoot);
        Gen6 = makeBranch("Generation 6", pokemonRoot);
        Gen7 = makeBranch("Generation 7", pokemonRoot);
        Gen8 = makeBranch("Generation 8", pokemonRoot);

        //placeholders so that the user can expand the lists
        makeBranch("placeholder", Gen1);
        makeBranch("placeholder", Gen2);
        makeBranch("placeholder", Gen3);
        makeBranch("placeholder", Gen4);
        makeBranch("placeholder", Gen5);
        makeBranch("placeholder", Gen6);
        makeBranch("placeholder", Gen7);
        makeBranch("placeholder", Gen8);

        //add listeners for when the lists are expanded, and add all pokemon and sprites to the list
        Gen1.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen1.getChildren().get(0).getValue().contains("placeholder"))
                Gen1.getChildren().remove(0);
            for(String i: Pokedex[0]) {
                try {
                    FileInputStream input;
                    if(i.compareTo("Nidoran♀") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 1/nidoran-f.png");
                    else if(i.compareTo("Nidoran♂") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 1/nidoran-m.png");
                    else if(i.compareTo("Mr. Mime") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 1/mr-mime.png");
                    else if(i.compareTo("Farfetch'd") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 1/farfetchd.png");
                    else
                        input = new FileInputStream("Images/PC Sprites/Generation 1/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen1).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen1);
                }
            }
        });
        Gen2.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen2.getChildren().get(0).getValue().contains("placeholder"))
                Gen2.getChildren().remove(0);
            for(String i: Pokedex[1]) {
                try {
                    FileInputStream input = new FileInputStream("Images/PC Sprites/Generation 2/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen2).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen2);
                }
            }
        });
        Gen3.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen3.getChildren().get(0).getValue().contains("placeholder"))
                Gen3.getChildren().remove(0);
            for(String i: Pokedex[2]) {
                try {
                    FileInputStream input = new FileInputStream("Images/PC Sprites/Generation 3/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen3).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen3);
                }
            }
        });
        Gen4.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen4.getChildren().get(0).getValue().contains("placeholder"))
                Gen4.getChildren().remove(0);
            for(String i: Pokedex[3]) {
                try {
                    FileInputStream input;
                    if(i.compareTo("Mime Jr.") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 4/mime-jr.png");
                    else
                        input = new FileInputStream("Images/PC Sprites/Generation 4/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen4).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen4);
                }
            }
        });
        Gen5.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen5.getChildren().get(0).getValue().contains("placeholder"))
                Gen5.getChildren().remove(0);
            for(String i: Pokedex[4]) {
                try {
                    FileInputStream input = new FileInputStream("Images/PC Sprites/Generation 5/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen5).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen5);
                }
            }
        });
        Gen6.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen6.getChildren().get(0).getValue().contains("placeholder"))
                Gen6.getChildren().remove(0);
            for(String i: Pokedex[5]) {
                try {
                    FileInputStream input;
                    if(i.compareTo("Flabébé") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 6/flabebe.png");
                    else
                        input = new FileInputStream("Images/PC Sprites/Generation 6/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen6).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen6);
                }
            }
        });
        Gen7.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen7.getChildren().get(0).getValue().contains("placeholder"))
                Gen7.getChildren().remove(0);
            for(String i: Pokedex[6]) {
                try {
                    FileInputStream input;
                    if(i.compareTo("Type: Null") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 7/type-null.png");
                    else if(i.compareTo("Tapu Koko") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 7/tapu-koko.png");
                    else if(i.compareTo("Tapu Lele") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 7/tapu-lele.png");
                    else if(i.compareTo("Tapu Bulu") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 7/tapu-bulu.png");
                    else if(i.compareTo("Tapu Fini") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 7/tapu-fini.png");
                    else
                        input = new FileInputStream("Images/PC Sprites/Generation 7/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen7).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen7);
                }
            }
        });
        Gen8.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen8.getChildren().get(0).getValue().contains("placeholder"))
                Gen8.getChildren().remove(0);
            for(String i: Pokedex[7]) {
                try {
                    FileInputStream input;
                    if(i.compareTo("Mr. Rime") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 8/mr-rime.png");
                    else if(i.compareTo("Sirfetch'd") == 0)
                        input = new FileInputStream("Images/PC Sprites/Generation 8/mr-rime.png");
                    else
                        input = new FileInputStream("Images/PC Sprites/Generation 8/" + i.toLowerCase() + ".png");
                    Image image = new Image(input);
                    ImageView sprite = new ImageView(image);
                    makeBranch(i, Gen8).setGraphic(sprite);
                }catch (FileNotFoundException e){
                    System.out.println(i + " wasn't found");
                    makeBranch(i, Gen8);
                }
            }
        });

        PokemonList.setRoot(pokemonRoot);
        PokemonList.setShowRoot(false);
    }

    //creates game list
    public void InitializeGameList(int generation){
        clearMethodList();//clears Method list
        selectedGame = new Game();//resets selected Game

        //resets Shiny Charm and Lure checkboxes
        shinyCharmCheckBox.setSelected(false);
        lureCheckBox.setSelected(false);

        //since legendaries are only available in certain games, I created a method to restrict what games are displayed
        if(!selectedPokemon.getHuntable()) {
            InitializeRestrictedGameList(generation);
            return;
        }

        //initializes all tree items
        gameRoot = new TreeItem<>();
        treeGamesGen1 = new TreeItem<>();
        treeGamesGen2 = new TreeItem<>();
        treeGamesGen3 = new TreeItem<>();
        treeGamesGen4 = new TreeItem<>();
        treeGamesGen5 = new TreeItem<>();
        treeGamesGen6 = new TreeItem<>();
        treeGamesGen7 = new TreeItem<>();
        treeGamesGen8 = new TreeItem<>();

        //checks to see if the selected pokemon is alolan or galarian and sets the generation accordingly
        if(selectedPokemon.getName().length() > 6)
            if(selectedPokemon.getName().substring(0,6).compareTo("Alolan") == 0)
                generation = 7;
        if(selectedPokemon.getName().length() > 8)
            if(selectedPokemon.getName().substring(0,8).compareTo("Galarian") == 0)
                generation = 8;

        //goes through and adds the games to the respective generations based on the selected pokemon's generation
        if(generation == 0)
            return;
        if(generation <= 1) {
            treeGamesGen1 = makeBranch("Generation 1", gameRoot);
            for (String i : Games[0]) {
                makeBranch(i, treeGamesGen1);
            }
        }
        if(generation <= 2) {
            treeGamesGen2 = makeBranch("Generation 2", gameRoot);
            for (String i : Games[1]) {
                makeBranch(i, treeGamesGen2);
            }
        }
        if(generation <= 3) {
            treeGamesGen3 = makeBranch("Generation 3", gameRoot);
            for (String i : Games[2]) {
                makeBranch(i, treeGamesGen3);
            }
        }
        if(generation <= 4) {
            treeGamesGen4 = makeBranch("Generation 4", gameRoot);
            for (String i : Games[3]) {
                makeBranch(i, treeGamesGen4);
            }
        }
        if(generation <= 5) {
            treeGamesGen5 = makeBranch("Generation 5", gameRoot);
            for (String i : Games[4]) {
                makeBranch(i, treeGamesGen5);
            }
        }
        if(generation <= 6) {
            treeGamesGen6 = makeBranch("Generation 6", gameRoot);
            for (String i : Games[5]) {
                makeBranch(i, treeGamesGen6);
            }
        }
        if(generation <= 7){
            treeGamesGen7 = makeBranch("Generation 7", gameRoot);
            for(String i: Games[6]) {
                //only generation 1 pokemon are available in the lets go games
                if(i.substring(0,3).compareTo("Let") == 0) {
                    if (generation == 1)
                        makeBranch(i, treeGamesGen7);
                    if(selectedPokemon.getName().length() > 6)
                        if(selectedPokemon.getName().substring(0,6).compareTo("Alolan") == 0)
                            makeBranch(i, treeGamesGen7);
                }else
                    makeBranch(i, treeGamesGen7);
            }
        }
        if(generation <= 8) {
            treeGamesGen8 = makeBranch("Generation 8", gameRoot);
            for (String i : Games[7]) {
                //since the whole national dex isn't in SWSH it needs to check if the pokemon is in the SWSH pokedex
                //if the selected pokemon's generation is lower than 8
                if (generation < 8) {
                    for (String j : SWSHPokedex)
                        if (j.compareTo(selectedPokemon.getName()) == 0)
                            makeBranch(i, treeGamesGen8);
                } else
                    makeBranch(i, treeGamesGen8);
            }
        }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    //creates game list for legendary pokemon
    public void InitializeRestrictedGameList(int generation){
        //initializes needed tree elements
        gameRoot = new TreeItem<>();
        treeGamesGen1 = new TreeItem<>();
        treeGamesGen2 = new TreeItem<>();
        treeGamesGen3 = new TreeItem<>();
        treeGamesGen4 = new TreeItem<>();
        treeGamesGen5 = new TreeItem<>();
        treeGamesGen6 = new TreeItem<>();
        treeGamesGen7 = new TreeItem<>();
        treeGamesGen8 = new TreeItem<>();

        //creates object to allow access to legendaryIsAvaliable method
        Game testGame;

        //goes through games array and checks if the legendary is avaliable in any given game
        //if they aren't the game isn't displayed
        if(generation == 0)
            return;
        if(generation <= 1) {
            treeGamesGen1 = makeBranch("Generation 1", gameRoot);
            for (String i : Games[0]) {
                testGame = new Game(i, 1);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen1);
            }
        }
        if(generation <= 2) {
            treeGamesGen2 = makeBranch("Generation 2", gameRoot);
            for (String i : Games[1]) {
                testGame = new Game(i, 2);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen2);
            }
        }
        if(generation <= 3) {
            treeGamesGen3 = makeBranch("Generation 3", gameRoot);
            for (String i : Games[2]) {
                testGame = new Game(i, 3);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen3);
            }
        }
        if(generation <= 4) {
            treeGamesGen4 = makeBranch("Generation 4", gameRoot);
            for (String i : Games[3]) {
                testGame = new Game(i, 4);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen4);
            }
        }
        if(generation <= 5) {
            treeGamesGen5 = makeBranch("Generation 5", gameRoot);
            for (String i : Games[4]) {
                testGame = new Game(i, 5);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen5);
            }
        }
        if(generation <= 6) {
            treeGamesGen6 = makeBranch("Generation 6", gameRoot);
            for (String i : Games[5]) {
                testGame = new Game(i, 6);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen6);
            }
        }
        if(generation <= 7) {
            treeGamesGen7 = makeBranch("Generation 7", gameRoot);
            for (String i : Games[6]) {
                testGame = new Game(i, 7);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen7);
            }
        }
        if(generation <= 8) {
            treeGamesGen8 = makeBranch("Generation 8", gameRoot);
            for (String i : Games[7]) {
                testGame = new Game(i, 8);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen8);
            }
        }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    //creates method list
    public void InitializeMethodList(){
        selectedMethod = new Method();
        methodRoot = new TreeItem<>();
        createFamily(selectedPokemon.getName());
        selectedGame.generateMethods(selectedPokemon);
        evolution2 = makeBranch(selectedPokemon.getName(), methodRoot);
        for(String i: selectedGame.getMethods())
            if (i != null) {
                makeBranch(i, evolution2);
            }
            evolution2.setExpanded(true);

        if(evolutionStage == 2 && findGenerationPokemon(Stage1.getName()) <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage1);
            evolution1 = makeBranch(Stage1.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution1);

        }if(evolutionStage >= 1 && findGenerationPokemon(Stage0.getName()) <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage0);
            evolution0 = makeBranch(Stage0.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution0);
        }

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    //clears method list
    public void clearMethodList(){
        selectedMethod = new Method();
        methodRoot = new TreeItem<>();
        Stage0 = new Pokemon();
        Stage1 = new Pokemon();
        evolutionStage = 0;
        shinyCharmCheckBox.setDisable(true);
        lureCheckBox.setDisable(true);
        beginHuntButton.setDisable(true);
        helpButton.setVisible(false);

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    //creates Stage0 and Stage1 objects based on selected pokemon's prior evolutions
    public void createFamily(String name){
        String [][] pokemonEvolutions = {{"Bulbasaur", "Ivysaur", "Venusaur"}, {"Charmander", "Charmeleon", "Charizard"}, {"Squirtle", "Wartortle", "Blastoise"}, {"Caterpie", "Metapod", "Butterfree"}, {"Weedle", "Kakuna", "Beedrill"}, {"Pidgey", "Pidgeotto", "Pidgeot"}, {"Rattata", "Raticate", ""}, {"Spearow", "Fearow", ""}, {"Ekans", "Arbok", ""}, {"Pichu", "Pikachu", "Raichu"}, {"Sandshrew", "Sandslash", ""}, {"Nidoran♀", "Nidorina", "Nidoqueen"}, {"Nidoran♂", "Nidorino", "Nidoking"}, {"Cleffa", "Clefairy", "Clefable"}, {"Vulpix", "Ninetales", ""}, {"Igglybuff", "Jigglypuff", "Wigglytuff"}, {"Zubat", "Golbat", "Crobat"}, {"Oddish", "Gloom", "Vileplume"}, {"Paras", "Parasect", ""}, {"Venonat", "Venomoth", ""}, {"Diglett", "Dugtrio", ""}, {"Meowth", "Persian", ""}, {"Psyduck", "Golduck", ""}, {"Mankey", "Primeape", ""}, {"Growlithe", "Arcanine", ""}, {"Poliwag", "Poliwhirl", "Poliwrath"}, {"Abra", "Kadabra", "Alakazam"}, {"Machop", "Machoke", "Machamp"}, {"Bellsprout", "Weepinbell", "Victreebel"}, {"Tentacool", "Tentacruel", ""}, {"Geodude", "Graveler", "Golem"}, {"Ponyta", "Rapidash", ""}, {"Slowpoke", "Slowbro", ""}, {"Magnemite", "Magneton", "Magnezone"}, {"Doduo", "Dodrio", ""}, {"Seel", "Dewgong", ""}, {"Grimer", "Muk", ""}, {"Shellder", "Cloyster", ""}, {"Gastly", "Haunter", "Gengar"}, {"Onix", "Steelix", ""}, {"Drowzee", "Hypno", ""}, {"Krabby", "Kingler", ""}, {"Voltorb", "Electrode", ""}, {"Exeggcute", "Exeggutor", ""}, {"Cubone", "Marowak", ""}, {"Tyrogue", "Hitmonlee", ""}, {"Tyrogue", "Hitmonchan", ""}, {"Lickitung", "Lickilicky", ""}, {"Koffing", "Weezing", ""}, {"Rhyhorn", "Rhydon", "Rhyperior"}, {"Happiny", "Chansey", "Blissey"}, {"Tangela", "Tangrowth", ""}, {"Horsea", "Seadra", "Kingdra"}, {"Goldeen", "Seaking", ""}, {"Staryu", "Starmie", ""}, {"Mime Jr.", "Mr. Mime", ""}, {"Scyther", "Scizor", ""}, {"Smoochum", "Jynx", ""}, {"Elekid", "Electabuzz", "Electivire"}, {"Magby", "Magmar", "Magmortar"}, {"Magikarp", "Gyarados", ""}, {"Eevee", "Vaporeon", ""}, {"Eevee", "Jolteon", ""}, {"Eevee", "Flareon", ""}, {"Porygon", "Porygon2", "Porygon-Z"}, {"Omanyte", "Omastar", ""}, {"Kabuto", "Kabutops", ""}, {"Munchlax", "Snorlax", ""}, {"Dratini", "Dragonair", "Dragonite"},
                {"Chikorita", "Bayleef", "Meganium"}, {"Cyndaquil", "Quilava", "Typhlosion"}, {"Totodile", "Croconaw", "Feraligatr"}, {"Sentret", "Furret", ""}, {"Hoothoot", "Noctowl", ""}, {"Ledyba", "Ledian", ""}, {"Spinarak", "Ariados", ""}, {"Chinchou", "Lanturn", ""}, {"Togepi", "Togetic", "Togekiss"}, {"Natu", "Xatu", ""}, {"Mareep", "Flaaffy", "Ampharos"}, {"Oddish", "Gloom", "Bellossom"}, {"Azurill", "Marill", "Azumarill"}, {"Bonsly", "Sudowoodo", ""}, {"Poliwag", "Poliwhirl", "Politoed"}, {"Hoppip", "Skiploom", "Jumpluff"}, {"Aipom", "Amibipom", ""}, {"Sunkern", "Sunflora", ""}, {"Yanma", "Yanmega", ""}, {"Wooper", "Quagsire", ""}, {"Eevee", "Espeon", ""}, {"Eevee", "Umbreon", ""}, {"Murkrow", "Honchkrow", ""}, {"Slowpoke", "Slowking", ""}, {"Misdreavus", "Mismagius", ""}, {"Wynaut", "Wobbuffet", ""}, {"Pineco", "Forretress", ""}, {"Gligar", "Gliscor", ""}, {"Snubbull", "Granbull", ""}, {"Sneasel", "Weavile", ""}, {"Teddiursa", "Ursaring", ""}, {"Slugma", "Magcargo", ""}, {"Swinub", "Piloswine", "Mamoswine"}, {"Remoraid", "Octillery", ""}, {"Mantyke", "Mantine", ""}, {"Houndour", "Houndoom", ""}, {"Phanpy", "Donphan", ""}, {"Tyrogue", "Hitmontop", ""}, {"Larvitar", "Pupitar", "Tyranitar"},
                {"Treecko", "Grovyle", "Sceptile"}, {"Torchic", "Combusken", "Blaziken"}, {"Mudkip", "Marshtomp", "Swampert"}, {"Poochyena", "Mightyena", ""}, {"Zigzagoon", "Linoone", ""}, {"Wurmple", "Silcoon", "Beautifly"}, {"Wurmple", "Cascoon", "Dustox"}, {"Lotad", "Lombre", "Ludicolo"}, {"Seedot", "Nuzleaf", "Shiftry"}, {"Taillow", "Swellow", ""}, {"Wingull", "Pelipper", ""}, {"Ralts", "Kirlia", "Gardevoir"}, {"Surskit", "Masquerain", ""}, {"Shroomish", "Breloom", ""}, {"Slakoth", "Vigoroth", "Slaking"}, {"Nincada", "Ninjask", "Shedinja"}, {"Whismur", "Loudred", "Exploud"}, {"Makuhita", "Hariyama", ""}, {"Nosepass", "Probopass", ""}, {"Skitty", "Delcatty", ""}, {"Aron", "Lairon", "Aggron"}, {"Meditite", "Medicham", ""}, {"Electrike", "Manectric", ""}, {"Budew", "Roselia", "Roserade"}, {"Gulpin", "Swalot", ""}, {"Carvanha", "Sharpedo", ""}, {"Wailmer", "Wailord", ""}, {"Numel", "Camerupt", ""}, {"Spoink", "Grumpig", ""}, {"Trapinch", "Vibrava", "Flygon"}, {"Cacnea", "Cacturne", ""}, {"Swablu", "Altaria", ""}, {"Barboach", "Whiscash", ""}, {"Corphish", "Crawdaunt", ""}, {"Baltoy", "Claydol", ""}, {"Lileep", "Cradily", ""}, {"Anorith", "Armaldo", ""}, {"Feebas", "Milotic", ""}, {"Shuppet", "Banette", ""}, {"Duskull", "Dusclops", "Dusknoir"}, {"Chingling", "Chimecho", ""}, {"Snorunt", "Glalie", ""}, {"Spheal", "Sealeo", "Walrein"}, {"Clamperl", "Huntail", ""}, {"Clamperl", "Gorebyss", ""}, {"Bagon", "Shelgon", "Salamence"}, {"Beldum", "Metang", "Metagross"},
                {"Turtwig", "Grotle", "Torterra"}, {"Chimchar", "Monferno", "Infernape"}, {"Piplup", "Prinplup", "Empoleon"}, {"Starly", "Staravia", "Staraptor"}, {"Bidoof", "Bibarel", ""}, {"Kricketot", "Kricketune", ""}, {"Shinx", "Luxio", "Luxray"}, {"Cranidos", "Rampardos", ""}, {"Shieldon", "Bastiodon", ""}, {"Burmy", "Wormadam", ""}, {"Burmy", "Mothim", ""}, {"Combee", "Vespiquen", ""}, {"Buizel", "Floatzel", ""}, {"Cherubi", "Cherrim", ""}, {"Shellos", "Gastrodon", ""}, {"Drifloon", "Drifblim", ""}, {"Buneary", "Lopunny", ""}, {"Glameow", "Purugly", ""}, {"Stunky", "Skuntank", ""}, {"Bronzor", "Bronzong", ""}, {"Gible", "Gabite", "Garchomp"}, {"Riolu", "Lucario", ""}, {"Hippopotas", "Hippowdon", ""}, {"Skorupi", "Drapion", ""}, {"Croagunk", "Toxicroak", ""}, {"Finneon", "Lumineon", ""}, {"Snover", "Abomasnow", ""}, {"Eevee", "Leafeon", ""}, {"Eevee", "Glaceon", ""}, {"Ralts", "Kirlia", "Gallade"}, {"Snorunt", "Froslass", ""},
                {"Snivy", "Servine", "Serperior"}, {"Tepig", "Pignite", "Emboar"}, {"Oshawott", "Dewott", "Samurott"}, {"Patrat", "Watchog", ""}, {"Lillipup", "Herdier", "Stoutland"}, {"Purrloin", "Liepard", ""}, {"Pansage", "Simisage", ""}, {"Pansear", "Simisear", ""}, {"Panpour", "Simipour", ""}, {"Munna", "Musharna", ""}, {"Pidove", "Tranquill", "Unfezant"}, {"Blitzle", "Zebstrika", ""}, {"Roggenrola", "Boldore", "Gigalith"}, {"Woobat", "Swoobat", ""}, {"Drilbur", "Excadrill", ""}, {"Timburr", "Gurdurr", "Conkeldurr"}, {"Tympole", "Palpitoad", "Seismitoad"}, {"Sewaddle", "Swadloon", "Leavanny"}, {"Venipede", "Whirlipede", "Scolipede"}, {"Cottonee", "Whimsicott", ""}, {"Petilil", "Lilligant", ""}, {"Sandile", "Krokorok", "Krookodile"}, {"Darumaka", "Darmanitan", ""}, {"Dwebble", "Crustle", ""}, {"Scraggy", "Scrafty", ""}, {"Yamask", "Cofagrigus", ""}, {"Tirtouga", "Carracosta", ""}, {"Archen", "Archeops", ""}, {"Trubbish", "Garbodor", ""}, {"Zorua", "Zoroark", ""}, {"Minccino", "Cinccino", ""}, {"Gothita", "Gothorita", "Gothitelle"}, {"Solosis", "Duosion", "Reuniclus"}, {"Ducklett", "Swanna", ""}, {"Vanillite", "Vanillish", "Vanilluxe"}, {"Deerling", "Sawsbuck", ""}, {"Karrablast", "Escavalier", ""}, {"Foongus", "Amoonguss", ""}, {"Frillish", "Jellicent", ""}, {"Joltik", "Galvantula", ""}, {"Ferroseed", "Ferrothorn", ""}, {"Klink", "Klang", "Klinklang"}, {"Tynamo", "Eelektrik", "Eelektross"}, {"Elgyem", "Beheeyem", ""}, {"Litwick", "Lampent", "Chandelure"}, {"Axew", "Fraxure", "Haxorus"}, {"Cubchoo", "Beartic", ""}, {"Shelmet", "Accelgor", ""}, {"Mienfoo", "Mienshao", ""}, {"Golett", "Golurk", ""}, {"Pawniard", "Bisharp", ""}, {"Rufflet", "Braviary", ""}, {"Vullaby", "Mandibuzz", ""}, {"Deino", "Zweilous", "Hydreigon"}, {"Larvesta", "Volcarona", ""},
                {"Chespin", "Quilladin", "Chesnaught"}, {"Fennekin", "Braixen", "Delphox"}, {"Froakie", "Frogadier", "Greninja"}, {"Bunnelby", "Diggersby", ""}, {"Fletchling", "Fletchinder", "Talonflame"}, {"Scatterbug", "Spewpa", "Vivillon"}, {"Litleo", "Pyroar", ""}, {"Flabébé", "Floette", "Florges"}, {"Skiddo", "Gogoat", ""}, {"Pancham", "Pangoro", ""}, {"Espurr", "Meowstic", ""}, {"Honedge", "Doublade", "Aegislash"}, {"Spritzee", "Aromatisse", ""}, {"Swirlix", "Slurpuff", ""}, {"Inkay", "Malamar", ""}, {"Binacle", "Barbaracle", ""}, {"Skrelp", "Dragalge", ""}, {"Clauncher", "Clawitzer", ""}, {"Helioptile", "Heliolisk", ""}, {"Tyrunt", "Tyrantrum", ""}, {"Amaura", "Aurorus", ""}, {"Eevee", "Sylveon", ""}, {"Goomy", "Sliggoo", "Goodra"}, {"Phantump", "Trevenant", ""}, {"Pumpkaboo", "Gourgeist", ""}, {"Bergmite", "Avalugg", ""}, {"Noibat", "Noivern", ""},
                {"Rowlet", "Dartrix", "Decidueye"}, {"Litten", "Torracat", "Incineroar"}, {"Popplio", "Brionne", "Primarina"}, {"Pikipek", "Trumbeak", "Toucannon"}, {"Yungoos", "Gumshoos", ""}, {"Grubbin", "Charjabug", "Vikavolt"}, {"Crabrawler", "Crabominable", ""}, {"Cutiefly", "Ribombee", ""}, {"Rockruff", "Lycanroc", ""}, {"Mareanie", "Toxapex", ""}, {"Mudbray", "Mudsdale", ""}, {"Dewpider", "Araquanid", ""}, {"Fomantis", "Lurantis", ""}, {"Morelull", "Shiinotic", ""}, {"Salandit", "Salazzle", ""}, {"Stufful", "Bewear", ""}, {"Bounsweet", "Steenee", "Tsareena"}, {"Wimpod", "Golisopod", ""}, {"Sandygast", "Palossand", ""}, {"Type: Null", "Silvally", ""}, {"Jangmo-o", "Hakamo-o", "Kommo-o"}, {"Cosmog", "Cosmoem", "Solgaleo"}, {"Cosmog", "Cosmoem", "Lunala"}, {"Poipole", "Naganadel", ""},
                {"Grookey", "Thwackey", "Rillaboom"}, {"Scorbunny", "Raboot", "Cinderace"}, {"Sobble", "Drizzile", "Inteleon"}, {"Skwovet", "Greedent", ""}, {"Rookidee", "Corvisquire", "Corviknight"}, {"Blipbug", "Dottler", "Orbeetle"}, {"Nickit", "Thievul", ""}, {"Gossifleur", "Eldegoss", ""}, {"Wooloo", "Dubwool", ""}, {"Chewtle", "Drednaw", ""}, {"Yamper", "Boltund", ""}, {"Rolycoly", "Carkol", "Coalossal"}, {"Applin", "Flapple", ""}, {"Applin", "Appletun", ""}, {"Silicobra", "Sandaconda", ""}, {"Arrokuda", "Barraskewda", ""}, {"Toxel", "Toxtricity", ""}, {"Sizzlipede", "Centiskorch", ""}, {"Clobbopus", "Grapploct", ""}, {"Sinistea", "Polteageist", ""}, {"Hatenna", "Hattrem", "Hatterene"}, {"Impidimp", "Morgrem", "Grimmsnarl"}, {"Galarian Zigzagoon", "Galarian Linoone", "Obstagoon"}, {"Galarian Meowth", "Perrserker", ""}, {"Galarian Corsola", "Cursola", ""}, {"Galarian Farfetch'd", "Sirfetch'd", ""}, {"Mime Jr.", "Galarian Mr. Mime", "Mr. Rime"}, {"Galarian Yamask", "Runerigus", ""}, {"Milcery", "Alcremie", ""}, {"Snom", "Frosmoth", ""}, {"Cufant", "Copperajah", ""}, {"Dreepy", "Drakloak", "Dragapult"}, {"Kubfu", "Urshifu", ""},
                {"Exeggcute", "Alolan Exeggutor", ""}, {"Alolan Geodude", "Alolan Graveler", "Alolan Golem"}, {"Alolan Grimer", "Alolan Muk", ""}, {"Cubone", "Alolan Marowak", ""}, {"Alolan Vulpix", "Alolan Ninetales", ""}, {"Alolan Meowth", "Alolan Persian", ""}, {"Pichu", "Pikachu", "Alolan Raichu"},{"Alolan Rattata", "Alolan Raticate", ""}, {"Alolan Sandshrew", "Alolan Sandslash", ""},
                {"Galarian Darumaka","Galarian Darmanitan", ""}, {"Galarian Ponyta", "Galarian Rapidash", ""}, {"Koffing", "Galarian Weezing", ""}, {"Galarian Slowpoke", "Galarian Slowbro", ""}};

        for (String[] pokemonEvolution : pokemonEvolutions) {
            for (int j = 0; j < pokemonEvolutions[0].length; j++) {
                if (pokemonEvolution[j].compareTo(name) == 0) {
                    evolutionStage = j;
                    if (j >= 1) {
                        Stage0 = new Pokemon(pokemonEvolution[0], findGenerationPokemon(pokemonEvolution[0]));
                    }
                    if (j == 2) {
                        Stage1 = new Pokemon(pokemonEvolution[1], findGenerationPokemon(pokemonEvolution[1]));
                    }
                    return;
                }
            }
        }
    }

    //returns the generation of the given pokemon
    public int findGenerationPokemon(String name){
        for(String i: Pokedex[0])
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: Pokedex[1])
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: Pokedex[2])
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: Pokedex[3])
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: Pokedex[4])
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: Pokedex[5])
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: Pokedex[6])
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: Pokedex[7])
            if(i.compareTo(name) == 0)
                return 8;
        return 0;
    }

    //returns the generation of the given game
    public int findGenerationGame(String name){
        for(String i: Games[0])
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: Games[1])
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: Games[2])
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: Games[3])
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: Games[4])
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: Games[5])
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: Games[6])
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: Games[7])
            if(i.compareTo(name) == 0)
                return 8;
        return 0;
    }

    //expands the given generation tree view
    public void collapseGeneration(int generation){
        switch(generation){
            case 1:
                treeGamesGen1.setExpanded(true);
                break;
            case 2:
                treeGamesGen2.setExpanded(true);
                break;
            case 3:
                treeGamesGen3.setExpanded(true);
                break;
            case 4:
                treeGamesGen4.setExpanded(true);
                break;
            case 5:
                treeGamesGen5.setExpanded(true);
                break;
            case 6:
                treeGamesGen6.setExpanded(true);
                break;
            case 7:
                treeGamesGen7.setExpanded(true);
                break;
            case 8:
                treeGamesGen8.setExpanded(true);
                break;
            default:
                break;
        }
    }

    //appends "Alolan " to the front of the selected pokemon's name
    public void setAlolan(){
        galarianCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 9 && selectedPokemon.getName().substring(0, 9).compareTo("Galarian ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(9));
        if(alolanCheckBox.isSelected())
            selectedPokemon.setName("Alolan " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(7));
        InitializeGameList(selectedPokemon.getGeneration());
    }

    //appends "Galarian " to the front of the selected pokemon's name
    public void setGalarian(){
        alolanCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 7 && selectedPokemon.getName().substring(0, 7).compareTo("Alolan ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(7));
        if(galarianCheckBox.isSelected())
            selectedPokemon.setName("Galarian " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(9));
        InitializeGameList(selectedPokemon.getGeneration());
    }

    //adds 2 to the selected method modifier
    public void setShinyCharm(){
        if(shinyCharmCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 2);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 2);
    }

    //adds 1 to the selected method modifier
    public void setLure(){
        if(lureCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 1);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 1);
    }

    //creates a tool tip with breif description of the selected hunting method
    public void setToolTip(String selectedMethod){
        switch (selectedMethod){
            case "None":
                methodToolTip.setText("Base odds are being used");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Breeding with Shiny":
                methodToolTip.setText("Breeding a normal pokemon with a shiny pokemon increases your shiny odds dramatically");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Masuda":
                methodToolTip.setText("Breeding pokemon from 2 different languages (ie. English and French) increases your shiny odds");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Radar Chaining":
                methodToolTip.setText("Using the PokeRadar, encounter and defeat a " + selectedPokemon.getName() + "\nOnce the chain has started run into grass that is violently shaking\nIf no violently shaking grass appears, walk 50 steps to reset your radar\nOnce a shiny pokemon appears, sparkles will accompany the shaking grass\nThe shiny odds cap at a chain of 40\n\nThe chain is broken when a pokemon other than " + selectedPokemon.getName() + " is encountered or you leave the grass area that you where hunting in\nIf the chain is broken, the music will change back to default music of the route");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Chain Fishing":
                methodToolTip.setText("For every fish that is encountered, your shiny odds increase\n\nCaps at a chain of 20\nChain is broken when no fish is encountered (\"Nothing seems to be biting...\", \"You Reeled it in too fast!\", or \"You Reeled it in too slow!\")");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Friend Safari":
                methodToolTip.setText("Shiny odds are better in the Friend Safari");
                helpButton.setTooltip(methodToolTip);
                break;
            case "DexNav":
                methodToolTip.setText("Shiny odds increase as Search Level increases\nLarge shiny odd boosts are given at a chain of 50 and 100\nChain is broken when different Pokemon is encountered or the pokemon runs away");
                helpButton.setTooltip(methodToolTip);
                break;
            case "SOS Chaining":
                methodToolTip.setText("Every time a pokemon calls for help, the shiny odds increase\nAdrenaline orbs and the Intimidate ability increase the chance of a pokemon calling for help\n\nShiny odds cap at a chain of 30\nChain is broken when all pokemon are defeated");
                if(selectedGame.getName().compareTo("Sun") == 0 || selectedGame.getName().compareTo("Moon") == 0)
                    methodToolTip.setText(methodToolTip.getText() + "\nShiny odds reset at a chain of 255");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Catch Combo":
                methodToolTip.setText("When you catch the same pokemon back to back the shiny odds increase\n\nShiny odds cap at a chain of 31\nChain is broken when the pokemon flees or you catch a different pokemon");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Ultra Wormholes":
                methodToolTip.setText("Shiny odds increase based on distance, and number of rings surrounding the wormhole");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Total Encounters":
                methodToolTip.setText("The more of the pokemon that you have caught/defeated in total your shiny odds increase\n\nShiny odds cap at 500 total encounters");
                helpButton.setTooltip(methodToolTip);
                break;
            default:
                break;
        }
    }

    //method to create branch on given tree item
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }

    //closes selection window and opens huntControls window
    public void beginHunt(ActionEvent event) {
        Stage selectionWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        selectionWindow.close();

        controller.addHuntWindow(selectedPokemon, selectedGame, selectedMethod, Stage0.getName(), Stage1.getName(),currentLayout,0, 0, 1);
    }

    public void pokemonQuickSelect(KeyEvent e){
        int PokedexSize = 0;
        for(String[] i : Pokedex)
            PokedexSize += i.length;

        if(e.getCharacter().compareTo("\b") == 0) {
            index = 0;
            try {
                pokemonSearch.setValue(pokemonSearch.getValue().substring(0, pokemonSearch.getValue().length() - 1));
            }catch (StringIndexOutOfBoundsException f){
                selectedPokemon.setName("");
            }
        }
        else {
            if(pokemonSearch.getValue() == null)
                pokemonSearch.setValue(e.getCharacter());
            else
                pokemonSearch.setValue(pokemonSearch.getValue() + e.getCharacter());

            String searchString = pokemonSearch.getValue();

            while (index < PokedexSize) {
                if (index < 151) {
                    String selectedPokemon = Pokedex[0][index];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen1.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen1.getChildren().get(index));
                        collapsePokemonTrees(Gen1);
                        return;
                    }
                } else if (index < 251) {
                    String selectedPokemon = Pokedex[1][index - 151];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen2.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen2.getChildren().get(index - 151));
                        collapsePokemonTrees(Gen2);
                        return;
                    }
                } else if (index < 386) {
                    String selectedPokemon = Pokedex[2][index - 251];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen3.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen3.getChildren().get(index - 251));
                        collapsePokemonTrees(Gen3);
                        return;
                    }
                } else if (index < 493) {
                    String selectedPokemon = Pokedex[3][index - 386];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen4.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen4.getChildren().get(index - 386));
                        collapsePokemonTrees(Gen4);
                        return;
                    }
                } else if (index < 649) {
                    String selectedPokemon = Pokedex[4][index - 493];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen5.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen5.getChildren().get(index - 493));
                        collapsePokemonTrees(Gen5);
                        return;
                    }
                } else if (index < 721) {
                    String selectedPokemon = Pokedex[5][index - 649];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen6.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen6.getChildren().get(index - 649));
                        collapsePokemonTrees(Gen6);
                        return;
                    }
                } else if (index < 809) {
                    String selectedPokemon = Pokedex[6][index - 721];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen7.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen7.getChildren().get(index - 721));
                        collapsePokemonTrees(Gen7);
                        return;
                    }
                } else {
                    String selectedPokemon = Pokedex[7][index - 809];
                    if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                        Gen8.setExpanded(true);
                        PokemonList.getSelectionModel().select(Gen8.getChildren().get(index - 809));
                        collapsePokemonTrees(Gen8);
                        return;
                    }
                }
                index++;
            }
            selectedPokemon.setName("");
            collapsePokemonTrees(new TreeItem<>());
            index = 0;
        }
    }

    //method to collapse all but the given treeitem
    public void collapsePokemonTrees(TreeItem<String> currentGeneration){
        for(int i = 0; i < pokemonRoot.getChildren().size(); i++){
            if(!PokemonList.getTreeItem(i).equals(currentGeneration))
                PokemonList.getTreeItem(i).setExpanded(false);
        }
    }

    public Pokemon getStage0(){
        return Stage0;
    }

    public Pokemon getStage1(){
        return Stage1;
    }

    public void setController(huntController controller){
        this.controller = controller;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}
