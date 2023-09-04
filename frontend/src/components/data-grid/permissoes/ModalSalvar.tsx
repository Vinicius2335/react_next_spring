import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader
} from "@nextui-org/react"
import { useMemo, useState } from "react"

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  nomePermissao: string
  onSetNome: (nome: string) => void
  onSalvarPressed: () => void
}

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  nomePermissao,
  onSetNome,
  onSalvarPressed
}: ModalSalvarProps) {
  const [invalido, setInvalido] = useState(true)

  const validateNome = useMemo(() => {
    if (nomePermissao !== "") {
      setInvalido(false)
      return "valid"
    } else {
      setInvalido(true)
      return "invalid"
    }
  }, [nomePermissao])

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
                  placeholder="Digite o nome da permissão"
                  variant="bordered"
                  value={nomePermissao}
                  onValueChange={e => onSetNome(e)}
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
                  onClick={onSalvarPressed}
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
