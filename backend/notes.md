# Anotações


1. Criar Exception Handler
2. Criar Carrinho de compra e CarrinhoCompraProduto
3. Testes Repository, Service, Controller, Integração 
4. upload de imagem - **_tabela imagens só guarda o nome do arquivo_**
5. Cascade Delete

-------------------

# Observação

- Produto tem que ter quantidade tambem.
  
- Ficar de olho em permissoes de Pessoas

- Por causa das config na classes domain/model Pessoa cascadeType.ALL e FetchType.LAZY, o cascade delete está acontecendo automaticamente no Relacionamento Pessoa -> Pessoa_Permissao -> Permissao
  - Não precisa necessariamente deletar a permissao quando deletar uma pessoa, apenas acabar com o relacionamento na tabela Pessoa_Permissao
  - cascade.ALL talvez esteja meio bruto aki

- Quando terminar, tentar fazer um cascadeDelete de estado alterando todos os relacionamentos dos models para CascadeType.ALL para testar oq acontece.
 


## Cascade Delete

- Estado
  - Cidade
    - Pessoa
      - Permissao

- Marca
  - Produto

- Categoria
  - Produto

- criado mas não foi implementado nos controllers ou testado