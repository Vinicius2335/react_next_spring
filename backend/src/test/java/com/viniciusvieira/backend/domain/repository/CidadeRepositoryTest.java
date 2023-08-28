package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface CidadeRepository")
@Log4j2
class CidadeRepositoryTest {
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private final Cidade expectedCidade = CidadeCreator.mockCidade();

    private Cidade inseriNovaCidade(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        return cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
    }

    @Test
    @DisplayName("saveAndFlush Insert new ciade when Successful")
    void saveAndFlush_InsertNewCidade_WhenSuccessful(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        Cidade cidadeSalva = cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());

        System.out.println();
        log.info("Cidade Salva -> {}", cidadeSalva);
        log.info("Expected Cidade -> {}", expectedCidade);

        assertAll(
                () -> assertNotNull(cidadeSalva),
                () -> assertEquals(expectedCidade.getId(), cidadeSalva.getId()),
                () -> assertEquals(expectedCidade.getNome(), cidadeSalva.getNome()),
                () -> assertEquals(expectedCidade.getEstado().getNome(), cidadeSalva.getEstado().getNome()),
                () -> assertEquals(expectedCidade.getEstado().getSigla(), cidadeSalva.getEstado().getSigla())
        );
    }

    @Test
    @DisplayName("findAll Return list of cidade When successful")
    void findAll_ReturnListCidade_WhenSuccessful(){
        Cidade cidadeInserida = inseriNovaCidade();
        List<Cidade> cidades = cidadeRepository.findAll();

        assertAll(
                () -> assertNotNull(cidades),
                () -> assertEquals(1, cidades.size()),
                () -> assertTrue(cidades.contains(cidadeInserida))
        );
    }

    @Test
    @DisplayName("findById Return a cidade When successful")
    void findById_ReturnCidade_WhenSuccessful(){
        Cidade cidadeInserida = inseriNovaCidade();
        Optional<Cidade> cidadeEncontrada = cidadeRepository.findById(cidadeInserida.getId());

        assertAll(
                () -> assertFalse(cidadeEncontrada.isEmpty()),
                () -> assertEquals(cidadeInserida, cidadeEncontrada.get())
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing cidade when successful")
    void saveAndFlush_UpdateExistingCidade_WhenSuccessful(){
        Cidade cidadeInserida = inseriNovaCidade();
        Cidade cidadeToUpdate = CidadeCreator.mockCidateToUpdate(cidadeInserida.getDataCriacao());
        Cidade cidadeAlterada = cidadeRepository.saveAndFlush(cidadeToUpdate);

        assertAll(
                () -> assertNotNull(cidadeAlterada),
                () -> assertEquals(cidadeToUpdate.getNome(), cidadeAlterada.getNome())
        );
    }

    @Test
    @DisplayName("delete Remove cidade When successful")
    void delete_RemoveCidade_WhenSuccessful(){
        Cidade cidadeInseirada = inseriNovaCidade();
        cidadeRepository.delete(cidadeInseirada);

        Optional<Cidade> cidade = cidadeRepository.findById(cidadeInseirada.getId());

        assertAll(
                () -> assertTrue(cidade.isEmpty())
        );
    }

    @Test
    @DisplayName("findAllCidadeByIdEstado_ResturnListCidade_WhenSuccessful")
    void findAllCidadeByIdEstado_ReturnListCidade_WhenSuccessful(){
        Cidade cidade = inseriNovaCidade();
        Cidade cidade2 = Cidade.builder()
                .id(2L)
                .nome("Estado 2")
                .estado(EstadoCreator.mockEstado())
                .dataCriacao(OffsetDateTime.now())
                .dataAlteracao(OffsetDateTime.now())
                .build();
        Cidade cidade2Inserida = cidadeRepository.saveAndFlush(cidade2);

        List<Cidade> cidades = cidadeRepository.findAllCidadeByIdEstado(1L);

        log.info(cidades);

        assertAll(
                () -> assertNotNull(cidades),
                () -> assertEquals(2, cidades.size()),
                () -> assertTrue(cidades.contains(cidade)),
                () -> assertTrue(cidades.contains(cidade2Inserida))
        );
    }
}