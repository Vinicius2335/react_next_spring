import {
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
import { MENU_ITEMS, MenuType } from "@/models/menu-items"

export function Navbar() {
  const menuItems: MenuType[] = MENU_ITEMS

  return (
    <NavbarUI position="sticky" maxWidth="xl" className="bg-sakai-bg">
        <NavbarBrand className="gap-3 max-w-fit">
          <NextLink
            href={"/"}
            className="flex justify-start items-center gap-2 tap-highlight-transparent transition-opacity active:opacity-50"
          >
            <Image alt="Logo" src={ImageLogo} width={50} height={50} color="white" />
            <p className="font-bold text-inherit">Logo</p>
          </NextLink>
        </NavbarBrand>
      <NavbarContent className="basis-1/5 sm:basis-full" justify="end">
        <ul className="hidden lg:flex gap-4 justify-start items-center">
          {menuItems.map(({ title, href }, i) => (
            <NavbarItem key={i}>
              <Link color="foreground" href={href}>
                {title}
              </Link>
            </NavbarItem>
          ))}
        </ul>
      </NavbarContent>
    </NavbarUI>
  )
}
