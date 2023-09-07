import { DataTypeMarca } from "@/components/data-grid/marca/TableMarcas"
import { BaseService } from "./BaseService"


export class CategoriaService extends BaseService<DataTypeMarca> {
  constructor(){
    super("categorias")
  }
}
