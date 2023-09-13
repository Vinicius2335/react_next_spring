import { DataTypeProdutoImage } from '@/models/produto-image';
import { BaseService } from './BaseService';

export class ProdutoImageService extends BaseService<DataTypeProdutoImage> {
  constructor(){
    super("imagens")
  }
}