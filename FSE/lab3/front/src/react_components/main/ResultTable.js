import React from "react";
import TableRow from "./TableRow";
import "../../js/dot"

function ResultTable({items}){
    return (
        <div id="rightTable">
            <table id="results">
                <caption><b>Результаты</b></caption>
                <thead>
                <tr>
                    <th scope="col">№</th>
                    <th scope="col">Попадание</th>
                    <th scope="col">x</th>
                    <th scope="col">y</th>
                    <th scope="col">R</th>
                    <th scope="col">Автор</th>
                </tr>
                </thead>
                <tbody id={"resultBody"}>
                {items.map((item) => {
                    return (
                        <TableRow key={item.id} id={item.id} hit={item.hit} x={item.x} y={item.y} r={item.r}
                                  author={item.author}/>
                    )
                })}
                </tbody>
            </table>
        </div>
    )
}

export default ResultTable;