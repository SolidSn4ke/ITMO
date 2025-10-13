import Coordinates from "./Coordinates";
import Organization from "./Organization";
import Position from "./Position";
import Status from "./Status";
import Person from "./Person";

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
            organization: this.organization?.toJSON(),
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

export default Worker
