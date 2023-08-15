import { AXIOS } from "../../libs/axios"
import { IEstado, IEstadoResponse } from "../../models/Estado"

export const EstadoService = {
  getAllEstados() {
    return AXIOS.get<IEstado[]>("/api/estados").then(resp => resp.data)
  },

  alterar(estado: Partial<IEstado>, id: number) {
    return AXIOS.put<IEstadoResponse>(`api/estados/${id}`, estado).then(resp => resp.data)
  },

  inserir(estado: Partial<IEstado>) {
    return AXIOS.post<IEstadoResponse>("/api/estados", estado).then(resp => resp.data)
  },

  delete(id: number) {
    return AXIOS.delete(`/api/estados/${id}`).then(resp => resp.data)
  }
}
