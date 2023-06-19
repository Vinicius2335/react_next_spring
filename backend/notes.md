# Anotações


1. Criar Exception Handler
2. Criar CarrinhoCompraProduto
3. Testes Repository, Service, Controller, Integração 
4. Cascade Delete

-------------------

# Observação

- Por causa das config na classes domain/model Pessoa cascadeType.ALL e FetchType.LAZY, o cascade delete está acontecendo automaticamente no Relacionamento Pessoa -> Pessoa_Permissao -> Permissao
  - Não precisa necessariamente deletar a permissao quando deletar uma pessoa, apenas acabar com o relacionamento na tabela Pessoa_Permissao
  - cascade.ALL talvez esteja meio bruto aki
    - resolvido, deixar aki como lembrete, com o CascadeType.ALL ele deletava a pessoa, deleteva a permissao, deletava todos os relacionamentos de e Pessoa_Permissao e dps deletava todas as pessoas relacionadas a permissao da pessoa que começou esse delete
    - resolvido com isso -> cascade = {CascadeType.MERGE, CascadeType.PERSIST}

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

- criado mas não foi implementado nos controllers ou testado

# Testes

- PessoaGerenciamentoController
- GerenciamentoService
- PessoaRepository - findByEmail - findByEmailAndCodigoRecuperacaoSenha
- CrudPessoaService - buscarPeloEmail - buscarPeloEmailECodigo
