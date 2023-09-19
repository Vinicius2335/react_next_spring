"use client"

import { Button, Card, CardBody, CardFooter, CardHeader, Input } from "@nextui-org/react";
import { Envelope, LockKey } from "@phosphor-icons/react";
import { useRouter } from "next/navigation";
import React from "react";
import { AuthenticationService } from '../../services/AuthenticationService';

interface LoginProps {
  onSuccessLogin: (autenticado: boolean) => void
}

export default function Login({ onSuccessLogin }: LoginProps){
  const emailInputRef = React.useRef<HTMLInputElement>(null)
  const senhaInputRef = React.useRef<HTMLInputElement>(null)

  const router = useRouter();

  const authenticationService = new AuthenticationService()

  function onSignIn(){
    let email = emailInputRef.current!.value
    let senha = senhaInputRef.current!.value

    // NOTE - REMOVER + PROPS
    // authenticationService.login(email, senha).then(() => {
    //   onSuccessLogin(true)
    // })

    authenticationService.login(email, senha).then(() => {
      router.push('/')
      router.refresh()
    })
    
  }

  function onCancel(){
    router.push('/user/produtos');
  }

  return (
    <div className='flex justify-center items-center py-8'>
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
            <Button  onClick={onCancel} color="default" variant="flat" className="hover:bg-default-300">
              Close
            </Button>
            <Button onClick={onSignIn} color="primary" variant="shadow" className="hover:bg-primary-200">
              Sign in
            </Button>
          </CardFooter>
        </Card>
    </div>
  )
}