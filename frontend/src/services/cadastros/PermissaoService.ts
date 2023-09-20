import { DataTypePermissao } from "@/models/permissao"
import { BaseService } from "../BaseService"

export class PermissaoService extends BaseService<DataTypePermissao> {
  constructor() {
    super("permissoes")
  }
}
