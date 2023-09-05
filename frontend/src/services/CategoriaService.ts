import { DataTypeMarca } from "@/components/data-grid/marca/TableMarcas"
import { AXIOS } from "@/libs/axios"


export const CategoriaService = {
  getAll(): Promise<DataTypeMarca[]> {
    return AXIOS.get<DataTypeMarca[]>("/categorias").then(resp => resp.data)
  },

  alterar(entity: Partial<DataTypeMarca>, id: number) {
    return AXIOS.put<DataTypeMarca>(`/categorias/${id}`, entity).then(resp => resp.data)
  },

  inserir(entity: Partial<DataTypeMarca>) {
    return AXIOS.post<DataTypeMarca>("/categorias", entity).then(resp => resp.data)
  },

  delete(id: number) {
    return AXIOS.delete(`/categorias/${id}`).then(resp => resp.data)
  }
}
