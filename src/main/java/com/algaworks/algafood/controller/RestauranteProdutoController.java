package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.dto.ProdutoModel;
import com.algaworks.algafood.domain.model.dto.input.ProdutoInput;
import com.algaworks.algafood.domain.sevice.CadastroProdutoService;
import com.algaworks.algafood.domain.sevice.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
    @Autowired
    private CadastroProdutoService produtoService;
    @Autowired
    private ProdutoModelAssembler produtoModelAssembler;
    @Autowired
    private ProdutoInputDisassembler produtoInputDisassembler;
    @Autowired
    private CadastroRestauranteService restauranteService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProdutoModel adicionar(@Valid @RequestBody ProdutoInput produtoInput, @PathVariable Long restauranteId){
        var restaurante = restauranteService.buscarOuFalhar(restauranteId);
        var produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);
        return produtoModelAssembler.toModel(produtoService.salvar(produto));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProdutoModel> listar(@PathVariable Long restauranteId,
                                     @RequestParam(required = false) Boolean incluirInativos){
        return produtoModelAssembler.toCollectionModel(produtoService.listar(restauranteId,incluirInativos));
    }

    @GetMapping(value = "/{produtoId}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
        return produtoModelAssembler.toModel(produtoService.buscarOuFalhar(restauranteId,produtoId));
    }

    @PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = produtoService.buscarOuFalhar(restauranteId, produtoId);

        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        return produtoModelAssembler.toModel( produtoService.salvar(produtoAtual));
    }
}
