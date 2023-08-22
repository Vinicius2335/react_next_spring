"use client"

import { Button, Card, CardBody, CardFooter, CardHeader, Checkbox, Input, Link } from "@nextui-org/react";
import { Envelope, LockKey } from "@phosphor-icons/react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  function onSignIn(){
    router.push('/admin/dashboard');
  }

  function onCancel(){
    router.push('/user/produtos');
  }

  return (
    <div className='flex justify-center items-center py-32'>
      <Card className="w-[500px] max-h-[400px] p-5">
          <CardHeader className="text-3xl !justify-center mx-auto">Login</CardHeader>

          <CardBody className="flex items-center flex-col gap-5">
            <Input
              autoFocus
              endContent={
                <Envelope className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
              }
              label="Email"
              placeholder="Enter your email"
              variant="bordered"
            />

            <Input
              endContent={
                <LockKey className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
              }
              label="Password"
              placeholder="Enter your password"
              type="password"
              variant="bordered"
            />
            <div className="flex w-full py-2 px-1 justify-between">
              <Checkbox
                classNames={{
                  label: "text-small"
                }}
              >
                Remember me
              </Checkbox>
              
              <Link color="primary" href="#" size="sm">
                Forgot password?
              </Link>
            </div>
          </CardBody>

          <CardFooter className="flex items-center justify-end gap-2">
            <Button  onClick={onCancel} color="default" variant="flat">
              Close
            </Button>
            <Button onClick={onSignIn} color="primary">
              Sign in
            </Button>
          </CardFooter>
        </Card>
    </div>
  )
}
