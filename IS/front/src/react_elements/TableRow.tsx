import React from "react";
import Worker from "../ts/data/Worker";
import {useAppDispatch} from "../ts/redux/hooks";
import {updateWorkerView} from "../ts/redux/workerSlice";

interface TableRowProps {
    worker: Worker
}

function TableRow({worker}: TableRowProps) {
    const dispatch = useAppDispatch()

    return (
        <tr onClick={() => {
            console.log(worker)
            dispatch(updateWorkerView(worker))
        }}>
            <td>{worker.id}</td>
            <td>{worker.name}</td>
            <td>({worker.coordinates.x}; {worker.coordinates.y})</td>
            <td>{worker.creationDate}</td>
            <td>{worker.organization?.id}</td>
            <td>{worker.salary}</td>
            <td>{worker.rating}</td>
            <td>{worker.startDate}</td>
            <td>{worker.position}</td>
            <td>{worker.status}</td>
            <td>{worker.person.passportID}</td>
        </tr>
    )
}

export default TableRow