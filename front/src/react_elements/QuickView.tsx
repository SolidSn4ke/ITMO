import React, { useState } from "react";
import Worker from "../ts/data/Worker";
import "../css/QuickView.css"
import ActionButton from "./ActionButton";
import trash_can from "../resources/trash_can.svg"
import change from "../resources/change.svg"
import profile_placeholder from "../resources/profile_placeholder.svg"
import history_icon from "../resources/history.svg"
import axios from "axios";
import { useAppDispatch } from "../ts/redux/hooks";
import { updateBuildMode, updateViewMode, updateWorkerView } from "../ts/redux/workerSlice";
import 'react-toastify/dist/ReactToastify.css';
import { showErrorNotification, showInfoNotification } from "./Main";
import small_profile_placeholder from "../resources/small_profile.svg";
import { saveAs } from 'file-saver';

interface QuickViewProps {
    worker: Worker | null
}

function QuickView({ worker }: QuickViewProps) {
    const [viewMode, setViewMode] = useState<"profile" | "history">("profile")
    const [importHistory, setImportHistory] = useState<{ id: number; successful: boolean; numOfEntitiesImported: number, filePath: string }[]>([]);

    const dispatch = useAppDispatch()

    if (worker?.id === 0) {
        worker = null
    }


    React.useEffect(() => {
        if (viewMode !== "history") return;

        const load = async () => {
            try {
                const response = await axios.post(
                    "http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/import-history",
                    { withCredentials: true }
                );
                if (response.status === 200) {
                    setImportHistory(response.data);
                }
            } catch (e) {
                console.log(e);
            }
        };

        load();
        const interval = setInterval(load, 5000);

        return () => clearInterval(interval);
    }, [viewMode]);

    const handleClearHistory = async () => {
        try {
            const response = await axios.post(
                "http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/clear-import-history",
                null,
                { withCredentials: true }
            );

            if (response.status === 200) {
                showInfoNotification("История очищена");
                setImportHistory([]);
            }
        } catch (e) {
            console.log(e);
        }
    };

    const handleUpdate = () => {
        if (worker === null) {
            showErrorNotification('Невозможно изменить: Работник не выбран')
        } else {
            dispatch(updateBuildMode(true))
            dispatch(updateViewMode(false))
        }
    }

    const handleDelete = async () => {
        if (worker === null) {
            showErrorNotification('Невозможно удалить: Работник не выбран')
        } else {
            try {
                const response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/delete-worker", { id: worker?.id }, { withCredentials: true })
                if (response.status === 200) {
                    showInfoNotification('Работник удален!')
                    dispatch(updateWorkerView(null))
                }
            } catch (e) {
                console.log(e)
            }

        }
    }

    const downloadFile = async (filePath: string) => {
        try {
            filePath = filePath.split('/').pop()!
            const response = await axios.post(`http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/download-file/${filePath}`,
                {},
                { responseType: 'blob', withCredentials: true });
            if (response.status === 200) {
                const blob = new Blob([response.data], { type: 'application/octet-stream' });
                saveAs(blob, filePath.split('-').pop()!);
            }
        } catch (e) {

        }
    }

    return (
        <div className={"quick-view"}>

            {viewMode === "profile" &&
                <>
                    <div id={"profile-picture"}>
                        <img src={profile_placeholder} alt={""} />
                    </div>
                    <div className={"scrollable-div"}>
                        <div>id: <b>{worker?.id}</b></div>
                        <br />
                        <div>Имя: <b>{worker?.name}</b></div>
                        <br />
                        <div>Координата:
                            <ul>
                                <li>x: <b>{worker?.coordinates.x}</b></li>
                                <li>y: <b>{worker?.coordinates.y}</b></li>
                            </ul>
                        </div>
                        <div>Дата Создания: <b>{worker?.creationDate}</b></div>
                        <br />
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
                        <br />
                        <div>Рейтинг: <b>{worker?.rating}</b></div>
                        <br />
                        <div>Дата начала работы: <b>{worker?.startDate}</b></div>
                        <br />
                        <div>Должность: <b>{worker?.position}</b></div>
                        <br />
                        <div>Статус: <b>{worker?.status}</b></div>
                        <br />
                        <div>Характеристика:<br />
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
                            tooltip={"Изменить"} />
                        <ActionButton buttonClass={"action-button-red"} action={handleDelete} icon={trash_can}
                            tooltip={"Удалить"} />
                        <ActionButton buttonClass={'action-button'} action={() => setViewMode("history")} icon={history_icon} tooltip={"История импорта"} />
                    </div>
                </>
            }

            {viewMode === "history" &&
                <div style={{ width: "100%" }}>
                    <h3>История импорта</h3>

                    <table className="worker-table" style={{ marginTop: "10px" }}>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Результат</th>
                                <th>Добавлено элементов</th>
                            </tr>
                        </thead>
                        <tbody>
                            {importHistory.length > 0 ? (
                                importHistory.map((item) => (
                                    <tr key={item.id}>
                                        <td>{item.id}</td>
                                        <td style={{ color: item.successful ? "green" : "red" }}>
                                            {item.successful ? "Успех" : "Ошибка"}
                                        </td>
                                        <td>{item.numOfEntitiesImported}</td>
                                        <td>{item.successful ? <a
                                            href="#"
                                            onClick={(e) => {
                                                e.preventDefault();
                                                downloadFile(item.filePath);
                                            }}
                                            style={{
                                                cursor: 'pointer',
                                                color: '#007bff',
                                                textDecoration: 'underline'
                                            }}
                                        >скачать</a> : <></>}
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={3}>История пуста</td>
                                </tr>
                            )}
                        </tbody>
                    </table>

                    <div className="row" id="quick-view-buttons">
                        <ActionButton
                            buttonClass={"action-button-red"}
                            action={handleClearHistory}
                            icon={trash_can}
                            tooltip={"Очистить историю"}
                        />

                        <ActionButton
                            buttonClass={"action-button"}
                            action={() => setViewMode("profile")}
                            icon={small_profile_placeholder}
                            tooltip={"Профиль работника"}
                        />
                    </div>
                </div>
            }
        </div>
    )
}

export default QuickView