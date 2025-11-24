import Coordinates from "./Coordinates";
import Organization from "./Organization";
import Position, { stringToPosition } from "./Position";
import Status, { stringToStatus } from "./Status";
import Person from "./Person";
import Location from "./Location";
import Address from "./Address";
import { stringToOrganizationType } from "./OrganizationType";
import { stringToColor } from "./Color";
import { stringToCountry } from "./Country";

class Worker {

    constructor(id: number, name: string, coordinates: Coordinates, creationDate: string, startDate: string, position: Position, person: Person, organization: Organization | null, salary: number | null, rating: number | null, status: Status | null) {
        this.id = id
        this.name = name
        this.coordinates = coordinates
        this.creationDate = creationDate
        this.startDate = startDate
        this.organization = organization
        this.salary = salary
        this.rating = rating
        this.position = position
        this.status = status
        this.person = person
    }

    id: number
    name: string
    coordinates: Coordinates
    creationDate: string
    organization: Organization | null
    salary: number | null
    rating: number | null
    startDate: string
    position: Position
    status: Status | null
    person: Person

    public toJSON() {
        return {
            name: this.name,
            coordinates: this.coordinates,
            organization: this.organization,
            salary: this.salary,
            rating: this.rating,
            startDate: this.startDate,
            position: this.position,
            status: this.status,
            person: this.person
        }
    }

    public validate() {
        let valid = true

        if (this.name.length === 0) valid = false
        if (this.organization !== null) valid = this.organization.validate()
        if (this.salary !== null && this.salary <= 0) valid = false
        if (this.rating !== null && this.rating <= 0) valid = false
        valid = this.person.validate()

        return valid
    }
}

export class FlatWorker {
    id?: number
    name?: string
    x?: number
    y?: number
    org_street?: string
    org_zip_code?: string
    org_x?: number
    org_y?: number
    org_z?: number
    org_name?: string
    annual_turnover?: number
    employees_count?: number
    org_rating?: number
    org_type?: string
    salary?: number
    rating?: number
    start_date?: string
    position?: string
    status?: string
    eye_color?: string
    hair_color?: string
    person_location_x?: number
    person_location_y?: number
    person_location_z?: number
    person_location_name?: string
    passport_id?: string
    nationality?: string

    public toWorker() {
        let worker, address, orgLocation, organisation, personLocation, person, coordinates, eye_color, position;
        if (this.org_x && this.org_y && this.org_z && this.org_name) {
            orgLocation = new Location(Number(this.org_x), Number(this.org_y), Number(this.org_z), this.org_name)
            console.log("Org location created:", orgLocation);
        }

        if (orgLocation) {
            address = new Address(orgLocation, this.org_street == undefined ? null : this.org_street, this.org_zip_code == undefined ? null : this.org_zip_code)
            console.log("Address created:", address);
        }
        if (address && this.employees_count && this.org_rating && this.org_type && this.annual_turnover) {
            organisation = new Organization(null, address, Number(this.employees_count), Number(this.org_rating), stringToOrganizationType(this.org_type), Number(this.annual_turnover))
            console.log("Organization created:", organisation);
        } else organisation = null

        if (this.person_location_x && this.person_location_y && this.person_location_z && this.person_location_name)
            personLocation = new Location(Number(this.person_location_x), Number(this.person_location_y), Number(this.person_location_z), this.person_location_name)

        if (this.eye_color)
            eye_color = stringToColor(this.eye_color)

        if (eye_color && personLocation && this.passport_id && this.nationality && this.hair_color)
            person = new Person(eye_color, personLocation, this.passport_id, stringToCountry(this.nationality), stringToColor(this.hair_color))

        if (this.x && this.y) {
            coordinates = new Coordinates(Number(this.x), Number(this.y))
        }

        if (this.position)
            position = stringToPosition(this.position)

        if (this.name && coordinates && this.start_date && this.position && person && this.status && position)
            worker = new Worker(0, this.name, coordinates, "", this.start_date, position, person, organisation, this.salary === undefined ? null : Number(this.salary), this.rating === undefined ? null : Number(this.rating), stringToStatus(this.status))

        return worker
    }
}

export default Worker
