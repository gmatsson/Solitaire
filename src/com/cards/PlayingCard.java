package com.cards;

import static com.cards.Suit.*;

//by Gustaf Matsson
//2018-07-22
public class PlayingCard {
        private Suit suit;
        private Rank rank;
        private boolean faceUp; //True = Suit and rank is visible.

    public PlayingCard(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp() {
        this.faceUp = true;
    }

    public void setFaceDown() { this.faceUp = false; }

    public boolean isRed() {
        return(suit.equals(HEARTS) || suit.equals(DIAMONDS));
    }
    public String getFileName() {
        if (!isFaceUp())return "C:\\Academy\\Solitaire\\src\\cards\\back.png";
        return "C:\\Academy\\Solitaire\\src\\cards\\" + suit + rank + ".png";
    }

    @Override // For a card to match, both suit and rank need to be equal.
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof PlayingCard && ((PlayingCard) obj).suit == suit);
    }
}
