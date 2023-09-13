import { Card, CardBody, CardFooter, Image, Tooltip } from "@nextui-org/react"
import { PencilSimpleLine, Trash } from "@phosphor-icons/react"
import ImgPlaceHolder from "../assets/product-placeholder.jpg"

export interface CardProdutoImagemProps {
  title: string
  src: string
  handleOnDelete: () => void
}

export function CardProdutoImagem({ src, title, handleOnDelete }: CardProdutoImagemProps) {
  return (
    <Card shadow="md">
      <CardBody className="overflow-visible p-0">
        <Image
          isBlurred
          shadow="sm"
          radius="lg"
          width="100%"
          alt={title}
          className="object-cover h-[200px] min-w-[200px]"
          src={src}
          fallbackSrc={ImgPlaceHolder.src}
        />
      </CardBody>

      <CardFooter className="text-small justify-center">
        <Tooltip color="danger" placement="bottom-start" content="Remover Imagem">
          <span className="text-lg text-danger hover:text-danger-500 cursor-pointer active:opacity-50">
            <Trash size={25} onClick={handleOnDelete} />
          </span>
        </Tooltip>
      </CardFooter>
    </Card>
  )
}
