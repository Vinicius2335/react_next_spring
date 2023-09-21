"use client"

import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Input,
  Select,
  SelectItem,
  Selection
} from "@nextui-org/react"
import { Envelope, LockKey } from "@phosphor-icons/react"
import { useRouter } from "next/navigation"
import React from "react"
import { PessoaGerenciamentoService } from "@/services/PessoaGerenciamentoService"
import { toast } from "react-toastify"
import { AxiosError } from "axios"

type AcaoType = "solicitar-codigo" | "alterar-senha"

export default function Senha() {
  const emailInputRef = React.useRef<HTMLInputElement>(null)
  const codigoInputRef = React.useRef<HTMLInputElement>(null)
  const senhaInputRef = React.useRef<HTMLInputElement>(null)

  const [loading, setloading] = React.useState(false)
  const [value, setValue] = React.useState<Selection>(new Set<AcaoType>(["solicitar-codigo"]))
  const [acaoSelecionada, setAcaoSelecionada] = React.useState<AcaoType>("solicitar-codigo")

  const pessoaGerenciamentoService = new PessoaGerenciamentoService()
  const router = useRouter()

  function handleOnSolicitarCodigo() {
    setloading(true)
    let email = emailInputRef.current!.value
    pessoaGerenciamentoService.solicitarCodigo(email).then(() => {
      setloading(false)
      setAcaoSelecionada("alterar-senha")
    })
  }

  function handleOnEnviarSenha() {
    let email = emailInputRef.current!.value
    let codigo = codigoInputRef.current!.value
    let senha = senhaInputRef.current!.value

    pessoaGerenciamentoService
      .alterarSenha(email, codigo, senha)
      .then(() => {
        toast.success("Senha salva com sucesso...")
        router.replace("/login")
      })
      .catch((erro: AxiosError) => {
        console.error(erro.response?.data)
        toast.error("Erro ao tentar salvar a senha...")
      })
  }

  const handleSelectionChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setValue(new Set([e.target.value]))
    setAcaoSelecionada(e.target.value as AcaoType)
  }

  return (
    <div className="flex justify-center items-center py-8 drop-shadow-lg shadow-primary">
      <Card className="w-[500px] p-5 drop-shadow-lg shadow-white">
        <CardHeader className="text-3xl !justify-center mx-auto">
          {
            acaoSelecionada === "alterar-senha" ? "Alterar Senha" : "Solicitar Código"
          }
        </CardHeader>

        <CardBody>
          <div className="flex items-center flex-col gap-5">
            <Select
              label="Selecionar Ação"
              variant="bordered"
              placeholder="Selecione uma ação"
              selectedKeys={value}
              onChange={handleSelectionChange}
              fullWidth
            >
              <SelectItem key={"solicitar-codigo"} value="solicitar-codigo">
                Solicitar Código
              </SelectItem>

              <SelectItem key={"alterar-senha"} value="alterar-senha">
                Alterar Senha
              </SelectItem>
            </Select>

            {acaoSelecionada === "alterar-senha" ? (
              <>
                <Input
                  autoFocus
                  label="Código"
                  placeholder="Digite o código..."
                  variant="bordered"
                  ref={codigoInputRef}
                />

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
                  autoFocus
                  endContent={
                    <LockKey className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
                  }
                  label="Senha"
                  placeholder="Digite sua senha..."
                  variant="bordered"
                  ref={senhaInputRef}
                />

                <Button
                  onClick={handleOnEnviarSenha}
                  color="primary"
                  variant="shadow"
                  className="hover:bg-primary-200"
                  fullWidth
                >
                  Salvar Senha
                </Button>
              </>
            ) : (
              <>
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

                <Button
                  onClick={handleOnSolicitarCodigo}
                  color={"primary"}
                  variant="shadow"
                  className="hover:bg-primary-200"
                  fullWidth
                  isLoading={loading}
                >
                  Solicitar Código
                </Button>
              </>
            )}

            {acaoSelecionada === "alterar-senha" ? (
              <p className="text-tiny text-zinc-500">
                OBS: Após solicitar um código, o insira junto com seu email e crie uma senha.
              </p>
            ) : (
              <p className="text-tiny text-zinc-500">
                OBS: Para criar uma senha ou caso esqueça ela, é necessário solicitar um código primeiro.
              </p>
            )}
          </div>
        </CardBody>
      </Card>
    </div>
  )
}
