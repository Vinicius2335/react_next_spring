/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import { DataTypeCategoria, createEmptyCategoria } from "@/models/categoria"
import { CategoriaService } from "@/services/cadastros/CategoriaService"
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
  categoria: DataTypeCategoria
  onSalvarPressed: () => void
}

const validationSchema = yup.object({
  nome: yup.string().required("Nome é obrigatório.")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onClose,
  categoria,
  onSalvarPressed
}: ModalSalvarProps) {
  const categoriaService = new CategoriaService()
  const text = "categoria"

  let formik = useFormik({
    initialValues: {
      nome: categoria.nome
    },

    validationSchema: validationSchema,

    onSubmit: values => {
      let entityToEdit = categoria
      entityToEdit.nome = values.nome

      if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
        categoriaService
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
        let entityToAdd: Partial<DataTypeCategoria> = {
          nome: values.nome
        }

        categoriaService
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
    formik.initialValues.nome = categoria.nome
  }, [categoria])

  function onCloseModal() {
    categoria = createEmptyCategoria()
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
              <ModalHeader className="flex flex-col gap-1">Salvar Categoria</ModalHeader>
              <ModalBody>
                <form id="formCategoria" onSubmit={formik.handleSubmit}>
                  <Input
                    autoFocus
                    label="Nome"
                    id="nome"
                    name="nome"
                    type="text"
                    placeholder="Digite um nome para a categoria..."
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
                  form="formCategoria"
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
