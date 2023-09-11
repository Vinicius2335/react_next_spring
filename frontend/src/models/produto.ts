export type DataTypeProduto = {
  [key: string]: any
  id: number
  quantidade: number
  descricaoCurta: string
  descricaoDetalhada: string
  valorCusto: number
  valorVenda: number
  marca: MarcaType
  categoria: CategoriaType
  dataCriacao: string
  dataAtualizacao: string
}

export type MarcaType = {
  id: number
}

export type CategoriaType = {
  id: number
}


export function createEmptyProduto(){
  const emptyProduto: DataTypeProduto = {
    id: 0,
    quantidade: 0,
    descricaoCurta: "",
    descricaoDetalhada: "",
    valorCusto: 0,
    valorVenda: 0,
    marca: createEmptyMarca(),
    categoria: createEmptyCategoria(),
    dataCriacao: "",
    dataAtualizacao: ""
  }

  return emptyProduto
}

function createEmptyCategoria(): CategoriaType{
  return {
    id: 0
  }
}

function createEmptyMarca(): MarcaType{
  return {
    id: 0
  }
}

export function getColumnsProduto(){
  return [
    { name: "ID", uid: "id", sortable: true },
    { name: "QUANTIDADE", uid: "quantidade", sortable: true },
    { name: "DESCRIÇÃO", uid: "descricao", sortable: true },
    { name: "DETALHE", uid: "detalhe"},
    { name: "V. CUSTO", uid: "custo", sortable: true},
    { name: "V. VENDA", uid: "venda", sortable: true},
    { name: "ACTIONS", uid: "actions" }
  ]
}