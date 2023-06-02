package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.Pessoa;
import com.viniciusvieira.backend.util.CidadeCreator;
import com.viniciusvieira.backend.util.EstadoCreator;
import com.viniciusvieira.backend.util.PessoaCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface PessoaRepository")
class PessoaRepositoryTest {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private final Pessoa expectedPessoa = PessoaCreator.mockPessoa();

    public Pessoa inserirNovaPessoaNoBanco(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        return pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());
    }

    @Test
    @DisplayName("saveAndFlush Insert new pessoa when Successful")
    void saveAndFlush_InsertNewPessoa_WhenSuccessful(){
        estadoRepository.saveAndFlush(EstadoCreator.mockEstado());
        cidadeRepository.saveAndFlush(CidadeCreator.mockCidade());
        Pessoa pessoaSaved = pessoaRepository.saveAndFlush(PessoaCreator.mockPessoa());

        assertAll(
                () -> assertNotNull(pessoaSaved),
                () -> assertEquals(expectedPessoa.getId(), pessoaSaved.getId()),
                () -> assertEquals(expectedPessoa.getNome(), pessoaSaved.getNome()),
                () -> assertEquals(expectedPessoa.getCep(), pessoaSaved.getCep()),
                () -> assertEquals(expectedPessoa.getCpf(), pessoaSaved.getCpf())
        );
    }

    @Test
    @DisplayName("findAll Return list of pessoa When successful")
    void findAll_ReturnListPessoa_WhenSuccessful(){
        Pessoa novaPessoaInserida = inserirNovaPessoaNoBanco();
        List<Pessoa> pessoas = pessoaRepository.findAll();

        assertAll(
                () -> assertNotNull(pessoas),
                () -> assertFalse(pessoas.isEmpty()),
                () -> assertEquals(1, pessoas.size()),
                () -> assertTrue(pessoas.contains(novaPessoaInserida))
        );
    }

    @Test
    @DisplayName("findById Return a pessoa When successful")
    void findById_ReturnPessoa_WhenSuccessful(){
        Pessoa novaPessoaInserida = inserirNovaPessoaNoBanco();
        Pessoa pessoaEncontrada = pessoaRepository.findById(novaPessoaInserida.getId()).get();

        assertAll(
                () -> assertNotNull(pessoaEncontrada),
                () -> assertEquals(novaPessoaInserida, pessoaEncontrada)
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing pessoa when successful")
    void saveAndFlush_UpdateExistingPessoa_WhenSuccessful(){
        Pessoa novaPessoaInserida = inserirNovaPessoaNoBanco();
        Pessoa pessoaParaAtualizar = PessoaCreator.mockPessoaToUpdate(novaPessoaInserida.getDataCriacao());

        Pessoa pessoaAtualizada = pessoaRepository.saveAndFlush(pessoaParaAtualizar);
        Pessoa pessoaEncontrada = pessoaRepository.findById(pessoaAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(pessoaAtualizada),
                () -> assertEquals(pessoaEncontrada, pessoaAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove pessoa When successful")
    void delete_RemovePessoa_WhenSuccessful(){
        Pessoa novaPessoaInserida = inserirNovaPessoaNoBanco();
        pessoaRepository.delete(novaPessoaInserida);
        List<Pessoa> pessoas = pessoaRepository.findAll();

        assertTrue(pessoas.isEmpty());
    }

    @Test
    @DisplayName("findByCpf Return a pessoa When Successful")
    void findByCpf_ReturnPessoa_WhenSuccessful(){
        Pessoa pessoa = inserirNovaPessoaNoBanco();
        Optional<Pessoa> pessoaEncontrada = pessoaRepository.findByCpf(pessoa.getCpf());

        assertTrue(pessoaEncontrada.isPresent());
    }

    @Test
    @DisplayName("findAllPessoasByCidadeId Return list pessoa When successful")
    void findAllPessoasByCidadeId_ReturnList_WhenSuccessful(){
        Pessoa pessoa = inserirNovaPessoaNoBanco();
        Pessoa pessoa2 = Pessoa.builder()
                .id(2L)
                .cep("11111-000")
                .cpf("302.218.730-08")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste 02")
                .senha("teste2")
                .email("teste2@gmail.com")
                .endereco("rua teste2")
                .build();
        Pessoa inserindoPessoa2 = pessoaRepository.saveAndFlush(pessoa2);

        List<Pessoa> pessoas = pessoaRepository.findAllPessoasByCidadeId(1L);

        assertAll(
                () -> assertNotNull(pessoas),
                () -> assertEquals(2, pessoas.size()),
                () -> assertTrue(pessoas.contains(pessoa)),
                () -> assertTrue(pessoas.contains(inserindoPessoa2))
        );
    }

    @Test
    @DisplayName("findAllPessoasByCidadeId2 Return list pessoa When successful")
    void findAllPessoasByCidadeId2_ReturnList_WhenSuccessful(){
        Pessoa pessoa = inserirNovaPessoaNoBanco();
        Pessoa pessoa2 = Pessoa.builder()
                .id(2L)
                .cep("11111-000")
                .cpf("302.218.730-08")
                .cidade(CidadeCreator.mockCidade())
                .nome("Teste 02")
                .senha("teste2")
                .email("teste2@gmail.com")
                .endereco("rua teste2")
                .build();
        Pessoa inserindoPessoa2 = pessoaRepository.saveAndFlush(pessoa2);

        List<Pessoa> pessoas = pessoaRepository.findPessoasByCidadeId(1L);

        log.info(pessoas);

        assertAll(
                () -> assertNotNull(pessoas),
                () -> assertEquals(2, pessoas.size()),
                () -> assertTrue(pessoas.contains(pessoa)),
                () -> assertTrue(pessoas.contains(inserindoPessoa2))
        );
    }
}