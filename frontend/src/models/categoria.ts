
export type DataTypeCategoria = {
  [key: string]: any
  id: number
  nome: string
  dataCriacao: string
  dataAtualizacao: string
}

export function createEmptyCategoria(){
  let categoria: DataTypeCategoria = {
    id : 0,
    nome : "",
    dataCriacao : "",
    dataAtualizacao : ""
  }

  return categoria
}

export function getColumnsCategoria(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "NOME", uid: "nome", sortable: true },
    { name: "DATA CRIAÇÃO", uid: "dataCriacao" },
    { name: "DATA ATUALIZAÇÃO", uid: "dataAtualizacao" },
    { name: "ACTIONS", uid: "actions" }
  ]
}