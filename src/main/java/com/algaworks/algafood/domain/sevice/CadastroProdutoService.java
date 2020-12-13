package com.algaworks.algafood.domain.sevice;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.exceptions.ProdutoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CadastroRestauranteService restauranteService;


    public Produto salvar(Produto produto){
        return produtoRepository.save(produto);
    }

    public List<Produto> listar(Long restauranteId, Boolean incluirInativos){

        List<Produto> produtos = null;
        if(Boolean.TRUE.equals(incluirInativos)){
            produtos =  produtoRepository.findByRestauranteId(restauranteId);
        }else{
            var restaurante = restauranteService.buscarOuFalhar(restauranteId);
            produtos = produtoRepository.findAtivosByRestauranteId(restaurante);

        }
        return produtos;
    }

    public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findById(restauranteId, produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }

}
