import React from "react";
import Worker from "../ts/data/Worker";
import "../css/QuickView.css"
import ActionButton from "./ActionButton";
import trash_can from "../resources/trash_can.svg"
import change from "../resources/change.svg"
import profile_placeholder from "../resources/profile_placeholder.svg"
import axios from "axios";
import {useAppDispatch} from "../ts/redux/hooks";
import {updateBuildMode, updateViewMode, updateWorkerView} from "../ts/redux/workerSlice";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

interface QuickViewProps {
    worker: Worker | null
}

function QuickView({worker}: QuickViewProps) {
    const dispatch = useAppDispatch()

    if (worker?.id === 0){
        worker = null
    }

    function showNotification(message: string) {
        toast.error(message, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true
        });
    }

    const handleUpdate = () => {
        if (worker === null) {
            showNotification('Невозможно изменить: Работник не выбран')
        } else {
            dispatch(updateBuildMode(true))
            dispatch(updateViewMode(false))
        }
    }

    const handleDelete = async () => {
        if (worker === null) {
            showNotification('Невозможно удалить: Работник не выбран')
        } else {
            const response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/delete-worker", {id: worker?.id}, {withCredentials: true})
            if (response.status === 200) {
                dispatch(updateWorkerView(null))
            }
        }
    }

    return (
        <div className={"quick-view"}>
            <ToastContainer position="top-right" autoClose={5000} hideProgressBar={false}
                            newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover/>
            <div id={"profile-picture"}>
                <img src={profile_placeholder} alt={""}/>
            </div>

            <div className={"scrollable-div"}>
                <div>id: <b>{worker?.id}</b></div>
                <br/>
                <div>Имя: <b>{worker?.name}</b></div>
                <br/>
                <div>Координата:
                    <ul>
                        <li>x: <b>{worker?.coordinates.x}</b></li>
                        <li>y: <b>{worker?.coordinates.y}</b></li>
                    </ul>
                </div>
                <div>Дата Создания: <b>{worker?.creationDate}</b></div>
                <br/>
                <div>Организация:
                    <ul>
                        <li>Адрес:</li>
                        <ul>
                            <li>Улица: <b>{worker?.organization?.officialAddress.street}</b></li>
                            <li>zip-код: <b>{worker?.organization?.officialAddress.zipCode}</b></li>
                            <li>Локация:</li>
                            <ul>
                                <li>x: <b>{worker?.organization?.officialAddress.town.x}</b></li>
                                <li>y: <b>{worker?.organization?.officialAddress.town.y}</b></li>
                                <li>z: <b>{worker?.organization?.officialAddress.town.z}</b></li>
                                <li>Название: <b>{worker?.organization?.officialAddress.town.name}</b></li>
                            </ul>
                        </ul>
                        <li>Оборот: <b>{worker?.organization?.annualTurnover}</b></li>
                        <li>Количество сотрудников: <b>{worker?.organization?.employeesCount}</b></li>
                        <li>Рейтинг: <b>{worker?.organization?.rating}</b></li>
                        <li>Тип организации: <b>{worker?.organization?.organizationType}</b></li>
                    </ul>
                </div>
                <div>Зарплата: <b>{worker?.salary}</b></div>
                <br/>
                <div>Рейтинг: <b>{worker?.rating}</b></div>
                <br/>
                <div>Дата начала работы: <b>{worker?.startDate}</b></div>
                <br/>
                <div>Должность: <b>{worker?.position}</b></div>
                <br/>
                <div>Статус: <b>{worker?.status}</b></div>
                <br/>
                <div>Характеристика:<br/>
                    <ul>
                        <li>Цвет глаз: <b>{worker?.person.eyeColor}</b></li>
                        <li>Цвет волос: <b>{worker?.person.hairColor}</b></li>
                        <li>Локация:</li>
                        <ul>
                            <li>x: <b>{worker?.person.location?.x}</b></li>
                            <li>y: <b>{worker?.person.location?.y}</b></li>
                            <li>z: <b>{worker?.person.location?.z}</b></li>
                            <li>Название: <b>{worker?.person.location?.name}</b></li>
                        </ul>
                        <li>Номер паспорта: <b>{worker?.person.passportID}</b></li>
                        <li>Национальность: <b>{worker?.person.nationality}</b></li>
                    </ul>
                </div>
            </div>

            <div className={"row"} id={"quick-view-buttons"}>
                <ActionButton buttonClass={"action-button"} action={handleUpdate} icon={change}
                              tooltip={"Изменить"}/>
                <ActionButton buttonClass={"action-button-red"} action={handleDelete} icon={trash_can}
                              tooltip={"Удалить"}/>
            </div>
        </div>
    )
}

export default QuickView