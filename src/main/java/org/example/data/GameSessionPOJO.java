package org.example.data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "game_sessions")
public class GameSessionPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private int id;

    @Column(name = "nb_faces", nullable = false)
    private int nbFaces;

    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id")
    private BoardPOJO board;

    @ManyToOne(optional = false)
    @JoinColumn(name = "deck_question_id")
    private QuestionDeckPOJO deck;

    @OneToMany
    @JoinTable(
            name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<InfoPlayerPOJO> players;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbFaces() {
        return nbFaces;
    }

    public void setNbFaces(int nbFaces) {
        this.nbFaces = nbFaces;
    }

    public BoardPOJO getBoard() {
        return board;
    }

    public void setBoard(BoardPOJO board) {
        this.board = board;
    }

    public QuestionDeckPOJO getDeck() {
        return deck;
    }

    public void setDeck(QuestionDeckPOJO deck) {
        this.deck = deck;
    }

    public List<InfoPlayerPOJO> getPlayers() {
        return players;
    }

    public void setPlayers(List<InfoPlayerPOJO> players) {
        this.players = players;
    }
}
