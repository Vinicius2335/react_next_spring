/* eslint-disable react-hooks/exhaustive-deps */
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
  Navbar as NavbarUI,
  image
} from "@nextui-org/react"
import Image from "next/image"
import ImageLogo from "../../assets/logo-white.svg"
import NextLink from "next/link"
import { NAVBAR_ITEMS, MenuItemType } from "@/models/menu-items"
import { AuthenticationService } from "@/services/AuthenticationService"
import React from "react"
import { toast } from "react-toastify"

export function Navbar() {
  const menuItems: MenuItemType[] = NAVBAR_ITEMS

  const authenticataionService = new AuthenticationService()
  const [isAutenticado, setAutenticado] = React.useState(false)

  function handleDeslogar(){
    authenticataionService.logout()?.then(() => {
      toast.success("Logout realizado com sucesso!")
      localStorage.removeItem("ACCESS_TOKEN")
      setAutenticado(false)
    })
    .catch(() => {
      toast.error("Erro ao tentar realizar o logout, tente novamente mais tarde...")
    })
  }

  React.useEffect(() => {
    setAutenticado(Boolean(authenticataionService.isUserAuthenticated()))
  }, [isAutenticado])

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
                color="primary"
                name="JH"
                size="sm"
              />
            </DropdownTrigger>
            <DropdownMenu aria-label="Profile Actions" variant="flat" disabledKeys={["profile"]}>
              <DropdownItem key="profile" className="h-14 gap-2">
                <p className="font-semibold">Conectado como</p>
                <p className="font-semibold">zoey@example.com</p>
              </DropdownItem>
              <DropdownItem key="logout" color="danger" onClick={handleDeslogar}>
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
