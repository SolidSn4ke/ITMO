import React, {useEffect, useState} from "react";
import ActionButton from "./ActionButton";
import cancel from "../resources/cancel.svg"
import continue_icon from "../resources/continue.svg"
import profile_placeholder from "../resources/profile_placeholder.svg";
import "../css/WorkerBuilder.css"
import OrganizationBuilder from "./OrganizationBuilder";
import InputField from "./InputField";
import Position, {positionToString} from "../ts/data/Position";
import Status, {statusToString} from "../ts/data/Status";
import Selector from "./Selector";
import PersonBuilder from "./PersonBuilder";
import axios from "axios";
import {useAppDispatch, useAppSelector} from "../ts/redux/hooks";
import {updateBuildMode, updateLocationRequired, updateViewMode} from "../ts/redux/workerSlice";
import Organization, {toString} from "../ts/data/Organization";
import Address from "../ts/data/Address";
import Location from "../ts/data/Location";
import OrganizationType from "../ts/data/OrganizationType";
import Worker from "../ts/data/Worker";
import Coordinates from "../ts/data/Coordinates";
import Person from "../ts/data/Person";
import Color from "../ts/data/Color";
import Country from "../ts/data/Country";
import {showErrorNotification, showInfoNotification} from "./Main";

interface CreatedLocation {
    x: number,
    y: number,
    z: number,
    name: string
}

interface LocationBuilderProps {
    handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void
    location: Location | null | undefined,
    parentName: string
}

export function LocationBuilder({handleInputChange, location, parentName}: LocationBuilderProps) {
    const updateMode = useAppSelector((state) => state.updateMode)

    const [createdLocation, setCreatedLocation] = useState<CreatedLocation>({
        name: !updateMode || location === null || location === undefined ? '' : location?.name,
        x: !updateMode || location === null || location === undefined ? 0 : location?.x,
        y: !updateMode || location === null || location === undefined ? 0 : location?.y,
        z: !updateMode || location === null || location === undefined ? 0 : location?.z
    })

    const handleInputChangeLocation = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target
        setCreatedLocation(prevState => ({
            ...prevState,
            [name.replace(parentName + '_location_', '')]: value
        }))
        handleInputChange(e)
    }

    return (
        <div>
            <InputField name={parentName + "_location_x"} label={"Координата x"} type={"number"} step={"any"}
                        required={true} onChange={handleInputChangeLocation}
                        value={updateMode ? createdLocation.x : undefined}/>
            <InputField name={parentName + "_location_y"} label={"Координата y"} type={"number"} required={true}
                        onChange={handleInputChangeLocation} value={updateMode ? createdLocation.y : undefined}/>
            <InputField name={parentName + "_location_z"} label={"Координата z"} type={"number"} required={true}
                        onChange={handleInputChangeLocation} value={updateMode ? createdLocation.z : undefined}/>
            <InputField name={parentName + "_location_name"} label={"Название"} type={"text"} required={true}
                        onChange={handleInputChangeLocation} value={updateMode ? createdLocation.name : undefined}/>
        </div>
    )
}

interface CreatedWorker {
    name: string
    x: number
    y: number
    street: string | null
    zip_code: string | null
    organization_location_x: number
    organization_location_y: number
    organization_location_z: number
    organization_location_name: string
    annual_turnover: number | null
    employees_count: number
    organization_rating: number
    salary: number | null
    rating: number | null
    start_date: string
    person_location_x: number,
    person_location_y: number,
    person_location_z: number,
    person_location_name: string,
    passport: string,
    position: Position,
    status: Status | null,
    eye_color: Color | null,
    hair_color: Color,
    nationality: Country | null,
    organization_type: OrganizationType

}

interface WorkerBuilderProps {
    workerTemplate: Worker
}

