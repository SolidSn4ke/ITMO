import Color from "./Color";
import Location from "./Location";
import Country from "./Country";

class Person {
    constructor(hairColor: Color, location: Location | null, passportID: string, nationality: Country | null, eyeColor: Color | null) {
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.location = location;
        this.passportID = passportID;
        this.nationality = nationality;
    }

    eyeColor: Color | null
    hairColor: Color
    location: Location | null
    passportID: string
    nationality: Country | null

    public validate() {
        let valid = true

        if (this.passportID.length < 6) valid = false

        return valid
    }
}

export default Person