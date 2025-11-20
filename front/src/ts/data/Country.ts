enum Country {
    UNITED_KINGDOM = 'UNITED_KINGDOM',
    SPAIN = 'SPAIN',
    THAILAND = 'THAILAND'
}

export const countryToString = new Map<Country, string>()
countryToString.set(Country.UNITED_KINGDOM, "Великобритания")
countryToString.set(Country.SPAIN, "Испания")
countryToString.set(Country.THAILAND, "Таиланд")

export function stringToCountry(country: string) {
    switch (country.toUpperCase()) {
        case "UNITED_KINGDOM": return Country.UNITED_KINGDOM
        case "SPAIN": return Country.SPAIN
        case "THAILAND": return Country.THAILAND
        default: return null
    }
}

export default Country