package com.viniciusvieira.backend.domain.repository;

import com.viniciusvieira.backend.domain.model.usuario.Permissao;
import com.viniciusvieira.backend.domain.repository.usuario.PermissaoRepository;
import com.viniciusvieira.backend.util.PermissaoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("Teste Unitário para a interface PermissaoRepository")
class PermissaoRepositoryTest {
    @Autowired
    private PermissaoRepository permissaoRepository;
    
    private final Permissao expectedPermissao = PermissaoCreator.mockPermissao();

    public Permissao inserirNovaPermissaoNoBanco(){
        return permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());
    }

    @Test
    @DisplayName("saveAndFlush Insert new permissao when Successful")
    void saveAndFlush_InsertNewPermissao_WhenSuccessful(){
        Permissao permissaoSaved = permissaoRepository.saveAndFlush(PermissaoCreator.mockPermissao());

        assertAll(
                () -> assertNotNull(permissaoSaved),
                () -> assertEquals(expectedPermissao.getId(), permissaoSaved.getId()),
                () -> assertEquals(expectedPermissao.getNome(), permissaoSaved.getNome())
        );
    }

    @Test
    @DisplayName("findAll Return list of permissao When successful")
    void findAll_ReturnListPermissao_WhenSuccessful(){
        Permissao novaPermissaoInserida = inserirNovaPermissaoNoBanco();
        List<Permissao> permissaos = permissaoRepository.findAll();

        assertAll(
                () -> assertNotNull(permissaos),
                () -> assertFalse(permissaos.isEmpty()),
                () -> assertEquals(1, permissaos.size()),
                () -> assertTrue(permissaos.contains(novaPermissaoInserida))
        );
    }

    @Test
    @DisplayName("findById Return a permissao When successful")
    void findById_ReturnPermissao_WhenSuccessful(){
        Permissao novaPermissaoInserida = inserirNovaPermissaoNoBanco();
        Permissao permissaoEncontrada = permissaoRepository.findById(novaPermissaoInserida.getId()).get();

        assertAll(
                () -> assertNotNull(permissaoEncontrada),
                () -> assertEquals(novaPermissaoInserida, permissaoEncontrada)
        );
    }

    @Test
    @DisplayName("findByNome Return a optional permissao When successful")
    void findByNome_ReturnOptionalPermissao_WhenSuccessful(){
        Permissao novaPermissaoInserida = inserirNovaPermissaoNoBanco();
        Optional<Permissao> permissaoEncontrada = permissaoRepository.findByNome(novaPermissaoInserida.getNome());

        assertAll(
                () -> assertNotNull(permissaoEncontrada),
                () -> assertTrue(permissaoEncontrada.isPresent()),
                () -> assertEquals(novaPermissaoInserida, permissaoEncontrada.get())
        );
    }

    @Test
    @DisplayName("saveAndFlush Update existing permissao when successful")
    void saveAndFlush_UpdateExistingPermissao_WhenSuccessful(){
        inserirNovaPermissaoNoBanco();
        Permissao permissaoParaAtualizar = PermissaoCreator.mockPermissaoUpdated();

        Permissao permissaoAtualizada = permissaoRepository.saveAndFlush(permissaoParaAtualizar);
        Permissao permissaoEncontrada = permissaoRepository.findById(permissaoAtualizada.getId()).get();

        assertAll(
                () -> assertNotNull(permissaoAtualizada),
                () -> assertEquals(permissaoEncontrada, permissaoAtualizada)
        );
    }

    @Test
    @DisplayName("delete Remove permissao When successful")
    void delete_RemovePermissao_WhenSuccessful(){
        Permissao novaPermissaoInserida = inserirNovaPermissaoNoBanco();
        permissaoRepository.delete(novaPermissaoInserida);
        List<Permissao> permissaos = permissaoRepository.findAll();

        assertTrue(permissaos.isEmpty());
    }
}