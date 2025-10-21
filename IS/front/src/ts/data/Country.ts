enum Country {
    UNITED_KINGDOM = 'UNITED_KINGDOM',
    SPAIN = 'SPAIN',
    THAILAND = 'THAILAND'
}

export const countryToString = new Map<Country, string>()
countryToString.set(Country.UNITED_KINGDOM, "Великобритания")
countryToString.set(Country.SPAIN, "Испания")
countryToString.set(Country.THAILAND, "Таиланд")

export default Country