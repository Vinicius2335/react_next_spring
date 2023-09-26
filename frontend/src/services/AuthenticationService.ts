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

  public logout() {
    return ApiClient().post(this.url + "/logout").then(resp => resp.status)
  }
}
