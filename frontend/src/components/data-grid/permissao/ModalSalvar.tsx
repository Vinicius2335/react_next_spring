/* eslint-disable react-hooks/exhaustive-deps */
import { DataTypePermissao, createEmptyPermissao } from "@/models/permissao"
import { PermissaoService } from "@/services/PermissaoService"
import { capitalize } from "@/services/utils"
import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader
} from "@nextui-org/react"
import { useFormik } from "formik"
import React from "react"
import { toast } from "react-toastify"
import * as yup from "yup"

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  onClose: () => void
  permissao: DataTypePermissao
  onSalvarPressed: () => void
}

const validationSchema = yup.object({
  nome: yup.string().required("Nome é obrigatório.")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onClose,
  permissao,
  onSalvarPressed
}: ModalSalvarProps) {
  const permissaoService = new PermissaoService()
  const text = "permissão"

  let formik = useFormik({
    initialValues: {
      nome: permissao.nome
    },

    validationSchema: validationSchema,

    onSubmit: values => {
      let entityToEdit = permissao
      entityToEdit.nome = values.nome

      if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
        permissaoService
          .alterar(entityToEdit, entityToEdit.id)
          .then(() => {
            toast.success(`${capitalize(text)} editada com sucesso!`)
            onSalvarPressed()
            onCloseModal()
          })
          .catch(() => {
            toast.error(`Erro ao tentar editar ${text}, tente novamente mais tarde!`)
            onCloseModal()
          })
      } else {
        let entityToAdd: Partial<DataTypePermissao> = {
          nome: values.nome
        }

        permissaoService
          .inserir(entityToAdd)
          .then(() => {
            toast.success(`${capitalize(text)} criada com sucesso!`)
            onSalvarPressed()
            onCloseModal()
          })
          .catch(() => {
            toast.error(`Erro ao tentar salvar ${text}, tente novamente mais tarde!`)
            onCloseModal()
          })
      }
    }
  })

  React.useEffect(() => {
    formik.initialValues.nome = permissao.nome
  }, [permissao])

  function onCloseModal() {
    permissao = createEmptyPermissao()
    formik.resetForm()
    onClose()
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
        onClose={onCloseModal}
      >
        <ModalContent>
          {onClose => (
            <>
              <ModalHeader className="flex flex-col gap-1">Salvar Permissao</ModalHeader>
              <ModalBody>
                <form id="formPermissao" onSubmit={formik.handleSubmit}>
                  <Input
                    autoFocus
                    label="Nome"
                    id="nome"
                    name="nome"
                    type="text"
                    placeholder="Digite um nome para a permissao..."
                    variant="bordered"
                    value={formik.values.nome}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.nome) ? "danger" : "success"}
                    errorMessage={formik.errors.nome}
                    validationState={Boolean(formik.errors.nome) ? "invalid" : "valid"}
                    isRequired
                  />
                </form>
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
                  type="submit"
                  form="formPermissao"
                  variant="shadow"
                  className="hover:bg-success-200 hover:text-white"
                  isDisabled={!formik.isValid}
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
