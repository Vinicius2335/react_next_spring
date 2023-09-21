import axios, { AxiosError } from "axios"
import { getSession } from "next-auth/react"
import { toast } from "react-toastify"

export const ApiClient = () => {
  const instance = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
      "Content-type": "application/json"
    }
  })

  instance.interceptors.request.use(
    async request => {
      const session = await getSession()

      if (session) {
        request.headers.Authorization = `Bearer ${session.access_token}`
      }

      return request
    },
    error => Promise.reject(error)
  )

  instance.interceptors.response.use(
    (response) => {
        return response
    },
    (error: AxiosError) => {
        if (error.response?.status == 401){
          toast.error("O usuário não possui autorização para acessar este recurso. 😥")
          return
        }
    }
  )

  return instance
}
