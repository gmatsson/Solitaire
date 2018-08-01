package com.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//by Gustaf Matsson
//2018-07-22
public class PlayingCardDeck {
    private List<PlayingCard> cardDeck;

    public PlayingCardDeck() {
        this.cardDeck = new ArrayList<PlayingCard>();
    }

    public List<PlayingCard> getCardDeck() {
        return cardDeck;
    }

    public void instantiateCards() {
        for(Suit s : Suit.values()) {
            for(Rank r : Rank.values()) {
                PlayingCard pc = new PlayingCard(s, r);
                cardDeck.add(pc);

            }
        }
    }
    public void shuffleDeck () {
        Collections.shuffle(this.cardDeck);
    }
    public void printList(){
        for (PlayingCard pc : cardDeck) {
            System.out.println(pc.getSuit() + ", " + pc.getRank());
        }
    }
}
