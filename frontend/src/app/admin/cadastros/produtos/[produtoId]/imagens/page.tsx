"use client"

import { Button, Input } from "@nextui-org/react"
import { Upload } from "@phosphor-icons/react"
import React from "react"

interface ProdutoImagensProps {
  params: {
    produtoId: string
  }
}

export default function ProdutoImagens({ params }: ProdutoImagensProps) {
  const inputRef = React.useRef<HTMLInputElement>(null);

  const handleUpload = () => {
    inputRef.current?.click();
  };

  return (
    <>
      <h1 className="mb-4">Imagens do Produto {params.produtoId}</h1>

      <div>
        <Button onClick={handleUpload} isIconOnly color="primary" variant="shadow">
          <Upload size={25} />
        </Button>

        <Input ref={inputRef} type="file" className="hidden" />
      </div>

      {/* <div className="gap-2 grid grid-cols-2 sm:grid-cols-4"> */}
      <div className="flex flex-wrap justify-between items-center w-[1100px] my-0 mx-auto]">
        <p>Grid das Imagens de Produto</p>
      </div>
    </>
  )
}
