export type DataTypeMarca = {
  [key: string]: any
  id: number
  nome: string
  dataCriacao: string
  dataAtualizacao: string
}

export function createEmptyMarca(){
  const emptyMarca: DataTypeMarca = {
    id: 0,
    nome: "",
    dataCriacao: "",
    dataAtualizacao: ""
  }

  return emptyMarca
}

export function getColumnsMarca(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "NOME", uid: "nome", sortable: true },
    { name: "DATA CRIAÇÃO", uid: "dataCriacao" },
    { name: "DATA ATUALIZAÇÃO", uid: "dataAtualizacao" },
    { name: "ACTIONS", uid: "actions" }
  ]
}