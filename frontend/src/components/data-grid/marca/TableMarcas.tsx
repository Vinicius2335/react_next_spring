/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import { DataTypeMarca, createEmptyMarca, getColumnsMarca } from "@/models/marca"
import { MarcaService } from "@/services/cadastros/MarcaService"
import { capitalize } from "@/services/utils"
import {
  Button,
  Input,
  Pagination,
  Selection,
  SortDescriptor,
  Spinner,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Tooltip,
  useDisclosure
} from "@nextui-org/react"
import { MagnifyingGlass, PencilSimpleLine, Plus, Trash } from "@phosphor-icons/react"
import dayjs from "dayjs"
import React, { useCallback, useEffect, useState } from "react"
import { toast } from "react-toastify"
import ModalDeleteGeneric from "../ModalDeleteGeneric"
import ModalSalvar from "./ModalSalvar"

export default function TableMarcas() {
  let columns = getColumnsMarca()

  const [isLoading, setIsLoading] = React.useState(true)
  const [isEmptyContent, setIsEmptyContent] = React.useState(false)
  const [filterValue, setFilterValue] = React.useState("")
  const [selectedKeys, setSelectedKeys] = React.useState<Selection>(new Set([]))
  const [visibleColumns, setVisibleColumns] = React.useState<Selection>("all")
  const [statusFilter, setStatusFilter] = React.useState<Selection>("all")
  const [rowsPerPage, setRowsPerPage] = React.useState(5)
  const [page, setPage] = useState(1)
  const hasSearchFilter = Boolean(filterValue)
  const [sortDescriptor, setSortDescriptor] = React.useState<SortDescriptor>({
    column: "id",
    direction: "ascending"
  })

  const [data, setData] = useState<DataTypeMarca[]>([])
  const [marca, setMarca] = useState<DataTypeMarca>(createEmptyMarca())
  const text = "marca"
  const marcaService = new MarcaService()

  const salvarModal = useDisclosure()
  const deleteModal = useDisclosure()

  function carregaDados() {
    marcaService
      .getAll()
      .then(data => {
        if (data.length == 0) {
          setIsEmptyContent(true)
        }
        setIsLoading(false)
        setData(data)
      })
      .catch(() => {
        toast.error("Erro ao tentar carregar os dados!")
        setIsLoading(false)
        setIsEmptyContent(true)
      })
  }

  useEffect(() => {
    carregaDados()
  }, [])

  function onEdit(item: DataTypeMarca) {
    setMarca(item)
    salvarModal.onOpen()
  }

  function onDelete(item: DataTypeMarca) {
    setMarca(item)
    deleteModal.onOpen()
  }

  function onConfirmarDelete() {
    let entityToDelete = marca
    marcaService
      .delete(entityToDelete.id)
      .then(() => {
        toast.success(`${capitalize(text)} deletada com sucesso!`)
        carregaDados()
        setMarca(createEmptyMarca())
        deleteModal.onClose()
      })
      .catch(() => {
        toast.error(`Erro ao tentar deletar ${text}, tente novamente mais tarde!`)
        deleteModal.onClose()
      })
  }

  function onAdd() {
    setMarca(createEmptyMarca())
    salvarModal.onOpen()
  }

  // ------ TUDO RELACIONADO COM O HEADER DA TABELA ------
  const headerColumns = React.useMemo(() => {
    if (visibleColumns === "all") return columns

    return columns.filter(column => Array.from(visibleColumns).includes(column.uid))
  }, [visibleColumns])

  // ------ TUDO RELACIONADO COM O SEARCH ------
  const filteredItems = React.useMemo(() => {
    let Filtereds = [...data]

    if (hasSearchFilter) {
      Filtereds = Filtereds.filter(item =>
        item.nome.toLowerCase().includes(filterValue.toLowerCase())
      )
    }
    return Filtereds
  }, [data, filterValue, statusFilter])

  const pages = Math.ceil(filteredItems.length / rowsPerPage)

  const items = React.useMemo(() => {
    const start = (page - 1) * rowsPerPage
    const end = start + rowsPerPage

    return filteredItems.slice(start, end)
  }, [page, filteredItems, rowsPerPage])

  // ------ TUDO RELACIONADO COM A ORDENAÇAO DAS LINHAS ------
  const sortedItems = React.useMemo(() => {
    return [...items].sort((a: DataTypeMarca, b: DataTypeMarca) => {
      const first = a[sortDescriptor.column as keyof DataTypeMarca] as number
      const second = b[sortDescriptor.column as keyof DataTypeMarca] as number
      const cmp = first < second ? -1 : first > second ? 1 : 0

      return sortDescriptor.direction === "descending" ? -cmp : cmp
    })
  }, [sortDescriptor, items])

  // ------ TUDO RELACIONADO COM A RENDERIZAÇAO DAS COLUNAS ------
  const renderCell = React.useCallback((item: DataTypeMarca, columnKey: React.Key) => {
    const cellValue = item[columnKey]

    switch (columnKey) {
      case "actions":
        return (
          <div className="relative flex justify-start items-center gap-4">
            <Tooltip content={`Edit ${text}`}>
              <span
                onClick={() => onEdit(item)}
                className="text-lg text-default-400 hover:text-default-500 cursor-pointer active:opacity-50"
              >
                <PencilSimpleLine size={25} />
              </span>
            </Tooltip>
            <Tooltip color="danger" content={`Delete ${text}`}>
              <span
                onClick={() => onDelete(item)}
                className="text-lg text-danger hover:text-danger-500 cursor-pointer active:opacity-50"
              >
                <Trash size={25} />
              </span>
            </Tooltip>
          </div>
        )

      case "dataCriacao":
        return (
          <div>
            <p>{dayjs(item.dataCriacao).format("DD/MM/YYYY HH:mm")}</p>
          </div>
        )

      case "dataAtualizacao":
        return (
          <div>
            <p>{dayjs(item.dataAtualizacao).format("DD/MM/YYYY HH:mm")}</p>
          </div>
        )
      default:
        return cellValue
    }
  }, [])

  // ------ TUDO RELACIONADO COM PAGINAÇÃO ------
  const onNextPage = React.useCallback(() => {
    if (page < pages) {
      setPage(page + 1)
    }
  }, [page, pages])

  const onPreviousPage = React.useCallback(() => {
    if (page > 1) {
      setPage(page - 1)
    }
  }, [page])

  const onRowsPerPageChange = React.useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
    setRowsPerPage(Number(e.target.value))
    setPage(1)
  }, [])
  // ------ FIM PAGINAÇÃO ------

  // ------ TUDO RELACIONADO COM O BOTAO DE PROCURAR ------
  const onSearchChange = React.useCallback((value?: string) => {
    if (value) {
      setFilterValue(value)
      setPage(1)
    } else {
      setFilterValue("")
    }
  }, [])

  const onClear = useCallback(() => {
    setFilterValue("")
    setPage(1)
  }, [])

  // ------ TUDO RELACIONADO COM O CONTEUDO ACIMA DA TABELA ------
  const topContent = React.useMemo(() => {
    return (
      <div className="flex flex-col gap-4">
        <div className="flex justify-between gap-3 items-end">
          <Input
            isClearable
            className="w-full sm:max-w-[44%]"
            placeholder="Search by nome..."
            startContent={<MagnifyingGlass />}
            value={filterValue}
            onClear={() => onClear()}
            onValueChange={onSearchChange}
          />
          <div className="flex gap-3">
            <Button onClick={onAdd} color="primary" variant="shadow" endContent={<Plus />}>
              Add New
            </Button>
          </div>
        </div>
        <div className="flex justify-between items-center">
          <span className="text-default-400 text-small">Total {data.length} found</span>
          <label className="flex items-center text-default-400 text-small">
            Rows per page:
            <select
              className="bg-transparent outline-none text-default-400 text-small"
              onChange={onRowsPerPageChange}
            >
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="15">15</option>
            </select>
          </label>
        </div>
      </div>
    )
  }, [
    filterValue,
    statusFilter,
    visibleColumns,
    onSearchChange,
    onRowsPerPageChange,
    data.length,
    hasSearchFilter
  ])

  // ------ TUDO RELACIONADO COM O  CONTEUDO ABAIXO DA TABELA ------
  const bottomContent = React.useMemo(() => {
    return pages > 0 ? (
      <div className="py-2 px-2 flex justify-between items-center">
        <span className="w-[30%] text-small text-default-400">
          {selectedKeys === "all"
            ? "All items selected"
            : `${selectedKeys.size} of ${filteredItems.length} selected`}
        </span>
        <Pagination
          isCompact
          showControls
          showShadow
          color="primary"
          page={page}
          total={pages}
          onChange={setPage}
        />
        <div className="hidden sm:flex w-[30%] justify-end gap-2">
          <Button isDisabled={pages === 1} size="sm" variant="flat" onPress={onPreviousPage}>
            Previous
          </Button>
          <Button isDisabled={pages === 1} size="sm" variant="flat" onPress={onNextPage}>
            Next
          </Button>
        </div>
      </div>
    ) : null
  }, [selectedKeys, items.length, page, pages, hasSearchFilter])

  return (
    <>
      <Table
        aria-label={`"DataGrid de ${capitalize(text)}"`}
        isHeaderSticky
        bottomContent={bottomContent}
        bottomContentPlacement="inside"
        classNames={{
          wrapper: "max-h-[500px]",
          table: "min-h-[150px]"
        }}
        selectedKeys={selectedKeys}
        selectionMode="single"
        onSelectionChange={setSelectedKeys}
        onSortChange={setSortDescriptor}
        sortDescriptor={sortDescriptor}
        topContent={topContent}
        topContentPlacement="inside"
      >
        <TableHeader columns={headerColumns}>
          {column => (
            <TableColumn
              key={column.uid}
              align={column.uid === "actions" ? "center" : "start"}
              allowsSorting={column.sortable}
            >
              {column.name}
            </TableColumn>
          )}
        </TableHeader>

        <TableBody
          isLoading={isLoading}
          emptyContent={isEmptyContent ? `Nenhuma ${text} foi encontrada` : ""}
          items={sortedItems}
          loadingContent={
            <Spinner color="primary" className="mt-36" labelColor="primary" label="Loading..." />
          }
        >
          {item => (
            <TableRow key={item.name}>
              {columnKey => <TableCell height={50}>{renderCell(item, columnKey)}</TableCell>}
            </TableRow>
          )}
        </TableBody>
      </Table>

      {/* ------ MODAIS ------ */}
      <ModalSalvar
        isOpen={salvarModal.isOpen}
        onOpenChange={salvarModal.onOpenChange}
        onClose={salvarModal.onClose}
        marca={marca}
        onSalvarPressed={carregaDados}
      />

      <ModalDeleteGeneric
        isOpen={deleteModal.isOpen}
        onOpenChange={deleteModal.onOpenChange}
        onConfirmar={onConfirmarDelete}
        entity="marca"
      />
    </>
  )
}
