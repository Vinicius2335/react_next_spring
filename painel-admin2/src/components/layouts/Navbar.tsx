import Image from "next/image"
import Link from "next/link"
import ImgLogo from "../../assets/logo-white.svg"

export function Navbar() {
  return (
    <nav className="fixed w-full top-0 left-0 h-16 flex justify-between items-center py-4 px-5 bg-zinc-800 text-zinc-200">
      <div className="flex items-center justify-center gap-2">
        <Image src={ImgLogo} alt="Logo do Sakai Shop" className="h-7 w-7" />
        <h1>Sakai Shop</h1>
      </div>
      <ul className="flex">
        <li className="mr-5 text-white p-1 transition-colors border-b-2 border-solid border-transparent hover:border-white">
          <Link href={"/"}>Home</Link>
        </li>
        <li className="mr-5 text-white p-1 transition-colors border-b-2 border-solid border-transparent hover:border-white">
          <Link href={"/about"}>Sobre</Link>
        </li>
      </ul>
    </nav>
  )
}
