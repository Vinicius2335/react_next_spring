import NextAuth from "next-auth/next"

declare module "next-auth" {
  interface Session {
    refresh_tokne: string
    access_token: string
    user: User
    roles: string
  }
}

interface User {
	nome: string;
	cpf: string;
	email: string;
	senha: string;
	endereco: Endereco;
	dataCriacao: string;
	dataAtualizacao: string;
}

interface Endereco {
	logradouro: string;
	cidade: string;
	estado: string;
	cep: string;
}