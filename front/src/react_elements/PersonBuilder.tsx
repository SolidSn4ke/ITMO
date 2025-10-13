import React, {useEffect, useState} from "react";
import Selector from "./Selector";
import Color, {colorsToString} from "../ts/data/Color";
import {LocationBuilder} from "./WorkerBuilder";
import InputField from "./InputField";
import Country, {countryToString} from "../ts/data/Country";
import {useAppDispatch, useAppSelector} from "../ts/redux/hooks";
import {updateLocationRequired} from "../ts/redux/workerSlice";
import Person from "../ts/data/Person";

interface CreatedPerson {
    person_location_x: number,
    person_location_y: number,
    person_location_z: number,
    person_location_name: string,
    passport: string,
    eye_color: Color | null,
    hair_color: Color,
    nationality: Country | null
}

interface PersonBuilderProps {
    person: Person | undefined
    handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void
    handleSelectChange: (e: React.ChangeEvent<HTMLSelectElement>) => void
}

function PersonBuilder({person, handleInputChange, handleSelectChange}: PersonBuilderProps) {
    const locationRequired = useAppSelector((state) => state.locationRequired);
    const updateMode = useAppSelector((state) => state.updateMode)

    const [createdPerson, setCreatedPerson] = useState<CreatedPerson>({
        passport: !updateMode || person === undefined ? '' : person?.passportID,
        person_location_name: !updateMode || person === undefined || person.location === null ? '' : person?.location?.name,
        person_location_x: !updateMode || person === undefined || person.location === null ? 0 : person?.location?.x,
        person_location_y: !updateMode || person === undefined || person.location === null ? 0 : person?.location?.y,
        person_location_z: !updateMode || person === undefined || person.location === null ? 0 : person?.location?.z,
        eye_color: !updateMode || person === undefined ? null : person.eyeColor,
        hair_color: !updateMode || person === undefined ? Color.RED : person.hairColor,
        nationality: !updateMode || person === undefined ? null : person.nationality,
    })

    const handleInputChangePerson = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target
        setCreatedPerson(prevState => ({
            ...prevState,
            [name]: value
        }))
        handleInputChange(e)
    }

    const handleSelectChangePerson = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const {name, value} = e.target
        setCreatedPerson(prevState => ({
            ...prevState,
            [name]: value
        }))
        handleSelectChange(e)
    }

    const dispatch = useAppDispatch()

    function handleLocationCreation() {
        if (locationRequired) {
            dispatch(updateLocationRequired(false))
        } else dispatch(updateLocationRequired(true))
    }

    useEffect(() => {
        if (updateMode && person?.location !== null) {
            dispatch(updateLocationRequired(true))
        }
    }, [dispatch, updateMode, person?.location])

    return (
        <div>
            <Selector name={'eye_color'} items={colorsToString} label={"Цвет глаз"}
                      selectedValue={updateMode && createdPerson.eye_color !== null ? createdPerson.eye_color : undefined}
                      onChangeAction={handleSelectChangePerson}/>
            <Selector name={'hair_color'} items={colorsToString} label={"Цвет волос"} required={true}
                      selectedValue={updateMode ? createdPerson.hair_color : undefined}
                      onChangeAction={handleSelectChangePerson}/>
            <div>
                Локация: <input id={'location-checkbox'} type={"checkbox"}
                                onChange={handleLocationCreation} checked={!!locationRequired}/>
                {locationRequired ? <LocationBuilder parentName={'person'} location={person?.location}
                                                     handleInputChange={handleInputChangePerson}/> : null}
            </div>
            <InputField name={'passport'} label={"Номер паспорта"} type={"text"} required={true} minLength={6}
                        value={updateMode ? createdPerson.passport : undefined}
                        onChange={handleInputChangePerson}/>
            <Selector name={'nationality'} items={countryToString} label={"Национальность"}
                      selectedValue={updateMode && createdPerson.nationality !== null ? createdPerson.nationality : undefined}
                      onChangeAction={handleSelectChangePerson}/>
        </div>
    )
}

export default PersonBuilder