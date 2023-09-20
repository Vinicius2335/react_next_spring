import { DataTypeMarca } from "@/models/marca"
import { BaseService } from "../BaseService"

export class MarcaService extends BaseService<DataTypeMarca> {
  constructor() {
    super("marcas")
  }
}
