/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import { getSessionUtil } from "@/app/api/auth/[...nextauth]/utils"
import { Button, Card, CardBody, CardFooter, CardHeader, Input, Link, Spinner } from "@nextui-org/react"
import { Envelope, LockKey } from "@phosphor-icons/react"
import { signIn } from "next-auth/react"
import { useRouter } from "next/navigation"
import React, { FormEvent } from "react"
import { toast } from "react-toastify"

export default function Login() {
  const emailInputRef = React.useRef<HTMLInputElement>(null)
  const senhaInputRef = React.useRef<HTMLInputElement>(null)

  const [isLoading, setIsLoading] = React.useState(true)

  const router = useRouter()

  async function handleLogin(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    let email = emailInputRef.current!.value
    let password = senhaInputRef.current!.value

    const result = await signIn("credentials", {
      email,
      password,
      redirect: false
    })

    if (result?.error) {
      toast.error("Login ou Senha inválidos. 😞")
      return
    }

    getSessionUtil().then(session => {
      
      if (session) {
        const possuiPermissao = session.roles.match("ROLE_ADMIN") != null || session.roles.match("ROLE_GERENTE") != null  
        if (possuiPermissao){
          router.replace("/dashboard")
        } else {
          router.replace("/")
        }
      }
    })
  }

  function onCancel() {
    router.push("/")
  }

  React.useEffect(() => {
    getSessionUtil().then(session => {
      if (session) {
        router.replace("/")
      }

      setIsLoading(false)
    })
  }, [])

  return (
    <div>
      {isLoading ? (
        <Spinner size="lg" />
      ) : (
        <div className="flex justify-center items-center py-8">
          <Card className="w-[500px] max-h-[400px] p-5 ">
            <CardHeader className="text-3xl !justify-center mx-auto">Login</CardHeader>

            <CardBody>
              <form
                id="form-login"
                onSubmit={handleLogin}
                className="flex items-center flex-col gap-5"
              >
                <Input
                  autoFocus
                  endContent={
                    <Envelope className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
                  }
                  label="Email"
                  placeholder="Digite o seu email"
                  variant="bordered"
                  ref={emailInputRef}
                />

                <Input
                  endContent={
                    <LockKey className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
                  }
                  label="Senha"
                  placeholder="Digite a sua senha"
                  type="password"
                  variant="bordered"
                  ref={senhaInputRef}
                />

                <div className="flex py-2 px-1 justify-end">
                  <Link color="primary" href="/senha" size="sm">
                    Esqueceu/Criar Senha?
                  </Link>
                </div>
              </form>
            </CardBody>

            <CardFooter className="flex items-center justify-end gap-2">
              <Button
                onClick={onCancel}
                color="default"
                variant="flat"
                className="hover:bg-default-300"
              >
                Close
              </Button>
              <Button
                type="submit"
                form="form-login"
                color="primary"
                variant="shadow"
                className="hover:bg-primary-200"
              >
                Sign in
              </Button>
            </CardFooter>
          </Card>
        </div>
      )}
    </div>
  )
}
