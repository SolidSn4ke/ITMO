import React from "react";
import "../css/ActionButton.css"

interface ActionButtonProps {
    action: (any?: any) => any
    icon?: string
    tooltip?: string
    buttonClass: string
    form?: string
    text?: string
}


function ActionButton({action, icon, tooltip, buttonClass, form, text}: ActionButtonProps) {
    return (
        <div>
            <button onClick={action} className={buttonClass} title={tooltip} form={form}>
                <img src={icon} alt={""}/>
                <b>{text === undefined ? '' : text}</b>
            </button>
        </div>
    )
}

export default ActionButton