function WorkerBuilder({workerTemplate}: WorkerBuilderProps) {
    const items: Worker[] = useAppSelector((state) => state.items)
    const locationRequired = useAppSelector((state) => state.locationRequired)
    const updateMode = useAppSelector((state) => state.updateMode)

    const dispatch = useAppDispatch()

    const [organizationRequired, setOrganizationRequired] = useState(false)
    const [selectedOrganizationID, setSelectedOrganizationID] = useState<number | null>(null)

    const [createdWorker, setCreatedWorker] = useState<CreatedWorker>({
        name: updateMode ? workerTemplate.name : '',
        x: updateMode ? workerTemplate?.coordinates.x : 0,
        y: updateMode ? workerTemplate?.coordinates.y : 0,
        street: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? '' : workerTemplate.organization.officialAddress.street,
        zip_code: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? '' : workerTemplate.organization.officialAddress.zipCode,
        annual_turnover: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? null : workerTemplate?.organization?.annualTurnover,
        employees_count: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? 0 : workerTemplate?.organization?.employeesCount,
        organization_location_name: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? '' : workerTemplate?.organization?.officialAddress.town.name,
        organization_location_x: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? 0 : workerTemplate.organization.officialAddress.town.x,
        organization_location_y: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? 0 : workerTemplate.organization.officialAddress.town.y,
        organization_location_z: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? 0 : workerTemplate.organization.officialAddress.town.z,
        organization_rating: !updateMode || workerTemplate?.organization === null || workerTemplate?.organization === undefined ? 0 : workerTemplate?.organization?.rating,
        rating: updateMode ? workerTemplate?.rating : null,
        salary: updateMode ? workerTemplate?.salary : null,
        start_date: updateMode ? workerTemplate?.startDate : '',
        passport: updateMode ? workerTemplate.person.passportID : '',
        person_location_name: !updateMode || workerTemplate?.person.location === null ? '' : workerTemplate.person.location?.name,
        person_location_x: !updateMode || workerTemplate?.person.location === null ? 0 : workerTemplate.person.location?.x,
        person_location_y: !updateMode || workerTemplate?.person.location === null ? 0 : workerTemplate.person.location?.y,
        person_location_z: !updateMode || workerTemplate?.person.location === null ? 0 : workerTemplate.person.location?.z,
        position: updateMode ? workerTemplate.position : Position.DIRECTOR,
        status: updateMode ? workerTemplate.status : null,
        eye_color: updateMode ? workerTemplate.person.eyeColor : null,
        hair_color: updateMode ? workerTemplate.person.hairColor : Color.RED,
        nationality: updateMode ? workerTemplate.person.nationality : null,
        organization_type: updateMode && workerTemplate.organization !== null ? workerTemplate.organization?.organizationType : OrganizationType.COMMERCIAL,
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        let {name, value} = e.target
        setCreatedWorker(prevState => ({
            ...prevState,
            [name]: value
        }))
    }

    const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        let {name, value} = e.target
        setCreatedWorker(prevState => ({
            ...prevState,
            [name]: value
        }))
    }

    const handleCancel = () => {
        dispatch(updateViewMode(true))
        if (updateMode) {
            dispatch(updateBuildMode(false))
        }
        if (locationRequired) {
            dispatch(updateLocationRequired(false))
        }
        showInfoNotification('Действие отменено')
    }

    const findOrganizationById = (orgId: number | null): Organization | null => {
        if (!orgId) return null;

        // Ищем первого worker'а с нужной организацией и возвращаем его организацию
        const workerWithOrg = items.find(worker =>
            worker?.organization?.id === orgId
        );

        return workerWithOrg?.organization || null;
    };

    const handleCreate = async (event: Event) => {
        event.preventDefault()

        const form: HTMLFormElement = document.getElementById('worker-builder') as HTMLFormElement

        let worker, address, orgLocation, organisation, personLocation, person, coordinates;

        coordinates = new Coordinates(createdWorker.x, createdWorker.y)
        locationRequired ? personLocation = new Location(createdWorker.person_location_x, createdWorker.person_location_y, createdWorker.person_location_z, createdWorker.person_location_name) : personLocation = null
        person = new Person(createdWorker.hair_color, personLocation, createdWorker.passport, createdWorker.nationality, createdWorker.eye_color)

        if (organizationRequired) {
            orgLocation = new Location(createdWorker.organization_location_x, createdWorker.organization_location_y, createdWorker.organization_location_z, createdWorker.organization_location_name)
            address = new Address(orgLocation, createdWorker.street, createdWorker.zip_code)
            organisation = new Organization(-1, address, createdWorker.employees_count, createdWorker.organization_rating, createdWorker.organization_type, createdWorker.annual_turnover)
        } else {
            organisation = findOrganizationById(selectedOrganizationID)
            if (organisation !== null) {
                organisation = new Organization(organisation.id, organisation.officialAddress, organisation.employeesCount, organisation.rating, organisation.organizationType, organisation.annualTurnover)
            }
        }

        worker = new Worker(0, createdWorker.name, coordinates, '', createdWorker.start_date, createdWorker.position, person, organisation, createdWorker.salary, createdWorker.rating, createdWorker.status);

        if (form.checkValidity()) {
            let data: {}

            try {
                let url
                data = worker.toJSON()
                if (updateMode) {
                    url = `http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/update-worker/${workerTemplate.id}`
                } else {
                    url = "http://localhost:8080/back-1.0-SNAPSHOT/rest-server/actions/add-worker"
                }
                let response = await axios.post(url, data, {withCredentials: true})
                if (response.status === 200) {
                    if (updateMode) {
                        showInfoNotification('Данные о работнике обновлены!')
                    } else {
                        showInfoNotification('Работник добавлен!')
                    }
                    dispatch(updateViewMode(true))
                    dispatch(updateBuildMode(false))
                } else {
                    showErrorNotification(response.data)
                }
            } catch (e) {
                console.log(e)
            }
        } else form.reportValidity()
    }

    const handleOrganizationCreation = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.value === '0') {
            setOrganizationRequired(false)
        } else {
            setOrganizationRequired(true)
        }
    }

    useEffect(() => {
        if (updateMode && workerTemplate?.organization !== undefined) {
            setOrganizationRequired(true)
        }
    }, [updateMode, workerTemplate?.organization])

    const getUniqueOrganizations = () => {
        const orgMap = new Map<number, string>();
        items.forEach(item => {
            if (item.organization && item.organization.id) {
                orgMap.set(item.organization.id, toString(item.organization));
            }
        });
        return orgMap;
    };

    return (
        <div className={"worker-builder"}>
            <div id={"profile-picture"}>
                <img src={profile_placeholder} alt={""}/>
            </div>

            <div className={"scrollable-div"}>
                <form id={"worker-builder"}>
                    <InputField name={'name'} label={"Имя"} type={"text"} required={true}
                                value={updateMode ? createdWorker.name : undefined} onChange={handleInputChange}/>
                    <InputField name={'x'} label={"Координата x"} type={"number"} required={true} step={"any"}
                                value={updateMode ? createdWorker.x : undefined} onChange={handleInputChange}/>
                    <InputField name={'y'} label={"Координата y"} type={"number"} required={true}
                                value={updateMode ? createdWorker.y : undefined}
                                onChange={handleInputChange}/>
                    <div>


                        <fieldset>
                            <legend>Организация:</legend>
                            <br/>
                            <input name={'organization'} type={"radio"} onChange={(e) => handleOrganizationCreation(e)}
                                   value={0}
                                   id={'existing'} required={true}/>
                            <label htmlFor={'existing'}>Использовать существующую</label><br/>
                            <input name={'organization'} type={"radio"} onChange={(e) => handleOrganizationCreation(e)}
                                   value={1}
                                   id={'from-scratch'}/>
                            <label htmlFor={'from-scratch'}>Создать с нуля</label>
                        </fieldset>

                        {organizationRequired ?
                            <OrganizationBuilder handleSelectChange={handleSelectChange}
                                                 handleInputChange={handleInputChange}
                                                 organization={workerTemplate?.organization}/> :
                            <Selector
                                name={"organization"} items={getUniqueOrganizations()}
                                label={"Выберите организацию"}
                                onChangeAction={(e) => setSelectedOrganizationID(Number(e.target.value))}/>
                        }
                    </div>
                    <InputField name={'salary'} label={"Зарплата"} type={"number"} minValue={1}
                                value={updateMode ? createdWorker.salary : undefined} onChange={handleInputChange}/>
                    <InputField name={"rating"} label={"Рейтинг"} type={"number"} minValue={1}
                                value={updateMode ? createdWorker.rating : undefined} onChange={handleInputChange}/>
                    <InputField name={"start_date"} label={"Дата начала работы"} type={"date"} required={true}
                                value={updateMode ? createdWorker.start_date : undefined} onChange={handleInputChange}/>
                    <Selector name={'position'} items={positionToString} label={"Должность"} required={true}
                              selectedValue={updateMode ? createdWorker.position : undefined}
                              onChangeAction={handleSelectChange}/>
                    <Selector name={'status'} items={statusToString} label={"Статус"}
                              selectedValue={updateMode && createdWorker.status !== null ? createdWorker.status : undefined}
                              onChangeAction={handleSelectChange}/>
                    <PersonBuilder handleSelectChange={handleSelectChange} handleInputChange={handleInputChange}
                                   person={workerTemplate?.person}/>
                </form>
            </div>

            <div className={"row"} id={"quick-view-buttons"}>
                <ActionButton action={handleCancel} buttonClass={"action-button-red"} icon={cancel}
                              tooltip={"Отменить"}/>
                <ActionButton action={handleCreate} buttonClass={"action-button"} icon={continue_icon}
                              tooltip={updateMode ? "Обновить" : "Создать"} form={"worker-builder"}/>
            </div>
        </div>
    )
}

export default WorkerBuilder