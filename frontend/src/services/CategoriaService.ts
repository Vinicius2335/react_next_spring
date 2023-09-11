import { DataTypeCategoria } from "@/models/categoria"
import { BaseService } from "./BaseService"

export class CategoriaService extends BaseService<DataTypeCategoria> {
  constructor() {
    super("categorias")
  }
}
