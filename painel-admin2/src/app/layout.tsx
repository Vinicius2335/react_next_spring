import { Navbar } from "@/components/layouts/Navbar"
import { Sidebar } from "@/components/layouts/Sidebar"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { Footer } from "@/components/layouts/Footer"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Sakai Shop",
  description: "E-Commerce"
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR">
      <body className={`${inter.className} bg-zinc-700 text-zinc-200`}>
        <Navbar />

        <div className="h-screen flex flex-col p-4">
          <div className="flex flex-1">
            <Sidebar />

            {children}
          </div>
        </div>

        <Footer />
      </body>
    </html>
  )
}
