import React from "react";

interface InputFieldProps {
    name: string,
    label: string
    type: string
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => any,
    step?: number | string
    required?: boolean
    minLength?: number
    minValue?: number
    value?: any
    id?: string
}

function InputField({name, label, type, step, required, minLength, minValue, onChange, value, id}: InputFieldProps) {
    return (
        <p>
            {label}: <br/><input name={name} className={"input-field"} type={type} step={step} required={required}
                                 minLength={minLength} min={minValue} onChange={onChange} value={value} id={id}/>
        </p>
    )
}

export default InputField