package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.FotoProdutoModel;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.dto.input.FotoProdutoInput;
import com.algaworks.algafood.domain.sevice.CadastroProdutoService;
import com.algaworks.algafood.domain.sevice.CatalogoFotoProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("restaurantes/{restauranteId}/produtos/{produtoId}/fotos")
public class RestauranteProdutoFotoController {

    @Autowired
    private CadastroProdutoService cadastroProduto;

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProduto;

    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;


    @PutMapping
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
                                          @PathVariable Long produtoId,
                                          @Valid FotoProdutoInput fotoProdutoInput) throws Exception {


            Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

            MultipartFile arquivo = fotoProdutoInput.getArquivo();

            FotoProduto foto = new FotoProduto();
            foto.setProduto(produto);
            foto.setDescricao(fotoProdutoInput.getDescricao());
            foto.setContentType(arquivo.getContentType());
            foto.setTamanho(arquivo.getSize());
            foto.setNomeArquivo(arquivo.getOriginalFilename());

            FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto);

            return fotoProdutoModelAssembler.toModel(fotoSalva);
        }

}
