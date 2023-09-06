import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader
} from "@nextui-org/react"
import { useEffect, useMemo, useState } from "react"
import { PermissaoService } from '@/services/PermissaoService';
import { capitalize } from "@/services/utils";
import { toast } from "react-toastify";
import React from "react";
import { DataTypePermissao } from "@/models/permissao";

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  permissao: DataTypePermissao
  onSalvarPressed: () => void
}

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onSalvarPressed,
  permissao
}: ModalSalvarProps) {
  const [invalido, setInvalido] = useState(true)
  const [permissaoNome, setPermissaoNome] = useState("")
  const permissaoService: PermissaoService = new PermissaoService()
  const text = "permissão"

  const validateNome = useMemo(() => {
    if (permissaoNome !== "") {
      setInvalido(false)
      return "valid"
    } else {
      setInvalido(true)
      return "invalid"
    }
  }, [permissaoNome])

  useEffect(() => {
    setPermissaoNome(permissao.nome)
  }, [permissao])

  function onSalvar() {
    let entityToEdit = permissao
    entityToEdit.nome = permissaoNome

    if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
      permissaoService
        .alterar(entityToEdit, entityToEdit.id)
        .then(() => {
          toast.success(`${capitalize(text)} editada com sucesso!`)
          reset()
          onSalvarPressed()
        })
        .catch(() => {
          toast.error(`Erro ao tentar editar ${text}, tente novamente mais tarde!`)
          reset()
        })
    } else {
      let entityToAdd: Partial<DataTypePermissao> = {
        nome: permissaoNome
      }
  
      permissaoService
        .inserir(entityToAdd)
        .then(() => {
          toast.success(`${capitalize(text)} criada com sucesso!`)
          reset()
          onSalvarPressed()
        })
        .catch(() => {
          toast.error(`Erro ao tentar salvar ${text}, tente novamente mais tarde!`)
          reset()
        })
    }
  }

  function reset(){
    setPermissaoNome("")
    setInvalido(true)
  }

  return (
    <>
      <Modal
        isOpen={isOpen}
        onOpenChange={onOpenChange}
        placement="top-center"
        scrollBehavior="inside"
        size="md"
        backdrop="blur"
      >

        <ModalContent>
          {onClose => (
            <>
              <ModalHeader className="flex flex-col gap-1">Salvar Permissão</ModalHeader>
              <ModalBody>
                <Input
                  autoFocus
                  label="Nome"
                  placeholder="Digite um nome para permissão..."
                  variant="bordered"
                  value={permissaoNome}
                  onValueChange={e => setPermissaoNome(e)}
                  color={validateNome === "invalid" ? "danger" : "success"}
                  errorMessage={validateNome === "invalid" && "Porfavor insira um nome"}
                  validationState={validateNome}
                  isRequired
                  isClearable
                />
              </ModalBody>

              <ModalFooter>
                <Button
                  color="secondary"
                  variant="flat"
                  onPress={onClose}
                  className="hover:bg-secondary-400 hover:text-white"
                >
                  Cancelar
                </Button>
                <Button
                  color="success"
                  onPress={onClose}
                  className="hover:bg-success-200 hover:text-white"
                  onClick={onSalvar}
                  isDisabled={invalido}
                >
                  Salvar
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}