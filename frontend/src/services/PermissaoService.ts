import { DataTypePermissao } from "@/components/data-grid/permissao/TablePermissoes"
import { BaseService } from "./BaseService"

export class PermissaoService extends BaseService<DataTypePermissao> {
  constructor() {
    super("permissoes")
  }
}
