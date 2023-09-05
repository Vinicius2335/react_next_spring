import { DataTypeMarca } from "@/components/data-grid/marca/TableMarcas"
import { AXIOS } from "@/libs/axios"


export const MarcaService = {
  getAll(): Promise<DataTypeMarca[]> {
    return AXIOS.get<DataTypeMarca[]>("/marcas").then(resp => resp.data)
  },

  alterar(entity: Partial<DataTypeMarca>, id: number) {
    return AXIOS.put<DataTypeMarca>(`/marcas/${id}`, entity).then(resp => resp.data)
  },

  inserir(entity: Partial<DataTypeMarca>) {
    return AXIOS.post<DataTypeMarca>("/marcas", entity).then(resp => resp.data)
  },

  delete(id: number) {
    return AXIOS.delete(`/marcas/${id}`).then(resp => resp.data)
  }
}
