import Image from "next/image"
import ImageLogo from "../../assets/logo-white.svg"

export default function Footer() {
  return (
    <footer className="my-5 pt-4 text-sm flex items-center justify-center">
      <Image src={ImageLogo} alt="Logo" className="h-8 w-8 mr-2" />
      by
      <span className="font-medium ml-2">Vinicius Vieira</span>
    </footer>
  )
}
