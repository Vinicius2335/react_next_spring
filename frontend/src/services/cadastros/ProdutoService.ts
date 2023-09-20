import { DataTypeProduto } from "@/models/produto";
import { BaseService } from "../BaseService";

export class ProdutoService extends BaseService<DataTypeProduto>{
  constructor(){
    super("produtos")
  }
}