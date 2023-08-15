"use client"

import { ColunaOpcoes } from "@/components/ColunaOpcoes"
import { IEstado } from "@/models/Estado"
import { EstadoService } from "@/services/cadastros/EstadoService"
import { Button } from "primereact/button"
import { Column } from "primereact/column"
import { DataTable } from "primereact/datatable"
import { Dialog } from "primereact/dialog"
import { InputText } from "primereact/inputtext"
import { Toast } from "primereact/toast"
import { Toolbar } from "primereact/toolbar"
import { classNames } from "primereact/utils"
import React, { useEffect, useRef, useState } from "react"
import { Plus, MagnifyingGlass } from "@phosphor-icons/react"

export default function Estado() {
  let estadoNovo: IEstado = {
    id: 0,
    nome: "",
    sigla: ""
  }

  const [estados, setEstados] = useState<IEstado[]>([])
  const [estadoDialog, setEstadoDialog] = useState(false)
  const [deleteEstadoDialog, setDeleteEstadoDialog] = useState(false)
  const [estado, setEstado] = useState<IEstado>(estadoNovo)
  const [selectedEstados, setSelectedEstados] = useState<IEstado[]>([])
  const [submitted, setSubmitted] = useState(false)
  const [globalFilter, setGlobalFilter] = useState("")
  const toast = useRef<Toast>(null)
  const dt = useRef<DataTable<IEstado[]>>(null)

  // carregando os dados ao abrir a pagina
  useEffect(() => {
    // EstadoService.getAllEstados().then((data: IEstado[]) => setEstados(data))
  }, [])

  // ----- Salvando/ Editando / Delete -----
  const saveEstado = () => {
    setSubmitted(true)

    if (estado.nome.trim()) {
      let _estado = { ...estado }
      if (estado.id) {
        EstadoService.alterar(estado, estado.id).then(() => {
          toast.current?.show({
            severity: "success",
            summary: "Sucesso",
            detail: "Alterado com Sucesso",
            life: 3000
          })
          EstadoService.getAllEstados().then((data: IEstado[]) => setEstados(data))
        })
      } else {
        EstadoService.inserir(_estado).then(() => {
          toast.current?.show({
            severity: "success",
            summary: "Sucesso",
            detail: "Inserido com Sucesso",
            life: 3000
          })
          EstadoService.getAllEstados().then((data: IEstado[]) => setEstados(data))
        })
      }
      setEstadoDialog(false)
      setEstado(estadoNovo)
    }
  }

  const editEstado = (estado: IEstado) => {
    setEstado({ ...estado })
    setEstadoDialog(true)
  }

  const confirmDeleteEstado = (estado: IEstado) => {
    setEstado(estado)
    setDeleteEstadoDialog(true)
  }

  const deleteEstado = () => {
    EstadoService.delete(estado.id).then(() => {
      toast.current?.show({
        severity: "success",
        summary: "Sucesso",
        detail: "Removido",
        life: 3000
      })

      EstadoService.getAllEstados().then((data: IEstado[]) => setEstados(data))
      setDeleteEstadoDialog(false)
    })
  }

  //---- Dialog -----
  const hideDialog = () => {
    setSubmitted(false)
    setEstadoDialog(false)
  }

  const hideDeleteEstadoDialog = () => {
    setDeleteEstadoDialog(false)
  }

  // Input do Dialog
  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    nome: string
  ) => {
    const val = (e.target && e.target.value) || ""
    let _estado = { ...estado }
    _estado[`${nome}`] = val

    setEstado(_estado)
  }

  // Dialog Footers
  const estadoDialogFooter = (
    <>
      <Button label="Cancelar" icon="pi pi-times" text onClick={hideDialog} />
      <Button label="Salvar" icon="pi pi-check" text onClick={saveEstado} />
    </>
  )
  const deleteEstadoDialogFooter = (
    <>
      <Button label="Não" icon="pi pi-times" text onClick={hideDeleteEstadoDialog} />
      <Button label="Sim" icon="pi pi-check" text onClick={deleteEstado} />
    </>
  )

  // -------- Botões --------
  // Botao de Novo estado
  const openNew = () => {
    setEstado(estadoNovo)
    setSubmitted(false)
    setEstadoDialog(true)
  }

  // botao em cima da tabela na esquerda
  const leftToolbarTemplate = () => {
    return (
      <React.Fragment>
        <div className="my-2">
          <Button
            label="Novo Estado"
            icon={<Plus className="h-5 w-5 text-white font-medium" />}
            className="p-button-success flex items-center justify-center gap-2"
            onClick={openNew}
          />
        </div>
      </React.Fragment>
    )
  }

  // ----- colunas da tabela -----
  const idBodyTemplate = (rowData: IEstado) => {
    return (
      <>
        <span className="p-column-title">ID</span>
        {rowData.id}
      </>
    )
  }

  const nomeBodyTemplate = (rowData: IEstado) => {
    return (
      <>
        <span className="p-column-title">Nome</span>
        {rowData.nome}
      </>
    )
  }

  const siglaBodyTemplate = (rowData: IEstado) => {
    return (
      <>
        <span className="p-column-title">Sigla</span>
        {rowData.sigla}
      </>
    )
  }

  // Header da Tabela
  const header = (
    <div className="flex flex-column items-center justify-between rounded text-white">
      <h5 className="m-0">Estados Cadastrados</h5>
      <span className="block mt-2 md:mt-0">
        <InputText
          type="search"
          onInput={e => setGlobalFilter(e.currentTarget.value)}
          placeholder="Search..."
          className="bg-zinc-700 text-white"
        />
      </span>
    </div>
  )

  return (
    <div className="grid crud-demo m-[5%] bg-zinc-700 w-full">
      <div className="col-12">
        <div className="card">
          <Toast ref={toast} />
          <Toolbar className="mb-4" left={leftToolbarTemplate}></Toolbar>

          <DataTable
            ref={dt}
            value={estados}
            selection={selectedEstados}
            onSelectionChange={e => setSelectedEstados(e.value as IEstado[])}
            dataKey="id"
            paginator
            rows={10}
            rowsPerPageOptions={[5, 10, 25]}
            className="datatable-responsive"
            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
            currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
            globalFilter={globalFilter}
            emptyMessage="Nenhum estado foi encontrado."
            header={header}
            responsiveLayout="scroll"
          >
            <Column selectionMode="multiple" headerStyle={{ width: "4rem" }}></Column>
            <Column
              field="id"
              header="Id"
              sortable
              body={idBodyTemplate}
              headerStyle={{ minWidth: "15rem"}}
            ></Column>
            <Column
              field="nome"
              header="Nome"
              sortable
              body={nomeBodyTemplate}
              headerStyle={{ minWidth: "15rem" }}
            ></Column>
            <Column
              field="sigla"
              header="Sigla"
              sortable
              body={siglaBodyTemplate}
              headerStyle={{ minWidth: "15rem" }}
            ></Column>
            <Column
              body={rowData => {
                return (
                  <ColunaOpcoes
                    rowData={rowData}
                    editObjeto={editEstado}
                    confirmDeleteObjeto={confirmDeleteEstado}
                  />
                )
              }}
            ></Column>
          </DataTable>

          {/* Dialog */}
          <Dialog
            visible={estadoDialog}
            style={{ width: "450px" }}
            header="Cadastrar/Editar"
            modal
            className="p-fluid"
            footer={estadoDialogFooter}
            onHide={hideDialog}
          >
            <div className="field">
              <label htmlFor="nome">Nome</label>
              <InputText
                id="nome"
                value={estado.nome}
                onChange={e => onInputChange(e, "nome")}
                required
                autoFocus
                className={classNames({ "p-invalid": submitted && !estado.nome })}
              />
              {submitted && !estado.nome && (
                <small className="p-invalid">Nome é Obrigatório.</small>
              )}
            </div>
            <div className="field">
              <label htmlFor="sigla">Sigla</label>
              <InputText
                id="sigla"
                value={estado.sigla}
                onChange={e => onInputChange(e, "sigla")}
                required
                className={classNames({ "p-invalid": submitted && !estado.sigla })}
              />
              {submitted && !estado.sigla && (
                <small className="p-invalid">Sigla é Obrigatório.</small>
              )}
            </div>
          </Dialog>

          <Dialog
            visible={deleteEstadoDialog}
            style={{ width: "450px" }}
            header="Confirmação"
            modal
            footer={deleteEstadoDialogFooter}
            onHide={hideDeleteEstadoDialog}
          >
            <div className="flex align-items-center justify-content-center">
              <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: "2rem" }} />
              {estado && <span>Deseja Excluir?</span>}
            </div>
          </Dialog>
        </div>
      </div>
    </div>
  )
}
