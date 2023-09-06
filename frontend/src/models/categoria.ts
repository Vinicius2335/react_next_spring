
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