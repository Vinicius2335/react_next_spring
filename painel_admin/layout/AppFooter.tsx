/* eslint-disable @next/next/no-img-element */

import React, { useContext } from 'react';
import { LayoutContext } from './context/layoutcontext';
import Image from 'next/image'
import Logo from "../public/layout/images/logo-dark.svg"

const AppFooter = () => {
    const { layoutConfig } = useContext(LayoutContext);

    return (
        <div className="layout-footer text-sm">
            <Image src={Logo} alt='Logo' className='h-5 w-1'/>
            by
            <span className="font-medium ml-2">Vinicius Vieira</span>
        </div>
    );
};

export default AppFooter;
