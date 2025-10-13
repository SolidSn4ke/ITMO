import Address from "./Address";
import OrganizationType from "./OrganizationType";

class Organization {
    constructor(id: number | null, address: Address, employeesCount: number, rating: number, organizationType: OrganizationType, annualTurnover: number | null) {
        this.id = id
        this.officialAddress = address
        this.annualTurnover = annualTurnover
        this.rating = rating
        this.employeesCount = employeesCount
        this.organizationType = organizationType
    }

    id: number | null
    officialAddress: Address;
    annualTurnover: number | null
    employeesCount: number;
    rating: number;
    organizationType: OrganizationType;

    public toJSON(): {} {
        return {
            officialAddress: this.officialAddress,
            annualTurnover: this.annualTurnover,
            employeesCount: this.employeesCount,
            rating: this.rating,
            organizationType: this.organizationType
        }
    }

    public validate() {
        let valid = true

        if (this.rating <= 0)
            valid = false

        if (this.employeesCount <= 0)
            valid = false

        valid = this.officialAddress.validate()

        return valid
    }
}

export function toString(org: Organization | null | undefined) {
    if (org === null || org === undefined) return ''
    else return `type: ${org.organizationType}, address: {street: ${org.officialAddress.street}, zip-code: ${org.officialAddress.zipCode}, town: {x: ${org.officialAddress.town.x}, y: ${org.officialAddress.town.y}, z:${org.officialAddress.town.z}, name: ${org.officialAddress.town.name}}}, turnover: ${org.annualTurnover}, emploees: ${org.employeesCount}, rating: ${org.rating}`
}

export default Organization