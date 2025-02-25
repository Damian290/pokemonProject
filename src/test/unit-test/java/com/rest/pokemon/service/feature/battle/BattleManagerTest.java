package com.rest.pokemon.service.feature.battle;

import com.rest.pokemon.domain.pokemon.PokemonDetails;
import com.rest.pokemon.domain.pokemon.Statistics;
import com.rest.pokemon.repository.PokemonTeam;
import com.rest.pokemon.repository.PokemonTeamDao;
import com.rest.pokemon.service.feature.battle.exception.NoPokemonTeamException;
import com.rest.pokemon.service.feature.battle.onevsone.OneVsOneResolver;
import com.rest.pokemon.service.feature.battle.onevsone.OneVsOneRing;
import com.rest.pokemon.service.feature.battle.oponentfinder.OpponentFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BattleManagerTest {

    private final PokemonTeamDao pokemonTeamDao = mock(PokemonTeamDao.class);
    private final OpponentFinder opponentFinder = mock(OpponentFinder.class);
    private final OneVsOneResolver oneVsOneResolver = mock(OneVsOneResolver.class);
    private final BattleManager sut = new BattleManager(pokemonTeamDao, opponentFinder, oneVsOneResolver);

    private PokemonDetails myPokemon;
    private PokemonDetails opponentPokemon;

    @BeforeEach
    void setUp() {
        myPokemon = new PokemonDetails(1, "Pikachu", new Statistics(50, 55, 40, 90));
        opponentPokemon = new PokemonDetails(2, "Charmander", new Statistics(39, 52, 43, 65));
    }

    @Test
    void shouldThrowExceptionWhenNoPokemonInTeam() {
        //given
        when(pokemonTeamDao.getAll()).thenReturn(new PokemonTeam(List.of()));

        //when & then
        assertThrows(NoPokemonTeamException.class, sut::battle);
        verify(pokemonTeamDao, never()).removeFirst();
    }

    @Test
    void shouldReturnMyPokemonAsWinnerAndUpdateHisStateWhenMyPokemonWin() {
        //given
        when(pokemonTeamDao.getAll()).thenReturn(new PokemonTeam(List.of(myPokemon)));
        when(opponentFinder.find()).thenReturn(Mono.just(opponentPokemon));

        opponentPokemon.statistics().setHp(0);
        var battleResultWin = new OneVsOneRing(myPokemon, opponentPokemon);
        when(oneVsOneResolver.resolve(myPokemon, opponentPokemon)).thenReturn(battleResultWin);

        //when
        StepVerifier.create(sut.battle())
                .expectNext(myPokemon)
                .verifyComplete();

        verify(pokemonTeamDao).updateFirstPokemon(myPokemon);
        verify(pokemonTeamDao, never()).removeFirst();
    }

    @Test
    void shouldRemoveMyPokemonFromListWhenBattleIsWonByOpponent() {
        when(pokemonTeamDao.getAll()).thenReturn(new PokemonTeam(List.of(myPokemon)));
        when(opponentFinder.find()).thenReturn(Mono.just(opponentPokemon));
        myPokemon.statistics().setHp(0);
        var battleResultLose = new OneVsOneRing(myPokemon, opponentPokemon);
        when(oneVsOneResolver.resolve(myPokemon, opponentPokemon)).thenReturn(battleResultLose);
        when(pokemonTeamDao.removeFirst()).thenReturn(myPokemon);

        StepVerifier.create(sut.battle())
                .expectNext(opponentPokemon)
                .verifyComplete();

        verify(pokemonTeamDao).removeFirst();
        verify(pokemonTeamDao, never()).updateFirstPokemon(any());
    }
}
