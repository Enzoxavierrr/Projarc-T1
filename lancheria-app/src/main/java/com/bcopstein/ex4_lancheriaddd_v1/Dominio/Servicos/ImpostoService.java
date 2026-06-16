package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostoService implements IImpostoService {
    @Override
    public double calcularImposto(double subtotal) {
        return subtotal * 0.1; 
    }
    
}
