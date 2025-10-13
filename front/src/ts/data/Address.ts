import Location from "./Location";

class Address {
    constructor(town: Location, street: string | null, zipCode: string | null) {
        street !== null && street.length === 0 ? this.street = null : this.street = street
        zipCode !== null && zipCode.length === 0 ? this.zipCode = null : this.zipCode = zipCode
        this.town = town
    }

    street: string | null
    zipCode: string | null
    town: Location

    public validate() {
        let valid = true

        if (this.zipCode !== null && this.zipCode.length < 9) valid = false

        return valid
    }
}

export default Address