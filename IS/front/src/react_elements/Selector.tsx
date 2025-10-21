import React from "react";

interface SelectorProps {
    name: string
    items: Map<any, string>
    label: string
    onChangeAction: (e: React.ChangeEvent<HTMLSelectElement>) => any
    required?: boolean
    selectedValue?: string
}

interface OptionProps {
    label: string
    value: any
}

function Option({label, value}: OptionProps) {
    return (
        <option value={value}>{label}</option>
    )
}

function Selector({name, items, label, required, onChangeAction, selectedValue}: SelectorProps) {
    return (
        <p>{label}:<br/>
            <select name={name} onChange={onChangeAction} value={selectedValue}>
                {!required ? <Option label={""} value={undefined}/> : null}
                {Array.from(items).map(([key, value]) => {
                    return (
                        <Option key={key} value={key} label={value}></Option>
                    )
                })}
            </select>
        </p>
    )
}

export default Selector