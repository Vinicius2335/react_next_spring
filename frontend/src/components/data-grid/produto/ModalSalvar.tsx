/* eslint-disable react-hooks/exhaustive-deps */
import { DataTypeCategoria } from "@/models/categoria"
import { DataTypeMarca } from "@/models/marca"
import { CategoriaType, DataTypeProduto, MarcaType, createEmptyProduto } from "@/models/produto"
import { CategoriaService } from "@/services/CategoriaService"
import { MarcaService } from "@/services/MarcaService"
import { ProdutoService } from "@/services/ProdutoService"
import { capitalize } from "@/services/utils"
import {
  Button,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalHeader,
  Textarea,
  Select,
  SelectItem,
  Selection,
  ModalFooter
} from "@nextui-org/react"
import { useFormik } from "formik"
import React from "react"
import { toast } from "react-toastify"
import * as yup from "yup"
import CurrencyFormat, { Values } from "react-currency-format"

interface ModalSalvarProps {
  isOpen: boolean
  onOpenChange: () => void
  onClose: () => void
  produto: DataTypeProduto
  onSetProduto: (produto: DataTypeProduto) => void
  onSalvarPressed: () => void
}

const validationSchema = yup.object({
  quantidade: yup.number().required("Quantidade é obrigatório."),
  descricao: yup.string().required("Descrição é obrigatório."),
  detalhe: yup
    .string()
    .max(255, "Número máximo de caracteres é 255.")
    .required("Detalhes é obrigatório."),
  custo: yup
    .number()
    .transform(value => (Number.isNaN(value) ? 0 : value))
    .moreThan(0, "Valor de Custo não pode ser menor que 0.")
    .required("Valor de Custo é obrigatório."),
  venda: yup
    .number()
    .transform(value => (Number.isNaN(value) ? 0 : value))
    .moreThan(0, "Valor de Venda não pode ser menor que 0.")
    .required("Valor de Venda é obrigatório."),
  marca: yup.string().required("Marca é obrigatório."),
  categoria: yup.string().required("Categoria é obrigatório.")
})

