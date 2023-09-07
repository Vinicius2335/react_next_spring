import { DataTypeMarca } from "@/components/data-grid/marca/TableMarcas"
import { BaseService } from "./BaseService"


export class MarcaService extends BaseService<DataTypeMarca> {
  constructor(){
    super("marcas")
  }
}
