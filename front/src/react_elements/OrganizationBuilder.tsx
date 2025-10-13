import React, {useState} from "react";
import InputField from "./InputField";
import {LocationBuilder} from "./WorkerBuilder"
import Selector from "./Selector";
import OrganizationType, {organizationTypeToString} from "../ts/data/OrganizationType";
import {useAppSelector} from "../ts/redux/hooks";
import Organization from "../ts/data/Organization";

interface CreatedOrganization {
    street: string | null
    zip_code: string | null
    organization_location_x: number
    organization_location_y: number
    organization_location_z: number
    organization_location_name: string
    annual_turnover: number | null
    employees_count: number
    organization_rating: number
    organization_type: OrganizationType
}

interface OrganizationBuilderProps {
    organization: Organization | undefined | null
    handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void
    handleSelectChange: (e: React.ChangeEvent<HTMLSelectElement>) => void
}

function OrganizationBuilder({organization, handleInputChange, handleSelectChange}: OrganizationBuilderProps) {
    const updateMode = useAppSelector((state) => state.updateMode)

    const [createdOrganization, setCreatedOrganization] = useState<CreatedOrganization>({
        annual_turnover: !updateMode || organization === null || organization === undefined ? null : organization?.annualTurnover,
        employees_count: !updateMode || organization === null || organization === undefined ? 0 : organization?.employeesCount,
        organization_location_name: !updateMode || organization === null || organization === undefined ? '' : organization?.officialAddress.town.name,
        organization_location_x: !updateMode || organization === null || organization === undefined ? 0 : organization?.officialAddress.town.x,
        organization_location_y: !updateMode || organization === null || organization === undefined ? 0 : organization?.officialAddress.town.y,
        organization_location_z: !updateMode || organization === null || organization === undefined ? 0 : organization?.officialAddress.town.z,
        organization_rating: !updateMode || organization === null || organization === undefined ? 0 : organization?.rating,
        street: !updateMode || organization === null || organization === undefined ? '' : organization?.officialAddress.street,
        zip_code: !updateMode || organization === null || organization === undefined ? '' : organization?.officialAddress.zipCode,
        organization_type: !updateMode || organization === null || organization === undefined ? OrganizationType.COMMERCIAL : organization.organizationType
    })

    const handleInputChangeOrganization = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target
        setCreatedOrganization(prevState => ({
            ...prevState,
            [name]: value
        }))
        handleInputChange(e)
    }

    const handleSelectChangeOrganization = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const {name, value} = e.target
        setCreatedOrganization(prevState => ({
            ...prevState,
            [name]: value
        }))
        handleSelectChange(e)
    }

    return (
        <div>
            <InputField name={"street"} label={"Улица"} type={"text"} onChange={handleInputChangeOrganization}
                        value={updateMode ? createdOrganization.street : undefined}/>
            <InputField name={"zip_code"} label={"zip-код"} type={"text"} minLength={9}
                        onChange={handleInputChangeOrganization}
                        value={updateMode ? createdOrganization.zip_code : undefined}/>
            <LocationBuilder parentName={"organization"} location={organization?.officialAddress.town}
                             handleInputChange={handleInputChangeOrganization}/>
            <InputField name={'annual_turnover'} label={"Годовой оборот"} type={"number"} minValue={0}
                        value={updateMode ? createdOrganization.annual_turnover : undefined}
                        onChange={handleInputChangeOrganization}/>
            <InputField name={'employees_count'} label={"Кол-во сотрудников"} type={"number"} minValue={0}
                        required={true} value={updateMode ? createdOrganization.employees_count : undefined}
                        onChange={handleInputChangeOrganization}/>
            <InputField name={'organization_rating'} label={"Рейтинг"} type={"number"} required={true} minValue={0}
                        onChange={handleInputChangeOrganization}
                        value={updateMode ? createdOrganization.organization_rating : undefined}/>
            <Selector name={'organization_type'} items={organizationTypeToString} label={"Тип организации"}
                      required={true} onChangeAction={handleSelectChangeOrganization}
                      selectedValue={createdOrganization.organization_type}/>
        </div>
    )
}

export default OrganizationBuilder