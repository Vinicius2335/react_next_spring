/* eslint-disable react-hooks/exhaustive-deps */
"use client"

import ImgLeadingPage from "@/assets/demonstracao-em-dispositivo-digital.jpg"
import Image from "next/image"

export default function Home() {
   return (
    <div>
      <header className="bg-dark py-5">
        <div className="container px-4 px-lg-5 my-5">
          <div className="text-center text-white">
            <h1 className="font-bold text-2xl">Loja Sakai</h1>
            <p className="text-lg leading-relaxed font-normal text-white/50 mb-0">Home Page Template</p>
          </div>
        </div>
      </header>

      <section className="py-5">
        <div className="container px-4 px-lg-5 mt-5">
          <Image src={ImgLeadingPage} alt="Imagem Leading Page" />
          <div className="text-sm text-zinc-500">
            Imagem de{" "}
            <a href="https://br.freepik.com/fotos-gratis/demonstracao-em-dispositivo-digital_21794919.htm#query=landing%20page&position=43&from_view=search&track=ais">
              Freepik
            </a>
          </div>
        </div>
      </section>
    </div>
  )
}
