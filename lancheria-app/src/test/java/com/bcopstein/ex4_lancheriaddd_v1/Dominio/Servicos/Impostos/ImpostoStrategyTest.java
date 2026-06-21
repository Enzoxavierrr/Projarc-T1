package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoService;

class ImpostoStrategyTest {

    private static final double DELTA = 0.001;

    @Test
    void lei1234CalculaDezPorCentoFlat() {
        assertEquals(10.0, new ImpostoLei1234().calcular(100.0), DELTA);
    }

    @Test
    void lei1234RetornaIdCorreto() {
        assertEquals("lei_1234", new ImpostoLei1234().getId());
    }

    @Test
    void lei5678CalculaOitoPorCentoAteCem() {
        assertEquals(4.0, new ImpostoLei5678().calcular(50.0), DELTA);
    }

    @Test
    void lei5678CalculaDozePorCentoAcimaDeCem() {
        assertEquals(24.0, new ImpostoLei5678().calcular(200.0), DELTA);
    }

    @Test
    void lei5678RetornaIdCorreto() {
        assertEquals("lei_5678", new ImpostoLei5678().getId());
    }

    @Test
    void impostoServiceDelegaParaLei1234() {
        ImpostoService service = new ImpostoService(new ImpostoLei1234());
        assertEquals(10.0, service.calcularImposto(100.0), DELTA);
    }

    @Test
    void impostoServiceDelegaParaLei5678() {
        ImpostoService service = new ImpostoService(new ImpostoLei5678());
        assertEquals(24.0, service.calcularImposto(200.0), DELTA);
    }
}
