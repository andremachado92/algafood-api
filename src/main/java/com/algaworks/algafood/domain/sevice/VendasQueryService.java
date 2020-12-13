package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.vendaDto.VendaDiaria;

import java.util.List;

public interface VendasQueryService {
    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filter, String timeOffSet);
}
