/*
 * Copyright (c) 2020 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package com.example.demo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class CardGame {

    private static final String PAT = "PAT";
    private static final String CARD_VALUES = "2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A.";

    public static void main(String args[]) {
        int n = 26; // the number of cards for player 1
        String cards1 = "AH 4H 5D 6D QC JS 8S 2D 7D JD JC 6C KS QS 9D 2C 5S 9S 6S 8H AD 4D 2H 2S 7S 8C";
        String cards2 = "10 4C 6H 3C KC JH 10 AS 5H KH 10 9H 9C 8D 5C AC 3H 4S KD 7C 3S QH 10 3D 7H QD";
        CardHolder player1cards = new CardHolder();
        CardHolder player2cards = new CardHolder();
        Scorer score = new Scorer();
        for (int i = 0; i < n; i++) {
            String cardp1 = cards1.substring(i * 3, i * 3 + 2);
            player1cards.addCard(cardp1);
        }

        int m = n; // the number of cards for player 2
        for (int i = 0; i < m; i++) {
            String cardp2 = cards2.substring(i * 3, i * 3 + 2);
            player2cards.addCard(cardp2);
        }

        System.err.println("Initial 1 " + player1cards);
        System.err.println("Initial 2 " + player2cards);

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");


        playTheGame(score, player1cards, player2cards);
        System.err.println(score);

        System.out.println(score);
    }

    private static void playTheGame(Scorer score,
                                    CardHolder player1,
                                    CardHolder player2) {
        try {
            while (!player1.isEmpty()
                    && !player2.isEmpty()
            ) {
                playOneRoll(score,
                        player1,
                        player2);
            }
        }catch (NoMoreCardsException e){
            score.forcePAD();
        }
    }

    private static void playOneRoll(Scorer score,
                                    CardHolder player1,
                                    CardHolder player2) {
        CardHolder table1 = new CardHolder();
        CardHolder table2 = new CardHolder();
        playOneRoll(score, player1, player2, table1, table2);
    }

    private static int playOneRoll(Scorer score,
                                    CardHolder player1,
                                    CardHolder player2,
                                    CardHolder table1,
                                    CardHolder table2) {
        System.err.println("Player 1 " + player1);
        System.err.println("Player 2 " + player2);
        Card player1Card = player1.poll();
        table1.add(player1Card);
        Card player2Card = player2.poll();
        table2.add(player2Card);
        int winner = player1Card.compareTo(player2Card);
        if (winner > 0) {
            score.player1Wins();
            player1.addAll(table1);
            player1.addAll(table2);
        } else if (winner < 0) {
            score.player2Wins();
            player2.addAll(table1);
            player2.addAll(table2);
        } else {
            CardHolder war1 = player1.giveMe3();
            war1.pushAllReverser(table1);
            CardHolder war2 = player2.giveMe3();
            war2.pushAllReverser(table2);
            while (winner == 0) {
                winner = playOneRoll(score,player1,player2,war1,war2);
            }
        }
        return winner;
    }

    static class Scorer {
        int round = 0;
        int winner = -1;

        public void player1Wins() {
            round++;
            winner = 1;
        }

        public void player2Wins() {
            round++;
            winner = 2;
        }

        public void forcePAD(){
            winner = 0;
        }

        public String winner() {
            if (winner==0) {
                return PAT;
            }
            return String.valueOf(winner);
        }

        public String toString() {
            String winner = winner();
            if (PAT.equals(winner)) {
                return winner;
            }
            return String.format("%s %s",
                    winner,
                    round);
        }
    }

    static class CardHolder extends LinkedList<Card> {

        public Card giveMeOne() {
            return poll();
        }

        public CardHolder giveMe3() {
            if(size()<3){
                throw new NoMoreCardsException("this holder has only "+size());
            }
            CardHolder three = new CardHolder();
            three.add(this.giveMeOne());
            three.add(this.giveMeOne());
            three.add(this.giveMeOne());
            return three;
        }

        public void addCard(String card) {
            add(
                    new Card(card)
            );
        }

        public void pushAll(Queue<Card> q) {
            while (!q.isEmpty()) {
                push(q.poll());
            }
        }

        public void pushAllReverser(Queue<Card> q) {
            CardHolder reverserd = new CardHolder();
            reverserd.pushAll(q);
            pushAll(reverserd);
        }

        public String toString() {
            return stream()
                    .map(c -> c.toString())
                    .collect(Collectors.joining(" "));
        }
    }

    static class Card implements Comparable {
        final char card;
        final char suite;
        final int value;

        public Card(String card) {
            this.card = card.charAt(0);
            this.suite = card.charAt(1);
            this.value = CARD_VALUES.indexOf(this.card);
        }

        public String toString() {
            return String.format("%s%s", card, suite);
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(value, ((Card) o).value);
        }
    }

    static class NoMoreCardsException extends RuntimeException{
        public NoMoreCardsException(String s){
            super(s);
        }
    }

}

