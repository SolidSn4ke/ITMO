enum Position {
    DIRECTOR = 'DIRECTOR',
    COOK = "COOK",
    CLEANER = 'CLEANER',
    MANAGER_OF_CLEANING = 'MANAGER_OF_CLEANING'
}

export const positionToString = new Map<Position, string>()
positionToString.set(Position.DIRECTOR, "Управляющий")
positionToString.set(Position.COOK, "Повар")
positionToString.set(Position.CLEANER, "Уборщик")
positionToString.set(Position.MANAGER_OF_CLEANING, "Менеджер по клинингу")

export function stringToPosition(position: string) {
    switch (position.toUpperCase()) {
        case "DIRECTOR":
            return Position.DIRECTOR
        case "COOK":
            return Position.COOK
        case "CLEANER":
            return Position.CLEANER
        case "MANAGER_OF_CLEANING":
            return Position.MANAGER_OF_CLEANING
        default:
            return null
    }
}

export default Position