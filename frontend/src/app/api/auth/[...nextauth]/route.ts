import { NextAuthOptions } from "next-auth";
import NextAuth from "next-auth/next";
import CredentialsProvider from "next-auth/providers/credentials";

const nextAuthOptions: NextAuthOptions = {
  // Tipo de autenticação: credenciais, github, google, facebook
  providers: [
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        email: {
          label: 'email',
          type: 'text'
        },
        password: {
          label: 'password',
          type: 'password'
        }
      },

      async authorize(credentials, req) {
          const response = await fetch('http://localhost:8080/api/auth/login', {
            method: "POST",
            headers: {
              "Content-type": "application/json"
            },
            body: JSON.stringify({
              email: credentials?.email,
              password: credentials?.password
            })
          })

          const user = await response.json()

          if(user && response.ok){
            console.log(user)
            return user;
          }

          return null
      },
    })
  ],

  pages: {
    signIn: "/login"
  },

  // Funçoes que sao chamadas para cada açao do next-auth
  callbacks: {
    async jwt( {token, user} ){
      user && (token.user = user)
      return token
    },

    async session({ session, token }){
      session = token.user as any
      return session
    }
  }
}

const handler = NextAuth(nextAuthOptions)

export { handler as GET, handler as POST, nextAuthOptions }