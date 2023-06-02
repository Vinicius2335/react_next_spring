# Anotações


1. Criar Exception Handler
2. Criar Permissao e PermissaoPessoa, Carrinho de compra e CarrinhoCompraProduto
   - Inserir Pessoa, se já existe uma pessoa com o CPF já cadastrado, não permitir.
3. Testes Repository, Service, Controller, Integração 
4. upload de imagem - **_tabela imagens só guarda o nome do arquivo_**
5. Cascade Delete

-------------------

# Observação

- Produto tem que ter quantidade tambem.
- Regex CPF
  - ``[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}\-?[0-9]{2}``
- Regex CEP
  - ``\\d{5}-\\d{3}``
  
- Ficar de olho em permissoes de Pessoas

## Cascade Delete

- Estado
  - Cidade
    - Pessoa

- Marca
  - Produto

- Categoria
  - Produto

- criado mas não foi implementado nos controllers ou testado