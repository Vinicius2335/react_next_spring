# Anotações


1. Criar Exception Handler
2. Criar CarrinhoCompraProduto
3. Testes Repository, Service, Controller, Integração 
4. Cascade Delete

-------------------

# Observação

- Ficar de olho em permissoes de Pessoas

- Por causa das config na classes domain/model Pessoa cascadeType.ALL e FetchType.LAZY, o cascade delete está acontecendo automaticamente no Relacionamento Pessoa -> Pessoa_Permissao -> Permissao
  - Não precisa necessariamente deletar a permissao quando deletar uma pessoa, apenas acabar com o relacionamento na tabela Pessoa_Permissao
  - cascade.ALL talvez esteja meio bruto aki

- Quando terminar, tentar fazer um cascadeDelete de estado alterando todos os relacionamentos dos models para CascadeType.ALL para testar oq acontece.
 
- A pessoa só poderá realizar uma compra se estiver cadastrada ?


## Cascade Delete

- Estado
  - Cidade
    - Pessoa
      - Permissao

- Marca
  - Produto
    - Imagens (fazer) 

- Categoria
  - Produto
    - Imagens (fazer)

- criado mas não foi implementado nos controllers ou testado

# Testes

- ProdutoController
- UploadController
- ProdutoImagemRepository
- CrudProdutoImagemService