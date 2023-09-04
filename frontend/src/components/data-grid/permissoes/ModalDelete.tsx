import { Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader } from "@nextui-org/react"
import { Warning } from "@phosphor-icons/react"

interface ModalDeleteProps {
  isOpen: boolean
  onOpenChange: () => void
  onConfirmar: () => void
}

export default function ModalDelete({ isOpen, onOpenChange, onConfirmar }: ModalDeleteProps) {
  return (
    <>
      <Modal
        isOpen={isOpen}
        onOpenChange={onOpenChange}
        placement="top-center"
        scrollBehavior="inside"
        size="md"
        backdrop="blur"
        classNames={{
          closeButton: "hidden"
        }}
      >
        <ModalContent>
          {onClose => (
            <>
              <ModalHeader className="flex flex-col gap-1" />
              <ModalBody>
                <div className="flex items-center flex-col text-center">
                  <Warning size={40} className="mb-4" />
                  <h3 className="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
                    Tem certeza de que deseja excluir esta permissão?
                  </h3>
                </div>
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
                <Button color="danger" onPress={onConfirmar} className="hover:bg-danger-200">
                  Confirmar
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
