import { AXIOS } from "@/libs/axios"

import { DataTypePermissao } from "@/components/data-grid/permissoes/TablePermissoes"

export const PermissaoService = {
  getAll(): Promise<DataTypePermissao[]> {
    return AXIOS.get<DataTypePermissao[]>("/permissoes").then(resp => resp.data)
  },

  alterar(entity: Partial<DataTypePermissao>, id: number) {
    return AXIOS.put<DataTypePermissao>(`/permissoes/${id}`, entity).then(resp => resp.data)
  },

  inserir(entity: Partial<DataTypePermissao>) {
    return AXIOS.post<DataTypePermissao>("/permissoes", entity).then(resp => resp.data)
  },

  delete(id: number) {
    return AXIOS.delete(`/permissoes/${id}`).then(resp => resp.data)
  }
}
