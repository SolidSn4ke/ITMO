import React from "react";
import "../css/SearchBar.css"

interface SearchBarProps {
    id?: string
    onChangeAction: any
}

function SearchBar({id, onChangeAction}: SearchBarProps) {
    return (
        <div>
            <input id={id} type={"text"} className={"text-field"} onChange={onChangeAction} />
        </div>
    )
}

export default SearchBar