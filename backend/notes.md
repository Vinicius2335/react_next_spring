# Anotações


1. Criar Exception Handler
2. Criar CarrinhoCompraProduto
3. Testes Repository, Service, Controller, Integração 
4. Cascade Delete

-------------------

# Observação

- Por causa das config na classes domain/model Pessoa cascadeType.ALL e FetchType.LAZY, o cascade delete está acontecendo automaticamente no Relacionamento Pessoa -> Pessoa_Permissao -> Permissao
  - Não precisa necessariamente deletar a permissao quando deletar uma pessoa, apenas acabar com o relacionamento na tabela Pessoa_Permissao
  - cascade.ALL, deleta tudo
    - com o CascadeType.ALL ele deletava a pessoa, permissao e todos os relacionamentos de Pessoa_Permissao e dps deletava todas as pessoas relacionadas a permissao da pessoa que começou esse delete.
    - resolvido com isso -> cascade = {CascadeType.MERGE, CascadeType.PERSIST}
      - remove somente pessoa e pessoa_permissao

- Quando terminar, tentar fazer um cascadeDelete de estado alterando todos os relacionamentos dos models para CascadeType.ALL para testar oq acontece.
 
- A pessoa só poderá realizar uma compra se estiver cadastrada ?
- Em Pessoa -> adicionar uma lista de Permissoes e não uma permissao por vez


## Cascade Delete

- Estado
  - Cidade
    - Pessoa

- Marca
  - Produto
    - Imagens (fazer) 

- Categoria
  - Produto
    - Imagens (fazer)
    - CarrinhoDeCompraProduto
    


- criado mas não foi implementado nos controllers ou testado

# Testes

- PessoaGerenciamentoController
- CarrinhoDeCompraProdutoRepository
- CrudCarrinhoDeCompraProdutoService
- CarrinhoDeCompraProdutoController
- CascadeService
- EmailService