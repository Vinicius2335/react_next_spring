import { DataTypeMarca } from "@/models/marca"
import { BaseService } from "./BaseService"


export class CategoriaService extends BaseService<DataTypeMarca> {
  constructor(){
    super("categorias")
  }
}
