package org.acelera.brasil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.acelera.brasil.futebol.FutebolService;
import org.acelera.brasil.futebol.Jogador;
import org.acelera.brasil.futebol.Posicao;
import org.acelera.brasil.futebol.Time;
import org.acelera.brasil.futebol.exception.TimeNaoPossuiJogadoresException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Classe responsável pelo teste de {@link FutebolService}.
 * 
 * @author edmilson.santana
 *
 */
@RunWith(JUnitPlatform.class)
public class FutebolTest {

	private FutebolService service;

	@BeforeEach
	public void setUp() {
		this.service = new FutebolService();
		this.adicionarTime("A", 0, Posicao.ATAQUE);
		this.adicionarTime("B", 10, Posicao.DEFESA);
		this.adicionarTime("C", 5, Posicao.GOLEIRO);
		this.adicionarTime("D", 3, Posicao.ATAQUE);
		this.adicionarTime("E", 20, Posicao.DEFESA);
	}

	@Test
	public void deveBuscarJogadoresComDireitoAoGolNoFantastico() {
		List<Jogador> jogadores = this.service.buscarJogadoresComDireitoAoGolNoFantastico();

		assertEquals(4, jogadores.size());
	}

	@Test
	public void deveBuscarArtilheiroDoTime() {
		Time time = new Time("Time F");

		time.adicionarJogador(Jogador.builder().withNome("Jogador FA").withCidade("Cidade A").withPais("Brasil")
				.withNumeroDeGols(10).withPosicao(Posicao.ATAQUE).build());
		time.adicionarJogador(Jogador.builder().withNome("Jogador FB").withCidade("Cidade A").withPais("Brasil")
				.withNumeroDeGols(5).withPosicao(Posicao.DEFESA).build());
		time.adicionarJogador(Jogador.builder().withNome("Jogador FC").withCidade("Cidade A").withPais("Brasil")
				.withNumeroDeGols(2).withPosicao(Posicao.GOLEIRO).build());
		this.service.adicionarTime(time);

		Jogador artilheiro = this.service.buscarArtilheiroTime("Time F");

		assertEquals("Jogador FA", artilheiro.getNome());
		assertEquals(10, artilheiro.getNumeroDeGols());
	}

	@Test
	public void deveOcorrerErroAoBuscarArtilheiroDoTimeSemJogadores() {
		String nomeTime = "Time F";
		Time time = new Time(nomeTime);

		this.service.adicionarTime(time);

		assertThrows(TimeNaoPossuiJogadoresException.class, () -> this.service.buscarArtilheiroTime(nomeTime));
	}

	@Test
	public void deveBuscaJogadoresOrdenadosPeloNumeroDeGols() {
		List<Jogador> jogadores = this.service.buscarJogadoresOrdenadosPorNumeroDeGols();
		
		assertEquals(Arrays.asList("Jogador AA", "Jogador DA", "Jogador CA", "Jogador BA", "Jogador EA"),
				this.service.getNomesDeJogadores(jogadores));
	}

	@Test
	public void deveAgruparJogadoresPorPosicao() {
		Map<Posicao, List<Jogador>> jogadoresPorPosicao = this.service.agruparJogadoresPorPosicao();

		List<Jogador> jogadoresNoAtaque = jogadoresPorPosicao.get(Posicao.ATAQUE);
		List<Jogador> jogadoresNaDefesa = jogadoresPorPosicao.get(Posicao.DEFESA);
		List<Jogador> goleiros = jogadoresPorPosicao.get(Posicao.GOLEIRO);

		assertEquals(Arrays.asList("Jogador AA", "Jogador DA"), this.service.getNomesDeJogadores(jogadoresNoAtaque));
		assertEquals(Arrays.asList("Jogador EA", "Jogador BA"), this.service.getNomesDeJogadores(jogadoresNaDefesa));
		assertEquals(Arrays.asList("Jogador CA"), this.service.getNomesDeJogadores(goleiros));
	}

	private void adicionarTime(String nome, Integer numeroDeGols, Posicao posicao) {
		Time time = new Time(String.format("Time %s", nome));

		time.adicionarJogador(Jogador.builder().withNome(String.format("Jogador %sA", nome)).withCidade("Cidade A")
				.withPais("Brasil").withNumeroDeGols(numeroDeGols).withPosicao(posicao).build());
		
		this.service.adicionarTime(time);
	}

}
