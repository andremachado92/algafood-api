package com.algaworks.algafood.controller;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.vendaDto.VendaDiaria;
import com.algaworks.algafood.domain.sevice.VendasQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {
    @Autowired
    private VendasQueryService vendasQueryService;

    @GetMapping(value = "vendas-diarias")
    public List<VendaDiaria>consultarVendasDiarias(VendaDiariaFilter filter,
                                                   @RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
        return vendasQueryService.consultarVendasDiarias(filter,timeOffset);
    }
}
