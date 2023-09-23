/* eslint-disable react-hooks/exhaustive-deps */
import { MenuItemType, NAVBAR_ITEMS } from "@/models/menu-items"
import { AuthenticationService } from "@/services/AuthenticationService"
import {
  Avatar,
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Link,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Navbar as NavbarUI
} from "@nextui-org/react"
import { signOut, useSession } from "next-auth/react"
import Image from "next/image"
import NextLink from "next/link"
import { useRouter } from "next/navigation"
import React from "react"
import { toast } from "react-toastify"
import ImageLogo from "../../assets/logo-white.svg"

export function Navbar() {
  const menuItems: MenuItemType[] = NAVBAR_ITEMS
  const [isAutenticado, setIsAutenticado] = React.useState(false)
  const { data: session, status } = useSession()

  const authenticationService = new AuthenticationService()
  const router = useRouter()


  async function handleDeslogar(){
    authenticationService.logout().catch(() => {
      toast.error("Erro ao tentar realizar o logout, tente novamente mais tarde...")
    })

    await signOut({
      redirect: false
    })

    setIsAutenticado(false)
    router.replace("/")
  }

  React.useEffect(() => {
    if (status === "authenticated"){
      setIsAutenticado(true)
    }
  }, [status])

  return (
    <NavbarUI position="sticky" maxWidth="xl" className="bg-sakai-bg">
      <NavbarBrand className="gap-3 max-w-fit">
        <NextLink
          href={"/"}
          className="flex justify-start items-center gap-2 tap-highlight-transparent transition-opacity active:opacity-50"
        >
          <Image alt="Logo" src={ImageLogo} width={50} className="h-auto" color="white" />
          <p className="font-bold text-inherit">Sakai</p>
        </NextLink>
      </NavbarBrand>

      <NavbarContent className="basis-1/5 sm:basis-full" justify="center">
        <ul className="hidden lg:flex gap-4 justify-start items-center">
          {menuItems.map(({ label, href }, i) => (
            <NavbarItem key={i}>
              <Link color="foreground" href={href}>
                {label}
              </Link>
            </NavbarItem>
          ))}
        </ul>
      </NavbarContent>

      {isAutenticado ? (
        <NavbarContent as="div" justify="end">
          <Dropdown placement="bottom-end">
            <DropdownTrigger>
              <Avatar
                isBordered
                as="button"
                className="transition-transform"
                color="success"
                name={session?.user.nome[0]}
                size="sm"
              />
            </DropdownTrigger>
            <DropdownMenu aria-label="Profile Actions" variant="flat" disabledKeys={["profile"]}>
              <DropdownItem textValue="profile" key="profile" className="h-14 gap-2">
                <p className="font-semibold">Conectado como</p>
                <p className="font-semibold">{session?.user.email}</p>
              </DropdownItem>
              <DropdownItem textValue="logout" key="logout" color="danger" onClick={handleDeslogar}>
                Deslogar
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </NavbarContent>
      ) : (
        <NavbarContent justify="end">
          <NavbarItem className="hidden lg:flex">
            <Link href="/login">Login</Link>
          </NavbarItem>
          <NavbarItem>
            <Button as={Link} color="primary" href="#" variant="flat">
              Sign Up
            </Button>
          </NavbarItem>
        </NavbarContent>
      )}
    </NavbarUI>
  )
}
