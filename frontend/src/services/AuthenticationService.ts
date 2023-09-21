import { ApiClient } from "@/libs/axios"
import { AxiosError } from "axios"
import { toast } from "react-toastify"

export type AuthenticationResponseType = {
  access_token: string
  refresh_token: string
}

export type AuthenticationRequestType = {
  email: string
  password: string
}

export type ExceptionType = {
  error: string
  message: string
  path: string
  status: number
}

export class AuthenticationService {
  readonly url = "/auth"

  public login(email: string, senha: string) {
    let authenticationRequest: AuthenticationRequestType = {
      email: email,
      password: senha
    }

    return ApiClient().post<AuthenticationResponseType>(this.url + "/login", authenticationRequest)
      .then(resp => {
        localStorage.setItem("ACCESS_TOKEN", resp.data.access_token)
      })
      .catch((error: AxiosError<ExceptionType>) => {
        const errorMessage = error.response?.data.message
        if (errorMessage === "Bad credentials") {
          toast.error("Email ou Senha Inválido")
        } else {
          toast.error(errorMessage)
        }
      })
  }

  public logout() {
    return ApiClient().post(this.url + "/logout").then(resp => resp.status)
  }

  public isUserAuthenticated() {
    if (typeof window !== "undefined" && localStorage.getItem("ACCESS_TOKEN") != null) {
      return true
    } else {
      return false
    }
  }
}
