"use client"
import { Toast } from "@/components/ToastContainer"
import { NextUIProvider } from "@nextui-org/react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import React from "react"
import "./globals.css"
import NextAuthSessionProvider from "@/providers/sessionProvider"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "SaKai Loja",
  description: "Loja Virtual utilizando Spring Boot && NextJS"
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR" suppressHydrationWarning>
      <body className={`${inter.className} dark min-h-screen`}>
        <Toast />

        <NextUIProvider>
          <NextAuthSessionProvider>{children}</NextAuthSessionProvider>
        </NextUIProvider>
      </body>
    </html>
  )
}
