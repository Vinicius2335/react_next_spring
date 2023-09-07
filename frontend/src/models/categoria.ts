
export type DataTypeCategoria = {
  [key: string]: any
  id: number
  nome: string
  dataCriacao: string
  dataAtualizacao: string
}

export function createCategoria(nome: string){
  let categoria: DataTypeCategoria = {
    id : 0,
    nome : nome,
    dataCriacao : "",
    dataAtualizacao : ""
  }

  return categoria
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

export function getColumns(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "NOME", uid: "nome", sortable: true },
    { name: "DATA CRIAÇÃO", uid: "dataCriacao" },
    { name: "DATA ATUALIZAÇÃO", uid: "dataAtualizacao" },
    { name: "ACTIONS", uid: "actions" }
  ]
}