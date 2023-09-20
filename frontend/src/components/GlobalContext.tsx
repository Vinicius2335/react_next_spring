import { createContext, useContext } from "react"

export type GlobalContent = {
  isAutenticado: boolean
  setAutenticado:(c: boolean) => void
}

export const MyGlobalContext = createContext<GlobalContent>({
  isAutenticado: false,
  setAutenticado: () => {},
})

export const useGlobalContext = () => useContext(MyGlobalContext)