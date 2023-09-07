import { DataTypePessoa } from "@/models/pessoa"
import { BaseService } from "./BaseService"

export class PessoaService extends BaseService<DataTypePessoa> {
  constructor() {
    super("pessoas")
  }
}
