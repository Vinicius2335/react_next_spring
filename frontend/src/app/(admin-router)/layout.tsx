/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"
import { Sidebar } from "@/components/layout/Sidebar"
import React from "react"
import { getSessionUtil } from "../api/auth/[...nextauth]/utils"
import { useRouter } from "next/navigation"
import { Spinner } from "@nextui-org/react"
import { toast } from "react-toastify"

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const [isLoading, setIsLoading] = React.useState(true)
  const router = useRouter()

  React.useEffect(() => {
    getSessionUtil().then(session => {
      if (session){
        const possuiPermissao = session.roles.match("ROLE_ADMIN") != null || session.roles.match("ROLE_GERENTE") != null
        
        if (!possuiPermissao){
          toast.error("O usuário não possui acesso. 😁")
          router.replace("/")
        } else {
          setIsLoading(false)
        }
      }

      if (session == null) {
        toast.error("O usuário não está logado. 😴")
        router.replace("/")
      } 
    })
  }, [])

  return (
    <div>
      <Navbar />
      {isLoading ? (
        <>
          <main className="w-full h-screen mt-8 flex flex-col items-center justify-center">
            <Spinner size="lg" />
          </main>
          <Footer />
        </>
      ) : (
        <>
          <main className="relative mt-8 px-4 w-full z-10 min-h-[calc(100vh_-_64px_-_108px)] flex-grow">
            <div className="grid grid-cols-12">
              <div className="hidden relative z-10 lg:block lg:col-span-2 mt-8">
                <Sidebar />
              </div>

              <div className="col-span-10 flex flex-col items-center justify-center">
                {children}
              </div>
            </div>
          </main>
          <Footer />
        </>
      )}
    </div>
  )
}
