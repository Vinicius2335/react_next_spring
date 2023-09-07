/* eslint-disable react-hooks/exhaustive-deps */
import { DataTypeMarca, createEmptyMarca } from "@/models/marca";
import { DataTypePermissao } from "@/models/permissao";
import { MarcaService } from "@/services/MarcaService";
import { capitalize } from "@/services/utils";
import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalHeader
} from "@nextui-org/react";
import { useFormik } from "formik";
import React from "react";
import { toast } from "react-toastify";
import * as yup from "yup";

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  onClose: () => void
  marca: DataTypeMarca
  onSalvarPressed: () => void
}

const validationSchema = yup.object({
  nome: yup.string().required("Nome is required")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onClose,
  marca,
  onSalvarPressed
}: ModalSalvarProps) {
  const marcaService = new MarcaService()
  const text = "marca"

  let formik = useFormik({
    initialValues: {
      nome: marca.nome
    },

    validationSchema: validationSchema,

    onSubmit: values => {
      let entityToEdit = marca
      entityToEdit.nome = values.nome

      if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
        marcaService
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

        marcaService
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
    formik.initialValues.nome = marca.nome
  }, [marca])

  function onCloseModal(){
    marca = createEmptyMarca()
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
              <ModalHeader className="flex flex-col gap-1">Salvar Marca</ModalHeader>
              <ModalBody>
                <form onSubmit={formik.handleSubmit}>
                  <Input
                    autoFocus
                    label="Nome"
                    id="nome"
                    name="nome"
                    type="text"
                    placeholder="Digite um nome para a marca..."
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
                      variant="shadow"
                      className="hover:bg-success-200 hover:text-white"
                      onClick={onSalvarPressed}
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
