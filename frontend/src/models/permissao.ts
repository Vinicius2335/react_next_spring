export type DataTypePermissao = {
  [key: string]: any
  id: number
  nome: string
  dataCriacao: string
  dataAtualizacao: string
}

export function createEmptyPermissao(){
  const emptyPermissao: DataTypePermissao = {
    id: 0,
    nome: "",
    dataCriacao: "",
    dataAtualizacao: ""
  }

  return emptyPermissao
}

export function getColumnsPermissao(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "NOME", uid: "nome", sortable: true },
    { name: "DATA CRIAÇÃO", uid: "dataCriacao" },
    { name: "DATA ATUALIZAÇÃO", uid: "dataAtualizacao" },
    { name: "ACTIONS", uid: "actions" }
  ]
}