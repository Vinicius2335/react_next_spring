package com.viniciusvieira.backend.domain.service.venda;

import com.viniciusvieira.backend.api.mapper.venda.MarcaMapper;
import com.viniciusvieira.backend.api.representation.model.request.venda.MarcaRequest;
import com.viniciusvieira.backend.api.representation.model.response.venda.MarcaResponse;
import com.viniciusvieira.backend.domain.exception.venda.MarcaAlreadyExistsException;
import com.viniciusvieira.backend.domain.exception.venda.MarcaNaoEncontradaException;
import com.viniciusvieira.backend.domain.model.venda.Marca;
import com.viniciusvieira.backend.domain.repository.venda.MarcaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CrudMarcaService  {
    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public List<Marca> buscarTodos() {
        return marcaRepository.findAll();
    }

    public Marca buscarPeloId(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new MarcaNaoEncontradaException("Marca não cadastrada"));
    }

    @Transactional
    public MarcaResponse inserir(MarcaRequest marcaRequest) {
        verifyIfMarcaExistsByNome(marcaRequest.getNome());

        Marca marcaParaInserir = marcaMapper.toDomainMarca(marcaRequest);
        Marca marcaInserida = marcaRepository.saveAndFlush(marcaParaInserir);
        return marcaMapper.toMarcaResponse(marcaInserida);
    }

    private void verifyIfMarcaExistsByNome(String marcaNome) {
        boolean marcaExists = marcaRepository.findByNome(marcaNome).isPresent();

        if (marcaExists){
            throw new MarcaAlreadyExistsException("Já existe uma marca cadastrada com o NOME: " + marcaNome);
        }
    }

    @Transactional
    public MarcaResponse alterar(Long id, MarcaRequest marcaRequest) {
        Marca marcaEncontrada = buscarPeloId(id);
        verifyIfMarcaExistsByNome(marcaRequest.getNome());

        Marca marcaParaAlterar = marcaMapper.toDomainMarca(marcaRequest);
        marcaParaAlterar.setId(marcaEncontrada.getId());
        marcaParaAlterar.setDataCriacao(marcaEncontrada.getDataCriacao());

        Marca marcaAlterada = marcaRepository.saveAndFlush(marcaParaAlterar);
        return marcaMapper.toMarcaResponse(marcaAlterada);
    }

    @Transactional
    public void excluir(Long id) {
        Marca marcaParaExcluir = buscarPeloId(id);
        marcaRepository.delete(marcaParaExcluir);
    }
}
