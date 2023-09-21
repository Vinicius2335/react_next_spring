import { AXIOS } from "@/libs/axios"

export type PessoaGerenciamentoRequestType = {
  email: string
  codigoParaRecuperarSenha: string
  senha: string
}

export class PessoaGerenciamentoService {
  url: string = "/gerenciamento"

  public solicitarCodigo(email: string){
    return AXIOS.put(this.url + "/solicitar-codigo", null, {params: {
      email
    }}).then(resp => resp.status)
  }

  public alterarSenha(email: string, codigo: string, senha: string){
    let request: PessoaGerenciamentoRequestType = {
        email,
        senha,
        codigoParaRecuperarSenha: codigo
    }

    return AXIOS.put(this.url + "/alterar-senha", request).then(resp => resp.status)
  }
}
