/* eslint-disable react-hooks/exhaustive-deps */
import { DataTypePermissao } from "@/models/permissao"
import { DataTypePessoa, EnderecoType, createEmptyPessoa } from "@/models/pessoa"
import { PermissaoService } from "@/services/PermissaoService"
import { PessoaService } from "@/services/PessoaService"
import { capitalize } from "@/services/utils"
import { Button, Input, Modal, ModalBody, ModalContent, ModalHeader, Select, SelectItem, Selection } from "@nextui-org/react"
import { Certificate } from "@phosphor-icons/react"
import { useFormik } from "formik"
import React from "react"
import { toast } from "react-toastify"
import * as yup from "yup"

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  onClose: () => void
  pessoa: DataTypePessoa
  onSalvarPressed: () => void
}

const regexp = new RegExp(/^\d{3}\.\d{3}\.\d{3}\-\d{2}$/)

const validationSchema = yup.object({
  nome: yup.string().required("Nome is required"),
  cpf: yup.string().matches(regexp, "CPF is invalid").required("CPF is requided"),
  email: yup.string().email("Email is invalid").required("Email is requided"),
  nomePermissao: yup.string().required("Permissão is required"),
  cep: yup.string().required("CEP is requided"),
  logradouro: yup.string().required("Logradouro is requided"),
  cidade: yup.string().required("Cidade is requided"),
  estado: yup.string().required("Estado is requided")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onClose,
  pessoa,
  onSalvarPressed
}: ModalSalvarProps) {
  const pessoaService = new PessoaService()
  const text = "pessoa"

  const [selectedPermissao, setSelectedPermissao] = React.useState<Selection>(new Set([]))
  let service = new PermissaoService()
  const [data, setData] = React.useState<DataTypePermissao[]>([])

  let formik = useFormik({
    initialValues: {
      nome: pessoa.nome,
      cpf: pessoa.cpf,
      email: pessoa.email,
      cep: pessoa.endereco.cep,
      nomePermissao: selectedPermissao.currentKey, // NOTE - PEGAR O NOME DA PERMISSAO AO EDITAR
      logradouro: pessoa.endereco.logradouro,
      cidade: pessoa.endereco.cidade,
      estado: pessoa.endereco.estado
    },

    validationSchema: validationSchema,

    onSubmit: values => {
      let entityToEdit = pessoa
      entityToEdit.nome = values.nome

      if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
        pessoaService
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
        let endereco: EnderecoType = {
          cep: values.cep,
          logradouro: values.logradouro,
          cidade: values.cidade,
          estado: values.estado
        }

        let entityToAdd: Partial<DataTypePessoa> = {
          nome: values.nome,
          senha: "Dale",
          cpf: values.cpf,
          nomePermissao: selectedPermissao.currentKey, // FIXME - problema no type 
          cep: values.cep,
          email: values.email,
          endereco: endereco
        }

        // selectedPermissao.currentKey

        pessoaService
          .inserir(entityToAdd)
          .then(() => {
            toast.success(`${capitalize(text)} criada com sucesso!`)
            onSalvarPressed()
            onCloseModal()
          })
          .catch(data => {
            toast.error(`Erro ao tentar salvar ${text}, tente novamente mais tarde!`)
            onCloseModal()
          })
      }
    }
  })

  function onCloseModal() {
    pessoa = createEmptyPessoa()
    formik.resetForm()
    setSelectedPermissao(new Set([]))
    onClose()
  }

  React.useEffect(() => {
    service.getAll().then((response) => {
      setData(response)
    })
  }, [])

  React.useEffect(() => {
    formik.initialValues.nome = pessoa.nome
    formik.initialValues.cpf = pessoa.cpf
    formik.initialValues.email = pessoa.email
    formik.initialValues.cep = pessoa.endereco.cep
    formik.initialValues.logradouro = pessoa.endereco.logradouro
    formik.initialValues.cidade = pessoa.endereco.cidade
    formik.initialValues.estado = pessoa.endereco.estado
    
  }, [pessoa])

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
              <ModalHeader className="flex flex-col gap-1">Salvar Pessoa</ModalHeader>
              <ModalBody>
                <form onSubmit={formik.handleSubmit} className="flex flex-col gap-3">
                  <Input
                    autoFocus
                    label="Nome"
                    id="nome"
                    name="nome"
                    type="text"
                    placeholder="Digite o seu nome completo..."
                    variant="bordered"
                    value={formik.values.nome}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.nome) ? "danger" : "success"}
                    errorMessage={formik.errors.nome}
                    validationState={Boolean(formik.errors.nome) ? "invalid" : "valid"}
                    isRequired
                  />

                  <Input
                    autoFocus
                    label="Cpf"
                    id="cpf"
                    name="cpf"
                    type="text"
                    maxLength={14}
                    placeholder="Digite o seu CPF..."
                    variant="bordered"
                    value={formik.values.cpf}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.cpf) ? "danger" : "success"}
                    errorMessage={formik.errors.cpf}
                    validationState={Boolean(formik.errors.cpf) ? "invalid" : "valid"}
                    isRequired
                  />

                  <Input
                    autoFocus
                    label="Email"
                    id="email"
                    name="email"
                    type="email"
                    placeholder="Digite o seu email..."
                    variant="bordered"
                    value={formik.values.email}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.email) ? "danger" : "success"}
                    errorMessage={formik.errors.email}
                    validationState={Boolean(formik.errors.email) ? "invalid" : "valid"}
                    isRequired
                  />

                  <Select
                    label="Permissão"
                    placeholder="Selecione uma permissão"
                    startContent={<Certificate size={23} />}
                    selectedKeys={selectedPermissao}
                    onSelectionChange={setSelectedPermissao}
                    variant="faded"
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.nomePermissao) ? "danger" : "success"}
                    errorMessage={formik.errors.nomePermissao}
                    validationState={Boolean(formik.errors.nomePermissao) ? "invalid" : "valid"}
                    isRequired
                  >
                    {data.map(permissao => (
                      <SelectItem key={permissao.nome} value={permissao.nome}>
                        {permissao.nome}
                      </SelectItem>
                    ))}
                  </Select>

                  <h2>Endereço</h2>
                  {/* NOTE - AUTOCOMPLITE SOMENTE PELO CEP */}

                  <Input
                    autoFocus
                    label="Cep"
                    id="cep"
                    name="cep"
                    type="text"
                    maxLength={9}
                    placeholder="Digite o seu cep..."
                    variant="bordered"
                    value={formik.values.cep}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.cep) ? "danger" : "success"}
                    errorMessage={formik.errors.cep}
                    validationState={Boolean(formik.errors.cep) ? "invalid" : "valid"}
                    isRequired
                  />

                  <Input
                    autoFocus
                    label="Logradouro"
                    id="logradouro"
                    name="logradouro"
                    type="text"
                    placeholder="Digite o seu logradouro..."
                    variant="bordered"
                    value={formik.values.logradouro}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    color={Boolean(formik.errors.logradouro) ? "danger" : "success"}
                    errorMessage={formik.errors.logradouro}
                    validationState={Boolean(formik.errors.logradouro) ? "invalid" : "valid"}
                    isRequired
                  />

                  <div className="flex py-2 px-1 justify-center gap-4">
                    <Input
                      autoFocus
                      label="Cidade"
                      id="cidade"
                      name="cidade"
                      type="text"
                      placeholder="Digite o seu cidade..."
                      variant="bordered"
                      value={formik.values.cidade}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                      color={Boolean(formik.errors.cidade) ? "danger" : "success"}
                      errorMessage={formik.errors.cidade}
                      validationState={Boolean(formik.errors.cidade) ? "invalid" : "valid"}
                      isRequired
                    />

                    <Input
                      autoFocus
                      label="Estado"
                      id="estado"
                      name="estado"
                      type="text"
                      placeholder="Digite o seu estado..."
                      variant="bordered"
                      value={formik.values.estado}
                      onChange={formik.handleChange}
                      onBlur={formik.handleBlur}
                      color={Boolean(formik.errors.estado) ? "danger" : "success"}
                      errorMessage={formik.errors.estado}
                      validationState={Boolean(formik.errors.estado) ? "invalid" : "valid"}
                      isRequired
                    />
                  </div>

                  <div className="flex py-2 px-1 justify-end gap-4">
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
