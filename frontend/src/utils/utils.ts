import { getServerSession } from "next-auth/next"
import { redirect } from "next/navigation"
import { nextAuthOptions } from "../app/api/auth/[...nextauth]/route"

export async function getSessionUtils() {
  const session = await getServerSession(nextAuthOptions)

  if (!session) {
    redirect("/")
  }
}

export function getFirstAndLastCaractereAtName(name: string | null | undefined) {
  if (name) {
    let split = name.trim().split(" ")
    let letrasIniciais = ""
    let tamanhoNome = split.length

    letrasIniciais += split[0].charAt(0)
    letrasIniciais += split[tamanhoNome - 1].charAt(0)

    return letrasIniciais

  } else {
    return ""
  }
}
