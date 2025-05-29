import React from "react";

function TableRow({id, hit, x, y, r, author}){
        return (
            <tr>
                <td key={id}>{id}</td>
                <td>{hit.toString()}</td>
                <td>{x}</td>
                <td>{y}</td>
                <td>{r}</td>
                <td>{author}</td>
            </tr>
        )
}

export default TableRow;