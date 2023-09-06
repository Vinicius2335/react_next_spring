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