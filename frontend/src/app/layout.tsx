"use client"
import { Toast } from "@/components/ToastContainer"
import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"
import { NextUIProvider } from "@nextui-org/react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import React from "react"
import { MyGlobalContext } from "@/components/GlobalContext"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "SaKai Loja",
  description: "Loja Virtual utilizando Spring Boot && NextJS"
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  const [isAutenticado, setAutenticado] = React.useState(false)

  return (
    <html lang="pt-BR" suppressHydrationWarning>
      <body className={`${inter.className} dark min-h-screen`}>
        <Toast />

        <NextUIProvider>
          <MyGlobalContext.Provider value={{isAutenticado, setAutenticado }}>
            {children}
          </MyGlobalContext.Provider>
        </NextUIProvider>
      </body>
    </html>
  )
}
