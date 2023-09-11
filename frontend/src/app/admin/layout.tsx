"use client"

import Footer from "@/components/layout/Footer"
import { Navbar } from "@/components/layout/Navbar"
import { Sidebar } from "@/components/layout/Sidebar"
import { ToastContainer, toast } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <ToastContainer
        position="top-center"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />

      <Navbar />
      {/*container   max-w-7xl */}
      <main className="relative mt-8 px-4 w-full z-10 min-h-[calc(100vh_-_64px_-_108px)] flex-grow">
        <div className="grid grid-cols-12">
          <div className="hidden relative z-10 lg:block lg:col-span-2 mt-8">
            <Sidebar />
          </div>

          <div className="col-span-10 flex flex-col items-center justify-center">
            {children}
            <Footer />
          </div>
        </div>
      </main>
    </div>
  )
}
