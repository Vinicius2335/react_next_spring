/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import { CardProdutoImagem } from "@/components/CardProdutoImagem"
import ModalDeleteGeneric from "@/components/data-grid/ModalDeleteGeneric"
import { DataTypeProduto } from "@/models/produto"
import { ProdutoImagemResponse } from "@/models/produto-image-response"
import { ProdutoImageService } from "@/services/cadastros/ProdutoImagemService"
import { ProdutoService } from "@/services/cadastros/ProdutoService"
import { Button, Input, Link, useDisclosure } from "@nextui-org/react"
import { ArrowLeft, Upload } from "@phosphor-icons/react"
import React from "react"
import { toast } from "react-toastify"

interface ProdutoImagensProps {
  params: {
    produtoId: string
  }
}

export default function ProdutoImagens({ params }: ProdutoImagensProps) {
  const inputRef = React.useRef<HTMLInputElement>(null)
  const [produto, setProduto] = React.useState<DataTypeProduto>({} as DataTypeProduto)
  const [imagens, setImagens] = React.useState<ProdutoImagemResponse[]>([])
  const [imagemSelecionada, setImagemSelecionada] = React.useState<ProdutoImagemResponse>(
    {} as ProdutoImagemResponse
  )

  const produtoService = new ProdutoService()
  const produtoImageService = new ProdutoImageService()
  const deleteModal = useDisclosure()

  const handleUpload = () => {
    inputRef.current?.click()
  }

  const handleDisplayFileDetails = () => {
    if (inputRef.current?.files != null) {
      produtoImageService
        .uploadImage(produto.id, inputRef.current.files[0])
        .then(() => {
          toast.success(`Upload realizado com sucesso.`)
          setImagens([])
        })
        .catch(() => {
          toast.error("Erro ao tentar realizar o upload da imagem, tente novamente mais tarde.")
        })
      inputRef.current.value = ""
    }
  }

  function buscarImagensPorProduto(idProduto: number) {
    produtoImageService.buscarImagemPorProduto(idProduto).then(resp => {
      setImagens(resp)
      console.log(resp)
    })
  }

  function onDelete(item: ProdutoImagemResponse) {
    setImagemSelecionada(item)
    deleteModal.onOpen()
  }

  function onConfirmarDelete() {
    let entityToDelete = imagemSelecionada
    produtoImageService
      .delete(entityToDelete.id)
      .then(() => {
        toast.success(`Imagem deletada com sucesso!`)
        setImagens([])
        setImagemSelecionada({} as ProdutoImagemResponse)
        deleteModal.onClose()
      })
      .catch(() => {
        toast.error(`Erro ao tentar remover uma imagem, tente novamente mais tarde!`)
        setImagemSelecionada({} as ProdutoImagemResponse)
        deleteModal.onClose()
      })
  }

  React.useEffect(() => {
    if (imagens.length == 0) {
      produtoService.buscarPorId(Number(params.produtoId)).then(produto => {
        setProduto(produto)
        buscarImagensPorProduto(produto.id)
      })
    }
  }, [imagens])

  return (
    <>
      <div className="flex justify-between items-center bg-zinc-800 w-full p-4 rounded-md mb-4">
        <h1 className="mb-4">Produto: {produto.descricaoCurta}</h1>

        <div className="h-auto">
          <Button
            className="w-24"
            onClick={handleUpload}
            isIconOnly
            color="primary"
            variant="shadow"
          >
            <Upload size={25} />
          </Button>

          <Input
            onChange={handleDisplayFileDetails}
            ref={inputRef}
            type="file"
            className="hidden"
          />
        </div>
      </div>

      <div className="gap-4 grid grid-cols-4 mb-4">
        {imagens.length > 0 ? (
          imagens.map(image => (
            <CardProdutoImagem
              key={image.id}
              src={`data:image/png;base64,${image.arquivo}`}
              title={image.nome}
              handleOnDelete={() => onDelete(image)}
            />
          ))
        ) : (
          <p className="text-zinc-500 col-span-4">
            Nenhuma imagem foi adicionada para o produto:{" "}
            <span className="font-bold italic">{produto.descricaoCurta}</span>.
          </p>
        )}
      </div>

      <Link href="/admin/cadastros/produtos">
        <ArrowLeft size={32} className="mr-2" />
        Voltar
      </Link>

      <ModalDeleteGeneric
        isOpen={deleteModal.isOpen}
        onOpenChange={deleteModal.onOpenChange}
        onConfirmar={onConfirmarDelete}
        entity="Imagem"
      />
    </>
  )
}
