import { ApiClient } from "@/libs/axios"

export type PessoaGerenciamentoRequestType = {
  email: string
  codigoParaRecuperarSenha: string
  senha: string
}

export class PessoaGerenciamentoService {
  url: string = "/gerenciamento"

  public solicitarCodigo(email: string) {
    return ApiClient().put(this.url + "/solicitar-codigo", null, {
      params: {
        email
      }
    }).then(resp => resp.status)
  }

  public alterarSenha(email: string, codigo: string, senha: string) {
    let request: PessoaGerenciamentoRequestType = {
      email,
      senha,
      codigoParaRecuperarSenha: codigo
    }

    return ApiClient().put(this.url + "/alterar-senha", request).then(resp => resp.status)
  }
}
