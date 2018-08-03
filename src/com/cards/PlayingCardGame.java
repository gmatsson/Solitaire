package com.cards;

import java.util.*;

//by Gustaf Matsson
//2018-07-30
public class PlayingCardGame {

    public static void main(String[] args) {
        new PlayingCardGame();
    }

    private PlayingCardDeck pcd;
    private Stack<PlayingCard> stock; // Kortleken
    private Stack<PlayingCard> waste; // Redan utdelade kort
    private Stack<PlayingCard>[] foundation; // Dom 4 högarna
    private Stack<PlayingCard>[] piles; // Dom 7 högarna som spelas på
    private PlayingCardGameDisplay display;
    public int score;

    public PlayingCardGame() {
        pcd = new PlayingCardDeck();
        pcd.instantiateCards();
        pcd.shuffleDeck();
        foundation = (Stack<PlayingCard>[]) new Stack[40];
        piles = new Stack[7];
        stock = new Stack<PlayingCard>();
        waste = new Stack<PlayingCard>();
        display = new PlayingCardGameDisplay(this);
        score = 0;

        for (int i = 0; i < foundation.length; i++) {
            foundation[i] = new Stack();
        }
        for (int i = 0; i < piles.length; i++) {
            piles[i] = new Stack();
        }
        stock = createStock(pcd);
        deal();
    }

    private Stack<PlayingCard> createStock(PlayingCardDeck pcd) {
        List<PlayingCard> cardDeck = this.pcd.getCardDeck();
        stock = new Stack<PlayingCard>();
        while(cardDeck.size() != 0) {
            stock.push(cardDeck.remove(0));
            System.out.println(stock.peek().getRank());
        }
        return stock;
    }

    public void deal() {
        for (int i = 0; i < piles.length; i++) {
            int count = 0;
            piles[i] = new Stack<PlayingCard>();
            while(count != i + 1) {
                PlayingCard temp = stock.pop();
                piles[i].push(temp);
                count++;
            }
            piles[i].peek().setFaceUp();
        }
    }

    public void dealThreeCards() {
        for (int i = 0; i < 3; i++) {
            if(!stock.isEmpty()) {
                PlayingCard temp = stock.pop();
                temp.setFaceUp();
                waste.push(temp);
            }
        }
    }

    public void resetStock() {
        while(!waste.isEmpty()) {
            PlayingCard temp = waste.pop();
            temp.setFaceDown();
            stock.push(temp);
        }
    }

    private boolean canAddToPile(PlayingCard card, int index) {
        Stack<PlayingCard> pile = piles[index];
        if(pile.isEmpty())
            return (card.getRank() == Rank.KING);

        PlayingCard top = pile.peek();
        if(!top.isFaceUp())
            return false;
        return(card.isRed() != top.isRed() && (card.getRank().ordinal() == top.getRank().ordinal() - 1));
    }

    private boolean canAddToFoundation(PlayingCard card, int index) {
        if(foundation[index].isEmpty())
            return (card.getRank() == Rank.ACE);
        PlayingCard temp = foundation[index].peek();
        return (temp.getRank().ordinal() + 1 == card.getRank().ordinal() && (temp.equals(card)));
    }

    private void addToPile(Stack<PlayingCard> cards, int index) {
        while(!cards.isEmpty()) {
            piles[index].push(cards.pop());
        }
    }

    private Stack<PlayingCard> removeFaceUpCards(int index) {
        Stack<PlayingCard> cards = new Stack<PlayingCard>();
        while(!piles[index].isEmpty() && piles[index].peek().isFaceUp()) {
            cards.push(piles[index].pop());
        }
        return cards;
    }
    public void stockClicked() {
        System.out.println("stock clicked");
        display.unselect();
        if (!display.isWasteSelected() && ! display.isPileSelected()) {
            if (stock.isEmpty()) resetStock();
            else dealThreeCards();
        }

    }

    public void wasteClicked() {
        System.out.println("waste clicked");
        if (! waste.isEmpty())
        {
            if (! display.isWasteSelected()) display.selectWaste();
            else display.unselect();
        }
    }

    public void foundationClicked(int index)
    {
        System.out.println("foundation #" + index + " clicked");
        if (display.isWasteSelected()) {
            if (canAddToFoundation(waste.peek(), index)) {
                PlayingCard temp = waste.pop();
                foundation[index].push(temp);
                display.unselect();
            }
        }
        if (display.isPileSelected()) {
            Stack<PlayingCard> selectedPile = piles[display.selectedPile()];
            if (canAddToFoundation(selectedPile.peek(), index)) {
                PlayingCard temp = selectedPile.pop();
                foundation[index].push(temp);
                score += 15;
                if (! selectedPile.isEmpty()) selectedPile.peek().setFaceUp();
                    display.unselect();
            }

        }
    }

    public void pileClicked(int index) {
        System.out.println("pile #" + index + " clicked");
        if (display.isWasteSelected()) {
            PlayingCard temp = waste.peek();

            if (canAddToPile(temp, index)) {
                piles[index].push(waste.pop());
                piles[index].peek().setFaceUp();
                score += 5;
            }
            display.unselect();
            display.selectPile(index);
        }
        else if (display.isPileSelected()) {
            int oldPile = display.selectedPile();
            if (index != oldPile) {
                Stack<PlayingCard> temp = removeFaceUpCards(oldPile);
                if (canAddToPile(temp.peek(), index)) {
                    addToPile(temp, index);
                    score -= 3;
                    if(!piles[oldPile].isEmpty()) {
                        piles[oldPile].peek().setFaceUp();
                        display.unselect();
                    }
                }
                else {
                    addToPile(temp, oldPile);
                    display.unselect();
                    display.selectPile(index);

                }
            }
            else display.unselect();
        }
        else {
            display.selectPile(index);
            piles[index].peek().setFaceUp();
            score += 5;
        }
    }
    public PlayingCard getStockCard() {
        if (stock.size() == 0) return null;
        return stock.peek();
    }

    public PlayingCard getWasteCard() {
        if (waste.size() == 0) return null;
        return waste.peek();
    }

    public PlayingCard getFoundationCard(int index) {
        if (foundation[index].isEmpty()) return null;
        return foundation[index].peek();
    }

    public Stack<PlayingCard> getPile(int index) {
        return piles[index];
    }

    public boolean winCheck() {
        for (int i = 0; i < 4 ; i++) {
            if(!(foundation[i].size() == 13))
                return false;
        }
        return true;
    }
}