export default function ModalSalvar({
  isOpen,
  onOpenChange,
  onClose,
  produto,
  onSetProduto,
  onSalvarPressed
}: ModalSalvarProps) {
  const produtoService = new ProdutoService()
  const marcaService = new MarcaService()
  const categoriaService = new CategoriaService()
  const text = "produto"
  const [marcas, setMarcas] = React.useState<DataTypeMarca[]>([])
  const [categorias, setCategorias] = React.useState<DataTypeCategoria[]>([])
  const [selectedMarca, setSelectedMarca] = React.useState<Selection>(new Set([]))
  const [selectedCategoria, setSelectedCategoria] = React.useState<Selection>(new Set([]))
  const [idMarca, setIdMarca] = React.useState(0)
  const [idCategoria, setIdCategoria] = React.useState(0)

  let formik = useFormik({
    initialValues: {
      quantidade: produto.quantidade,
      descricao: produto.descricaoCurta,
      detalhe: produto.descricaoDetalhada,
      custo: produto.valorCusto,
      venda: produto.valorVenda,
      marca: "",
      categoria: ""
    },

    validationSchema: validationSchema,

    onSubmit: values => {
      let entityToEdit = produto

      entityToEdit.quantidade = values.quantidade
      entityToEdit.descricaoCurta = values.descricao
      entityToEdit.descricaoDetalhada = values.detalhe
      entityToEdit.valorCusto = values.custo
      entityToEdit.valorVenda = values.venda
      entityToEdit.marca.id = idMarca
      entityToEdit.categoria.id = idCategoria

      if (typeof entityToEdit.id !== "undefined" && entityToEdit.id != 0) {
        produtoService
          .alterar(entityToEdit, entityToEdit.id)
          .then(() => {
            toast.success(`${capitalize(text)} editada com sucesso!`)
            onSalvarPressed()
            onCloseModal()
          })
          .catch(error => {
            toast.error(`Erro ao tentar editar ${text}, tente novamente mais tarde!`)
            console.error(error)
            onCloseModal()
          })
      } else {
        let addMarca: MarcaType = {
          id: idMarca
        }

        let addCategoria: CategoriaType = {
          id: idCategoria
        }

        let entityToAdd: Partial<DataTypeProduto> = {
          quantidade: values.quantidade,
          descricaoCurta: values.descricao,
          descricaoDetalhada: values.detalhe,
          valorCusto: values.custo,
          valorVenda: values.venda,
          marca: addMarca,
          categoria: addCategoria
        }

        produtoService
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
    onSetProduto(createEmptyProduto())
    formik.resetForm()
    formik.values.marca = ""
    formik.values.categoria = ""
    setIdMarca(0)
    setIdCategoria(0)
    setSelectedCategoria(new Set([]))
    setSelectedMarca(new Set([]))
    onClose()
  }

  const handleSelectMarcaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    marcas.forEach(marca => {
      if (marca.nome == e.target.value) {
        setSelectedMarca(new Set([e.target.value]))
        setIdMarca(marca.id)
        formik.values.marca = e.target.value
      }
    })
  }

  const handleSelectCategoriaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    categorias.forEach(categoria => {
      if (categoria.nome == e.target.value) {
        setSelectedCategoria(new Set([e.target.value]))
        setIdCategoria(categoria.id)
        formik.values.categoria = e.target.value
      }
    })
  }

  const handleVendaChange = (values: Values) => {
    const { floatValue, value } = values
    if (value === "") {
      formik.values.venda = 0
    } else {
      formik.values.venda = floatValue
    }
  }

  const handleCustoChange = (values: Values) => {
    const { floatValue, value } = values
    if (value === "") {
      formik.values.custo = 0
    } else {
      formik.values.custo = floatValue
    }
  }

  React.useEffect(() => {
    marcaService.getAll().then(response => {
      setMarcas(response)
    })
    categoriaService.getAll().then(response => {
      setCategorias(response)
    })
  }, [])

  React.useEffect(() => {
    formik.initialValues.quantidade = produto.quantidade
    formik.initialValues.descricao = produto.descricaoCurta
    formik.initialValues.detalhe = produto.descricaoDetalhada
    formik.initialValues.custo = produto.valorCusto
    formik.initialValues.venda = produto.valorVenda

    if (produto.id != 0) {
      marcaService.buscarPorId(produto.marca.id).then(marca => {
        setSelectedMarca(new Set([marca.nome]))
        setIdMarca(marca.id)
        formik.values.marca = marca.nome
      })

      categoriaService.buscarPorId(produto.categoria.id).then(categoria => {
        setSelectedCategoria(new Set([categoria.nome]))
        setIdCategoria(categoria.id)
        formik.values.categoria = categoria.nome
      })
    }
  }, [produto])

  return (
    <>
      <Modal
        isOpen={isOpen}
        onOpenChange={onOpenChange}
        placement="top-center"
        scrollBehavior="inside"
        size="4xl"
        backdrop="blur"
        onClose={onCloseModal}
        isDismissable={false}
      >
        <ModalContent>
          {onClose => (
            <>
              <ModalHeader className="flex flex-col gap-1">Salvar Produto</ModalHeader>
              <ModalBody>
                <form id="formProduto" onSubmit={formik.handleSubmit} className="flex flex-col gap-3">
                  <div className="flex w-full gap-4">
                    <div className="w-[50%] flex flex-col gap-3">
                      <Input
                        autoFocus
                        label="Quantidade"
                        id="quantidade"
                        name="quantidade"
                        type="number"
                        placeholder="Digite a quantidade de produtos..."
                        variant="bordered"
                        value={formik.values.quantidade.toString()}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        color={Boolean(formik.errors.quantidade) ? "danger" : "success"}
                        errorMessage={formik.errors.quantidade}
                        validationState={Boolean(formik.errors.quantidade) ? "invalid" : "valid"}
                        isRequired
                      />

                      <Input
                        label="Descrição"
                        id="descricao"
                        name="descricao"
                        type="descricao"
                        placeholder="Digite o nome do produto..."
                        variant="bordered"
                        value={formik.values.descricao}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        color={Boolean(formik.errors.descricao) ? "danger" : "success"}
                        errorMessage={formik.errors.descricao}
                        validationState={Boolean(formik.errors.descricao) ? "invalid" : "valid"}
                        isRequired
                      />

                      <Textarea
                        label="Detalhe"
                        id="detalhe"
                        name="detalhe"
                        variant="bordered"
                        maxLength={255}
                        placeholder="Digite detalhes sobre o produto..."
                        value={formik.values.detalhe}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        color={Boolean(formik.errors.detalhe) ? "danger" : "success"}
                        errorMessage={formik.errors.detalhe}
                        validationState={Boolean(formik.errors.detalhe) ? "invalid" : "valid"}
                        isRequired
                      />
                    </div>

                    <div className="w-[50%] flex flex-col gap-3">
                      <CurrencyFormat
                        thousandSeparator={","}
                        decimalSeparator={"."}
                        decimalScale={2}
                        customInput={Input}
                        label="Valor de Custo"
                        id="custo"
                        name="custo"
                        placeholder="Ex: 2.99"
                        variant="bordered"
                        startContent={
                          <div className="pointer-events-none flex items-center">
                            <span className="text-default-400 text-small">R$</span>
                          </div>
                        }
                        value={formik.values.custo}
                        onValueChange={handleCustoChange}
                        onBlur={formik.handleBlur}
                        color={Boolean(formik.errors.custo) ? "danger" : "success"}
                        errorMessage={formik.errors.custo}
                        validationState={Boolean(formik.errors.custo) ? "invalid" : "valid"}
                        isRequired
                      />

                      <CurrencyFormat
                        thousandSeparator={","}
                        decimalSeparator={"."}
                        decimalScale={2}
                        customInput={Input}
                        label="Valor de Venda"
                        id="venda"
                        name="venda"
                        placeholder="Ex: 20.99"
                        variant="bordered"
                        startContent={
                          <div className="pointer-events-none flex items-center">
                            <span className="text-default-400 text-small">R$</span>
                          </div>
                        }
                        value={formik.values.venda}
                        onValueChange={handleVendaChange}
                        onBlur={formik.handleBlur}
                        color={Boolean(formik.errors.venda) ? "danger" : "success"}
                        errorMessage={formik.errors.venda}
                        validationState={Boolean(formik.errors.venda) ? "invalid" : "valid"}
                        isRequired
                      />

                      <div className="flex justify-between gap-3">
                        <Select
                          label="Marca"
                          name="marca"
                          id="marca"
                          placeholder="Selecione uma Marca"
                          variant="bordered"
                          selectedKeys={selectedMarca}
                          onChange={handleSelectMarcaChange}
                          onBlur={formik.handleBlur}
                          color={Boolean(formik.errors.marca) ? "danger" : "success"}
                          errorMessage={formik.errors.marca}
                          validationState={Boolean(formik.errors.marca) ? "invalid" : "valid"}
                          isRequired
                        >
                          {marcas.map(marca => (
                            <SelectItem key={marca.nome} value={marca.nome}>
                              {marca.nome}
                            </SelectItem>
                          ))}
                        </Select>

                        <Select
                          label="Categoria"
                          name="categoria"
                          id="categoria"
                          placeholder="Selecione uma Categoria"
                          variant="bordered"
                          selectedKeys={selectedCategoria}
                          onChange={handleSelectCategoriaChange}
                          onBlur={formik.handleBlur}
                          color={Boolean(formik.errors.categoria) ? "danger" : "success"}
                          errorMessage={formik.errors.categoria}
                          validationState={Boolean(formik.errors.categoria) ? "invalid" : "valid"}
                          isRequired
                        >
                          {categorias.map(categoria => (
                            <SelectItem key={categoria.nome} value={categoria.nome}>
                              {categoria.nome}
                            </SelectItem>
                          ))}
                        </Select>
                      </div>
                    </div>
                  </div>

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
                  form="formProduto"
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
