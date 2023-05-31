# Anotações


1. Criar Exception Handler
2. Criar Pessoa, Produto, Permissao, Carrinho de compra, PermissaoPessoa e CarrinhoCompraProduto
   - Inserir Pessoa, se já existe uma pessoa com o CPF já cadastrado, não permitir.
3. Testes Repository, Service, Controller, Integração 
4. validação Pessoa cpf, email, cep
5. upload de imagem - **_tabela imagens só guarda o nome do arquivo_**
6. Cascade Delete

-------------------

# Observação

- Produto tem que ter quantidade tambem.
- Regex CPF
  - ``[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}\-?[0-9]{2}``
- Regex CEP
  - ``\\d{5}-\\d{3}``

## Cascade Delete

- Estado
  - Cidade
    - Pessoa

- criado mas não foi implementado nos controllers ou testado