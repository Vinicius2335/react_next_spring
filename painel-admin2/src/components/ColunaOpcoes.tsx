import { Button } from "primereact/button";
import React from "react";

export const ColunaOpcoes = (props: any) =>{

    const {rowData, editObjeto, confirmDeleteObjeto} = props;

    return (
        <div className="actions">
            <Button icon="pi pi-pencil" className="p-button-rounded p-button-success mr-2" 
            onClick={() => editObjeto(rowData)} />
            <Button icon="pi pi-trash" className="p-button-rounded p-button-danger mt-2" 
            onClick={() => confirmDeleteObjeto(rowData)} />
        </div>
    );
}
