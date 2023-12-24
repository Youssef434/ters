package players;

import cards.Card;

import java.util.Set;

public record Player(Team team, Set<Card> cards) {}
