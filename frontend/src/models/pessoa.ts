export type DataTypePessoa = {
  [key: string]: any
  id: number
  nome: string
  senha: string
  cpf: string
  email: string
  endereco: EnderecoType
  nomePermissao: string
  dataCriacao: string
  dataAtualizacao: string
}

// TODO - Excluir senha

export type EnderecoType = {
  logradouro: string
  estado: string
  cidade: string
  cep: string
}

export function createEmptyPessoa(){
  const emptyPessoa: DataTypePessoa = {
    id: 0,
    nome: "",
    senha: "",
    cpf: "",
    email: "",
    nomePermissao: "",
    endereco: createEmptyEndereco(),
    dataCriacao: "",
    dataAtualizacao: ""
  }

  return emptyPessoa
}

function createEmptyEndereco(): EnderecoType{
  return {
    logradouro: "",
    estado: "",
    cidade: "",
    cep: ""
  }
}

export function getColumnsPessoa(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "NOME", uid: "nome", sortable: true },
    { name: "CPF", uid: "cpf"},
    { name: "EMAIL", uid: "email"},
    { name: "CEP", uid: "cep"},
    { name: "LOGRADOURO", uid: "logradouro"},
    { name: "CIDADE", uid: "cidade"},
    { name: "ESTADO", uid: "estado"},
    { name: "ACTIONS", uid: "actions" }
  ]
}