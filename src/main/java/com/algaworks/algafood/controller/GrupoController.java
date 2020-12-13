package com.algaworks.algafood.controller;

import com.algaworks.algafood.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.assembler.GrupoModelAssembler;
import com.algaworks.algafood.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.dto.GrupoModel;
import com.algaworks.algafood.domain.model.dto.PermissaoModel;
import com.algaworks.algafood.domain.model.dto.input.GrupoInput;
import com.algaworks.algafood.domain.sevice.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
    @Autowired
    private CadastroGrupoService grupoService;
    @Autowired
    private GrupoModelAssembler modelAssembler;
    @Autowired
    private GrupoInputDisassembler inputDisassembler;


    @PostMapping("/adicionar")
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel salvar(@Valid @RequestBody GrupoInput grupoInput) {
        var grupo = inputDisassembler.toDomainObject(grupoInput);
        return modelAssembler.toModel(grupoService.salvar(grupo));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GrupoModel> listar() {
        return modelAssembler.toCollectionModel(grupoService.listar());
    }

    @GetMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.OK)
    public GrupoModel buscar(@PathVariable Long grupoId) {
        return modelAssembler.toModel(grupoService.buscar(grupoId));
    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long grupoId) {
        grupoService.deletar(grupoId);
    }

    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.OK)
    private GrupoModel atualizar(@PathVariable Long grupoId, @Valid @RequestBody GrupoInput grupoInput) {
        var grupo = inputDisassembler.toDomainObject(grupoInput);
        return modelAssembler.toModel(grupoService.atualizar(grupoId, grupo));
    }

}
