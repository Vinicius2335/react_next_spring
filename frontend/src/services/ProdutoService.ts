import { DataTypeProduto } from "@/models/produto";
import { BaseService } from "./BaseService";
import { AXIOS } from "@/libs/axios";
import { ProdutoImagemResponse } from "@/models/produto-image-response";

export class ProdutoService extends BaseService<DataTypeProduto>{
  constructor(){
    super("produtos")
  }

  uploadImage(idProduto: number, image: File){
    const formData = new FormData()
    formData.append("file", image)

    const configHeader = {
      headers : {
        'content-type':'multipart/form-data'
      } 
    }

    return AXIOS.post(this.url + `/${idProduto}/image/`, formData, configHeader).then(resp => resp.data)
  }

  buscarImagemPorProduto(idProduto: number){
    return AXIOS.get<ProdutoImagemResponse[]>(this.url + `/${idProduto}/imagens`).then(resp => resp.data)
  }
}