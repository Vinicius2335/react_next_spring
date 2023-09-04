import { AXIOS } from "@/libs/axios"

import { DataTypePermissao } from "@/components/data-grid/permissoes/TablePermissoes"

export const PermissaoService = {
  getAllPermissoes(): Promise<DataTypePermissao[]> {
    return AXIOS.get<DataTypePermissao[]>("/permissoes").then(resp => resp.data)
  },

  alterar(permissao: Partial<DataTypePermissao>, id: number) {
    return AXIOS.put<DataTypePermissao>(`/permissoes/${id}`, permissao).then(resp => resp.data)
  },

  inserir(permissao: Partial<DataTypePermissao>) {
    return AXIOS.post<DataTypePermissao>("/permissoes", permissao).then(resp => resp.data)
  },

  delete(id: number) {
    return AXIOS.delete(`/permissoes/${id}`).then(resp => resp.data)
  }
}
