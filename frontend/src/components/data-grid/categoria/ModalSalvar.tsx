"use client"

import { DataTypeCategoria, createCategoria } from "@/models/categoria"
import { CategoriaService } from "@/services/CategoriaService"
import { capitalize } from "@/services/utils"
import { Button, Input, Modal, ModalBody, ModalContent, ModalHeader } from "@nextui-org/react"
import { useFormik } from "formik"
import React from "react"
import { toast } from "react-toastify"
import * as yup from "yup"

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  categoria: DataTypeCategoria
  onSalvarPressed: () => void
}

const validationSchema = yup.object({
  nome: yup.string().required("Nome is required")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
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
            reset(values)
            onSalvarPressed()
          })
          .catch(() => {
            toast.error(`Erro ao tentar editar ${text}, tente novamente mais tarde!`)
            reset(values)
          })
      } else {
        let entityToAdd: Partial<DataTypeCategoria> = {
          nome: values.nome
        }

        categoriaService
          .inserir(entityToAdd)
          .then(() => {
            toast.success(`${capitalize(text)} criada com sucesso!`)
            reset(values)
            onSalvarPressed()
          })
          .catch(() => {
            toast.error(`Erro ao tentar salvar ${text}, tente novamente mais tarde!`)
            reset(values)
          })
      }
    }
  })

  function reset(values: any){
    values.nome = ""
  }

  React.useEffect(() => {
    formik.initialValues.nome = categoria.nome
    if(formik.initialValues.nome){
      formik.errors.nome = undefined
    } else {
      formik.errors.nome = ""
    }
  }, [categoria])

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
              <ModalHeader className="flex flex-col gap-1">Salvar Categoria</ModalHeader>
              <ModalBody>
                <form onSubmit={formik.handleSubmit}>
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

                  <div className="flex py-2 px-1 justify-end mt-10 gap-4">
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
                      className="hover:bg-success-200 hover:text-white"
                      onClick={onSalvarPressed}
                      onPress={onClose}
                      isDisabled={!formik.isValid}
                    >
                      Salvar
                    </Button>
                  </div>
                </form>
              </ModalBody>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}