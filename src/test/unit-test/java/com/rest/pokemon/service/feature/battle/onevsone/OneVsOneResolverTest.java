package com.rest.pokemon.service.feature.battle.onevsone;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OneVsOneResolverTest {

    private final OneVsOneResolver sut = new OneVsOneResolver();

    @ParameterizedTest
    @CsvSource({
            "50,20,10,100, 40,15,5,99, Pikachu",   // Pikachu attacks first and win
            "30,10,5, 50, 60,30,10,49, Charmander", // Charmander with better stats win
            "80,40,30,70, 80,40,30,71, Charmander", // Charmander win by better speed
            "800,40,30,1, 80,40,30,71, Pikachu", // Pikachu win - is slower but has more hp
    })
    void shouldResolveBattleAndReturnWinner(long myHp, long myAttack, long myDefence, long mySpeed,
                                            long opponentHp, long opponentAttack, long opponentDefence, long opponentSpeed,
                                            String expectedWinnerName) {
        // Given
        PokemonDetails myPokemon = createPokemon(1, "Pikachu", myHp, myAttack, myDefence, mySpeed);
        PokemonDetails opponentPokemon = createPokemon(2, "Charmander", opponentHp, opponentAttack, opponentDefence, opponentSpeed);

        // When
        OneVsOneRing battleResult = sut.resolve(myPokemon, opponentPokemon);

        // Then
        assertNotNull(battleResult);
        assertEquals(expectedWinnerName, battleResult.getWinner().getPokemonDetails().name());
    }

    private PokemonDetails createPokemon(int id, String name, long hp, long attack, long defence, long speed) {
        Statistics stats = new Statistics(hp, attack, defence, speed);
        return new PokemonDetails(id, name, stats);
    }

}
