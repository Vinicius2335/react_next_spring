import { Card, CardBody, CardFooter, Image } from "@nextui-org/react"
import { PencilSimpleLine, Trash } from "@phosphor-icons/react"

interface CardProdutoImagemProps {
  title: string
  src: string
}

export function CardProdutoImagem({ src, title }: CardProdutoImagemProps) {
  return (
    <Card shadow="md">
      <CardBody className="overflow-visible p-0">
        <Image
          shadow="sm"
          radius="lg"
          width="100%"
          alt={title}
          className="w-full object-cover h-[140px]"
          src={src}
        />
      </CardBody>

      <CardFooter className="text-small justify-between">
        <span className="text-lg text-default-400 hover:text-default-500 cursor-pointer active:opacity-50">
          <PencilSimpleLine size={25} />
        </span>
        
        <span className="text-lg text-danger hover:text-danger-500 cursor-pointer active:opacity-50">
          <Trash size={25} />
        </span>
      </CardFooter>
    </Card>
  )
}
