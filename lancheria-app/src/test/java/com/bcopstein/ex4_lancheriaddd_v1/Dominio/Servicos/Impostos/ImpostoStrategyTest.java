package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoService;

class ImpostoStrategyTest {

    private static final double DELTA = 0.001;

    @Test
    void lei1234CalculaDezPorCentoFlat() {
        assertEquals(1_000.0, new ImpostoLei1234().calcular(10_000.0), DELTA);
    }

    @Test
    void lei1234RetornaIdCorreto() {
        assertEquals("lei_1234", new ImpostoLei1234().getId());
    }

    @Test
    void lei5678CalculaOitoPorCentoAteCem() {
        assertEquals(400.0, new ImpostoLei5678().calcular(5_000.0), DELTA);
    }

    @Test
    void lei5678CalculaDozePorCentoAcimaDeCem() {
        assertEquals(2_400.0, new ImpostoLei5678().calcular(20_000.0), DELTA);
    }

    @Test
    void lei5678RetornaIdCorreto() {
        assertEquals("lei_5678", new ImpostoLei5678().getId());
    }

    @Test
    void impostoServiceDelegaParaLei1234() {
        ImpostoService service = new ImpostoService(new ImpostoLei1234());
        assertEquals(1_000.0, service.calcularImposto(10_000.0), DELTA);
    }

    @Test
    void impostoServiceDelegaParaLei5678() {
        ImpostoService service = new ImpostoService(new ImpostoLei5678());
        assertEquals(2_400.0, service.calcularImposto(20_000.0), DELTA);
    }
}
