import NextAuth from "next-auth/next"

declare module "next-auth" {
  interface Session {
    refresh_tokne: string
    access_token: string
    user: User
  }
}

export interface Endereco {
  logradouro: string
  estado: string
  cidade: string
  cep: string
}

export interface Authority {
  authority: string
}

export interface User {
  id: number
  dataCriacao: string
  dataAtualizacao: string
  nome: string
  cpf: string
  email: string
  senha: string
  codigoRecuperacaoSenha?: any
  dataEnvioCodigo?: any
  endereco: Endereco
  enabled: boolean
  password: string
  authorities: Authority[]
  rolesString: string
  accountNonLocked: boolean
  username: string
  credentialsNonExpired: boolean
  accountNonExpired: boolean
}
