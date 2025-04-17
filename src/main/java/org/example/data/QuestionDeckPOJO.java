package org.example.data;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "decks_questions")
public class QuestionDeckPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_question_id")
    private int id;

    @ElementCollection
    @Column(name = "current_cards")
    private List<Integer> currentCards;

    @ElementCollection
    @Column(name = "discarded_cards")
    private List<Integer> discardCards;

    public void setCurrentCards(List<Integer> currentCards) {
        this.currentCards = currentCards;
    }

    public List<Integer> getDiscardCards() {
        return discardCards;
    }

    public void setDiscardCards(List<Integer> discardCards) {
        this.discardCards = discardCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getCurrentCards() {
        return currentCards;
    }

}
