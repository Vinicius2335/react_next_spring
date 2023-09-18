import { AXIOS } from "@/libs/axios"
import { DataTypePessoa } from "@/models/pessoa"
import { BaseService } from "./BaseService"
import { DataTypePermissao } from "@/models/permissao"

export class PessoaService extends BaseService<DataTypePessoa> {
  constructor() {
    super("pessoas")
  }

  getPermissao(id: number){
    return AXIOS.get<DataTypePermissao[]>(this.url + `/${id}/permissoes`).then(resp => resp.data)
  }
}
