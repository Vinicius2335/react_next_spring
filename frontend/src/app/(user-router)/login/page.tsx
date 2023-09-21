/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import { Button, Card, CardBody, CardFooter, CardHeader, Input, Spinner } from "@nextui-org/react"
import { Envelope, LockKey } from "@phosphor-icons/react"
import { redirect, useRouter } from "next/navigation"
import React from "react"
import { AuthenticationService } from "../../../services/AuthenticationService"
import { useGlobalContext } from "@/components/GlobalContext"
import { signIn, useSession } from "next-auth/react"

export default function Login() {
  const emailInputRef = React.useRef<HTMLInputElement>(null)
  const senhaInputRef = React.useRef<HTMLInputElement>(null)

  const router = useRouter()

  const authenticationService = new AuthenticationService()
  // const { setAutenticado } = useGlobalContext()
  const { data: session, status } = useSession()
  const [isLoading, setIsLoading] = React.useState(true)

  async function handleLogin() {
    let email = emailInputRef.current!.value
    let password = senhaInputRef.current!.value

    // authenticationService.login(email, password).then(() => {
    //   setAutenticado(true)
    //   router.push("/")
    // })

    const result = await signIn("credentials", {
      email,
      password,
      redirect: false // não queremos redirecionar automaticamente para uma pagina especifica ao fazer o login, dá mais liberdade para criar alertas por exemplo tbm
    })

    if (result?.error) {
      console.error(result)
      return
    }

    // setAutenticado(true)
    router.replace("/")
  }

  function onCancel() {
    router.push("/user/produtos")
  }

  React.useEffect(() => {
    if (status === "unauthenticated") {
      setIsLoading(false)
    }

    if (status === "authenticated") {
      redirect("/")
    }
  }, [session])

  return (
    <div>
      {isLoading ? (
        <Spinner size="lg" />
      ) : (
        <div className="flex justify-center items-center py-8">
          <Card className="w-[500px] max-h-[400px] p-5 ">
            <CardHeader className="text-3xl !justify-center mx-auto">Login</CardHeader>

            <CardBody className="flex items-center flex-col gap-5">
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
                onClick={handleLogin}
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
