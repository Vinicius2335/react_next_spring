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