import React, { useState } from "react";
import TableRow from "./TableRow";
import "../css/Table.css"
import Worker from "../ts/data/Worker"
import { useWorkerTable } from "../ts/hooks/useWorkerTable";
import SearchBar from "./SearchBar";
import ActionButton from "./ActionButton";
import magnifying_glass from "../resources/magnifying_glass.svg";
import send_icon from "../resources/continue.svg"
import plus from "../resources/plus.svg";
import { useAppDispatch, useAppSelector } from "../ts/redux/hooks";
import { setSearchValue, updateViewMode } from "../ts/redux/workerSlice";
import axios from "axios";
import Modal from "./Modal";
import InputField from "./InputField";
import Selector from "./Selector";
import { toString } from "../ts/data/Organization";
import star from '../resources/star.svg'
import { showErrorNotification, showInfoNotification } from "./Main";
import DragNDrop from "./DragNDrop";

interface WorkerWrapper {
    items: Worker[]
    controls: boolean
}

function Table({ items, controls }: WorkerWrapper) {
    const [specialItems, setSpecialItems] = useState(Array<Worker>)
    const [specialMode, setSpecialMode] = useState<null | "min" | "max" | "rating" | "enroll" | "move" | "import">(null);
    const [rating, setRating] = useState(0)
    const [selectedOrganizationId, setSelectedOrganizationId] = useState<number | null>(null)
    const [searchInput, setSearchInput] = useState('');

    const viewMode = useAppSelector((state) => state.viewMode)
    const workerView = useAppSelector((state) => state.workerView)

    const dispatch = useAppDispatch()

    const handleAdd = () => {
        if (viewMode) {
            dispatch(updateViewMode(false))
        }
    }

    const handleGetWorkerWithMinPosition = async () => {
        try {
            let response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/worker-min-position", null, { withCredentials: true })
            if (response.status === 200 && response.data) {
                setSpecialItems([response.data])
                setSpecialMode('min')
            }
        } catch (e) {
            console.log(e)
        }
    }

    const handleGetWorkerWithMaxSalary = async () => {
        try {
            let response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/worker-max-salary", null, { withCredentials: true })
            if (response.status === 200) {
                setSpecialItems([response.data])
                setSpecialMode('max')
            }
        } catch (e) {
            console.log(e)
        }
    }

    const handleGetWorkersWithSpecificRating = async () => {
        setSpecialMode('rating')
        setSpecialItems([])
    }

    const handleSendRating = async (event: Event) => {
        event.preventDefault()
        const form: HTMLFormElement = document.getElementById('modal-rating') as HTMLFormElement

        if (form.checkValidity()) {
            try {
                let response = await axios.post("http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/worker-specific-rating", { rating: rating }, { withCredentials: true })
                if (response.status === 200) {
                    setSpecialItems(response.data)
                }
            } catch (e) {
                console.log(e)
            }
        } else {
            form.reportValidity()
        }
    }

    const handleEnrollWorker = () => {
        if (workerView === null || workerView.id === 0) {
            showErrorNotification('Невозможно выполнить спец. функцию: Работник не выбран')
            return
        } else if (workerView.organization !== undefined) {
            showErrorNotification('Невозможно выполнить спец. функцию: Выбранный работник уже работает в организации')
            return
        }
        setSpecialMode('enroll')
        setSpecialItems([])
    }

    const handleMoveWorker = () => {
        if (workerView === null || workerView.id === 0) {
            showErrorNotification('Невозможно выполнить спец. функцию: Работник не выбран')
            return
        } else if (workerView.organization === undefined) {
            showErrorNotification('Невозможно выполнить спец. функцию: Работник не устроен на работу в организацию')
            return
        }
        setSpecialMode('move')
        setSpecialItems([])
    }

    const handleSendOrganization = async (event: Event) => {
        event.preventDefault()
        const form: HTMLFormElement = document.getElementById('modal-enroll') as HTMLFormElement
        if (form.checkValidity() && selectedOrganizationId) {
            try {
                let response = await axios.post(
                    `http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/enroll-worker/${workerView.id}`,
                    {
                        organizationID: selectedOrganizationId
                    },
                    { withCredentials: true }
                )
                if (response.status === 200) {
                    showInfoNotification('Успешное добавление организации')
                    setSpecialMode(null)
                    setSpecialMode(null)
                }
            } catch (e) {
                console.log(e)
            }
        } else {
            form.reportValidity()
        }
    }

    const {
        paginatedItems,
        sortConfig,
        pagination,
        totalPages,
        handleSort,
        handleFilterChange,
        handlePageChange,
    } = useWorkerTable(items);

    const getSortClass = (field: string) => {
        if (sortConfig.field !== field) return 'sortable';
        return `sortable sorted-${sortConfig.direction}`;
    };

    const getUniqueOrganizations = () => {
        const orgMap = new Map<number, string>();
        items.forEach(item => {
            if (item.organization && item.organization.id) {
                if (selectedOrganizationId === null) {
                    setSelectedOrganizationId(item.organization.id)
                }
                orgMap.set(item.organization.id, toString(item.organization));
            }
        });
        return orgMap;
    };

    return (
        <div>
            <Modal isOpen={specialMode !== null} onClose={() => setSpecialMode(null)}>
                {specialMode === "min" && (
                    <Table items={specialItems} controls={false} />
                )}

                {specialMode === "max" && (
                    <Table items={specialItems} controls={false} />
                )}

                {specialMode === "rating" && (
                    <>
                        <form id={'modal-rating'}>
                            <InputField
                                required={true}
                                name={"rating"}
                                label={"Рейтинг"}
                                type={"number"}
                                minValue={1}
                                onChange={(e) => setRating(Number(e.target.value))}
                            />
                            <ActionButton
                                icon={magnifying_glass}
                                action={handleSendRating}
                                buttonClass={'action-button'}
                                form={'modal-rating'}
                            />
                        </form>
                        <Table items={specialItems} controls={false} />
                    </>
                )}

                {specialMode === "enroll" && (
                    <form id={'modal-enroll'}>
                        <Selector
                            required={true}
                            name={"organization"}
                            items={getUniqueOrganizations()}
                            label={"Выберите организацию"}
                            onChangeAction={(e) => setSelectedOrganizationId(Number(e.target.value))}
                        />
                        <ActionButton action={handleSendOrganization} form={'modal-enroll'} buttonClass={'action-button'} />
                    </form>
                )}

                {specialMode === "move" && (
                    <form id={'modal-enroll'}>
                        <Selector
                            required={true}
                            name={"organization"}
                            items={getUniqueOrganizations()}
                            label={"Выберите новую организацию"}
                            onChangeAction={(e) => setSelectedOrganizationId(Number(e.target.value))}
                        />
                        <ActionButton action={handleSendOrganization} form={'modal-enroll'} buttonClass={'action-button'} />
                    </form>
                )}

                {specialMode === "import" && (
                    <div style={{display: "flex", justifyContent: "center"}}>
                        <DragNDrop types={["CSV"]} url={"http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/import-workers"} />
                    </div>
                )}
            </Modal>

            {controls ? <div className={"row"}>
                <SearchBar
                    id={'table-search'}
                    onChangeAction={(e: React.ChangeEvent<HTMLInputElement>) => setSearchInput(e.target.value)}
                />
                <ActionButton
                    buttonClass={"action-button"}
                    action={() => dispatch(setSearchValue(searchInput))}
                    icon={magnifying_glass}
                    tooltip={"Поиск"}
                />
                <ActionButton
                    buttonClass={"action-button"}
                    action={handleAdd}
                    icon={plus}
                    tooltip={"Добавить"}
                />
                <ActionButton
                    icon={star}
                    action={handleGetWorkerWithMinPosition}
                    buttonClass={"action-button"}
                    tooltip={"Работник с минимальной должностью"}
                />
                <ActionButton
                    icon={star}
                    action={handleGetWorkerWithMaxSalary}
                    buttonClass={"action-button"}
                    tooltip={"Работник с максимальной зарплатой"}
                />
                <ActionButton
                    icon={star}
                    action={handleGetWorkersWithSpecificRating}
                    buttonClass={"action-button"}
                    tooltip={"Работники по рейтингу"}
                />
                <ActionButton
                    icon={star}
                    action={handleEnrollWorker}
                    buttonClass={"action-button"}
                    tooltip={"Прикрепить работника к организации"}
                />
                <ActionButton
                    icon={star}
                    action={handleMoveWorker}
                    buttonClass={"action-button"}
                    tooltip={"Переместить работника в другую организации"}
                />
                <ActionButton action={() => setSpecialMode('import')} buttonClass={"action-button"} />
            </div> : undefined}

            <div className="pagination-controls">
                <ActionButton text={'<<'} action={() => handlePageChange(1)} buttonClass={'pagination-button'} />
                <ActionButton text={'<'} action={() => handlePageChange(pagination.currentPage - 1)}
                    buttonClass={'pagination-button'} />
                <ActionButton text={'>'} action={() => handlePageChange(pagination.currentPage + 1)}
                    buttonClass={'pagination-button'} />
                <ActionButton text={'>>'} action={() => handlePageChange(totalPages)}
                    buttonClass={'pagination-button'} />
            </div>

            <table className={"worker-table"}>
                <thead>
                    <tr>
                        <th className={getSortClass('id')} onClick={() => handleSort('id')}>
                            id
                        </th>
                        <th className={getSortClass('name')} onClick={() => handleSort('name')}>
                            Имя
                        </th>
                        <th className={getSortClass('coordinates')} onClick={() => handleSort('coordinates')}>
                            Координаты
                        </th>
                        <th className={getSortClass('creationDate')} onClick={() => handleSort('creationDate')}>
                            Дата Создания
                        </th>
                        <th className={getSortClass('organization')} onClick={() => handleSort('organization')}>
                            Организация
                        </th>
                        <th className={getSortClass('salary')} onClick={() => handleSort('salary')}>
                            Зарплата
                        </th>
                        <th className={getSortClass('rating')} onClick={() => handleSort('rating')}>
                            Рейтинг
                        </th>
                        <th className={getSortClass('startDate')} onClick={() => handleSort('startDate')}>
                            Дата начала работы
                        </th>
                        <th className={getSortClass('position')} onClick={() => handleSort('position')}>
                            Должность
                        </th>
                        <th className={getSortClass('status')} onClick={() => handleSort('status')}>
                            Статус
                        </th>
                        <th className={getSortClass('person')} onClick={() => handleSort('person')}>
                            Паспорт
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {paginatedItems.length > 0 ? (
                        paginatedItems.map((item: Worker) => (
                            <TableRow key={item.id} worker={item} />
                        ))
                    ) : (
                        <tr>
                            <td colSpan={11}>
                                Нет данных, соответствующих фильтру
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>

            <div className="pagination-controls">
                <div style={{ textAlign: "left", width: "100%" }}>
                    Страница <b>{pagination.currentPage}</b> из {totalPages}
                </div>
                <ActionButton text={'<<'} action={() => handlePageChange(1)} buttonClass={'pagination-button'} />
                <ActionButton text={'<'} action={() => handlePageChange(pagination.currentPage - 1)}
                    buttonClass={'pagination-button'} />
                <ActionButton text={'>'} action={() => handlePageChange(pagination.currentPage + 1)}
                    buttonClass={'pagination-button'} />
                <ActionButton text={'>>'} action={() => handlePageChange(totalPages)}
                    buttonClass={'pagination-button'} />
                <div style={{ textAlign: "right", width: "100%" }}>
                    Всего элементов: <b>{items.length}</b>
                </div>
            </div>
        </div>
    );
}

export default Table;