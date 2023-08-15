export interface IEstado {
  [key: string]: any
  id: number
  sigla: string
  nome: string
}

export interface IEstadoResponse {
  sigla: string
  nome: string
  dataCriacao: Date
  dataAtualizacao: Date
}
