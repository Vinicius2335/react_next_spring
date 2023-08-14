import Image from "next/image"
import ImgLogo from "../../assets/logo-white.svg"

export function Footer() {
  return (
    <footer className="layout-footer gap-2 text-sm">
      <Image src={ImgLogo} alt="Logo" className="h-9 w-9" />
      <p>by</p>
      <span className="font-medium">Vinicius Vieira</span>
    </footer>
  )
}
