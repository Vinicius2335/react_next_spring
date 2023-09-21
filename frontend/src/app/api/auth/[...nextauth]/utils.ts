import { getSession } from "next-auth/react"

export async function getSessionUtil() {
  return await getSession()
}