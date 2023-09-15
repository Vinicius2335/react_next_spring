import { DataTypeProdutoImage } from '@/models/produto-image';
import { BaseService } from './BaseService';
import { AXIOS } from '@/libs/axios';
import { ProdutoImagemResponse } from '@/models/produto-image-response';

export class ProdutoImageService extends BaseService<DataTypeProdutoImage> {
  constructor(){
    super("produtos/{idProduto}/imagens")
  }

  getEndpoint(idProduto: number){
    return this.url.replace("{idProduto}", idProduto.toString())
  }

  uploadImage(idProduto: number, image: File){
    const formData = new FormData()
    formData.append("file", image)

    const configHeader = {
      headers : {
        'content-type':'multipart/form-data'
      } 
    }

    return AXIOS.post(this.getEndpoint(idProduto), formData, configHeader).then(resp => resp.data)
  }

  buscarImagemPorProduto(idProduto: number){
    return AXIOS.get<ProdutoImagemResponse[]>(this.getEndpoint(idProduto)).then(resp => resp.data)
  }
}