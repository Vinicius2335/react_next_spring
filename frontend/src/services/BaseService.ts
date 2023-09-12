import { AXIOS } from "@/libs/axios"

export class BaseService<T> {
  url: string

  constructor(baseUrl: string){
    this.url = `/${baseUrl}`
  }
  
  getAll() : Promise<T[]> {
    return AXIOS.get<T[]>(this.url).then(resp => resp.data)
  }

  buscarPorId(id: number) : Promise<T> {
    return AXIOS.get<T>(this.url + "/" + id).then(resp => resp.data)
  }

  alterar(entity: Partial<T>, id: number) {
    return AXIOS.put<T>(this.url + "/" + id, entity).then(resp => resp.data)
  }

  inserir(entity: Partial<T>) {
    return AXIOS.post<T>(this.url, entity).then(resp => resp.data)
  }

  delete(id: number) {
    return AXIOS.delete(this.url + "/" + + id).then(resp => resp.data)
  }

  getUrl(){
    return this.url
  }
}