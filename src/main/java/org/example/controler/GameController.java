package org.example.controler;

import org.example.config.ConfigurationManager;
import org.example.data.Convertor;
import org.example.data.QuestionConvertor;
import org.example.data.QuestionDTO;
import org.example.model.business.*;
import org.example.model.technical.ClassicalBoardGenerator;
import org.example.stockage.*;
import org.example.view.View;
import org.example.data.BoardConvertor;
import org.example.data.BoardPOJO;
import org.example.stockage.jpa.BoardJPADAO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * General game controller
 */
public class GameController {

    /**
     * Link with the view
     */
    private final View view;

    /**
     * Game session
     */
    private GameSession session;

    /**
     * The constructor
     * @param playerNames the name of the players
     */
    public GameController(View view, List<String> playerNames) {
        this(view, playerNames, new Dice());
    }

    /**
     * The constructor
     * @param playerNames the name of the players
     * @param dice the dice
     */
    public GameController(View view, List<String> playerNames, Dice dice) {
        try {
            initDatabase();
            List<Question> questions = loadQuestionList();
            ClassicalBoardGenerator boardGenerator = new ClassicalBoardGenerator();
            this.session = new GameSession(-1,
                    createPlayers(playerNames),
                    boardGenerator.generateBoard(),
                    dice,
                    new Deck<>(questions));
            this.view = view;
        } catch (DAOException | DBAccessException | UnknownDAOException e) {
            throw new GameError(e);
        }
    }

    /**
     * The round for one player
     * @param p a player
     */
    public void playerRound(Pawn p) {
        Objects.requireNonNull(p);
        sendMessage("Player " + p.getName() + " on case " + p.getPosition() + " roll the dice.");
        int number = p.rollDice(this.session.dice());
        sendMessage(p.getName() + " go on " + number + " cases.");

        if (p.getPosition() + number > this.session.board().getSize()) {
            sendMessage(p.getName() + " get out of the board.");
            p.setPosition(this.session.board().getSize() - number);
            return;
        }

        p.setPosition(p.getPosition() + number);

        if (p.getPosition() + number < this.session.board().getSize()) {
            Case c = this.session.board().getCase(p.getPosition());
            sendMessage(p.getName() + " is on a new case.");
            Optional<String> res = c.effect(p);
            if (res.isPresent()) {
                applyQuestionEffect(p);
            }
        }
    }

    /**
     * Apply the question effect to a player
     * @param p the player
     */
    public void applyQuestionEffect(Pawn p) {
        Objects.requireNonNull(p);
        Question q = session.questionDeck().drawCard();
        Objects.requireNonNull(q);
        String answer = view.playerAnswerToQuestion(p.getName(), q.getAskedQuestion());
        if (!q.checkAnswer(answer)) {
            p.setPosition(p.getPosition() - 3);
            sendMessage(p.getName() + " give a wrong answer.");
        } else {
            sendMessage(p.getName() + " give a valid answer.");
        }
    }

    /**
     * Main game loop
     */
    public void runGame() {
        Pawn winner = null;
        while (winner == null) {
            for (Pawn p : session.players()) {
                playerRound(p);
                if (p.getPosition() == session.board().getSize()) {
                    winner = p;
                    break;
                }
            }
        }
        sendMessage(winner.getName() + " win the game.");
    }

    /**
     * Send message to the view
     * @param message the message
     */
    public void sendMessage(String message) {
        view.display(message);
    }

    /**
     * Get the list of questions
     * @return the deck
     * @throws DAOException if issues with DAO
     * @throws UnknownDAOException if DAO is unknown
     */
    public List<Question> loadQuestionList() throws DAOException, UnknownDAOException {
        DAO<QuestionDTO> dao = DAOFactory.getQuestionDAO();
        List<QuestionDTO> questionsDto = dao.getAll();
        Convertor<Question, QuestionDTO> convert = new QuestionConvertor();
        return questionsDto.stream().map(convert::fromDTO).toList();
    }

    /**
     * Accessor to the session
     */
    public GameSession getGameSession() {
        return session;
    }

    /**
     * Create human player from a list of names
     * @param playerNames the list of names
     * @return the list of players
     */
    public List<Pawn> createPlayers(List<String> playerNames) {
        Objects.requireNonNull(playerNames);
        return playerNames.stream()
                .map(playerName -> new Pawn(playerName, Color.BLUE)).toList();
    }

    /**
     * Initialize the database and initialize
     * @throws DBAccessException if access fails
     */
    public void initDatabase() throws DBAccessException {
        if (ConfigurationManager.getInstance().isInitDatabase()) {
            SQLScriptDB.runScriptOnDatabase("/question_db.sql");
            SQLScriptDB.runScriptOnDatabase("/game_session_db.sql");
        }
        if (ConfigurationManager.getInstance().isPopulateDatabase()) {
            SQLScriptDB.runScriptOnDatabase("/questions_insert_db.sql");
        }
    }

    /**
     * Sauvegarde le plateau dans la base de données
     */
    public void saveBoard(Board board) throws DAOException {
        BoardConvertor boardConvertor = new BoardConvertor();
        BoardPOJO boardPOJO = boardConvertor.toDTO(board);

        DAO<BoardPOJO> boardDAO = new BoardJPADAO();
        int boardId = boardDAO.create(boardPOJO);
        System.out.println("Plateau sauvegardé avec ID : " + boardId);
    }

    /**
     * Charger un plateau depuis la base de données
     * @param boardId l'ID du plateau à charger
     */
    public void loadBoard(int boardId) throws DAOException, UnknownDAOException {
        DAO<BoardPOJO> boardDAO = new BoardJPADAO();
        Optional<BoardPOJO> boardPOJOOpt = boardDAO.get(boardId);

        if (boardPOJOOpt.isPresent()) {
            BoardPOJO boardPOJO = boardPOJOOpt.get();
            BoardConvertor boardConvertor = new BoardConvertor();
            Board board = boardConvertor.fromDTO(boardPOJO);

            // Mettre à jour la session du jeu avec le plateau récupéré
            this.session = new GameSession(-1,
                    createPlayers(List.of("Player1", "Player2")), // Exemple de noms de joueurs
                    board,
                    new Dice(),
                    new Deck<>(loadQuestionList()));
            System.out.println("Plateau chargé avec succès.");
        } else {
            System.out.println("Le plateau demandé n'existe pas.");
        }
    }
}
